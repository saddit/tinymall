package jmu.shijh.tinymall.mq.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMqConfig {

    public static final String DELAY_EXCHANGE_NAME = "delay_exchange";
    public static final String TOPIC_EXCHANGE_NAME = "topic_exchange";

    @Bean("delayExchange")
    public CustomExchange delayExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        //插件定义类型 x-delayed-message
        return new CustomExchange(DELAY_EXCHANGE_NAME, "x-delayed-message", true, false, args);
    }

    @Bean("topicExchange")
    public TopicExchange topicExchange() {
        Map<String, Object> args = new HashMap<>();
        return new TopicExchange(TOPIC_EXCHANGE_NAME,true,false, args);
    }

    @Bean("orderDelayQueue")
    public Queue orderDelayQueue() {
        return new Queue(OrderMQ.ORDER_DELAY_QUEUE_NAME, true, false, false);
    }

    @Bean("orderDelayBinding")
    public Binding orderDelayBinding(@Qualifier("delayExchange") CustomExchange exchange, @Qualifier("orderDelayQueue") Queue queue) {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with(OrderMQ.ORDER_DELAY_ROUTING_KEY)
                .noargs();
    }

    @Bean("activityDelayQueue")
    public Queue activityDelayQueue() {
        return new Queue(ActivityMQ.ACTIVITY_DELAY_QUEUE_NAME, true, false, false);
    }

    @Bean("activityDelayBinding")
    public Binding activityDelayBinding(@Qualifier("delayExchange") CustomExchange exchange, @Qualifier("activityDelayQueue") Queue queue) {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with(ActivityMQ.ACTIVITY_DELAY_ROUTING_KEY)
                .noargs();
    }

    @Bean("redisCacheQueue")
    public Queue redisCacheQueue() {
        return new Queue(RedisMQ.REDIS_QUEUE_NAME, false, false, true);
    }

    @Bean("redisCacheBinding")
    public Binding redisCacheQueue(@Qualifier("redisCacheQueue") Queue queue, @Qualifier("topicExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue)
                .to(topicExchange())
                .with(RedisMQ.REDIS_ROUTING_KEY);
    }

    @Bean("emailSendQueue")
    public Queue emailSendQueue() {
        return new Queue(EmailMQ.EMAIL_QUEUE_NAME,false, false, true);
    }

    @Bean("emailSendBinding")
    public Binding emailSendBinding(@Qualifier("topicExchange") TopicExchange exchange, @Qualifier("emailSendQueue") Queue queue) {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with(EmailMQ.EMAIL_ROUTING_KEY);
    }
}
