package jmu.shijh.tinymall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jmu.shijh.tinymall.domain.entity.UserCoupon;
import jmu.shijh.tinymall.mapper.UserCouponMapper;
import jmu.shijh.tinymall.service.UserCouponService;
import org.springframework.stereotype.Service;

@Service
public class UserCouponServiceImpl extends ServiceImpl<UserCouponMapper, UserCoupon> implements UserCouponService {
}
