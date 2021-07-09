package jmu.shijh.tinymall.controller;


import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import jmu.shijh.tinymall.common.annotation.MultiRequestBody;
import jmu.shijh.tinymall.common.annotation.ParamCheck;
import jmu.shijh.tinymall.common.exception.CustomException;
import jmu.shijh.tinymall.common.util.*;
import jmu.shijh.tinymall.domain.dto.*;
import jmu.shijh.tinymall.domain.entity.Orders;
import jmu.shijh.tinymall.domain.entity.Shop;
import jmu.shijh.tinymall.domain.enums.OrderStatus;
import jmu.shijh.tinymall.domain.vo.MonthIncomeVO;
import jmu.shijh.tinymall.domain.vo.OrdersVO;
import jmu.shijh.tinymall.mq.config.OrderMQ;
import jmu.shijh.tinymall.mq.sender.OrderMessageSender;
import jmu.shijh.tinymall.mq.sender.RedisCacheMsgSender;
import jmu.shijh.tinymall.service.OrdersService;
import jmu.shijh.tinymall.service.ShopService;
import jmu.shijh.tinymall.shiro.UserIdentity;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author shijh
 * @since 2021-05-20
 */
@RestController
@RequestMapping("/orders")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private OrderMessageSender orderMessageSender;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    private RedisCacheMsgSender redisCacheMsgSender;

    @Autowired
    private UserIdentity user;

    @PostMapping("/shop/income/month")
    @RequiresPermissions({"orders:select", "shopinfo:select"})
    @ParamCheck(include = {"startMonth", "endMonth"}, value = "缺少查询月份")
    public JsonResp getPerMonthIncome(@RequestBody MonthIncomeDTO dto) {
        Shop userShop = shopService.getUserShop();
        dto.setShopId(userShop.getShopId());
        if (dto.getYear() == null) {
            dto.setYear(Times.localNow().getYear());
        }
        Object o = redisTemplate.opsForValue().get(RedisKeys.getMonthIncomeKey(dto));
        if (o == null) {
            o = ordersService.getPerMonthIncome(dto);
            redisCacheMsgSender.send(new RedisCache()
                    .setKey(RedisKeys.getMonthIncomeKey(dto))
                    .setValue(o)
                    .setDuration(Duration.ofDays(30)));
        }
        return R.ok().data(o).build();
    }

    @RequestMapping("/place")
    @RequiresPermissions({"orders:insert", "coupon:use"})
    public JsonResp placeOrder(@RequestBody List<OrderSubmit> orderSubmit) {
        if (orderSubmit.isEmpty()) {
            throw new CustomException("订单为空");
        }
        List<Long> orderId = ordersService.placeOrderBatch(orderSubmit);
        orderId.forEach(id -> orderMessageSender.delayCancelOrder(id));
        return R.ok().data(orderId).msg("下单成功").build();
    }

    @GetMapping("/opt/{method}/{orderId}")
    @RequiresPermissions("orders:update")
    public JsonResp cancelOrder(@PathVariable("method") String method,
                                @PathVariable("orderId") Long orderId,
                                HttpServletResponse response) {
        String msg;
        switch (method) {
            case "cancel":
                ordersService.cancelOrder(orderId);
                msg = "订单已取消";
                break;
            case "remove":
                ordersService.removeOrder(orderId);
                msg = "删除成功";
                break;
            case "receive":
                ordersService.receiveOrder(orderId);
                msg = "收货成功";
                break;
            case "refund":
                ordersService.requestRefundOrder(orderId);
                msg = "已申请退款";
                orderMessageSender.delaySendOrder(orderId, Times.DAY * 7, OrderMQ.ORDER_REFUND_TYPE);
                break;
            default:
                response.setStatus(HttpStatus.NOT_FOUND.value());
                return R.error().build();
        }
        return R.ok().msg(msg).build();
    }

    @PostMapping("/express")
    @ParamCheck
    @RequiresPermissions("orders:update")
    public JsonResp expressOrder(@RequestBody OrderExpress express) {
        ordersService.expressOrder(express);
        orderMessageSender.delayFinishOrder(express.getOrdersId());
        return R.ok().build();
    }

    @GetMapping("/pay/{orderId}")
    @RequiresPermissions("orders:update")
    public JsonResp payOrder(@PathVariable Long orderId) {
        Orders orders = ordersService.getOrderById(orderId);
        if (!orders.getOrdersStatus().equals(OrderStatus.NO_PAY.code())) {
            throw new CustomException("订单已支付或取消");
        }
        if (!orders.getUserId().equals(user.getUserId())) {
            throw new CustomException("越权访问");
        }
        //TODO 调用支付宝或微信接口查询支付信息
        PayInfo payInfo = new PayInfo().setPayId(IdWorker.getId())
                .setPayType("Alipay")
                .setPayTime(Times.localNow());
        ordersService.payOrder(orders, payInfo);
        orderMessageSender.delaySendOrder(orderId, Times.DAY * 14, OrderMQ.ORDER_REFUND_TYPE);
        return R.ok().msg("支付成功").build();
    }


    @GetMapping("/refund/agree/{orderId}")
    @RequiresPermissions({"orders:update", "shopinfo:select"})
    public JsonResp agreeRefund(@PathVariable Long orderId) {
        Orders orders = ordersService.getOrderById(orderId);
        Shop shop = shopService.getUserShop();
        if (!orders.getShopId().equals(shop.getShopId())) {
            throw new CustomException("越权访问");
        }
        //TODO 验证退款状态
        ordersService.agreeRefundOrder(orders);
        return R.ok().msg("退款成功").build();
    }

    @PostMapping("/query/page")
    @RequiresPermissions("orders:select")
    public JsonResp queryOrderPages(@MultiRequestBody(value = "dto", required = false) OrderDTO dto,
                                    @MultiRequestBody(value = "page", required = false) PageDTO page) {
        dto.setUserId(user.getId());
        dto.setProductId(null);
        PageVO pageVO = ordersService.pagingQueryOrder(dto, page);
        return R.ok().data(pageVO).build();
    }

    @GetMapping("/query/{oid}")
    @RequiresRoles("manager")
    @RequiresPermissions("orders:select")
    public JsonResp queryOrderPages(@PathVariable Long oid) {
        Shop shop = shopService.getUserShop();
        PageVO pageVO = ordersService.pagingQueryOrder(new OrderDTO().setShopId(shop.getShopId()).setOrdersId(oid), new PageDTO());
        if (pageVO.getList().isEmpty()) {
            throw new CustomException("不存在该订单");
        }
        OrdersVO vo = (OrdersVO) pageVO.getList().get(0);
        return R.ok().data(vo).build();
    }

    @PostMapping("/query/shop/page")
    @RequiresRoles("manager")
    public JsonResp queryShopOrdersPage(@MultiRequestBody(value = "dto", required = false) OrderDTO dto,
                                        @MultiRequestBody(value = "page", required = false) PageDTO page) {
        Shop shop = shopService.getUserShop();
        dto.setShopId(shop.getShopId());
        PageVO pageVO = ordersService.pagingQueryPreOrder(dto, page);
        return R.ok().data(pageVO).build();
    }
}
