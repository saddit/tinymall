package jmu.shijh.tinymall.mq.sender;

import jmu.shijh.tinymall.domain.dto.EmailContent;
import jmu.shijh.tinymall.mq.config.EmailMQ;
import jmu.shijh.tinymall.mq.config.RabbitMqConfig;
import jmu.shijh.tinymall.service.EmailService;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailMessageSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;


    public void send(EmailContent emailContent) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.TOPIC_EXCHANGE_NAME, EmailMQ.EMAIL_ROUTING_KEY, emailContent);
    }

}
