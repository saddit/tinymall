package jmu.shijh.tinymall.mapper;

import com.github.pagehelper.Page;
import jmu.shijh.tinymall.common.sqlbuilder.Condition;
import jmu.shijh.tinymall.common.sqlbuilder.ConditionSQL;
import jmu.shijh.tinymall.common.sqlbuilder.Conditions;
import jmu.shijh.tinymall.common.sqlbuilder.enums.Logic;
import jmu.shijh.tinymall.common.sqlbuilder.enums.Rule;
import jmu.shijh.tinymall.domain.dto.ProductDTO;
import jmu.shijh.tinymall.domain.entity.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jmu.shijh.tinymall.domain.vo.ProductPreVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author shijh
 * @since 2021-05-21
 */
@Repository
public interface ProductMapper extends BaseMapper<Product> {

    @SelectProvider(value = provider.class, method = "query")
    Page<ProductPreVO> selectPageByDTO(ProductDTO dto);

    int reduceStockBatch(@Param("ids") List<Long> ids, @Param("reduce") int reduce);

    class provider {
        public String query(ProductDTO dto) {
            return new ConditionSQL(dto, "product")
                    .select("product.shop_id","product.product_name",
                            "product.preview_img","product.product_id",
                            "product.sale_price","product.original_price","s.shop_name")
                    .select(dto.getShopId()!=null,
                            "product.create_time","product.deploy_time","product.stock","product.category_id","product.product_status")
                    .join("shop s on s.shop_id = product.shop_id")
                    .c(Condition.get("s.shop_id", "shopId"))
                    .c(Condition.get("s.activity_id", "activityId"))
                    .c(Condition.get("product_status", "productStatus"))
                    .c(Condition.get("product.category_id", "categoryId"))
                    .c(Condition.get("product.create_time", Rule.GE, "createTimeStart"))
                    .c(Condition.get("create_time", Rule.LT, "createTimeEnd"))
                    .c(Condition.get("deploy_time", Rule.GE, "deployTimeStart"))
                    .c(Condition.get("deploy_time", Rule.LT, "deployTimeEnd"))
                    .c(Condition.get("sale_price", Rule.GE, "minPrice"))
                    .c(Condition.get("sale_price", Rule.LE, "maxPrice"))
                    .cs(
                            Condition.s().get(Logic.OR, "product_name", Rule.REGEXP, "keyword")
                                    .get(Logic.OR, "product_desc", Rule.REGEXP, "keyword")
                                    .get(Logic.OR, "product_tags", Rule.REGEXP, "keyword")
                    ).toString();
        }
    }
}
