package com.springboottestapp.springboot_test_app;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;


/*@EnableWebMvc
@EnableSwagger2*/
@Configuration
@OpenAPIDefinition(info = @Info(title = "Users API", version = "2.0", description = "Users Information"))
public class SpringFoxConfig {

    /*@Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.springboottestapp.springboot_test_app.controllers"))
                .paths(PathSelectors.ant("/api/accounts/*"))
                .build();

    }*/
}
