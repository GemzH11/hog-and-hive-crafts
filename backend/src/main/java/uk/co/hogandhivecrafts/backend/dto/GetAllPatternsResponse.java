package uk.co.hogandhivecrafts.backend.dto;

import java.util.List;

/**
 * Response DTO containing a paginated list of patterns and pagination metadata.
 *
 * @param patterns      list of pattern items returned for the current page
 * @param totalElements total number of patterns in the database
 * @param totalPages    total number of pages available
 * @param page          current page number (zero-indexed)
 * @param size          number of items per page
 */
public record GetAllPatternsResponse(
        List<GetAllPatternsItem> patterns,
        long totalElements,
        int totalPages,
        int page,
        int size
) {
}