package jmu.shijh.tinymall.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * null
 * @TableName coupon
 */
@TableName(value ="coupon")
@Data
public class Coupon implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long couponId;

    /**
     * 
     */
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

    /**
     * 1-true
     */
    private Boolean deleted;

    /**
     * 是否按照梯度多次应用 1-true
     */
    private Boolean applyMulti;

    /**
     * 用户领取限额
     */
    private Integer userLimit;

    /**
     * 优惠券数量 -1为无限制
     */
    private Integer count;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}