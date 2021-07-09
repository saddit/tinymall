package jmu.shijh.tinymall.shiro;


import jmu.shijh.tinymall.domain.entity.Role;
import jmu.shijh.tinymall.domain.entity.UserInfo;
import lombok.Data;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;

@Data
@Component
@Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserIdentity implements Serializable {
    private String username;
    private String email;
    private String phone;
    private Long userId;
    private Role role;
    private String salt;
    private Boolean initSuccess = true;

    public Long getId() {
        return getUserId();
    }

    public static UserIdentity from(UserInfo userInfo) {
        UserIdentity identity = new UserIdentity();
        BeanUtils.copyProperties(userInfo, identity);
        return identity;
    }

    @PostConstruct
    public void init() {
        UserIdentity i = (UserIdentity) SecurityUtils.getSubject().getPrincipal();
        if (i == null) {
            initSuccess = false;
        } else {
            try {
                BeanUtils.copyProperties(i, this);
            } catch (BeansException e) {
                initSuccess = false;
            }
        }
    }
}
