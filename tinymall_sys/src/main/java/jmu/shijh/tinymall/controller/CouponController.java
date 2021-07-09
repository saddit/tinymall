package jmu.shijh.tinymall.controller;


import jmu.shijh.tinymall.common.annotation.ArrayParamChecks;
import jmu.shijh.tinymall.common.annotation.ParamCheck;
import jmu.shijh.tinymall.common.annotation.ParamChecks;
import jmu.shijh.tinymall.common.exception.CustomException;
import jmu.shijh.tinymall.common.util.JsonResp;
import jmu.shijh.tinymall.common.util.R;
import jmu.shijh.tinymall.domain.dto.ActivityDTO;
import jmu.shijh.tinymall.domain.dto.CouponDTO;
import jmu.shijh.tinymall.domain.entity.Coupon;
import jmu.shijh.tinymall.domain.entity.Shop;
import jmu.shijh.tinymall.domain.vo.CouponVO;
import jmu.shijh.tinymall.service.ActivityService;
import jmu.shijh.tinymall.service.CouponService;
import jmu.shijh.tinymall.service.ShopService;
import jmu.shijh.tinymall.shiro.UserIdentity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author shijh
 * @since 2021-06-08
 */
@RestController
@RequestMapping("/coupon")
public class CouponController {
    @Autowired
    private CouponService couponService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private UserIdentity user;

    @GetMapping("/query/activity/{activityId}")
    public JsonResp getCoupon(@PathVariable Long activityId) {
        List<Coupon> coupons = couponService.getActivityCoupons(activityId);
        return R.ok().data(coupons).build();
    }

    @GetMapping("/query/shop/{shopId}")
    public JsonResp getCouponByShopId(@PathVariable Long shopId) {
        Shop shop = shopService.getShop(shopId);
        if (shop.getActivityId() == null) {
            throw new CustomException("店铺没有参加活动");
        }
        List<Coupon> coupons = couponService.getActivityCoupons(shop.getActivityId());
        return R.ok().data(coupons).build();
    }

    @PostMapping("/query/user")
    @RequiresPermissions("coupon:select")
    public JsonResp getCoupon(@RequestBody CouponDTO dto) {
        dto.setUserId(user.getId());
        List<CouponVO> coupons = couponService.queryCoupon(dto);
        return R.ok().data(coupons).build();
    }

    @GetMapping("/receive/{couponId}")
    @RequiresPermissions("coupon:get")
    public JsonResp rushCoupon(@PathVariable("couponId") Long couponId) {
        couponService.rushCoupon(couponId);
        return R.ok().msg("领取成功").build();
    }

    @GetMapping("/delete/{couponId}")
    @RequiresPermissions("coupon:delete")
    public JsonResp deleteCoupon(@PathVariable Long couponId) {
        couponService.removeCoupon(couponId);
        return R.ok().msg("删除成功").build();
    }

    @PostMapping("/add")
    @RequiresPermissions("coupon:insert")
    @ParamCheck(exclude = {"deleted","couponId"},value = "优惠券信息填写不全")
    public JsonResp addCoupon(@RequestBody Coupon coupon) {
        couponService.addCoupon(coupon);
        return R.ok().msg("添加成功").build();
    }

    @PostMapping("/add/batch")
    @RequiresPermissions("coupon:insert")
    @ArrayParamChecks(targets = "coupons", value = {
            @ParamCheck(exclude = {"deleted","couponId"},value = "优惠券信息填写不全")
    })
    public JsonResp addCoupon(@RequestBody List<Coupon> coupons) {
        couponService.addCoupons(coupons);
        return R.ok().msg("添加成功").build();
    }
    @PostMapping("/update")
    @RequiresPermissions("coupon:update")
    public JsonResp updateCoupon(@RequestBody Coupon coupon) {
        couponService.updateCoupon(coupon);
        return R.ok().msg("更新成功").build();
    }

}
