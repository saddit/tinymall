package jmu.shijh.tinymall.service.impl;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jmu.shijh.tinymall.common.exception.CustomException;
import jmu.shijh.tinymall.common.util.*;
import jmu.shijh.tinymall.domain.dto.ActivityDTO;
import jmu.shijh.tinymall.domain.dto.ActivityUpdate;
import jmu.shijh.tinymall.domain.entity.Activity;
import jmu.shijh.tinymall.domain.entity.Coupon;
import jmu.shijh.tinymall.domain.entity.Shop;
import jmu.shijh.tinymall.domain.enums.ActivityStatus;
import jmu.shijh.tinymall.domain.vo.ActivityVO;
import jmu.shijh.tinymall.mapper.CouponMapper;
import jmu.shijh.tinymall.mq.sender.ActivityMessageSender;
import jmu.shijh.tinymall.service.CouponService;
import jmu.shijh.tinymall.service.ShopService;
import jmu.shijh.tinymall.shiro.UserIdentity;
import jmu.shijh.tinymall.service.ActivityService;
import jmu.shijh.tinymall.mapper.ActivityMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

/**
 *
 */
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements ActivityService {

    @Autowired
    private UserIdentity user;
    @Autowired
    private CouponService couponService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ActivityMessageSender activityMessageSender;

    @Override
    @Transactional
    public void removeActivity(Long activityId) {
        Activity activity = getById(activityId);
        if (activity == null || ActivityStatus.DEPLOYED.equal(activity.getActivityStatus())) {
            throw new CustomException("活动不存在或者已发布");
        }
        if (!activity.getDeployUid().equals(user.getId())) {
            throw new CustomException("越权访问");
        }
        couponService.lambdaUpdate().eq(Coupon::getActivityId, activityId).remove();
        removeById(activityId);
    }

    @Override
    public List<Activity> getByIds(Long... ids) {
        return getBaseMapper().selectBatchIds(Cl.list(ids));
    }

    @Override
    public Long addActivity(Activity activity) {
        activity.setDeployUid(user.getId());
        activity.setActivityStatus(ActivityStatus.UNDEPLOY.code());
        save(activity);
        return activity.getActivityId();
    }

    private LambdaQueryChainWrapper<Activity> dtoQueryWrapper(ActivityDTO dto) {
        return lambdaQuery().eq(Str.notEmpty(dto.getActivityStatus()), Activity::getActivityStatus, dto.getActivityStatus())
                .eq(Str.notEmpty(dto.getDeployUid()), Activity::getDeployUid, dto.getDeployUid())
                .ge(Str.notEmpty(dto.getCreateTimeStart()), Activity::getCreateTime, dto.getCreateTimeStart())
                .lt(Str.notEmpty(dto.getCreateTimeEnd()), Activity::getCreateTime, dto.getCreateTimeEnd())
                .ge(Str.notEmpty(dto.getExpiredTimeStart()), Activity::getExpiredTime, dto.getCreateTimeStart())
                .lt(Str.notEmpty(dto.getExpiredTimeEnd()), Activity::getExpiredTime, dto.getExpiredTimeEnd());
    }

    private LambdaQueryChainWrapper<Activity> keywordQueryWrapper(String keyword) {
        return lambdaQuery().like(Activity::getActivityTitle, keyword)
                .like(Activity::getContent, keyword);
    }

    @Override
    public PageVO pagingQueryPage(ActivityDTO dto, PageDTO page) {
        Page<Activity> activityPage = new Page<>();
        activityPage.setCurrent(page.getPageNum())
                .setSize(page.getPageSize());
        if(page.getOrderBy() != null) {
            activityPage.setOrders(Cl.list(page.getIsDesc() ? OrderItem.desc(page.orderBy()) : OrderItem.asc(page.orderBy())));
        }
        Page<Activity> resPage;
        if (Str.notEmpty(dto.getKeyword())) {
            resPage = keywordQueryWrapper(dto.getKeyword()).page(activityPage);
        } else {
            resPage = dtoQueryWrapper(dto).page(activityPage);
        }
        return new PageVO(resPage);
    }

    @Override
    @Transactional
    public void updateByDTO(ActivityDTO dto, Activity activity) {
        update(activity, dtoQueryWrapper(dto));
    }

    @Override
    public void updateActivity(ActivityUpdate activity) {
        activity.setActivityStatus(null);
        Activity current = getById(activity.getActivityId());
        if (!current.getDeployUid().equals(user.getId())) {
            throw new CustomException("越权访问");
        }
        if (ActivityStatus.DEPLOYED.equal(current.getActivityStatus())) {
            if (activity.getExpiredTime().isAfter(current.getExpiredTime())) {
                BeanUtils.copyProperties(activity,current);
                Duration delay = Duration.between(current.getExpiredTime(), current.getDeployTime());
                activityMessageSender.delaySendOrder(current.getActivityId(), delay.getSeconds() * Times.SEC);
            } else {
                throw new CustomException("已发布活动仅允许延长活动时间");
            }
            updateById(current);
        } else if (ActivityStatus.UNDEPLOY.equal(current.getActivityStatus())) {
            BeanUtils.copyProperties(activity,current);
            updateById(current);
        } else {
            throw new CustomException("活动已失效");
        }
    }

    @Override
    public ActivityVO getActivityDetail(Long id) {
        return getBaseMapper().selectByActivityId(id);
    }
}




