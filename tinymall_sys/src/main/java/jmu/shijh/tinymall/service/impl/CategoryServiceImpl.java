package jmu.shijh.tinymall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jmu.shijh.tinymall.domain.entity.Category;
import jmu.shijh.tinymall.domain.vo.CategoryProduct;
import jmu.shijh.tinymall.domain.vo.CategoryPv;
import jmu.shijh.tinymall.service.CategoryService;
import jmu.shijh.tinymall.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> getAll() {
        return categoryMapper.selectAll();
    }

    @Override
    public void addCategory(Category category) {
        save(category);
    }

    @Override
    public List<CategoryProduct> getCateProduct() {
        return categoryMapper.selectCateProd();
    }

    @Override
    public List<CategoryPv> getShopCategoryPv(Long shopId) {
        return categoryMapper.sumShopCatePv(shopId);
    }
}




