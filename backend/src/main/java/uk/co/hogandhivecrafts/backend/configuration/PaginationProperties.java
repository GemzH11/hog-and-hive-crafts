package uk.co.hogandhivecrafts.backend.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.Sort;
import uk.co.hogandhivecrafts.backend.model.PatternSortField;

/**
 * Configuration properties for pagination defaults, loaded from application.yml under "app
 * .pagination" prefix.
 *
 * <p>These properties define the default values used when clients do not specify pagination
 * parameters
 * in their API requests. All properties are required and must be configured in application.yml.
 *
 * @param defaultPageSize         the default number of items per page (must be between 1-100)
 * @param defaultSortDirection    the default sort direction (ASC or DESC)
 * @param defaultPatternSortField the default field to sort patterns by
 */
@ConfigurationProperties(prefix = "app.pagination")
public record PaginationProperties(Integer defaultPageSize, Sort.Direction defaultSortDirection,
                                   PatternSortField defaultPatternSortField) {
}
