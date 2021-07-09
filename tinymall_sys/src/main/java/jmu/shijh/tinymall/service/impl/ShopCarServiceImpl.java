package jmu.shijh.tinymall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jmu.shijh.tinymall.common.exception.CustomException;
import jmu.shijh.tinymall.common.util.Cl;
import jmu.shijh.tinymall.common.util.PageDTO;
import jmu.shijh.tinymall.common.util.PageVO;
import jmu.shijh.tinymall.domain.entity.Product;
import jmu.shijh.tinymall.domain.entity.ShopCar;
import jmu.shijh.tinymall.domain.vo.ShopCarVO;
import jmu.shijh.tinymall.shiro.UserIdentity;
import jmu.shijh.tinymall.mapper.ProductMapper;
import jmu.shijh.tinymall.service.ShopCarService;
import jmu.shijh.tinymall.mapper.ShopCarMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 */
@Service
public class ShopCarServiceImpl extends ServiceImpl<ShopCarMapper, ShopCar> implements ShopCarService{

    @Autowired
    private UserIdentity user;
    @Autowired
    private ShopCarMapper shopCarMapper;
    @Autowired
    private ProductMapper productMapper;

    @Override
    public PageVO pagingQueryUserShopCar(PageDTO page) {
        PageHelper.startPage(page);
        Page<ShopCarVO> shopCars = shopCarMapper.selectByUserId(user.getId());
        return new PageVO(shopCars);
    }

    @Override
    public void removeItem(Long shopCarId) {
        ShopCar shopCar = getById(shopCarId);
        if (!shopCar.getUserId().equals(user.getUserId())) {
            throw new CustomException("越权访问");
        }
        boolean b = removeById(shopCarId);
        if (!b) {
            throw new CustomException("删除失败");
        }
    }

    @Override
    public void removeItemBatch(List<Long> shopCarIds) {
        boolean success = lambdaUpdate().eq(ShopCar::getUserId, user.getId())
                .in(ShopCar::getCarId, shopCarIds)
                .remove();
        if (!success) {
            throw new CustomException("删除失败");
        }
    }

    @Override
    @Transactional
    public void addItem(ShopCar car) {
        Long pid = car.getProductId();
        ShopCar exitsCar = lambdaQuery().eq(ShopCar::getProductId, pid).eq(ShopCar::getUserId, user.getId())
                .one();
        if (exitsCar != null ) {
            exitsCar.setProductSize(exitsCar.getProductSize()+1);
            updateShopCar(exitsCar);
            return;
        }
        Product product = productMapper.selectById(car.getProductId());
        List<String> types = Cl.list(product.getProductTypes().split(","));
        if (car.getProductType() == null) {
            car.setProductType(types.get(0));
        }
        else if (!types.contains(car.getProductType())) {
            throw new CustomException("暂无此型号");
        }
        BeanUtils.copyProperties(product,car, "deleted");
        car.setUserId(user.getId());
        car.setProductImg(product.getPreviewImg());
        car.setProductPrice(product.getSalePrice());
        int i = shopCarMapper.insert(car);
        if (i == 0) throw new CustomException("添加购物车失败");
    }

    @Override
    @Transactional
    public void updateShopCar(ShopCar shopCar) {
        if (shopCar.getProductSize() < 1) throw new CustomException("不能再减少了");
        boolean s = lambdaUpdate().eq(ShopCar::getCarId, shopCar.getCarId())
                        .set(ShopCar::getProductSize, shopCar.getProductSize())
                        .set(ShopCar::getProductType, shopCar.getProductType()).update();
        if (!s) throw new CustomException("更新失败");
    }

    @Override
    @Transactional
    public void invalidateShopCar(Long carId) {
        ShopCar shopCar = new ShopCar();
        shopCar.setCarId(carId);
        shopCar.setValid(false);
        updateById(shopCar);
    }
}




