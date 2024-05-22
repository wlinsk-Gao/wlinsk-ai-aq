package com.wlinsk.basic.config;

import com.wlinsk.basic.config.logInterceptor.LogIdInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: wlinsk
 * @Date: 2024/5/22
 */
@Slf4j
@Configuration
public class WebInterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("add interceptor!!!!!!!!!!!!!!!!");
        registry.addInterceptor(new LogIdInterceptor()).addPathPatterns("/**");
    }
    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        log.info(" addResourceHandlers!!!!!!!!!!!!!!!!");
        // 解决静态资源无法访问
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
        // 解决swagger无法访问
        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        // 解决swagger的js文件无法访问
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
