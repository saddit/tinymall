package jmu.shijh.tinymall.mapper;

import jmu.shijh.tinymall.common.sqlbuilder.ConditionSQL;
import jmu.shijh.tinymall.common.sqlbuilder.QuerySQL;
import jmu.shijh.tinymall.common.util.Times;
import jmu.shijh.tinymall.domain.dto.CouponDTO;
import jmu.shijh.tinymall.domain.entity.Coupon;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jmu.shijh.tinymall.domain.vo.CouponVO;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author SJH
 * @Entity jmu.shijh.tinymall.domain.entity.Coupon
 */
@Repository
public interface CouponMapper extends BaseMapper<Coupon> {

    @Select("select * from coupon where activity_id = #{arg0} and `deleted`=0")
    List<Coupon> selectByActivityId(Long activityId);

    @SelectProvider(value = provider.class, method = "selectByDto")
    List<CouponVO> selectCouponByDTO(CouponDTO dto);

    @Select("select c.*, uc.user_id, uc.count user_count, uc.max_count, uc.id uc_id " +
            "from coupon c, user_coupon uc " +
            "where uc.user_id=#{userId} and uc.coupon_id = #{couponId} " +
            "and uc.coupon_id=c.coupon_id and c.`deleted`=0 ")
    CouponVO selectUserCoupon(@Param("userId") Long userId, @Param("couponId") Long couponId);

    @Insert("insert into user_coupon (user_id,coupon_id,count) values (#{userId}, #{couponId}, 1)")
    int insertUserCoupon(@Param("userId") Long userId, @Param("couponId") Long couponId);

    @Update("update user_coupon set count=#{userCount}, max_count=#{maxCount} where id=#{ucId}")
    int updateUserCoupon(CouponVO couponVO);

    @Delete("delete from user_coupon where coupon_id = #{couponId} and user_id = #{userId}")
    int deleteUserCoupon(@Param("userId") Long userId, @Param("couponId") Long couponId);

    class provider {
        public String selectByDto(CouponDTO dto) {
            return new QuerySQL(dto, "coupon c")
                    .join("user_coupon uc on c.coupon_id=uc.coupon_id")
                    .join(dto.getShopId() != null, "shop sp on sp.activity_id = c.activity_id")
                    .select("c.*, uc.user_id, uc.count user_count, uc.max_count, uc.id uc_id")
                    .eq("uc.user_id", "userId")
                    .eq("c.activity_id", "activityId")
                    .eq("sp.shop_id", "shopId")
                    .gtVal("uc.count", 0)
                    .toString();
        }
    }
}




