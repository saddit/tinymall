package jmu.shijh.tinymall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jmu.shijh.tinymall.common.exception.CustomException;
import jmu.shijh.tinymall.common.util.*;
import jmu.shijh.tinymall.domain.dto.NoticeDTO;
import jmu.shijh.tinymall.domain.entity.Notice;
import jmu.shijh.tinymall.domain.vo.NoticeVO;
import jmu.shijh.tinymall.shiro.UserIdentity;
import jmu.shijh.tinymall.service.NoticeService;
import jmu.shijh.tinymall.mapper.NoticeMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService{

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private UserIdentity user;

    @Override
    public PageVO getNotices(NoticeDTO dto, PageDTO page) {
        PageHelper.startPage(page);
        Page<NoticeVO> noticeVOS = noticeMapper.selectPageByDTO(dto);
        return new PageVO(noticeVOS);
    }


    @Override
    public void addNotice(Notice notice, List<String> to) {
        List<Notice> notices = new ArrayList<>(to.size());
        for (String userId : to) {
            Notice n = new Notice();
            BeanUtils.copyProperties(notice, n, "userId");
            n.setUserId(Long.valueOf(userId));
            notices.add(n);
        }
        saveBatch(notices);
    }

    @Override
    public void readNotice(Long noticeId) {
        Notice notice = getById(noticeId);
        if (!notice.getUserId().equals(user.getId())) {
            throw new CustomException("非法访问");
        }
        notice.setIsRead(true);
        notice.setReadTime(Times.localNow());
        updateById(notice);
    }

    @Override
    @Transactional
    public void readAll() {
        lambdaUpdate().set(Notice::getIsRead, true)
                .eq(Notice::getUserId, user.getId()).update();
    }

    @Override
    @Transactional
    public void removeBatch(List<Long> noticeIds) {
        List<Notice> notices = noticeMapper.selectBatchIds(noticeIds);
        for (Notice notice : notices) {
            if (!notice.getUserId().equals(user.getId())) throw new CustomException("非法访问");
        }
        removeByIds(noticeIds);
    }

    @Override
    public Integer getNoReadCount() {
        return lambdaQuery().select(Notice::getNoticeId)
                .eq(Notice::getUserId,user.getId())
                .eq(Notice::getIsRead, false)
                .count();
    }

    @Override
    @Transactional
    public void removeAll(Boolean isRead) {
        boolean success = lambdaUpdate().eq(Str.notEmpty(isRead), Notice::getIsRead, isRead)
                .eq(Notice::getUserId, user.getId()).remove();
        if(!success) {
            throw new CustomException("删除失败");
        }
    }
}




