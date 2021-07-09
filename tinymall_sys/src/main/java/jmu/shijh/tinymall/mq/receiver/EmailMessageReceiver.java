package jmu.shijh.tinymall.mq.receiver;

import com.rabbitmq.client.Channel;
import jmu.shijh.tinymall.common.util.Cl;
import jmu.shijh.tinymall.domain.dto.EmailContent;
import jmu.shijh.tinymall.mq.config.EmailMQ;
import jmu.shijh.tinymall.service.EmailService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EmailMessageReceiver {

    @Autowired
    private EmailService emailService;

    @RabbitListener(queues = EmailMQ.EMAIL_QUEUE_NAME)
    public void send(Channel channel, Message message, @Payload EmailContent emailContent) throws IOException {
        emailService.sendSimpleEmail(emailContent.getTitle(), emailContent.getContent(),
                emailContent.getTo().toArray(Cl.emptyArr(String.class)));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
