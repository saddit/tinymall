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
 * @TableName notice
 */
@TableName(value ="notice")
@Data
public class Notice implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long noticeId;

    /**
     * 内容
     */
    private String content;

    /**
     * 标题
     */
    private String noticeTitle;

    /**
     * 发送通知的用户id
     */
    private Long deployUid;

    /**
     * 接受通知的用户id
     */
    private Long userId;

    /**
     * 1-true
     */
    private Boolean deleted;

    /**
     * 已读 1-true
     */
    private Boolean isRead;


    private LocalDateTime createTime;

    /**
     * 已读时间
     */
    private LocalDateTime readTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}