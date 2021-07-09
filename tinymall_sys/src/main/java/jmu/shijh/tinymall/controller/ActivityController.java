package jmu.shijh.tinymall.controller;


import jmu.shijh.tinymall.common.annotation.MultiRequestBody;
import jmu.shijh.tinymall.common.annotation.ParamCheck;
import jmu.shijh.tinymall.common.exception.CustomException;
import jmu.shijh.tinymall.common.util.*;
import jmu.shijh.tinymall.domain.dto.ActivityDTO;
import jmu.shijh.tinymall.domain.dto.ActivityUpdate;
import jmu.shijh.tinymall.domain.dto.RedisCache;
import jmu.shijh.tinymall.domain.entity.Activity;
import jmu.shijh.tinymall.domain.entity.Shop;
import jmu.shijh.tinymall.domain.enums.ActivityStatus;
import jmu.shijh.tinymall.domain.vo.ActivityVO;
import jmu.shijh.tinymall.mq.sender.ActivityMessageSender;
import jmu.shijh.tinymall.mq.sender.RedisCacheMsgSender;
import jmu.shijh.tinymall.service.ActivityService;
import jmu.shijh.tinymall.service.ShopService;
import jmu.shijh.tinymall.shiro.UserIdentity;
import org.apache.catalina.User;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.crypto.interfaces.PBEKey;
import java.time.Duration;
import java.time.ZoneOffset;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author shijh
 * @since 2021-06-08
 */
@RestController
@RequestMapping("/activity")
public class ActivityController {
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    private RedisCacheMsgSender redisCacheMsgSender;
    @Autowired
    private ActivityMessageSender activityMessageSender;
    @Autowired
    private UserIdentity user;

    @PostMapping("/query/page")
    public JsonResp getActivityPage(@MultiRequestBody(required = false) ActivityDTO dto,
                                    @MultiRequestBody(required = false) PageDTO page) {
        dto.setActivityStatus(ActivityStatus.DEPLOYED.code());
        PageVO pageVO = activityService.pagingQueryPage(dto, page);
        return R.ok().data(pageVO).build();
    }

    @PostMapping("/query/shop/page")
    @RequiresPermissions("shopinfo:select")
    public JsonResp getActivityPageShop(@MultiRequestBody(required = false) ActivityDTO dto,
                                        @MultiRequestBody(required = false) PageDTO page) {
        dto.setDeployUid(user.getId());
        PageVO pageVO = activityService.pagingQueryPage(dto, page);
        return R.ok().data(pageVO).build();
    }

    @GetMapping("/query/{aid}")
    public JsonResp getActivity(@PathVariable Long aid) {
        ActivityVO activityDetail = activityService.getActivityDetail(aid);
        if (ActivityStatus.DEPLOYED.equal(activityDetail.getActivityStatus())) {
            redisTemplate.opsForZSet().incrementScore(RedisKeys.ZSET_ACTIVITY_KEY, activityDetail.getActivityId(), 1);
        } else if (!activityDetail.getDeployUid().equals(user.getId())) {
            throw new CustomException("不可访问");
        }
        return R.ok().data(activityDetail).build();
    }

    @PostMapping("/deploy")
    @RequiresPermissions("activity:update")
    @ParamCheck(include = {"activityId", "expiredTime"})
    public JsonResp deploy(@RequestBody Activity ac) {
        Shop userShop = shopService.getUserShop();
        if (userShop.getActivityId() != null) {
            throw new CustomException("当前有活动正在进行");
        }
        Activity current = activityService.getById(ac.getActivityId());
        if (ActivityStatus.DEPLOYED.equal(current.getActivityStatus())) {
            throw new CustomException("不可重复发布");
        }
        current.setDeployTime(Times.localNow());
        current.setExpiredTime(ac.getExpiredTime());
        current.setActivityStatus(ActivityStatus.DEPLOYED.code());
        activityService.updateById(current);
        userShop.setActivityId(current.getActivityId());
        shopService.updateShop(userShop);
        //发送延时过期活动的消息到消息队列中
        Duration delay = Duration.between(ac.getExpiredTime(), ac.getDeployTime());
        activityMessageSender.delaySendOrder(current.getActivityId(), delay.getSeconds() * Times.SEC);
        return R.ok().msg("发布成功").build();
    }

    @PostMapping("/admin/deploy")
    @RequiresRoles("admin")
    @ParamCheck(include = {"activityId", "expiredTime"})
    public JsonResp deployAdmin(@RequestBody Activity ac) {
        Activity current = activityService.getById(ac.getActivityId());
        if (ActivityStatus.DEPLOYED.equal(current.getActivityStatus())) {
            throw new CustomException("不可重复发布");
        }
        current.setDeployTime(Times.localNow());
        current.setExpiredTime(ac.getExpiredTime());
        current.setActivityStatus(ActivityStatus.DEPLOYED.code());
        activityService.updateById(current);
        return R.ok().msg("发布成功").build();
    }

    @GetMapping("/query/hot/{count}")
    public JsonResp getHotActivity(@PathVariable(required = false) Integer count) {
        List<?> activities = (List<?>) redisTemplate.opsForValue().get(RedisKeys.getActivityHotKey());
        if (activities == null) {
            Set<Object> range = redisTemplate.opsForZSet().range(RedisKeys.ZSET_ACTIVITY_KEY, 0L, 10L);
            if (range != null && range.size() >= count) {
                activities = activityService.getByIds(range.toArray(new Long[0]));
                redisCacheMsgSender.send(new RedisCache()
                        .setKey(RedisKeys.getActivityHotKey())
                        .setValue(activities)
                        .setDuration(Duration.ofDays(1)));
            } else {
                activities = activityService.pagingQueryPage(
                        new ActivityDTO().setActivityStatus(ActivityStatus.DEPLOYED.code()),
                        new PageDTO().setPageNum(1).setPageSize(count % 11)
                ).getList();
            }
        }
        return R.ok().data(activities.size() > count ? activities.subList(0, count - 1) : activities).build();
    }

    @PostMapping("/add")
    @RequiresPermissions("activity:insert")
    @ParamCheck(include = {"activityTile", "content"})
    public JsonResp addActivity(@RequestBody(required = false) Activity activity) {
        Long id = activityService.addActivity(activity);
        return R.ok().data(id).build();
    }

    @PostMapping("/update")
    @ParamCheck(include = {"activityId"})
    @RequiresPermissions("activity:update")
    public JsonResp updateActivity(@RequestBody(required = false) ActivityUpdate activity) {
        activityService.updateActivity(activity);
        return R.ok().build();
    }

    @GetMapping("/delete/{activityId}")
    @RequiresPermissions("activity:delete")
    public JsonResp deleteActivity(@PathVariable Long activityId) {
        activityService.removeActivity(activityId);
        return R.ok().msg("删除成功").build();
    }
}
