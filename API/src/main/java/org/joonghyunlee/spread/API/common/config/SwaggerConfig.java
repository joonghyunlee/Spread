package org.joonghyunlee.spread.API.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Profile({"local"})
public class SwaggerConfig {

    @Bean
    public Docket divisionV1() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Division")
                .select()
                    .apis(RequestHandlerSelectors.basePackage("org.joonghyunlee.spread.API.web"))
                    .paths(PathSelectors.any())
                .build();
    }
}
