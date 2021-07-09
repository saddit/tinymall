package jmu.shijh.tinymall.common.config;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.dialects.IDialect;
import jmu.shijh.tinymall.common.MultiRequestBodyArgumentResolver;
import jmu.shijh.tinymall.common.config.properties.ResourcePathProperties;
import jmu.shijh.tinymall.common.config.serializer.LocalDateTImeSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({ResourcePathProperties.class})
public class SpringMvcConfig implements WebMvcConfigurer {

    @Autowired
    private MultiRequestBodyArgumentResolver multiRequestBodyArgumentResolver;

    @Autowired
    ResourcePathProperties resourceProp;

    private FastJsonHttpMessageConverter fastjson() {
        FastJsonHttpMessageConverter fjc = new FastJsonHttpMessageConverter();
        fjc.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
        fjc.setDefaultCharset(StandardCharsets.UTF_8);
        SerializeConfig serializeConfig = SerializeConfig.globalInstance;
        serializeConfig.put(Long.class , ToStringSerializer.instance);
        serializeConfig.put(Long.TYPE , ToStringSerializer.instance);
        serializeConfig.put(LocalDateTime.class, new LocalDateTImeSerializer("yyyy-MM-dd HH:mm:ss"));
        fjc.setFastJsonConfig(new FastJsonConfig() {{
            setDateFormat("yyyy-MM-dd HH:mm:ss");
            setSerializeConfig(serializeConfig);
            setSerializerFeatures(
                    SerializerFeature.DisableCircularReferenceDetect,
                    SerializerFeature.WriteDateUseDateFormat,
                    SerializerFeature.WriteEnumUsingToString
            );
        }});
        return fjc;
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    /**
     * 配置 resolver 支持多个RequestBody解析
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(multiRequestBodyArgumentResolver);
    }

    /**
     * 配置前后端跨域
     */
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
                .allowCredentials(true)
                .allowedOrigins("*")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .allowedHeaders("*")
                .maxAge(3600);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(0, fastjson());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(resourceProp.getVideoPattern())
                .addResourceLocations(resourceProp.getVideoPath());
        registry.addResourceHandler(resourceProp.getImagePattern())
                .addResourceLocations(resourceProp.getImagePath());
        registry.addResourceHandler(resourceProp.getHtmlPattern())
                .addResourceLocations(resourceProp.getHtmlPath());
    }
}
