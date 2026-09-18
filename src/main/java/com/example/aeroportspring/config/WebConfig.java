package com.example.aeroportspring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Raccourci : /docs -> la page Scalar (static/scalar.html)
        registry.addRedirectViewController("/docs", "/scalar.html");
    }
}
