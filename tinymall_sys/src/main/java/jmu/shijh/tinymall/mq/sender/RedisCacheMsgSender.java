package jmu.shijh.tinymall.mq.sender;

import jmu.shijh.tinymall.domain.dto.EmailContent;
import jmu.shijh.tinymall.domain.dto.RedisCache;
import jmu.shijh.tinymall.mq.config.EmailMQ;
import jmu.shijh.tinymall.mq.config.RabbitMqConfig;
import jmu.shijh.tinymall.mq.config.RedisMQ;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RedisCacheMsgSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(RedisCache cache) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.TOPIC_EXCHANGE_NAME, RedisMQ.REDIS_ROUTING_KEY, cache);
    }
}
