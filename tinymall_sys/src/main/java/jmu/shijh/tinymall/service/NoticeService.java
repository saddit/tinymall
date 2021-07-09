package jmu.shijh.tinymall.service;

import jmu.shijh.tinymall.common.util.PageDTO;
import jmu.shijh.tinymall.common.util.PageVO;
import jmu.shijh.tinymall.common.util.Str;
import jmu.shijh.tinymall.domain.dto.NoticeDTO;
import jmu.shijh.tinymall.domain.entity.Notice;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 */
public interface NoticeService extends IService<Notice>{

    PageVO getNotices(NoticeDTO dto, PageDTO page);

    void addNotice(Notice notice, List<String> to);

    void readNotice(Long noticeId);

    void readAll();

    void removeBatch(List<Long> noticeIds);

    @Transactional
    void removeAll(Boolean isRead);

    Integer getNoReadCount();
}
