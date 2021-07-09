package jmu.shijh.tinymall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jmu.shijh.tinymall.domain.entity.Permission;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shijh
 * @since 2021-05-20
 */
public interface PermissionService {
    List<Permission> getPermissionsByRoleId(Long roleId);
}
