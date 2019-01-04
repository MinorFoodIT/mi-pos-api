package com.minor.store.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.Arrays;

//import static com.google.common.collect.Lists.newArrayList;
import static springfox.documentation.builders.PathSelectors.regex;

//@Configuration
@EnableSwagger2
@Profile({"!test"})
public class SwaggerConfiguration {

    public static final String DEFAULT_INCLUDE_PATTERN = "/api/.*";

    @Autowired
    private ServletContext servletContext;

    @Value("${server.servlet-path:minor-store-api}")
    private String servletPath;


    /**
     * Swagger Springfox configuration.
     * @throws java.io.IOException
     */
    @Bean
    public Docket swaggerSpringfoxDocket(ApplicationConfiguration applicationConfiguration) throws IOException {
        Contact contact = new Contact(
                applicationConfiguration.getSwagger().getContactName(),
                applicationConfiguration.getSwagger().getContactUrl(),
                applicationConfiguration.getSwagger().getContactEmail());

        ApiInfo apiInfo = new ApiInfo(
                applicationConfiguration.getSwagger().getTitle(),
                applicationConfiguration.getSwagger().getDescription(), //markdownReaderService.getMarkdownContent("api_description"),
                applicationConfiguration.getSwagger().getVersion(),
                applicationConfiguration.getSwagger().getTermOfServiceUrl(),
                contact,
                applicationConfiguration.getSwagger().getLicense(),
                applicationConfiguration.getSwagger().getLicenseUrl());

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .forCodeGeneration(true)
                .groupName("foodstore-api")
                .globalOperationParameters(Arrays.asList(new ParameterBuilder()
                        .name("Accept-Language")
                        .description("Language")
                        .modelRef(new ModelRef("string"))
                        .parameterType("header")
                        .defaultValue("en_US")
                        .required(false)
                        .build()))
                .useDefaultResponseMessages(false)
                .pathProvider(
                        new RelativePathProvider(servletContext){
                            @Override
                            public String getApplicationBasePath() {
                                return servletPath;
                                //return "/serviceName" + super.getApplicationBasePath();
                            }
                        }
                )
                .genericModelSubstitutes(ResponseEntity.class)
                .select()
                .paths(regex(DEFAULT_INCLUDE_PATTERN))
                .build();

    }
}
