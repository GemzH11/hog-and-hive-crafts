package uk.co.hogandhivecrafts.backend.dto;

import java.util.UUID;

/**
 * Response DTO containing the ID of the created pattern. This DTO is returned when a new pattern is
 * created.
 *
 * @param id unique identifier of the pattern
 */
public record PostPatternResponse(UUID id) {
}