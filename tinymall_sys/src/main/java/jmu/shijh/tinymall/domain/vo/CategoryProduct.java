package jmu.shijh.tinymall.domain.vo;

import jmu.shijh.tinymall.domain.entity.Category;
import jmu.shijh.tinymall.domain.entity.Product;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CategoryProduct implements Serializable {
    private Long categoryId;
    private String categoryDesc;
    private List<Product> products;
}
