package jmu.shijh.tinymall.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author sjh
 */
@Data
public class CarItem implements Serializable {
    /**
     * 购物车id
     */
    private Long carId;

    /**
     * 商家id
     */
    private Long shopId;

    /**
     * 买家id
     */
    private Long userId;

    /**
     * 商品id
     */
    private Long productId;

    /**
     * 购物车预览图地址
     */
    private String productImg;

    /**
     * 失效 1-true
     */
    private Boolean valid;

    /**
     * 加入购物车时的价格
     */
    private BigDecimal productPrice;

    /**
     * 商品型号
     */
    private String productType;

    /**
     * 购买数量
     */
    private Integer productSize;

    private String productName;

}
