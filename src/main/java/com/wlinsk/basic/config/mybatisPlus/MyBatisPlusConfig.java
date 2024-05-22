package com.wlinsk.basic.config.mybatisPlus;

import com.github.pagehelper.PageInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: wlinsk
 * @Date: 2024/5/22
 */
@Configuration
@MapperScan("com.wlinsk.mapper")
public class MyBatisPlusConfig {
    @Bean
    public PageInterceptor pageInterceptor() {
        return new PageInterceptor();
    }

}
