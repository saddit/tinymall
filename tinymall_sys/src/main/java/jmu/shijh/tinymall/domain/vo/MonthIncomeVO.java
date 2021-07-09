package jmu.shijh.tinymall.domain.vo;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class MonthIncomeVO implements Serializable {
    private Integer month;
    private BigDecimal income;
}
