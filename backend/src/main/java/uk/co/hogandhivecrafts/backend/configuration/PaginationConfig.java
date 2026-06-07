package uk.co.hogandhivecrafts.backend.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(PaginationProperties.class)
public class PaginationConfig {
}
