package jmu.shijh.tinymall.shiro.oauth;


import com.alibaba.fastjson.JSON;
import jmu.shijh.tinymall.common.exception.LoginFailureException;
import jmu.shijh.tinymall.common.exception.UserStateException;
import jmu.shijh.tinymall.common.util.CryptoKeys;
import jmu.shijh.tinymall.common.util.CryptoUtils;
import jmu.shijh.tinymall.common.util.Str;
import jmu.shijh.tinymall.domain.dto.LoginDTO;
import jmu.shijh.tinymall.shiro.FilterExceptionResolver;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * 若携带了 token 且未登录则进行自动进行登录验证 登录失败则拦截返回错误信息
 * 若未携带token则放行
 */
@Slf4j
@Component
public class OAuth2Filter extends AuthenticatingFilter {
    private String getToken(HttpServletRequest request) {
        return request.getHeader("token");
    }

    private HttpServletRequest toHttp(ServletRequest request) {
        return WebUtils.toHttp(request);
    }

    private HttpServletResponse toHttp(ServletResponse request) {
        return WebUtils.toHttp(request);
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        String token = getToken(toHttp(servletRequest));
        if (Str.empty(token)) return null;
        return new OAuth2Token(token);
    }

    /**
     * 在executeLogin登录失败后调用，token == null 不会调用 loginFailure
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        log.debug("Principal:'{}' login failure, because {}", token.getPrincipal(), e.getMessage());
        if (e instanceof UserStateException) {
            FilterExceptionResolver.doThrow(
                    toHttp(request),
                    toHttp(response),
                    LoginFailureException.class,
                    "token无效"
            );
            return false;
        }
        return true;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        try {
            return executeLogin(servletRequest, servletResponse);
        } catch (IllegalStateException e) {
            //header上没有token 不进行登录拦截
            return true;
        }
    }
}
