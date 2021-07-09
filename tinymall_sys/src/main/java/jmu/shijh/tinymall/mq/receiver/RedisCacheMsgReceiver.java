package jmu.shijh.tinymall.mq.receiver;

import com.rabbitmq.client.Channel;
import jmu.shijh.tinymall.domain.dto.RedisCache;
import jmu.shijh.tinymall.mq.config.OrderMQ;
import jmu.shijh.tinymall.mq.config.RedisMQ;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RedisCacheMsgReceiver {
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @RabbitListener(queues = RedisMQ.REDIS_QUEUE_NAME)
    public void receiveOrder(Message message, Channel channel, @Payload RedisCache cache) throws IOException {
        if (cache.getDuration() == null) {
            redisTemplate.opsForValue().set(cache.getKey(), cache.getValue());
        } else  {
            redisTemplate.opsForValue().set(cache.getKey(), cache.getValue(), cache.getDuration());
        }
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
