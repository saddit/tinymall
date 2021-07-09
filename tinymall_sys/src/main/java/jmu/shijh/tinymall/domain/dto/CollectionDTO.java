package jmu.shijh.tinymall.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class CollectionDTO implements Serializable {
    /**
     * 收藏id
     */
    private Long collId;

    /**
     * 分类id
     */
    private Long categoryId;

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
    private LocalDateTime createTimeStart;
    private LocalDateTime createTimeEnd;

    /**
     * 状态 0-正常 1-失效
     */
    private Integer collStatus;

    private String keyword;
}
