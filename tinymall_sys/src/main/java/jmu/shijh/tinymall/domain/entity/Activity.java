package jmu.shijh.tinymall.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * null
 * @TableName activity
 */
@TableName(value ="activity")
@Data
@Accessors(chain = true)
public class Activity implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.ASSIGN_ID)
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
     * 
     */
    private LocalDateTime createTime;

    /**
     * 过期时间
     */
    private LocalDateTime expiredTime;

    private LocalDateTime deployTime;

    /**
     * 1-true
     */
    private Boolean deleted;

    /**
     * 活动标题
     */
    private String activityTitle;

    /**
     * 活动页面地址
     */
    private String activityPage;

    /**
     * 发布者id
     */
    private Long deployUid;

    /**
     * 0-未发布 1-已发布 2-已失效
     */
    private Integer activityStatus;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}