package jmu.shijh.tinymall.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OrdersDetailVO implements Serializable {
    private Long detailId;
    private Long ordersId;
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

    private String productName;

    //VIEW OBJECT
    private String productImg;
}
