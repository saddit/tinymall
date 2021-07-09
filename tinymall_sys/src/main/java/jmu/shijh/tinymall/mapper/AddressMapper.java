package jmu.shijh.tinymall.mapper;

import jmu.shijh.tinymall.domain.entity.Address;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/**
 * @Entity jmu.shijh.tinymall.domain.entity.Address
 */
public interface AddressMapper extends BaseMapper<Address> {

    @Update("update address set is_default=0 where user_id=#{userId} and is_default=1")
    int cancelDefault(Long userId);
}




