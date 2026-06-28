package uk.co.hogandhivecrafts.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Main entry point for the Hog & Hive Crafts backend Spring Boot application.
 * <p>
 * This application provides a REST API for managing craft patterns, users, and associated files.
 * Configuration properties are automatically scanned and registered as beans from application.yml.
 */
@SpringBootApplication
// Search the classpath for classes annotated with @ConfigurationProperties and register them as beans with values
// bound from application.yml.
@ConfigurationPropertiesScan
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

}
