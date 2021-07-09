package jmu.shijh.tinymall.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class PayInfo {

    /**
     * 支付单号
     */
    private Long payId;

    /**
     * 支付类型 银行卡 微信 支付宝
     */
    private String payType;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;
}