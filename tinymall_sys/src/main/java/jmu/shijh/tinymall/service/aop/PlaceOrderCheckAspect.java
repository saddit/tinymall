package jmu.shijh.tinymall.service.aop;

import jmu.shijh.tinymall.common.exception.CustomException;
import jmu.shijh.tinymall.common.util.Cl;
import jmu.shijh.tinymall.common.util.Times;
import jmu.shijh.tinymall.domain.dto.OrderItem;
import jmu.shijh.tinymall.domain.dto.OrderSubmit;
import jmu.shijh.tinymall.domain.entity.*;
import jmu.shijh.tinymall.domain.enums.ActivityStatus;
import jmu.shijh.tinymall.domain.enums.ProductStatus;
import jmu.shijh.tinymall.domain.vo.CouponVO;
import jmu.shijh.tinymall.service.CouponService;
import jmu.shijh.tinymall.service.UserCouponService;
import jmu.shijh.tinymall.shiro.UserIdentity;
import jmu.shijh.tinymall.mapper.*;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlaceOrderCheckAspect {
    @Autowired
    private UserCouponService userCouponService;

    @Autowired
    private UserIdentity user;

    public void checkProduct(OrderSubmit order, OrderItem item, Product product) {
        if (!ProductStatus.NORMAL.equal(product.getProductStatus())) {
            throw new CustomException(product.getProductName() + "已经失效");
        }
        if (product.getStock().compareTo(item.getProductSize())<0) {
            throw new CustomException(product.getProductName() + "库存不足");
        }
        if (!Cl.list(product.getProductTypes().split(",")).contains(item.getProductType())) {
            throw new CustomException("不存在" + item.getProductType());
        }
        if (!product.getShopId().equals(order.getShopId())) {
            throw new CustomException(product.getProductName() + "不属于该商家");
        }
    }

    public void checkCoupon(Long couponId) {
        if(couponId != null) {
            Integer count = userCouponService.lambdaQuery()
                    .select(UserCoupon::getId)
                    .gt(UserCoupon::getCount, 0)
                    .eq(UserCoupon::getUserId, user.getId())
                    .eq(UserCoupon::getCouponId, couponId)
                    .count();
            if(count == 0) {
                throw new CustomException("优惠券不可用");
            }
        }
    }

    public void checkOrders(OrderSubmit orders) {
        if (orders.getAddressId() == null) {
            throw new CustomException("地址不能为空");
        }
        if (orders.getItems() == null || orders.getItems().isEmpty()) {
            throw new CustomException("订单不能为空");
        }
    }
}
