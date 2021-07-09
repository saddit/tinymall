package jmu.shijh.tinymall.domain.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class NoticeVO implements Serializable {
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

    private String deployUsername;

    private LocalDateTime createTime;

    private Boolean isRead;

    /**
     * 接受通知的用户id
     */
    private Long userId;

}
