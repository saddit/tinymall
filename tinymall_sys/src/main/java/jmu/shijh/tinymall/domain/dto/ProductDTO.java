package jmu.shijh.tinymall.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class ProductDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String keyword;

    private Integer minPrice;
    private Integer maxPrice;
    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 商品参与的活动id
     */
    private Long activityId;

    /**
     * 所属商店id
     */
    private Long shopId;

    /**
     * 状态 0-正常 1-未发布 2-失效
     */
    private Integer productStatus;

    private LocalDateTime createTimeStart;
    private LocalDateTime createTimeEnd;

    /**
     * 上架时间
     */
    private LocalDateTime deployTimeStart;
    private LocalDateTime deployTimeEnd;
}
