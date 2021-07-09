package jmu.shijh.tinymall.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SmsCode {
    private String phoneNumber;
    private String code;
}
