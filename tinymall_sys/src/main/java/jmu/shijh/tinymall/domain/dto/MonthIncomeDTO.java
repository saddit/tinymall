package jmu.shijh.tinymall.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class MonthIncomeDTO implements Serializable {
    private Long shopId;
    private Integer startMonth;
    private Integer endMonth;
    private Integer year;
}
