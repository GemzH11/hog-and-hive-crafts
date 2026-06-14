package uk.co.hogandhivecrafts.backend.dto;

import java.util.List;

public record GetAllPatternsResponse(
        List<GetAllPatternsItem> patterns,
        long totalElements,
        int totalPages,
        int page,
        int size
) {
}