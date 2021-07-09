package jmu.shijh.tinymall.mq.config;

public class CouponMQ {
    // 延迟队列 TTL 名称
    public static final String COUPON_DELAY_QUEUE_NAME = "coupon.delay.queue";

    // routing key 名称
    public static final String COUPON_DELAY_ROUTING_KEY = "coupon_delay";
}
