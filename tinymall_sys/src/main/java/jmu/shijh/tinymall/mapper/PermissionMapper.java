package jmu.shijh.tinymall.mapper;

import jmu.shijh.tinymall.domain.entity.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author shijh
 * @since 2021-05-20
 */
@Repository
public interface PermissionMapper extends BaseMapper<Permission> {

    @Select("select p.* from permission p, role_perm rp where p.perm_id = rp.perm_id and rp.role_id = #{arg0}")
    List<Permission> selectByRoleId(Long roleId);

    @Select("select perm_name from permission")
    List<Permission> selectAll();
}
