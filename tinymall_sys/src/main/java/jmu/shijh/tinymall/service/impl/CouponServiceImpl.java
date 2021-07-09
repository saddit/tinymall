package jmu.shijh.tinymall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jmu.shijh.tinymall.common.exception.CustomException;
import jmu.shijh.tinymall.common.util.Times;
import jmu.shijh.tinymall.domain.dto.CouponDTO;
import jmu.shijh.tinymall.domain.entity.Activity;
import jmu.shijh.tinymall.domain.entity.Coupon;
import jmu.shijh.tinymall.domain.enums.ActivityStatus;
import jmu.shijh.tinymall.domain.vo.CouponVO;
import jmu.shijh.tinymall.shiro.UserIdentity;
import jmu.shijh.tinymall.mapper.ActivityMapper;
import jmu.shijh.tinymall.mapper.CouponMapper;
import jmu.shijh.tinymall.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

/**
 * 1. 优惠券领取
 * 2. 优惠券使用
 * 3. 优惠券添加
 * 4. 优惠券删除
 * 5. 优惠券查询
 */
@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements CouponService {

    @Autowired
    private UserIdentity user;
    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private ActivityMapper activityMapper;

    @Override
    public List<Coupon> getActivityCoupons(Long activityId) {
        return lambdaQuery().eq(Coupon::getActivityId, activityId).list();
    }

    @Override
    public List<CouponVO> queryCoupon(CouponDTO dto) {
        return couponMapper.selectCouponByDTO(dto);
    }

    @Override
    @Transactional
    public void addCoupon(Coupon coupon) {
        Long activityId = coupon.getActivityId();
        Activity activity = activityMapper.selectById(activityId);
        checkActivity(activity);
        boolean s = save(coupon);
        if (!s) {
            throw new CustomException("添加失败");
        }
    }

    @Override
    @Transactional
    public void addCoupons(List<Coupon> coupons) {
        Long activityId = coupons.get(0).getActivityId();
        Activity activity = activityMapper.selectById(activityId);
        checkActivity(activity);
        boolean s = saveBatch(coupons);
        if (!s) {
            throw new CustomException("添加失败");
        }
    }

    private void checkActivity(Activity activity) {
        if (activity == null || ActivityStatus.INVALID.equal(activity.getActivityStatus())) {
            throw new CustomException("活动不存在或者已过期");
        }
        if (!activity.getDeployUid().equals(user.getUserId())) {
            throw new CustomException("无授权访问");
        }
    }

    @Override
    public void removeCoupon(Long couponId) {
        Coupon coupon = getById(couponId);
        Activity activity = activityMapper.selectById(coupon.getActivityId());
        if (activity == null || !ActivityStatus.UNDEPLOY.equal(activity.getActivityStatus())) {
            throw new CustomException("活动已发布或失效，不允许更改");
        }
        if (!activity.getDeployUid().equals(user.getId())) {
            throw new CustomException("越权访问");
        }
        boolean b = removeById(couponId);
        if (!b) {
            throw new CustomException("删除失败");
        }
    }

    @Override
    @Transactional
    public void updateCoupon(Coupon coupon) {
        Long activityId = coupon.getActivityId();
        Activity activity = activityMapper.selectById(activityId);
        if (activity == null || ActivityStatus.INVALID.equal(activity.getActivityStatus())) {
            throw new CustomException("活动不存在或者已过期");
        }

        if (ActivityStatus.DEPLOYED.equal(activity.getActivityStatus())) {
            throw new CustomException("活动已发布，不允许更改");
        }
        boolean b = updateById(coupon);
        if (!b) throw new CustomException("更新失败");
    }

    @Override
    @Transactional
    public void updateUserCoupon(CouponVO couponVO) {
        int i = couponMapper.updateUserCoupon(couponVO);
        if (i == 0) {
            throw new CustomException("更新用户优惠券信息失败");
        }
    }


    @Override
    @Transactional
    public void useCoupon(Long couponId) {
        CouponVO couponVO = couponMapper.selectUserCoupon(user.getUserId(), couponId);
        if (couponVO == null || couponVO.getUserCount() == 0) {
            throw new CustomException("优惠券没有了");
        }
        couponVO.setUserCount(couponVO.getUserCount() - 1);
        updateUserCoupon(couponVO);
    }

    @Override
    @Transactional
    public void rushCoupon(Long couponId) {
        Coupon coupon = couponMapper.selectById(couponId);

        if (coupon == null) {
            throw new CustomException("优惠券不存在");
        }

        Activity activity = activityMapper.selectById(coupon.getActivityId());
        if (activity.getExpiredTime().isBefore(Times.localNow())) {
            throw new CustomException("活动已过期，优惠券失效");
        }
        Integer couponCount = coupon.getCount();
        if (couponCount.equals(0)) {
            throw new CustomException("优惠券已经被抢完了");
        }

        CouponVO couponVO = couponMapper.selectUserCoupon(user.getId(), couponId);
        if (couponVO != null) {
            if (couponVO.getMaxCount().equals(coupon.getUserLimit())) {
                throw new CustomException("领取数量上限");
            }
            couponVO.setMaxCount(couponVO.getMaxCount() + 1);
            couponVO.setUserCount(couponVO.getUserCount() + 1);
            updateUserCoupon(couponVO);
        } else {
            couponMapper.insertUserCoupon(user.getId(), couponId);
        }

        //count = -1 即无限量
        if (!couponCount.equals(-1)) {
            coupon.setCount(couponCount - 1);
            couponMapper.updateById(coupon);
        }
    }
}
