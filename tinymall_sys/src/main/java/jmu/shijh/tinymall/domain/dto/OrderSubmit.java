package jmu.shijh.tinymall.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author sjh
 */
@Data
public class OrderSubmit implements Serializable {
    private Long shopId;
    private Long addressId;
    private List<OrderItem> items;
    Long couponId;
}
