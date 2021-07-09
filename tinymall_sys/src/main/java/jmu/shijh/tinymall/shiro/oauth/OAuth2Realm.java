package jmu.shijh.tinymall.shiro.oauth;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jmu.shijh.tinymall.common.exception.LoginFailureException;
import jmu.shijh.tinymall.common.util.CryptoKeys;
import jmu.shijh.tinymall.common.util.CryptoUtils;
import jmu.shijh.tinymall.common.util.Jwt;
import jmu.shijh.tinymall.common.util.Str;
import jmu.shijh.tinymall.domain.dto.UserDTO;
import jmu.shijh.tinymall.domain.entity.UserInfo;
import jmu.shijh.tinymall.shiro.realm.WebRealm;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.springframework.stereotype.Component;

import java.security.GeneralSecurityException;

@Component
public class OAuth2Realm extends WebRealm {
    public OAuth2Realm() {
        this.setCredentialsMatcher(new AllowAllCredentialsMatcher());
        this.setAuthenticationTokenClass(OAuth2Token.class);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authToken) throws AuthenticationException {
        String token = (String) authToken.getCredentials();
        if (Str.empty(token)) throw new AuthenticationException("token不能为空");
        Long userId;
        try {
            token = CryptoUtils.decryptAES(token, CryptoKeys.AES_KEY);
            userId = Jwt.verifyToken(token);
        } catch (UnsupportedJwtException | SignatureException | GeneralSecurityException e) {
            throw new LoginFailureException("非法token");
        } catch (ExpiredJwtException te) {
            throw new LoginFailureException("token已失效");
        }
        UserInfo userInfo = getUserInfo(userId.toString());
        return new SimpleAuthenticationInfo(getIdentity(userInfo), token, getName());
    }
}
