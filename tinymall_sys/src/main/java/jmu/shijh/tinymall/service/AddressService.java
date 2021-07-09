package jmu.shijh.tinymall.service;

import jmu.shijh.tinymall.domain.entity.Address;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 */
public interface AddressService extends IService<Address> {

    List<Address> getUserAddress();

    Address getUserDefaultAddress();

    void removeAddress(Long addressId);

    void updateAddress(Address address);

    @Transactional
    void addAddress(Address address);
}
