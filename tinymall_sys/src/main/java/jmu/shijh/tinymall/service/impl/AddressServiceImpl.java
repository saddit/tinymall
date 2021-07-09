package jmu.shijh.tinymall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jmu.shijh.tinymall.common.exception.CustomException;
import jmu.shijh.tinymall.common.util.Cl;
import jmu.shijh.tinymall.domain.entity.Address;
import jmu.shijh.tinymall.shiro.UserIdentity;
import jmu.shijh.tinymall.service.AddressService;
import jmu.shijh.tinymall.mapper.AddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 1. 添加地址
 * 2. 获取地址
 * 3. 设为默认
 * 4. 删除地址
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService{

    @Autowired
    private UserIdentity userIdentity;

    @Override
    public List<Address> getUserAddress() {
        return lambdaQuery().eq(Address::getUserId, userIdentity.getId()).list();
    }

    @Override
    public Address getUserDefaultAddress() {
        return lambdaQuery().eq(Address::getUserId, userIdentity.getId())
                        .eq(Address::getIsDefault, true).one();
    }

    @Override
    public void removeAddress(Long addressId) {
        Address addr = getById(addressId);
        if (!addr.getUserId().equals(userIdentity.getId())) {
            throw new CustomException("越权访问");
        }
        removeById(addressId);
    }

    @Override
    @Transactional
    public void updateAddress(Address address) {
        Address current = getById(address.getAddressId());
        if (current == null) {
            throw new CustomException("地址不存在");
        }
        if (!current.getUserId().equals(userIdentity.getUserId())) {
            throw new CustomException("越权访问");
        }
        if (address.getIsDefault()) {
            baseMapper.cancelDefault(current.getUserId());
        }
        updateById(address);
    }

    @Override
    @Transactional
    public void addAddress(Address address) {
        address.setUserId(userIdentity.getId());
        if(address.getIsDefault() == null) {
            address.setIsDefault(false);
        }
        else if (address.getIsDefault()) {
            getBaseMapper().cancelDefault(userIdentity.getId());
        }
        save(address);
    }
}




