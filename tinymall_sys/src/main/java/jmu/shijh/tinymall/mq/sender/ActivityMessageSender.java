package jmu.shijh.tinymall.mq.sender;


import jmu.shijh.tinymall.mq.config.OrderMQ;
import jmu.shijh.tinymall.mq.config.RabbitMqConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActivityMessageSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void delaySendOrder(final Long aid, final Long delay) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.DELAY_EXCHANGE_NAME, OrderMQ.ORDER_DELAY_ROUTING_KEY, aid, message -> {
            message.getMessageProperties().setDelay(delay.intValue());
            return message;
        });
    }
}
