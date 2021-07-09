package jmu.shijh.tinymall.mapper;

import jmu.shijh.tinymall.domain.entity.Activity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jmu.shijh.tinymall.domain.vo.ActivityVO;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @Entity jmu.shijh.tinymall.domain.entity.Activity
 */
@Repository
public interface ActivityMapper extends BaseMapper<Activity> {

    @Select("select * from activity where activity_id=#{arg0} and deleted=0")
    @Results(id = "activityMap", value = {
            @Result(id = true, column = "activity_id", property = "activityId"),
            @Result(column = "activity_id", property = "coupons",
                many = @Many(select = "jmu.shijh.tinymall.mapper.CouponMapper.selectByActivityId")
            )
    })
    ActivityVO selectByActivityId(Long activityId);
}




