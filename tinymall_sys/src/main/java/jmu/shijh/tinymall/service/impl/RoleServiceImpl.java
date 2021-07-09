package jmu.shijh.tinymall.service.impl;

import jmu.shijh.tinymall.domain.entity.Role;
import jmu.shijh.tinymall.mapper.RoleMapper;
import jmu.shijh.tinymall.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shijh
 * @since 2021-05-20
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Role getRole(Long roleId) {
        return roleMapper.selectRoleById(roleId);
    }
}
