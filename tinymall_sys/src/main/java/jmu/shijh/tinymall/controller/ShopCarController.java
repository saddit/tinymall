package jmu.shijh.tinymall.controller;


import jmu.shijh.tinymall.common.annotation.ParamCheck;
import jmu.shijh.tinymall.common.util.JsonResp;
import jmu.shijh.tinymall.common.util.PageDTO;
import jmu.shijh.tinymall.common.util.PageVO;
import jmu.shijh.tinymall.common.util.R;
import jmu.shijh.tinymall.domain.entity.ShopCar;
import jmu.shijh.tinymall.service.ShopCarService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author shijh
 * @since 2021-06-08
 */
@RestController
@RequestMapping("/shopcar")
public class ShopCarController {
    @Autowired
    private ShopCarService shopCarService;

    @PostMapping("/get")
    @RequiresPermissions("shopcar:select")
    public JsonResp getUserShopCar(@RequestBody PageDTO page) {
        PageVO pageVO = shopCarService.pagingQueryUserShopCar(page);
        return R.ok().data(pageVO).build();
    }

    @PostMapping("/add")
    @ParamCheck(include = {"productId"})
    @RequiresPermissions("shopcar:insert")
    public JsonResp addShopCar(@RequestBody(required = false) ShopCar item) {
        if (item.getProductSize() == null) {
            item.setProductSize(1);
        }
        shopCarService.addItem(item);
        return R.ok().msg("添加成功").build();
    }

    @PostMapping("/update")
    @ParamCheck(include = {"carId", "productSize", "productType"})
    @RequiresPermissions("shopcar:update")
    public JsonResp updateShopCar(@RequestBody ShopCar item) {
        shopCarService.updateShopCar(item);
        return R.ok().msg("更新成功").build();
    }

    @GetMapping("/delete/{carId}")
    @RequiresPermissions("shopcar:delete")
    public JsonResp deleteShopCar(@PathVariable Long carId) {
        shopCarService.removeItem(carId);
        return R.ok().msg("删除成功").build();
    }

    @PostMapping("/delete/batch")
    @RequiresPermissions("shopcar:delete")
    public JsonResp deleteBatch(@RequestBody List<Long> carIds) {
        shopCarService.removeItemBatch(carIds);
        return R.ok().msg("移除成功").build();
    }
}
