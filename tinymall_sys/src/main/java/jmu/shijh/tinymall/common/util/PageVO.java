package jmu.shijh.tinymall.common.util;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.pagehelper.PageInfo;
import lombok.Data;
import org.apache.ibatis.annotations.Lang;

import java.util.List;

@Data
public class PageVO {
    private List<?> list;
    private Integer pageNum;
    private Integer pageSize;
    private Integer totalNum;
    private Integer totalPage;
    private String orderBy;

    public PageVO() {

    }

    public PageVO(Page<?> resPage) {
        setList(resPage.getRecords());
        setPageNum(Math.toIntExact(resPage.getCurrent()));
        setPageSize(Math.toIntExact(resPage.getSize()));
        setTotalNum(Math.toIntExact(resPage.getTotal()));
        setTotalPage(Math.toIntExact(resPage.getPages()));
        orderBy = resPage.getOrders().isEmpty() ? "" : resPage.getOrders().get(0).getColumn();
    }

    public PageVO(com.github.pagehelper.Page<?> page) {
        PageInfo<?> pageInfo = page.toPageInfo();
        list = pageInfo.getList();
        pageNum = Math.toIntExact(pageInfo.getPageNum());
        pageSize = Math.toIntExact(pageInfo.getPageSize());
        totalNum = Math.toIntExact(pageInfo.getTotal());
        totalPage = Math.toIntExact(pageInfo.getPages());
        orderBy = page.getOrderBy();
    }
}
