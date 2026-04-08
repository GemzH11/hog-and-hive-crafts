package uk.co.hogandhivecrafts.backend.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final CorsProperties cors;

    public WebConfig(CorsProperties cors) {
        this.cors = cors;
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/api",
                HandlerTypePredicate.forBasePackage("uk.co.hogandhivecrafts.backend.controller"));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        List<String> origins = cors.allowedOrigins();
        if (origins == null || origins.isEmpty()) {
            return;
        }

        registry.addMapping("/api/**")
                .allowedOrigins(origins.toArray(String[]::new))
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}