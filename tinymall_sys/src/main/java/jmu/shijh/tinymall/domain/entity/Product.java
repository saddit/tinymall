package jmu.shijh.tinymall.domain.entity;

import com.alibaba.fastjson.annotation.JSONField;
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
 * @TableName product
 */
@TableName(value ="product")
@Data
public class Product implements Serializable {

    @TableId
    private Long productId;

    /**
     * 商品名
     */
    private String productName;

    /**
     * 商品描述
     */
    private String productDesc;

    /**
     * 商品图册地址，逗号分隔，最多五个
     */
    private String productPic;

    /**
     * 预览图地址
     */
    private String previewImg;

    /**
     * 库存量
     */
    private Integer stock;

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 所属商店id
     */
    private Long shopId;

    private String introPage;

    /**
     * 状态 0-正常 1-未发布 2-失效
     */
    private Integer productStatus;

    /**
     * 原定价
     */
    private BigDecimal originalPrice;

    /**
     * 销售价
     */
    private BigDecimal salePrice;

    /**
     * 商品型号，逗号分割
     */
    private String productTypes;

    /**
     * 商品标签，逗号分割，用于搜索最多10个
     */
    private String productTags;

    /**
     * 商品生产公司名
     */
    private String productCompany;

    /**
     * 删除 0-false
     */
    private Boolean deleted;

    /**
     * 
     */
    private LocalDateTime createTime;

    /**
     * 上架时间
     */
    private LocalDateTime deployTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}