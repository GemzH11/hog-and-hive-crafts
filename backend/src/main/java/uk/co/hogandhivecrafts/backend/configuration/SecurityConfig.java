package uk.co.hogandhivecrafts.backend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security configuration class for the Hog & Hive Crafts backend.
 *
 * <p>This configuration handles security settings including CSRF protection, CORS integration,
 * and HTTP authorization. Currently configured for development with all endpoints allowing public
 * access.
 * <strong>TODO: For production, this should be properly configured with proper authentication
 * and authorization.</strong>
 */
@Configuration
public class SecurityConfig {

  /**
   * Configures the security filter chain for HTTP requests.
   *
   * <p>Current configuration:
   * - CSRF protection is disabled (suitable for API with token-based auth) - CORS configuration is
   * delegated to MVC configuration (WebConfig) - All HTTP requests are permitted without
   * authentication (development only)
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