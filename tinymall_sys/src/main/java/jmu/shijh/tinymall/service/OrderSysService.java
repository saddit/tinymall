package jmu.shijh.tinymall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jmu.shijh.tinymall.domain.entity.Orders;

public interface OrderSysService extends IService<Orders> {
    /**
     *  系统调用(RabbitMQ),不开放至controller
     */
    void finishOrder(Long orderId);

    /**
     *  系统调用(RabbitMQ),不开放至controller
     */
    void invalidateOrder(Long orderId);

    /**
     *  系统调用(RabbitMQ),不开放至controller
     */
    void chargeback(Long orderId);
}
