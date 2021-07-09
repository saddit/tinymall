package jmu.shijh.tinymall.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 只选择其中一个Realm进行 SingleRealmAuthentication 验证 <br>
 * 要求一类 token 只能对应一类 Realm
 */
@Component("authenticator")
public class SelectiveRealmAuthenticator extends ModularRealmAuthenticator {
    @Override
    protected AuthenticationInfo doMultiRealmAuthentication(Collection<Realm> realms, AuthenticationToken token) {
        Realm currentRealm = null;
        for (Realm realm : realms) {
            if (realm.supports(token)) {
                currentRealm = realm;
                break;
            }
        }
        if (currentRealm == null) {
            return null;
        }
        return doSingleRealmAuthentication(currentRealm, token);
    }
}
