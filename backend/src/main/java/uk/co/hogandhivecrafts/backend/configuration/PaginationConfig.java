package uk.co.hogandhivecrafts.backend.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class that enables and registers PaginationProperties from application.yml.
 * <p>
 * This configuration class is responsible for loading pagination-related configuration properties
 * from the application configuration file and making them available as a Spring bean
 * for injection into other components.
 */
@Configuration
@EnableConfigurationProperties(PaginationProperties.class)
public class PaginationConfig {
}
