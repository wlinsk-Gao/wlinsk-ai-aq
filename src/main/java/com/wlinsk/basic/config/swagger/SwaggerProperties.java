package com.wlinsk.basic.config.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: wlinsk
 * @Date: 2024/5/22
 */
@Data
@ConfigurationProperties(prefix = SwaggerProperties.PREFIX)
public class SwaggerProperties {
    static final String PREFIX = "swagger";
    /**
     * 是否开启swagger
     */
    private Boolean enable = true;
}
