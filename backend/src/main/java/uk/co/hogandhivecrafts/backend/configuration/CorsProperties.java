package uk.co.hogandhivecrafts.backend.configuration;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for CORS (Cross-Origin Resource Sharing) settings, loaded from
 * application.yml under the {@code app.cors} prefix.
 *
 * <p>These properties control which origins are allowed to make cross-origin requests to the API.
 * If no origins are configured or the list is empty, CORS is disabled.
 *
 * @param allowedOrigins origins allowed to make cross-origin requests to the API, such as
 *                       {@code http://localhost:5173} or {@code https://example.com}
 */
@ConfigurationProperties(prefix = "app.cors")
public record CorsProperties(List<String> allowedOrigins) {
}
