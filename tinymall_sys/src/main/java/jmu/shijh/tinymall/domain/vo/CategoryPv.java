package jmu.shijh.tinymall.domain.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CategoryPv implements Serializable {
    private Long categoryId;
    private String categoryDesc;
    private Integer pv;
}
