package jmu.shijh.tinymall.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderItem implements Serializable {
    private Long productId;
    private String productType;
    private Integer productSize;
}
