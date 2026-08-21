package uk.co.hogandhivecrafts.backend.configuration;

import java.util.List;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC configuration class for the Hog & Hive Crafts backend.
 *
 * <p>This configuration handles:
 * - API path prefix routing (/api) - CORS (Cross-Origin Resource Sharing) setup for cross-origin
 * requests
 *
 * <p>All REST controllers in the "uk.co.hogandhivecrafts.backend.controller" package
 * automatically have the "/api" prefix applied to their request mappings.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

  private final CorsProperties cors;

  /**
   * Constructs WebConfig with CORS properties.
   *
   * @param cors the CORS configuration properties
   */
  public WebConfig(CorsProperties cors) {
    this.cors = cors;
  }

  /**
   * Configures path matching to add "/api" prefix to all controller endpoints.
   *
   * <p>This method applies the "/api" prefix to all request mappings of controllers
   * in the "uk.co.hogandhivecrafts.backend.controller" package.
   *
   * @param configurer the path match configurer
   */
  @Override
  public void configurePathMatch(PathMatchConfigurer configurer) {
    configurer.addPathPrefix("/api", HandlerTypePredicate.forBasePackage(
        "uk.co.hogandhivecrafts.backend.controller"));
  }

  /**
   * Configures CORS settings for the API.
   *
   * <p>Enables CORS for all /api/** endpoints with the configured allowed origins.
   * If no origins are configured or the list is empty, CORS is not set up.
   *
   * <p>Allowed methods: GET, POST, DELETE, OPTIONS
   * Allowed headers: All (*)
   *
   * @param registry the CORS registry to configure
   */
  @Override
  public void addCorsMappings(@NonNull CorsRegistry registry) {
    List<String> origins = cors.allowedOrigins();
    if (origins == null || origins.isEmpty()) {
      return;
    }

    registry.addMapping("/api/**")
            .allowedOrigins(origins.toArray(String[]::new))
            .allowedMethods("GET", "POST", "DELETE", "OPTIONS")
            .allowedHeaders("*");
  }
}