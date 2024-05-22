package com.wlinsk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author: wlinsk
 * @Date: 2024/5/22
 */
@EnableSwagger2
@SpringBootApplication(scanBasePackages = {"com.wlinsk"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
