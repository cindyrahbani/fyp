package murex.dev.mxem.Scheduler.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    //Swagger Metadata: http://localhost:8080/v2/api-docs
    //Swagger UI URL: http://localhost:8080/swagger-ui.html

    private ApiInfo getApiInfo(){
        return new ApiInfoBuilder()
                .title("FYP Scheduler Services")
                .description("This page lists Mxem environment services")
                .version("2.1")
                .contact(new Contact("ESIB FYP","wwww.murex.com","christie.matta@murex.com"))
                .license("License 2.0")
                .licenseUrl("http://license.com")
                .build();

    }

}
