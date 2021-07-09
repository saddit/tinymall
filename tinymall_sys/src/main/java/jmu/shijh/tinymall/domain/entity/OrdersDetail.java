package jmu.shijh.tinymall.domain.entity;

import java.math.BigDecimal;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author shijh
 * @since 2021-05-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrdersDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long detailId;

    /**
     * 订单编号
     */
    private Long ordersId;

    /**
     * 商品编号
     */
    private Long productId;

    /**
     * 商品型号
     */
    private String productType;

    /**
     * 商品数量
     */
    private Integer productSize;

    /**
     * 参与活动id
     */
    private Long activityId;

    /**
     * 商品价格
     */
    private BigDecimal productPrice;

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 商品名
     */
    private String productName;

    private Boolean deleted;
}
