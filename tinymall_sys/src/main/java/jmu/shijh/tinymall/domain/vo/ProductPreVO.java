package jmu.shijh.tinymall.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ProductPreVO implements Serializable {
    /**
     *
     */
    private Long productId;

    /**
     * 商品名
     */
    private String productName;

    /**
     * 预览图地址
     */
    private String previewImg;

    /**
     * 库存量
     */
    private Integer stock;

    /**
     * 所属商店id
     */
    private Long shopId;
    private String shopName;

    private LocalDateTime deployTime;
    private LocalDateTime createTime;

    private Long categoryId;

    private Integer productStatus;

    /**
     * 原定价
     */
    private BigDecimal originalPrice;

    /**
     * 销售价
     */
    private BigDecimal salePrice;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
