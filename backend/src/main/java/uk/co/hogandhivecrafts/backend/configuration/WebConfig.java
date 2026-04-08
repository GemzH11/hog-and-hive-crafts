package uk.co.hogandhivecrafts.backend.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // Restrict the /api prefix to only custom controllers in the project's controller package,
        // preventing it from affecting Spring Boot's own controllers (e.g. error controller).
        configurer.addPathPrefix("/api",
                HandlerTypePredicate.forBasePackage("uk.co.hogandhivecrafts.backend.controller"));
    }
}