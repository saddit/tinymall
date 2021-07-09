package jmu.shijh.tinymall.mapper;

import jmu.shijh.tinymall.domain.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jmu.shijh.tinymall.domain.vo.CategoryProduct;
import jmu.shijh.tinymall.domain.vo.CategoryPv;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Entity jmu.shijh.tinymall.domain.entity.Category
 */
@Repository
public interface CategoryMapper extends BaseMapper<Category> {

    @Select("select * from category limit 100")
    List<Category> selectAll();

    List<CategoryProduct> selectCateProd();

    List<CategoryPv> sumShopCatePv(@Param("shopId") Long shopId);
}




