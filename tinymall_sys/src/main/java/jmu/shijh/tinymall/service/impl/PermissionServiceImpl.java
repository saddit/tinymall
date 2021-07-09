package jmu.shijh.tinymall.service.impl;

import jmu.shijh.tinymall.domain.entity.Permission;
import jmu.shijh.tinymall.mapper.PermissionMapper;
import jmu.shijh.tinymall.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shijh
 * @since 2021-05-20
 */
@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;


    @Override
    public List<Permission> getPermissionsByRoleId(Long roleId) {
        return permissionMapper.selectByRoleId(roleId);
    }
}
