package jmu.shijh.tinymall.controller;

import jmu.shijh.tinymall.common.annotation.MultiRequestBody;
import jmu.shijh.tinymall.common.annotation.ParamCheck;
import jmu.shijh.tinymall.common.annotation.ParamChecks;
import jmu.shijh.tinymall.common.util.*;
import jmu.shijh.tinymall.domain.dto.NoticeDTO;
import jmu.shijh.tinymall.domain.entity.Notice;
import jmu.shijh.tinymall.service.NoticeService;
import jmu.shijh.tinymall.shiro.UserIdentity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;
    @Autowired
    private UserIdentity user;

    @PostMapping("/add")
    @RequiresPermissions("notice:insert")
    @ParamChecks({
        @ParamCheck(include = {"noticeTitle","content"}, target = "notice"),
        @ParamCheck(include = {"to"}, target = "to", isArray = true, lengthLT = 1000, value = "缺少发送对象")
    })
    public JsonResp addNotice(@MultiRequestBody("notice") NoticeDTO notice, @MultiRequestBody("to") List<String> to) {
        Notice n = new Notice();
        BeanUtils.copyProperties(notice, n);
        n.setDeployUid(user.getId());
        noticeService.addNotice(n, to);
        return R.ok().build();
    }

    @GetMapping("/count")
    @RequiresPermissions("notice:select")
    public JsonResp getNoReadCount() {
        Integer i = noticeService.getNoReadCount();
        return R.ok().data(i).build();
    }

    @PostMapping("/query/page")
    @RequiresPermissions("notice:select")
    public JsonResp getNotices(@MultiRequestBody(required = false) NoticeDTO dto,
                               @MultiRequestBody(required = false) PageDTO page) {
        dto.setUserId(user.getUserId());
        PageVO notices = noticeService.getNotices(dto, page);
        return R.ok().data(notices).build();
    }

    @PostMapping("/query/shop/page")
    @RequiresPermissions({"notice:select","shopinfo:select"})
    public JsonResp getNoticesShop(@MultiRequestBody(required = false) NoticeDTO dto,
                               @MultiRequestBody(required = false) PageDTO page) {
        dto.setDeployUid(user.getId());
        PageVO notices = noticeService.getNotices(dto, page);
        return R.ok().data(notices).build();
    }

    @GetMapping("/read/{id}")
    @RequiresPermissions("notice:select")
    public JsonResp readNotice(@PathVariable String id) {
        if("all".equals(id)) {
            noticeService.readAll();
        } else {
            noticeService.readNotice(Long.valueOf(id));
        }
        return R.ok().build();
    }

    @GetMapping("/delete/{id}/{isRead}")
    @RequiresPermissions("notice:delete")
    public JsonResp deleteNotice(@PathVariable String id, @PathVariable(required = false) Boolean isRead) {
        if("all".equals(id)) {
            noticeService.removeAll(isRead);
        } else {
            noticeService.removeBatch(Cl.list(Long.valueOf(id)));
        }
        return R.ok().build();
    }

    @PostMapping("/delete/batch")
    @RequiresPermissions("notice:delete")
    public JsonResp deleteBatch(@RequestBody List<Long> ids) {
        noticeService.removeBatch(ids);
        return R.ok().build();
    }
}
