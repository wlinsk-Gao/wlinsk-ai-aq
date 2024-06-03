package com.wlinsk.basic.config.ai;

import com.zhipu.oapi.ClientV4;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: wlinsk
 * @Date: 2024/5/31
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ai")
public class AIConfig {
    private String apiKey;

    @Bean
    public ClientV4 getClientV4() {
        return new ClientV4.Builder(apiKey).build();
    }

}
