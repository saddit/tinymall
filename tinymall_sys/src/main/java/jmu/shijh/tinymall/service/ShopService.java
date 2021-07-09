package jmu.shijh.tinymall.service;

import jmu.shijh.tinymall.domain.entity.Shop;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
public interface ShopService extends IService<Shop> {

    Shop getUserShop();

    Shop getShop(Long shopId);

    @Transactional
    void registerShop(Shop shop);

    void updateShop(Shop shop);

    @Transactional
    void joinActivity(Long activityId);
}
