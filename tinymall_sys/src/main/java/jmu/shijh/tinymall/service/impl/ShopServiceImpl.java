package jmu.shijh.tinymall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jdk.nashorn.internal.ir.ThrowNode;
import jmu.shijh.tinymall.common.exception.CustomException;
import jmu.shijh.tinymall.common.util.RedisKeys;
import jmu.shijh.tinymall.common.util.Times;
import jmu.shijh.tinymall.domain.entity.Activity;
import jmu.shijh.tinymall.domain.entity.Shop;
import jmu.shijh.tinymall.domain.enums.ActivityStatus;
import jmu.shijh.tinymall.shiro.UserIdentity;
import jmu.shijh.tinymall.mapper.ActivityMapper;
import jmu.shijh.tinymall.service.ShopService;
import jmu.shijh.tinymall.mapper.ShopMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements ShopService{

    @Autowired
    private UserIdentity user;
    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Override
    public Shop getUserShop() {
        return lambdaQuery().eq(Shop::getUserId, user.getId()).one();
    }

    @Override
    public Shop getShop(Long shopId) {
        return getById(shopId);
    }

    @Override
    @Transactional
    public void registerShop(Shop shop) {
        Shop userShop = getUserShop();
        if (userShop != null) {
            throw new CustomException("不允许多个店铺");
        }
        shop.setUserId(user.getId());
        shop.setActivityId(null);
        save(shop);
    }

    @Override
    @Transactional
    public void updateShop(Shop shop) {
        Shop userShop = getUserShop();
        userShop.setShopDesc(shop.getShopDesc());
        userShop.setShopIcon(shop.getShopIcon());
        userShop.setShopName(shop.getShopName());
        userShop.setCategoryId(shop.getCategoryId());
        boolean i = updateById(userShop);
        if (!i) {
            throw new CustomException("更新失败");
        }
    }


    @Override
    @Transactional
    public void joinActivity(Long activityId) {
        Activity activity = activityMapper.selectById(activityId);
        Shop userShop = getUserShop();
        if (userShop == null) throw new CustomException("未拥有店铺");
        if (activity == null) throw new CustomException("活动不存在");
        if (ActivityStatus.UNDEPLOY.equal(activity.getActivityStatus())) throw new CustomException("活动未发布");
        if (ActivityStatus.INVALID.equal(activity.getActivityStatus()) || activity.getExpiredTime().isBefore(Times.localNow())) throw new CustomException("活动过期");
        userShop.setActivityId(activityId);
        updateById(userShop);
    }
}