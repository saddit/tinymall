package jmu.shijh.tinymall.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author sjh
 */
@Data
@Accessors(chain = true)
public class CouponDTO implements Serializable {
    private Long activityId;
    private Long userId;
    private Long shopId;
}
