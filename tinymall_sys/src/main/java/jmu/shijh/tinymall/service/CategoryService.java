package jmu.shijh.tinymall.service;

import jmu.shijh.tinymall.domain.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import jmu.shijh.tinymall.domain.vo.CategoryProduct;
import jmu.shijh.tinymall.domain.vo.CategoryPv;

import java.util.List;

/**
 *
 */
public interface CategoryService extends IService<Category> {

    List<Category> getAll();

    void addCategory(Category category);

    List<CategoryProduct> getCateProduct();

    List<CategoryPv> getShopCategoryPv(Long shopId);
}
