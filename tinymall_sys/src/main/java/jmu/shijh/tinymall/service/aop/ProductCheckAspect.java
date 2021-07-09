package jmu.shijh.tinymall.service.aop;

import jmu.shijh.tinymall.common.exception.CustomException;
import jmu.shijh.tinymall.domain.entity.Category;
import jmu.shijh.tinymall.domain.entity.Product;
import jmu.shijh.tinymall.shiro.UserIdentity;
import jmu.shijh.tinymall.mapper.ProductMapper;
import jmu.shijh.tinymall.mapper.ShopMapper;
import jmu.shijh.tinymall.service.CategoryService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ProductCheckAspect {
    @Autowired
    private CategoryService categoryService;

    @Before(value = "execution(* jmu.shijh.tinymall.service.impl.ProductServiceImpl.*(..)) && args(product)")
    public void checkProduct(Product product) {
        if (product.getSalePrice().compareTo(product.getOriginalPrice()) > 0) {
            throw new CustomException("销售价不能高于原价");
        }
        Category category = categoryService.getById(product.getCategoryId());
        if (category == null) {
            throw new CustomException("分类不存在");
        }
    }
}
