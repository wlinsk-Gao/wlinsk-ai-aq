package com.wlinsk.basic.config.swagger;

import com.google.common.base.Predicates;
import io.swagger.annotations.Api;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * http://localhost:8080/api/swagger-ui.html
 * @Author: wlinsk
 * @Date: 2024/5/22
 */
@EnableSwagger2
@Configuration
@EnableConfigurationProperties(SwaggerProperties.class)
@ConditionalOnProperty(prefix = "swagger", value = {"enable"}, havingValue = "true")
public class SwaggerAutoConfiguration {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                //正则隐藏路径
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .paths(Predicates.not(PathSelectors.regex("/health.*")))
                .paths(PathSelectors.regex("/.*"))
                .build().enable(true);
    }

    private ApiInfo apiInfo() {
        return(new ApiInfoBuilder()).build();
    }
    @Bean
    public UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
                .deepLinking(true)
                .displayOperationId(false)
                // 隐藏UI上的Models模块
                .defaultModelsExpandDepth(-1)
                .defaultModelExpandDepth(0)
                .defaultModelRendering(ModelRendering.EXAMPLE)
                .displayRequestDuration(false)
                .docExpansion(DocExpansion.NONE)
                .filter(false)
                .maxDisplayedTags(null)
                .operationsSorter(OperationsSorter.ALPHA)
                .showExtensions(false)
                .tagsSorter(TagsSorter.ALPHA)
                .validatorUrl(null)
                .build();
    }
    @Bean
    @Primary
    public BasicModelToSwagger2Mapper camServiceModelToSwagger2Mapper() {
        return new BasicModelToSwagger2Mapper();
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE+1)
    public BasicOperationBuilderPlugin camOperationBuilderPlugin(){
        return new BasicOperationBuilderPlugin();
    }

}
