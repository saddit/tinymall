package jmu.shijh.tinymall.shiro;


import jmu.shijh.tinymall.common.config.properties.ResourcePathProperties;
import jmu.shijh.tinymall.common.util.Cl;
import jmu.shijh.tinymall.shiro.filter.CNInvalidRequestFilter;
import jmu.shijh.tinymall.shiro.oauth.OAuth2Filter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.crazycake.shiro.IRedisManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.servlet.Filter;
import java.net.UnknownHostException;
import java.util.Map;

@Configuration
@Slf4j
public class ShiroConfig {
    @Autowired
    private ResourcePathProperties resourceProp;

    @Bean("shiroRedisTemplate")
    public RedisTemplate<byte[], byte[]> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    /**
     * 创建ShiroFilterFactoryBean
     */
    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(SessionsSecurityManager securityManager,
                                                            Map<String, Filter> filterMap,
                                                            SelectiveRealmAuthenticator authenticator,
                                                            MyRedisManager myRedisManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        authenticator.setRealms(securityManager.getRealms());
        securityManager.setAuthenticator(authenticator);
        RedisCacheManager cacheManager = (RedisCacheManager) securityManager.getCacheManager();
        //shiro-redis插件使用 principal 的某个属性为 key 默认是 "id" TODO 此处有bug，修改后依然使用"id"
        cacheManager.setPrincipalIdFieldName("userId");
        cacheManager.setRedisManager(myRedisManager);
        //设置安全管理器
        shiroFilterFactoryBean.setLoginUrl("/auth/login");
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setFilters(filterMap);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(
                Cl.map().add("/**", OAuth2Filter.class.getSimpleName())
                        .add(resourceProp.getVideoPattern(),"anon")
                        .add(resourceProp.getImagePattern(),"anon")
                        .add(resourceProp.getHtmlPattern(),"anon")
                        .build()
        );
        shiroFilterFactoryBean.setGlobalFilters(
                Cl.list(CNInvalidRequestFilter.class.getSimpleName())
        );
        return shiroFilterFactoryBean;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SessionsSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
