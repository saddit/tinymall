package jmu.shijh.tinymall.mapper;

import com.github.pagehelper.Page;
import jmu.shijh.tinymall.common.sqlbuilder.Condition;
import jmu.shijh.tinymall.common.sqlbuilder.ConditionSQL;
import jmu.shijh.tinymall.common.sqlbuilder.enums.Logic;
import jmu.shijh.tinymall.common.sqlbuilder.enums.Rule;
import jmu.shijh.tinymall.domain.dto.CollectionDTO;
import jmu.shijh.tinymall.domain.entity.Collection;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jmu.shijh.tinymall.domain.vo.CollectionVO;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

/**
 * @Entity jmu.shijh.tinymall.domain.entity.Collection
 */
@Repository
public interface CollectionMapper extends BaseMapper<Collection> {

    @SelectProvider(value = provider.class, method = "query")
    Page<CollectionVO> selectPageByDTO(CollectionDTO dto);

    class provider {
        public String query(CollectionDTO dto) {
            return new ConditionSQL(dto, "collection c")
                    .select("c.*, p.product_name, p.preview_img product_img, p.sale_price product_price, sp.shop_name")
                    .join("product p on p.product_id = c.product_id")
                    .join("shop sp on sp.shop_id = c.shop_id")
                    .eqVal("p.deleted", 0)
                    .eq("c.category_id", "categoryId")
                    .eq("c.coll_status", "collStatus")
                    .eq("c.user_id", "userId")
                    .ge("c.create_time", "createTimeStart")
                    .lt("c.create_time", "createTimeEnd")
                    .cs(
                            Condition.s()
                                    .get(Logic.OR,"p.product_name", Rule.REGEXP, "keyword")
                                    .get(Logic.OR,"p.product_desc", Rule.REGEXP, "keyword")
                    )
                    .toString();
        }
    }

}




