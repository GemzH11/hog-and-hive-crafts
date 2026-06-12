package uk.co.hogandhivecrafts.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record GetAllPatternsRequest(
        @Min(value = 0, message = "Page must be greater than or equal to 0")
        Integer page,
        @Min(value = 1, message = "Size must be greater than or equal to 1")
        @Max(value = 100, message = "Size must be less than or equal to 100")
        Integer size) {
}
