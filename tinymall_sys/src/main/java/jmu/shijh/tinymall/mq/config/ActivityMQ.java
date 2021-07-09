package jmu.shijh.tinymall.mq.config;

public class ActivityMQ {
    // 延迟队列 TTL 名称
    public static final String ACTIVITY_DELAY_QUEUE_NAME = "activity.delay.queue";

    // routing key 名称
    // 具体消息发送在该 routingKey 的
    public static final String ACTIVITY_DELAY_ROUTING_KEY = "activity_delay";
}
