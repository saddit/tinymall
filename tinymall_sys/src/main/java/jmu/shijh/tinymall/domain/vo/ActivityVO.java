package jmu.shijh.tinymall.domain.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import jmu.shijh.tinymall.domain.entity.Coupon;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class ActivityVO implements Serializable {

    private Long activityId;

    /**
     * 展示图地址
     */
    private String previewImg;

    private Long deployUid;

    /**
     * 活动内容
     */
    private String content;

    /**
     * 过期时间
     */
    private LocalDateTime expiredTime;
    private LocalDateTime createTime;

    /**
     * 活动标题
     */
    private String activityTitle;

    private Integer activityStatus;

    /**
     * 活动页面地址
     */
    private String activityPage;

    //VIEW OBJECT
    private List<Coupon> coupons;
}
