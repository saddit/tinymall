package jmu.shijh.tinymall.shiro.realm;

import jmu.shijh.tinymall.common.exception.UserStateException;
import jmu.shijh.tinymall.domain.entity.Permission;
import jmu.shijh.tinymall.domain.entity.Role;
import jmu.shijh.tinymall.domain.entity.UserInfo;
import jmu.shijh.tinymall.mapper.PermissionMapper;
import jmu.shijh.tinymall.shiro.UserIdentity;
import jmu.shijh.tinymall.service.RoleService;
import jmu.shijh.tinymall.service.UserInfoService;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author sjh
 */
public abstract class WebRealm extends AuthorizingRealm {
    @Autowired
    protected UserInfoService userService;
    @Autowired
    protected RoleService roleService;
    @Autowired
    private PermissionMapper permissionMapper;

    protected AuthorizationInfo getAuthorizationInfo(Role role) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if (role != null) {
            info.addRole(role.getRoleName());
            if (role.getPermissions() != null) {
                role.getPermissions().forEach(perm -> info.addStringPermission(perm.getPermName()));
            }
            //region temp
            if (3L == role.getRoleId()) {
                List<Permission> permissions = permissionMapper.selectByRoleId(1L);
                permissions.forEach(perm -> info.addStringPermission(perm.getPermName()));
            }
            if(2L == role.getRoleId()) {
                List<Permission> permissions = permissionMapper.selectAll();
                permissions.forEach(perm -> info.addStringPermission(perm.getPermName()));
            }

            //endregion
        }
        return info;
    }

    protected UserInfo getUserInfo(String username) throws UserStateException{
        UserInfo userInfo = userService.getUserInfo(username);
        if (userInfo == null) {
            return null;
        } else if (userInfo.getUserStatus() == 1) {
            throw new UserStateException("用户被封禁");
        }

        return userInfo;
    }

    protected UserIdentity getIdentity(UserInfo userInfo) {
        UserIdentity identity = UserIdentity.from(userInfo);
        Role role = roleService.getRole(userInfo.getRoleId());
        identity.setRole(role);
        return identity;
    }

    @Override
    public AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        UserIdentity user = (UserIdentity) principalCollection.getPrimaryPrincipal();
        return getAuthorizationInfo(user.getRole());
    }
}
