package com.minor.store.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@Import(SwaggerConfiguration.class)
public class MvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/doc/v1/**").addResourceLocations("classpath:/doc/v1/");
        registry.addResourceHandler("/doc/v1/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
