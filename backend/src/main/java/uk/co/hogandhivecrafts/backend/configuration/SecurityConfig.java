package uk.co.hogandhivecrafts.backend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security configuration class for the Hog & Hive Crafts backend.
 *
 * <p>CSRF protection is disabled, CORS is delegated to {@link WebConfig}, and all requests are
 * currently permitted without authentication. Authentication and authorization must be configured
 * before exposing the application in production.
 */
@Configuration
public class SecurityConfig {

  /**
   * Configures the security filter chain for HTTP requests.
   *
   * <p>CSRF protection is disabled, CORS uses the MVC configuration, and every request is
   * permitted without authentication.
   *
   * @param http the HttpSecurity object to configure
   * @return the configured SecurityFilterChain
   */
  @Bean
  // TODO: For development purposes only, this should be properly configured for production
  public SecurityFilterChain filterChain(HttpSecurity http) {
    http.csrf(AbstractHttpConfigurer::disable).cors(cors -> {})
        // use MVC CORS configuration from WebConfig
        .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

    return http.build();
  }
}