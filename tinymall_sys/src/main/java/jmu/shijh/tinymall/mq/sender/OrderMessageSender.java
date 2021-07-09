package jmu.shijh.tinymall.mq.sender;

import jmu.shijh.tinymall.common.util.Times;
import jmu.shijh.tinymall.mq.config.OrderMQ;
import jmu.shijh.tinymall.mq.config.RabbitMqConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderMessageSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void delayCancelOrder(final Long orderId) {
        delaySendOrder(orderId, Times.DAY, OrderMQ.ORDER_CANCEL_TYPE);
    }

    public void delayFinishOrder(final Long orderId) {
        delaySendOrder(orderId, Times.DAY * 7, OrderMQ.ORDER_FINISH_TYPE);
    }

    public void delaySendOrder(final Long orderId, final Long delay, String type) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.DELAY_EXCHANGE_NAME, OrderMQ.ORDER_DELAY_ROUTING_KEY, orderId, message -> {
            message.getMessageProperties().setDelay(delay.intValue());
            message.getMessageProperties().setType(type);
            return message;
        });
    }
}