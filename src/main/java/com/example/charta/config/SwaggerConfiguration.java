package com.example.charta.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Конфигурация swagger ui
 * http://localhost:8080/swagger-ui/index.html
 *
 * @author Egor Mitrofanov
 */
@Configuration
public class SwaggerConfiguration {

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Chartographer Application")
                .description("Тестовое задание для стажировке в Контуре by Egor Mitrofanov")
                .version("1.1")
                .contact(new Contact("Egor Mitrofanov", "", "egor.mitrofanov@hotmail.com"))
                .build();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build()
                .enable(true)
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false);
    }
}
