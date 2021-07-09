package jmu.shijh.tinymall.shiro.realm;


import jmu.shijh.tinymall.domain.dto.UserDTO;
import jmu.shijh.tinymall.shiro.UserIdentity;
import jmu.shijh.tinymall.domain.entity.UserInfo;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DefaultWebRealm extends WebRealm {

    public DefaultWebRealm() {
        this.setCredentialsMatcher(new HashedCredentialsMatcher("MD5"));
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        val username = (String) authenticationToken.getPrincipal();
        UserInfo userInfo = getUserInfo(username);
        UserIdentity identity = getIdentity(userInfo);
        ByteSource salt = ByteSource.Util.bytes(userInfo.getSalt());
        return new SimpleAuthenticationInfo(identity, userInfo.getPassword(), salt, getName());
    }
}
