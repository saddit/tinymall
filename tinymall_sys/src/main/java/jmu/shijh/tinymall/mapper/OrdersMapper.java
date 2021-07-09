package jmu.shijh.tinymall.mapper;

import com.github.pagehelper.Page;
import jmu.shijh.tinymall.common.sqlbuilder.Condition;
import jmu.shijh.tinymall.common.sqlbuilder.ConditionSQL;
import jmu.shijh.tinymall.common.sqlbuilder.Conditions;
import jmu.shijh.tinymall.common.sqlbuilder.enums.Logic;
import jmu.shijh.tinymall.common.sqlbuilder.enums.Rule;
import jmu.shijh.tinymall.common.util.Cl;
import jmu.shijh.tinymall.common.util.Str;
import jmu.shijh.tinymall.domain.dto.MonthIncomeDTO;
import jmu.shijh.tinymall.domain.dto.OrderDTO;
import jmu.shijh.tinymall.domain.entity.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jmu.shijh.tinymall.domain.vo.MonthIncomeVO;
import jmu.shijh.tinymall.domain.vo.OrdersVO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Protocol;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author shijh
 * @since 2021-05-20
 */
@Repository
public interface OrdersMapper extends BaseMapper<Orders> {

    @SelectProvider(value = provider.class, method = "selectPage")
    @Results(id = "orderMap", value = {
            @Result(id = true, column = "orders_id", property = "ordersId"),
            @Result(column = "orders_id", property = "items",
                    many = @Many(select = "jmu.shijh.tinymall.mapper.OrdersDetailMapper.selectByOrderId"))
    })
    Page<OrdersVO> selectPageByDTO(OrderDTO dto);

    @SelectProvider(value = provider.class, method = "selectPrePage")
    Page<Orders> selectPrePageByDTO(OrderDTO dto);


    @Select("select distinct user_id from orders where orders_status=#{ordersStatus} and shop_id=#{shopId} limit 1000")
    List<String> getRelationUserId(OrderDTO dto);

    @ResultMap("orderMap")
    @Select("select distinctrow orders.*, s.shop_name, s.shop_icon from orders " +
            "join orders_detail od on orders.orders_id = od.orders_id " +
            "join shop s on orders.shop_id = s.shop_id " +
            "where orders.user_id =#{userId} and orders.deleted=0 " +
            "and (od.product_name regexp #{keyword} or s.shop_name regexp #{keyword})")
    Page<OrdersVO> selectPageByKeyword(OrderDTO dto);

    List<MonthIncomeVO> sumPerMonthIncome(MonthIncomeDTO dto);

    class provider {
        private ConditionSQL baseChain(OrderDTO dto) {
            return new ConditionSQL(dto, "orders")
                    .c(Condition.get("orders.shop_id", "shopId"))
                    .c(Condition.get("orders_status","ordersStatus"))
                    .c(Condition.get("orders.user_id", "userId"))
                    .c(Condition.get("orders.create_time", Rule.GE, "createTimeStart"))
                    .c(Condition.get("orders.create_time", Rule.LT, "createTimeEnd"));
        }
        public String selectPage(OrderDTO dto) {
            return baseChain(dto)
                    .select("orders.*, s.shop_name, s.shop_icon")
                    .join("shop s on orders.shop_id = s.shop_id")
                    .eq("orders_id", "ordersId")
                    .toString();
        }
        public String selectPrePage(OrderDTO dto) {
            return baseChain(dto)
                    .select("*")
                    .cs(
                            Condition.s().get(Logic.OR,"orders.orders_id", "keyword")
                                .get(Logic.OR,"orders.pay_id", "keyword")
                    ).toString();
        }
    }
}
