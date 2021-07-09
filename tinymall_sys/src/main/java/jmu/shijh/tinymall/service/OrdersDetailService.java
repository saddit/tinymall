package jmu.shijh.tinymall.service;

import jmu.shijh.tinymall.domain.entity.OrdersDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shijh
 * @since 2021-05-20
 */
public interface OrdersDetailService extends IService<OrdersDetail> {


    void removeByOrderId(Long orderId);

    List<OrdersDetail> getByOrderId(Long orderId);
}
