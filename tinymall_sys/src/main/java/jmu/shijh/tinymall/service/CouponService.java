package jmu.shijh.tinymall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jmu.shijh.tinymall.domain.dto.CouponDTO;
import jmu.shijh.tinymall.domain.entity.Coupon;
import jmu.shijh.tinymall.domain.vo.CouponVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CouponService extends IService<Coupon> {
    List<CouponVO> queryCoupon(CouponDTO dto);

    @Transactional
    void addCoupon(Coupon coupon);

    @Transactional
    void addCoupons(List<Coupon> coupons);

    void removeCoupon(Long couponId);

    @Transactional
    void updateCoupon(Coupon coupon);

    List<Coupon> getActivityCoupons(Long activityId);

    @Transactional
    void updateUserCoupon(CouponVO couponVO);

    @Transactional
    void useCoupon(Long couponId);

    @Transactional
    void rushCoupon(Long couponId);
}
