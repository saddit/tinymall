package jmu.shijh.tinymall.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * null
 * @TableName shop
 */
@TableName(value ="shop")
@Data
public class Shop implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long shopId;

    /**
     * 
     */
    private String shopIcon;

    /**
     * 
     */
    private String shopName;

    /**
     * 
     */
    private Long userId;

    /**
     * 
     */
    private Long activityId;

    /**
     * 店铺分类
     */
    private Long categoryId;

    /**
     * 店铺描述
     */
    private String shopDesc;

    private String alipayId;

    private String wepayId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}