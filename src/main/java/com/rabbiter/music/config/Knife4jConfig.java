package com.rabbiter.music.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * Knife4j config
 */
@Configuration
@EnableSwagger2WebMvc
@EnableKnife4j
@ConfigurationProperties(prefix = "swagger.config")
public class Knife4jConfig {

    @Bean(value = "ApiDoc")
    public Docket defaultApi2() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("Music System API")
                        .description("API documentation")
                        .termsOfServiceUrl("http://localhost:8888/swagger-ui.html")
                        .version("v2.0")
                        .contact(new Contact("yuan", "https://github.com/orgs/MJUniversity/repositories", "2584278161@qq.com"))
                        .build())
                .groupName("2.X")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.rabbiter.music.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}
