package uk.co.hogandhivecrafts.backend.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.Sort;
import uk.co.hogandhivecrafts.backend.dto.PatternSortField;

@ConfigurationProperties(prefix = "app.pagination")
public record PaginationProperties(Integer defaultPageSize, Sort.Direction defaultSortDirection,
                                   PatternSortField defaultPatternSortField) {
}
