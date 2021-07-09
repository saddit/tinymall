package jmu.shijh.tinymall.service.impl;

import jmu.shijh.tinymall.domain.entity.OrdersDetail;
import jmu.shijh.tinymall.mapper.OrdersDetailMapper;
import jmu.shijh.tinymall.service.OrdersDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shijh
 * @since 2021-05-20
 */
@Service
public class OrdersDetailServiceImpl extends ServiceImpl<OrdersDetailMapper, OrdersDetail> implements OrdersDetailService {

    @Override
    public void removeByOrderId(Long orderId) {
        lambdaUpdate().eq(OrdersDetail::getOrdersId, orderId).remove();
    }

    @Override
    public List<OrdersDetail> getByOrderId(Long orderId) {
        return lambdaQuery().eq(OrdersDetail::getOrdersId, orderId).list();
    }
}
