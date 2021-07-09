package jmu.shijh.tinymall.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * null
 * @TableName collection
 */
@TableName(value ="collection")
@Data
public class Collection implements Serializable {
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

    /**
     * 商家id
     */
    private Long shopId;

    /**
     * 收藏用户id
     */
    private Long userId;

    /**
     * 收藏时间
     */
    private LocalDateTime createTime;

    /**
     * 状态 0-正常 1-失效
     */
    private Integer collStatus;

    /**
     * 删除 1-true
     */
    private Boolean deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}