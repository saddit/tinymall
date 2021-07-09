package jmu.shijh.tinymall.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShopCarSubmit implements Serializable {
    /**
     * 商品id
     */
    private Long productId;

    /**
     * 商品型号
     */
    private String productType;

    /**
     * 购买数量
     */
    private Integer productSize;
}
