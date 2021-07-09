package jmu.shijh.tinymall.service;

import jmu.shijh.tinymall.common.util.PageDTO;
import jmu.shijh.tinymall.common.util.PageVO;
import jmu.shijh.tinymall.domain.dto.*;
import jmu.shijh.tinymall.domain.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;
import jmu.shijh.tinymall.domain.entity.OrdersDetail;
import jmu.shijh.tinymall.domain.vo.MonthIncomeVO;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shijh
 * @since 2021-05-20
 */
public interface OrdersService extends IService<Orders> {

    PageVO pagingQueryOrder(OrderDTO dto, PageDTO page);

    PageVO pagingQueryPreOrder(OrderDTO dto, PageDTO page);

    List<String> getRelationUserId(OrderDTO dto);

    void receiveOrder(Long orderId);

    List<MonthIncomeVO> getPerMonthIncome(MonthIncomeDTO dto);

    /**
     * 通过Id查找订单
     * @param orderId 订单id
     * @return 订单实体
     */
    Orders getOrderById(Long orderId);

    void expressOrder(OrderExpress orderExpress);

    void cancelOrder(Long orderId);

    void payOrder(Orders orders, PayInfo payInfo);

    void rollbackStock(Long orderId);

    void requestRefundOrder(Long ordersId);

    void agreeRefundOrder(Orders orders);

    List<Long> placeOrderBatch(List<OrderSubmit> submits);

    Long placeOrder(OrderSubmit orders);

    void removeOrder(Long orderId);

    void addAll(Collection<Orders> list, Collection<OrdersDetail> details);
}
