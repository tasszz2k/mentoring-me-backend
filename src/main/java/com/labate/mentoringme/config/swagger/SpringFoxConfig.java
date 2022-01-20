package com.labate.mentoringme.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.net.URI;

import static org.springframework.web.servlet.function.RequestPredicates.GET;
import static org.springframework.web.servlet.function.RouterFunctions.route;

// @Profile("dev")
// @Profile({"dev","qa"}) //make it enable for multiple environments
// @Profile("!prod") // enable for all environment except Production environment
@Configuration
@EnableSwagger2
public class SpringFoxConfig {

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .groupName("Mentoring Me")
        .apiInfo(apiInfo())
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.labate.mentoringme"))
        .paths(PathSelectors.any())
        .build();
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("[LABATE] Mentoring Me Swagger API")
        .description("Mentoring Me - Swagger Profile")
        .version("1.0")
        .contact(new Contact("LABATE", "https://labate.com", "tasszz2k@gmail.com"))
        .license("Labate License 1.0")
        .licenseUrl("contact@labate.com")
        .version("1.0")
        .build();
  }

  /**
   * Swagger UI: Change default /swagger-ui.html to /swagger
   *
   * @return RouterFunction
   */
  @Bean
  RouterFunction<ServerResponse> swaggerRouter() {
    return route(
        GET("/swagger"),
        req -> ServerResponse.temporaryRedirect(URI.create("swagger-ui.html")).build());
  }
}
