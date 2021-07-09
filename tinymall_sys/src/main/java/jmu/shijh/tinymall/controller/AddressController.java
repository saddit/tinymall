package jmu.shijh.tinymall.controller;

import jmu.shijh.tinymall.common.annotation.ParamCheck;
import jmu.shijh.tinymall.common.exception.CustomException;
import jmu.shijh.tinymall.common.util.JsonResp;
import jmu.shijh.tinymall.common.util.R;
import jmu.shijh.tinymall.domain.entity.Address;
import jmu.shijh.tinymall.domain.entity.Shop;
import jmu.shijh.tinymall.service.AddressService;
import jmu.shijh.tinymall.service.ShopService;
import jmu.shijh.tinymall.shiro.UserIdentity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {
    @Autowired
    private AddressService addressService;
    @Autowired
    private UserIdentity user;
    @Autowired
    private ShopService shopService;

    @GetMapping("/get")
    @RequiresPermissions("address:select")
    public JsonResp getUserAddress() {
        List<Address> address = addressService.getUserAddress();
        return R.ok().data(address).build();
    }

    @GetMapping("/get/{aid}")
    @RequiresPermissions("address:select")
    public JsonResp getAddress(@PathVariable Long aid) {
        Address address = addressService.getById(aid);
        if (address == null) {
            throw new CustomException("不存在");
        } else if (!address.getUserId().equals(user.getId())) {
            throw new CustomException("不可访问");
        }
        return R.ok().data(address).build();
    }

    @GetMapping("/shop/get/{aid}")
    @RequiresPermissions({"address:select","shopinfo:select"})
    public JsonResp getAddressShop(@PathVariable Long aid) {
        Address address = addressService.getById(aid);
        return R.ok().data(address).build();
    }

    @GetMapping("/get/default")
    @RequiresPermissions("address:select")
    public JsonResp getDefaultAddress() {
        Address address = addressService.getUserDefaultAddress();
        return R.ok().data(address).build();
    }

    @PostMapping("/add")
    @ParamCheck(exclude = {"addressId","deleted","userId","isDefault"})
    @RequiresPermissions("address:insert")
    public JsonResp addAddress(@RequestBody Address address) {
        addressService.addAddress(address);
        return R.ok().build();
    }

    @PostMapping("/update")
    @ParamCheck(include = {"addressId"})
    @RequiresPermissions("address:update")
    public JsonResp updateAddress(@RequestBody Address address) {
        addressService.updateAddress(address);
        return R.ok().build();
    }

    @GetMapping("/delete/{id}")
    @RequiresPermissions("address:delete")
    public JsonResp remove(@PathVariable Long id) {
        Address add = addressService.getById(id);
        if (add == null) {
            throw new CustomException("不存在");
        }
        if (!add.getUserId().equals(user.getId())) {
            throw new CustomException("越权访问");
        }
        return R.ok().msg("删除成功").build();
    }
}
