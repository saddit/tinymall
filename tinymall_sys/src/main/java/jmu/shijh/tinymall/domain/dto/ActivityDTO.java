package jmu.shijh.tinymall.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class ActivityDTO implements Serializable {

    private Long activityId;

    private LocalDateTime createTimeStart;
    private LocalDateTime createTimeEnd;

    /**
     * 过期时间
     */
    private LocalDateTime expiredTimeStart;
    private LocalDateTime expiredTimeEnd;

    /**
     * 发布者id
     */
    private Long deployUid;
    /**
     * 商店id
     */
    private Long shopId;

    /**
     * 0-未发布 1-已发布 2-已失效
     */
    private Integer activityStatus;

    private String keyword;
}
