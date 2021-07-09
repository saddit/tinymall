package jmu.shijh.tinymall.service;

import jmu.shijh.tinymall.common.util.PageDTO;
import jmu.shijh.tinymall.common.util.PageVO;
import jmu.shijh.tinymall.domain.entity.Shop;
import jmu.shijh.tinymall.domain.entity.ShopCar;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 */
public interface ShopCarService extends IService<ShopCar> {

    PageVO pagingQueryUserShopCar(PageDTO page);

    void removeItem(Long shopCarId);

    void removeItemBatch(List<Long> shopCarIds);

    void addItem(ShopCar car);

    @Transactional
    void updateShopCar(ShopCar shopCar);

    /**
     * used by system
     */
    void invalidateShopCar(Long carId);
}
