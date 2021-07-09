package jmu.shijh.tinymall.mapper;

import jmu.shijh.tinymall.domain.dto.OrderDTO;
import jmu.shijh.tinymall.domain.entity.OrdersDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jmu.shijh.tinymall.domain.vo.OrdersDetailVO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author shijh
 * @since 2021-05-20
 */
public interface OrdersDetailMapper extends BaseMapper<OrdersDetail> {

    @Select("select od.*, p.preview_img product_img " +
            "from orders_detail od, product p " +
            "where p.product_id = od.product_id and od.orders_id = #{arg0} and od.deleted = 0")
    List<OrdersDetailVO> selectByOrderId(Long orderId);
}
