package jmu.shijh.tinymall.mq.receiver;

import com.rabbitmq.client.Channel;
import jmu.shijh.tinymall.domain.entity.Orders;
import jmu.shijh.tinymall.domain.enums.OrderStatus;
import jmu.shijh.tinymall.mq.config.OrderMQ;
import jmu.shijh.tinymall.service.OrderSysService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class OrderMessageReceiver {

    @Autowired
    private OrderSysService ordersService;

    private void cancelOrder(Long orderId) {
        Orders order = ordersService.getById(orderId);
        if(order == null) {
            return;
        }
        if (OrderStatus.NO_PAY.equal(order.getOrdersStatus())) {
            ordersService.invalidateOrder(orderId);
        }
    }

    private void refundOrder(Long ordersId) {
        Orders order = ordersService.getById(ordersId);
        if (order == null) return;
        if(OrderStatus.WAIT_REFUND.equal(order.getOrdersStatus())
                || OrderStatus.WAIT_EXPR.equal(order.getOrdersStatus())) {
            ordersService.chargeback(ordersId);
        }
    }

    private void finishOrder(Long orderId) {
        Orders order = ordersService.getById(orderId);
        if (OrderStatus.WAIT_RECEIVE.equal(order.getOrdersStatus())) {
            ordersService.finishOrder(orderId);
        }
    }

    @RabbitListener(queues = OrderMQ.ORDER_DELAY_QUEUE_NAME)
    public void receiveOrder(Message message, Channel channel, @Payload Long orderId) throws IOException {
        switch (message.getMessageProperties().getType()) {
            case OrderMQ.ORDER_CANCEL_TYPE: cancelOrder(orderId);break;
            case OrderMQ.ORDER_FINISH_TYPE: finishOrder(orderId);break;
            case OrderMQ.ORDER_REFUND_TYPE: refundOrder(orderId);break;
            default: break;
        }
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
