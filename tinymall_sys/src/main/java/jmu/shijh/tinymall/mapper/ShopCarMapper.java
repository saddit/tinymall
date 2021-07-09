package jmu.shijh.tinymall.mapper;

import com.github.pagehelper.Page;
import jmu.shijh.tinymall.common.sqlbuilder.Condition;
import jmu.shijh.tinymall.common.sqlbuilder.ConditionSQL;
import jmu.shijh.tinymall.common.sqlbuilder.Conditions;
import jmu.shijh.tinymall.common.sqlbuilder.UpdateSQL;
import jmu.shijh.tinymall.common.sqlbuilder.enums.Logic;
import jmu.shijh.tinymall.domain.dto.ShopCarDTO;
import jmu.shijh.tinymall.domain.entity.ShopCar;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jmu.shijh.tinymall.domain.vo.CarItem;
import jmu.shijh.tinymall.domain.vo.ShopCarVO;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Entity jmu.shijh.tinymall.domain.entity.ShopCar
 */
@Repository
public interface ShopCarMapper extends BaseMapper<ShopCar> {

    @Select("select s.shop_id, s.shop_name, s.shop_icon, sc.user_id uid " +
            "from shop s, shop_car sc " +
            "where sc.shop_id=s.shop_id and sc.user_id=#{arg0} and sc.deleted=0 " +
            "group by s.shop_id")
    @Results(id = "shopCarMap", value = {
            @Result(id = true, column = "shop_id", property = "shopId"),
            @Result(column = "{userId=uid,shopId=shop_id}", property = "items",
                    many = @Many(select = "selectCarItem"))
    })
    Page<ShopCarVO> selectByUserId(Long userId);

    @Select("select * from shop_car where user_id=#{userId} and shop_id=#{shopId} and deleted=0")
    List<CarItem> selectCarItem(@Param("userId") Long userId, @Param("shopId") Long shopId);
}




