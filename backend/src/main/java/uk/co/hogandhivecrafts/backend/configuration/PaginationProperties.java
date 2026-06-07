package uk.co.hogandhivecrafts.backend.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.pagination")
public record PaginationProperties(Integer defaultPageSize) {
}
