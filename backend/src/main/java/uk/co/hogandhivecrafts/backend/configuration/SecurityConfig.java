package uk.co.hogandhivecrafts.backend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    // TODO: For development purposes only, this should be properly configured for production
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http.csrf(AbstractHttpConfigurer::disable).cors(cors -> {
                }) // use MVC CORS configuration from WebConfig
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        return http.build();
    }
}