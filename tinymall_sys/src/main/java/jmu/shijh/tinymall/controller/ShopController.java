package jmu.shijh.tinymall.controller;

import jmu.shijh.tinymall.common.annotation.ParamCheck;
import jmu.shijh.tinymall.common.exception.CustomException;
import jmu.shijh.tinymall.common.util.JsonResp;
import jmu.shijh.tinymall.common.util.R;
import jmu.shijh.tinymall.domain.dto.OrderDTO;
import jmu.shijh.tinymall.domain.entity.Orders;
import jmu.shijh.tinymall.domain.entity.Shop;
import jmu.shijh.tinymall.domain.entity.UserInfo;
import jmu.shijh.tinymall.domain.enums.OrderStatus;
import jmu.shijh.tinymall.service.OrdersService;
import jmu.shijh.tinymall.service.ShopService;
import jmu.shijh.tinymall.service.UserInfoService;
import jmu.shijh.tinymall.shiro.UserIdentity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author sjh
 */
@RestController
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserIdentity user;

    @GetMapping("/user")
    @RequiresPermissions("shopinfo:select")
    public JsonResp getUserShop() {
        Shop userShop = shopService.getUserShop();
        return R.ok().data(userShop).build();
    }

    @GetMapping("/query/{sid}")
    public JsonResp getShopInfo(@PathVariable Long sid) {
        Shop shop = shopService.getShop(sid);
        return R.ok().data(shop).build();
    }

    @PostMapping("/register")
    @RequiresPermissions("shopinfo:insert")
    @ParamCheck(include = {"shopIcon","shopName","shopDesc","categoryId"}, value = "必要参数不可为空")
    public JsonResp registerShop(@RequestBody Shop shop) {
        Shop userShop = shopService.getUserShop();
        if (userShop!=null) {
            throw new CustomException("不可重复开店");
        }
        shopService.registerShop(shop);
        userInfoService.updateById(new UserInfo().setUserId(user.getId()).setRoleId(3L));
        return R.ok().msg("注册成功,请重新登录").build();
    }

    @PostMapping("/update")
    @RequiresPermissions("shopinfo:update")
    public JsonResp updateShop(@RequestBody(required = false) Shop shop) {
        shopService.updateShop(shop);
        return R.ok().msg("更新成功").build();
    }

    @GetMapping("/join/{activityId}")
    @RequiresPermissions("shopinfo:update")
    public JsonResp joinActivity(@PathVariable Long activityId) {
        shopService.joinActivity(activityId);
        return R.ok().msg("参与成功").build();
    }

    @GetMapping("/user/unpaid")
    @RequiresPermissions({"orders:select","shopinfo:select"})
    public JsonResp getUnpaidUser() {
        Shop shop = shopService.getUserShop();
        List<String> relationUserId = ordersService.getRelationUserId(
                new OrderDTO().setShopId(shop.getShopId())
                        .setOrdersStatus(OrderStatus.NO_PAY.code())
        );
        return R.ok().data(relationUserId).build();
    }

    @GetMapping("/user/unreceive")
    @RequiresPermissions({"orders:select","shopinfo:select"})
    public JsonResp getUnReceiveUser() {
        Shop shop = shopService.getUserShop();
        List<String> relationUserId = ordersService.getRelationUserId(
                new OrderDTO().setShopId(shop.getShopId())
                        .setOrdersStatus(OrderStatus.WAIT_RECEIVE.code())
        );
        return R.ok().data(relationUserId).build();
    }

    @GetMapping("/user/finished")
    @RequiresPermissions({"orders:select","shopinfo:select"})
    public JsonResp getFinishedUser() {
        Shop shop = shopService.getUserShop();
        List<String> relationUserId = ordersService.getRelationUserId(
                new OrderDTO().setShopId(shop.getShopId())
                        .setOrdersStatus(OrderStatus.FINISH.code())
        );
        return R.ok().data(relationUserId).build();
    }
}
