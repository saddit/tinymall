package jmu.shijh.tinymall.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CollectionVO implements Serializable {
    /**
     * 收藏id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long collId;

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 商品id
     */
    private Long productId;

    //VIEW OBJECT
    private String productName;
    private String productImg;
    private BigDecimal productPrice;
    private String shopName;

    /**
     * 商家id
     */
    private Long shopId;

    /**
     * 收藏时间
     */
    private LocalDateTime createTime;

    /**
     * 状态 0-正常 1-失效
     */
    private Integer collStatus;

}
