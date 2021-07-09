package jmu.shijh.tinymall.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jmu.shijh.tinymall.common.exception.CustomException;
import jmu.shijh.tinymall.common.util.*;
import jmu.shijh.tinymall.domain.dto.*;
import jmu.shijh.tinymall.domain.entity.*;
import jmu.shijh.tinymall.domain.enums.CouponType;
import jmu.shijh.tinymall.domain.enums.OrderStatus;
import jmu.shijh.tinymall.domain.vo.MonthIncomeVO;
import jmu.shijh.tinymall.domain.vo.OrdersVO;
import jmu.shijh.tinymall.service.*;
import jmu.shijh.tinymall.service.aop.PlaceOrderCheckAspect;
import jmu.shijh.tinymall.shiro.UserIdentity;
import jmu.shijh.tinymall.mapper.OrdersMapper;
import jmu.shijh.tinymall.mapper.ShopMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.FetchProfile;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *  1. 查询订单
 *  2. 下单
 *  3. 取消订单
 *  4. 订单退款 (退钱未完成）
 *  5. 订单付款 (支付未完成）
 *  6. 收货
 *  7. 发货
 *  6. 自动失效 自动收货
 * @author shijh
 * @since 2021-05-20
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService, OrderSysService {
    @Autowired
    private OrdersDetailService detailService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private CouponService couponService;

    @Autowired
    private UserIdentity user;

    @Autowired
    private PlaceOrderCheckAspect placeOrderCheckAspect;


    @Override
    public PageVO pagingQueryOrder(OrderDTO dto, PageDTO page) {
        PageHelper.startPage(page);
        Page<OrdersVO> ordersVOS;
        if (Str.notEmpty(dto.getKeyword())) {
            ordersVOS = ordersMapper.selectPageByKeyword(dto);
        } else {
            ordersVOS = ordersMapper.selectPageByDTO(dto);
        }
        return new PageVO(ordersVOS);
    }

    @Override
    public PageVO pagingQueryPreOrder(OrderDTO dto, PageDTO page) {
        PageHelper.startPage(page);
        Page<Orders> orders = ordersMapper.selectPrePageByDTO(dto);
        return new PageVO(orders);
    }

    @Override
    public List<String> getRelationUserId(OrderDTO dto) {
        if (dto.getShopId() == null || dto.getOrdersStatus() == null) {
            throw new CustomException("缺少必要参数条件");
        }
        return ordersMapper.getRelationUserId(dto);
    }

    @Override
    @Transactional
    public void receiveOrder(Long orderId) {
        Orders orders = getOrderById(orderId);
        if (!orders.getOrdersStatus().equals(OrderStatus.WAIT_RECEIVE.code())) {
            throw new CustomException("该订单不能收货");
        }
        if (!orders.getUserId().equals(user.getUserId())) {
            throw new CustomException("越权访问");
        }
        orders.setOrdersStatus(OrderStatus.FINISH.code());
        orders.setFinishTime(Times.localNow());
        ordersMapper.updateById(orders);
    }

    @Override
    public Orders getOrderById(Long orderId) {
        Orders orders = ordersMapper.selectById(orderId);
        if (orders == null) {
            throw new CustomException("订单不存在");
        }
        return orders;
    }

    @Override
    @Transactional
    public void expressOrder(OrderExpress orderExpress) {
        Orders orders = getOrderById(orderExpress.getOrdersId());
        Shop shop = shopMapper.selectOne(Wrappers.lambdaQuery(Shop.class).eq(Shop::getUserId, user.getId()));
        if (shop == null || !orders.getShopId().equals(shop.getShopId())) {
            throw new CustomException("越权访问");
        }
        BeanUtils.copyProperties(orderExpress, orders);
        orders.setOrdersStatus(OrderStatus.WAIT_RECEIVE.code());
        orders.setExpressTime(Times.localNow());
        ordersMapper.updateById(orders);
    }


    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        Orders orders = getOrderById(orderId);
        if (!orders.getOrdersStatus().equals(OrderStatus.NO_PAY.code())) {
            throw new CustomException("不能取消已付款的订单");
        }
        if (!orders.getUserId().equals(user.getUserId())) {
            throw new CustomException("越权访问");
        }
        if (orders.getOrdersStatus().equals(OrderStatus.NO_PAY.code())) {
            orders.setOrdersStatus(OrderStatus.INVALID.code());
            ordersMapper.updateById(orders);
            rollbackStock(orderId);
        } else {
            throw new CustomException("该订单不能取消");
        }
    }

    @Transactional
    @Override
    public void payOrder(Orders orders, PayInfo payInfo) {
        orders.setOrdersStatus(OrderStatus.WAIT_EXPR.code());
        orders.setPayTime(Times.localNow());
        BeanUtils.copyProperties(payInfo, orders);
        ordersMapper.updateById(orders);
    }

    @Override
    @Transactional
    public void rollbackStock(Long orderId) {
        List<OrdersDetail> list = detailService.getByOrderId(orderId);
        for (OrdersDetail item : list) {
            Product p = productService.getById(item.getProductId());
            p.setStock(p.getStock()+item.getProductSize());
            productService.updateProduct(p);
        }
    }

    @Override
    @Transactional
    public void requestRefundOrder(Long ordersId) {
        Orders orders = getById(ordersId);
        if (!orders.getUserId().equals(user.getId())) {
            throw new CustomException("越权访问");
        }
        if (orders.getOrdersStatus().equals(OrderStatus.WAIT_EXPR.code())) {
            orders.setOrdersStatus(OrderStatus.WAIT_REFUND.code());
            orders.setRefundTime(Times.localNow());
            ordersMapper.updateById(orders);
        } else {
            throw new CustomException("该订单不能退款");
        }
    }

    @Override
    @Transactional
    public void agreeRefundOrder(Orders orders) {
        if (!OrderStatus.WAIT_REFUND.equal(orders.getOrdersStatus())) {
            throw new CustomException("该订单不能退款");
        }
        orders.setOrdersStatus(OrderStatus.HAS_REFUND.code());
        orders.setFinishTime(Times.localNow());
        rollbackStock(orders.getOrdersId());
        ordersMapper.updateById(orders);
    }

    @Override
    public List<Long> placeOrderBatch(List<OrderSubmit> submits) {
        List<Long> orderIds = new ArrayList<>(submits.size());
        for (OrderSubmit submit : submits) {
            orderIds.add(placeOrder(submit));
        }
        return orderIds;
    }

    private OrdersDetail getOrdersDetail(Orders orders, OrderItem orderItem, Product product) {
        OrdersDetail ordersDetail = new OrdersDetail();
        ordersDetail.setOrdersId(orders.getOrdersId());
        ordersDetail.setCategoryId(product.getCategoryId());
        ordersDetail.setProductName(product.getProductName());
        ordersDetail.setProductPrice(product.getSalePrice());
        BeanUtils.copyProperties(orderItem, ordersDetail);
        return ordersDetail;
    }

    /**
     * 部分结算
     * @param orders 正在结算的订单
     * @param detail 正在参与结算的详单
     */
    private void calcOrderPrice(Orders orders, final OrdersDetail detail) {
        //当前商品总价
        BigDecimal price = detail.getProductPrice().multiply(BigDecimal.valueOf(detail.getProductSize()));
        //订单总价
        orders.setTotalPrice(orders.getTotalPrice().add(price));
    }

    private void calcOrderPrice(Orders orders, Coupon coupon) {
        //优惠券作用次数
        int times = coupon.getApplyMulti() ?
                orders.getTotalPrice().divide(BigDecimal.valueOf(coupon.getCouponStep()), BigDecimal.ROUND_FLOOR).intValue() : 1;
        if (times == 0) {
            throw new CustomException("优惠券不满足使用条件");
        }
        BigDecimal discountPrice;
        if (coupon.getCouponType().equals(CouponType.FULL_CUT.code())) {
            discountPrice = orders.getTotalPrice().subtract(BigDecimal.valueOf(coupon.getCouponValue() * times));
        } else {
            discountPrice = orders.getTotalPrice().multiply(BigDecimal.valueOf(Math.pow(coupon.getCouponValue()*0.01, times)));
        }
        //订单折后总价
        orders.setDiscountPrice(discountPrice);
    }

    @Override
    @Transactional
    public Long placeOrder(OrderSubmit orderSubmit) {
        placeOrderCheckAspect.checkOrders(orderSubmit);
        List<OrdersDetail> details = new ArrayList<>(10);
        HashMap<Integer, List<Long>> sizeProdsMap = new HashMap<>(5);
        Orders orders = new Orders(IdWorker.getId(), user.getUserId(), orderSubmit.getShopId(), orderSubmit.getAddressId());
        //创建详单
        for (OrderItem orderItem : orderSubmit.getItems()) {
            Product product = productService.getById(orderItem.getProductId());
            placeOrderCheckAspect.checkProduct(orderSubmit,orderItem,product);
            List<Long> ids = sizeProdsMap.computeIfAbsent(orderItem.getProductSize(), k -> new ArrayList<>(3));
            ids.add(product.getProductId());
            OrdersDetail ordersDetail = getOrdersDetail(orders,orderItem,product);
            calcOrderPrice(orders, ordersDetail);
            details.add(ordersDetail);
        }
        if(orderSubmit.getCouponId() != null) {
            placeOrderCheckAspect.checkCoupon(orderSubmit.getCouponId());
            Coupon coupon = couponService.getById(orderSubmit.getCouponId());
            calcOrderPrice(orders, coupon);
            orderSubmit.setCouponId(orderSubmit.getCouponId());
            couponService.useCoupon(orderSubmit.getCouponId());
        }
        //减库存
        sizeProdsMap.forEach((k,v)-> productService.reduceStockBatch(v,k));
        addAll(Cl.list(orders), details);
        return orders.getOrdersId();
    }

    @Override
    @Transactional
    public void removeOrder(Long orderId) {
        Orders orders = getById(orderId);
        if (!orders.getUserId().equals(user.getId())) {
            throw new CustomException("越权访问");
        }
        if (OrderStatus.INVALID.equal(orders.getOrdersStatus()) || OrderStatus.FINISH.equal(orders.getOrdersStatus())) {
            detailService.removeByOrderId(orderId);
        } else {
            throw new CustomException("该订单不能删除");
        }
        removeById(orderId);
    }

    @Override
    @Transactional
    public void addAll(Collection<Orders> orders, Collection<OrdersDetail> details) {
        boolean s1 = saveBatch(orders);
        boolean s2 = detailService.saveBatch(details);
        if (!s1 || !s2) {
            throw new CustomException("下单失败");
        }
    }

    @Override
    public List<MonthIncomeVO> getPerMonthIncome(MonthIncomeDTO dto) {
        if (dto.getEndMonth() - dto.getStartMonth() > 5) {
            throw new CustomException("最多查询五个月");
        }
        int thisYear = Times.localNow().getYear();
        if (dto.getYear() > thisYear || thisYear - dto.getYear() > 3) {
            throw new CustomException("仅允许查询近三年的数据");
        }
        return ordersMapper.sumPerMonthIncome(dto);
    }

    //system interface

    @Override
    public void finishOrder(Long orderId) {
        Orders orders = new Orders();
        orders.setOrdersId(orderId);
        orders.setOrdersStatus(OrderStatus.FINISH.code());
        orders.setFinishTime(Times.localNow());
        getBaseMapper().updateById(orders);
    }

    @Override
    public void invalidateOrder(Long orderId) {
        Orders orders = new Orders();
        orders.setOrdersId(orderId);
        orders.setOrdersStatus(OrderStatus.INVALID.code());
        getBaseMapper().updateById(orders);
        rollbackStock(orderId);
    }

    @Override
    public void chargeback(Long orderId) {
        Orders orders = new Orders();
        orders.setOrdersId(orderId);
        orders.setOrdersStatus(OrderStatus.HAS_REFUND.code());
        getBaseMapper().updateById(orders);
        rollbackStock(orderId);
    }
}
