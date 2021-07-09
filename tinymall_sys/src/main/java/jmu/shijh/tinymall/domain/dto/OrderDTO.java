package jmu.shijh.tinymall.domain.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class OrderDTO implements Serializable {
    /**
     * 状态 0-未支付 1-待发货 2-待收货 3-待退货 4-已退货 5-已完成 6-失效
     */
    private Integer ordersStatus;
    private LocalDateTime createTimeStart;
    private LocalDateTime createTimeEnd;
    private String keyword;
    private Long productId;
    private Long ordersId;

    @JSONField(serialize = false, deserialize = false)
    private Long userId;
    @JSONField(serialize = false, deserialize = false)
    private Long shopId;
}
