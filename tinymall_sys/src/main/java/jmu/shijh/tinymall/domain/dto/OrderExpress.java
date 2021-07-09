package jmu.shijh.tinymall.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class OrderExpress implements Serializable {
    private Long ordersId;
    /**
     * 快递单号
     */
    private String expressId;

    /**
     * 快递公司名
     */
    private String expressName;

    /**
     * 发货地址
     */
    private String expressAddress;
}
