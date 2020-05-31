package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    // ResourceHandler를 추가하자.
    // 기존에 재공하는 ResourceHandler는 유지하면서
    // 원하는 resource handler만 추가할 수 있다.
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/m/**")
                .addResourceLocations("classpath:/m/")
                .setCachePeriod(20);
    }
}
