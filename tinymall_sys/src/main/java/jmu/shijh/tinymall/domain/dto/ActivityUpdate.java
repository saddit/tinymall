package jmu.shijh.tinymall.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ActivityUpdate implements Serializable {
    private Long activityId;

    /**
     * 展示图地址
     */
    private String previewImg;

    /**
     * 活动内容
     */
    private String content;

    /**
     * 过期时间
     */
    private LocalDateTime expiredTime;

    /**
     * 活动标题
     */
    private String activityTitle;

    /**
     * 活动页面地址
     */
    private String activityPage;

    /**
     * 0-未发布 1-已发布 2-已失效
     */
    private Integer activityStatus;
}
