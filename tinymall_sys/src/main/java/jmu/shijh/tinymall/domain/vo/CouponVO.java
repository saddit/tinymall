package jmu.shijh.tinymall.domain.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CouponVO implements Serializable {
    private Long couponId;

    private Long activityId;

    /**
     * 折扣类型 0-满减 1-百分比
     */
    private Integer couponType;

    /**
     * 折扣梯度 满足则应用一次规则
     */
    private Integer couponStep;

    /**
     * 应用一次的折扣值，折扣类型为 1 则表示百分比
     */
    private Integer couponValue;

    private Boolean applyMulti;


    //user_coupon
    private Long ucId;
    private Integer maxCount;
    private Long userId;
    private  Integer userCount;
}
