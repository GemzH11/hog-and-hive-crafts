package uk.co.hogandhivecrafts.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Sort;
import uk.co.hogandhivecrafts.backend.model.PatternSortField;

/**
 * Request DTO for retrieving a paginated list of patterns.
 * All fields are optional and will use default values if not provided.
 *
 * @param page          zero-indexed page number; defaults to 0 if not specified.
 *                      Must be greater than or equal to 0.
 * @param size          number of items per page; defaults to configured value if not specified.
 *                      Must be between 1 and 100 (inclusive).
 * @param sortField     field to sort patterns by; defaults to configured value if not specified.
 *                      Valid values are: ID, NAME, CREATED_AT, UPDATED_AT.
 * @param sortDirection sort direction (ASC or DESC); defaults to configured value if not specified.
 */
public record GetAllPatternsRequest(
        @Min(value = 0, message = "Page must be greater than or equal to 0")
        Integer page,

        @Min(value = 1, message = "Size must be greater than or equal to 1")
        @Max(value = 100, message = "Size must be less than or equal to 100")
        Integer size,

        PatternSortField sortField,
        Sort.Direction sortDirection
) {
}
