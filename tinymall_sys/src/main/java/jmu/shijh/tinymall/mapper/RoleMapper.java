package jmu.shijh.tinymall.mapper;

import jmu.shijh.tinymall.domain.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author shijh
 * @since 2021-05-20
 */
@Repository
public interface RoleMapper extends BaseMapper<Role> {

    @Select("select * from role where role_id=#{arg0}")
    @Results(id = "roleMap", value = {
            @Result(id = true, column = "role_id", property = "roleId"),
            @Result(
                    column = "role_id",property = "permissions",
                    many = @Many(select ="jmu.shijh.tinymall.mapper.PermissionMapper.selectByRoleId")
            )
    })
    Role selectRoleById(Long roleId);
}
