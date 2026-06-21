package uk.co.hogandhivecrafts.backend.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Configuration properties for CORS (Cross-Origin Resource Sharing) settings,
 * loaded from application.yml under "app.cors" prefix.
 * <p>
 * These properties control which origins are allowed to make cross-origin requests
 * to the API. If no origins are configured or the list is empty, CORS is disabled.
 *
 * @param allowedOrigins list of origins that are allowed to make CORS requests to the API.
 *                       Examples: ["http://localhost:3000", "https://example.com"]
 */
@ConfigurationProperties(prefix = "app.cors")
public record CorsProperties(List<String> allowedOrigins) {
}
