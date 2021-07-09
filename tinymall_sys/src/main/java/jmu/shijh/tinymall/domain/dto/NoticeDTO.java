package jmu.shijh.tinymall.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class NoticeDTO implements Serializable {
    /**
     * 内容
     */
    private String content;

    /**
     * 标题
     */
    private String noticeTitle;

    private LocalDateTime createTimeStart;
    private LocalDateTime createTimeEnd;

    private Long deployUid;
    private Long userId;

    private Boolean isRead;
}
