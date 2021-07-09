package jmu.shijh.tinymall.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * null
 * @TableName shop_car
 */
@TableName(value ="shop_car")
@Data
public class ShopCar implements Serializable {
    /**
     * 购物车id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long carId;

    /**
     * 买家id
     */
    private Long userId;

    /**
     * 商品id
     */
    private Long productId;

    /**
     * 卖家id
     */
    private Long shopId;

    /**
     * 购物车预览图地址
     */
    private String productImg;

    /**
     * 加入时间
     */
    private LocalDateTime createTime;

    /**
     * 失效 1-true
     */
    private Boolean valid;

    /**
     * 删除 1-true
     */
    private Boolean deleted;

    /**
     * 加入购物车时的价格
     */
    private BigDecimal productPrice;

    /**
     * 失效时间
     */
    private LocalDateTime validTime;

    /**
     * 商品型号
     */
    private String productType;

    /**
     * 购买数量
     */
    private Integer productSize;

    /**
     * 分类id
     */
    private Long categoryId;

    private String productName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}