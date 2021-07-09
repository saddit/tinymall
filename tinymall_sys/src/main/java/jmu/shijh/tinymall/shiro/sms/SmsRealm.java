package jmu.shijh.tinymall.shiro.sms;

import jmu.shijh.tinymall.common.util.RedisKeys;
import jmu.shijh.tinymall.domain.dto.UserDTO;
import jmu.shijh.tinymall.domain.entity.UserInfo;
import jmu.shijh.tinymall.shiro.realm.WebRealm;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class SmsRealm extends WebRealm {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    public SmsRealm() {
        this.setAuthenticationTokenClass(SmsToken.class);
        this.setCredentialsMatcher(new SimpleCredentialsMatcher());
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        SmsToken token = (SmsToken) authenticationToken;
        UserInfo userInfo = getUserInfo(token.getPhone());
        String credential = (String) redisTemplate.opsForValue()
                .get(RedisKeys.getLoginSmsKey(token.getPhone()));
        return new SimpleAuthenticationInfo(getIdentity(userInfo), credential, getName());
    }
}
