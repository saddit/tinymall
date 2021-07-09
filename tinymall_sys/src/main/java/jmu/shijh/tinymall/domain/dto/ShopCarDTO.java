package jmu.shijh.tinymall.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class ShopCarDTO implements Serializable {

    private Long shopId;
    private Long userId;

    private LocalDateTime createTimeStart;
    private LocalDateTime createTimeEnd;

    private String keyword;
}
