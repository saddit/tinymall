package jmu.shijh.tinymall.mq.receiver;

import com.rabbitmq.client.Channel;
import jmu.shijh.tinymall.common.util.Times;
import jmu.shijh.tinymall.domain.entity.Activity;
import jmu.shijh.tinymall.domain.entity.Coupon;
import jmu.shijh.tinymall.domain.entity.Shop;
import jmu.shijh.tinymall.domain.enums.ActivityStatus;
import jmu.shijh.tinymall.mq.config.ActivityMQ;
import jmu.shijh.tinymall.mq.config.OrderMQ;
import jmu.shijh.tinymall.service.ActivityService;
import jmu.shijh.tinymall.service.CouponService;
import jmu.shijh.tinymall.service.ShopService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Component
public class ActivityMessageReceiver {
    @Autowired
    private ActivityService activityService;
    @Autowired
    private CouponService couponService;
    @Autowired
    private ShopService shopService;

    @RabbitListener(queues = ActivityMQ.ACTIVITY_DELAY_QUEUE_NAME)
    @Transactional
    public void invalidActivity(Message message, Channel channel, @Payload Long aid) throws IOException {
        Activity ac = activityService.getById(aid);
        if(ac != null) {
            if (ActivityStatus.DEPLOYED.equal(ac.getActivityStatus()) && ac.getExpiredTime().isBefore(Times.localNow())) {
                ac.setActivityStatus(ActivityStatus.INVALID.code());
                activityService.updateById(ac);
                shopService.lambdaUpdate().eq(Shop::getActivityId,aid).set(Shop::getActivityId, null).update();
                couponService.lambdaUpdate().eq(Coupon::getActivityId,aid).remove();
            }
        }
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
