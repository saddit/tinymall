package jmu.shijh.tinymall.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserCoupon implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Integer maxCount;
    private Long couponId;
    private Long userId;
    private Integer count;
}
