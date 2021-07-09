package jmu.shijh.tinymall.common.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class PageDTO {
    private Integer pageSize;
    private Integer pageNum;
    private String orderBy;
    private Boolean isDesc;

    public PageDTO() {
        pageNum = 1;
        pageSize = 10;
        orderBy = null;
        isDesc = false;
    }

    public String orderBy() {
        return orderBy;
    }

    public String getOrderBy() {
        if(orderBy == null) return null;
        if (isDesc) {
           return orderBy + " desc";
        }
        return orderBy;
    }
}
