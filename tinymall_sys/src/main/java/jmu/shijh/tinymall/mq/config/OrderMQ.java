package jmu.shijh.tinymall.mq.config;

public class OrderMQ {
    // 延迟队列 TTL 名称
    public static final String ORDER_DELAY_QUEUE_NAME = "order.delay.queue";

    // routing key 名称
    // 具体消息发送在该 routingKey 的
    public static final String ORDER_DELAY_ROUTING_KEY = "order_delay";

    public static final String ORDER_CANCEL_TYPE = "order_cancel";
    public static final String ORDER_FINISH_TYPE = "order_finish";
    public static final String ORDER_REFUND_TYPE = "order_refund";
}
