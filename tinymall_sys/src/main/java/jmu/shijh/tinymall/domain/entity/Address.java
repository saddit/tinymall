package jmu.shijh.tinymall.domain.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import lombok.Data;

/**
 * null
 * @TableName address
 */
@TableName(value ="address")
@Data
public class Address implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private Long addressId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 收货人姓名
     */
    private String consignee;

    /**
     * 收货人电话
     */
    private String consigneePhone;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 县区
     */
    private String country;

    /**
     * 详细地址
     */
    private String street;

    /**
     * 默认地址 1-true
     */
    private Boolean isDefault;

    private Boolean deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}