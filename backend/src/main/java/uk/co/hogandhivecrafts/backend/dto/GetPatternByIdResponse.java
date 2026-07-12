package uk.co.hogandhivecrafts.backend.dto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import uk.co.hogandhivecrafts.backend.model.CraftType;

/**
 * Response DTO containing detailed information about a single pattern.
 * This DTO is returned when a pattern is requested by its ID.
 *
 * @param id        unique identifier of the pattern
 * @param name      name of the pattern
 * @param source    original source or reference for the pattern
 * @param craftType type of craft this pattern is for
 * @param notes     additional notes about the pattern
 * @param createdAt timestamp when the pattern was created
 * @param updatedAt timestamp when the pattern was last updated
 * @param userId    ID of the user who owns this pattern
 * @param fileIds   list of file IDs associated with this pattern
 */
public record GetPatternByIdResponse(
        UUID id,
        String name,
        String source,
        CraftType craftType,
        String notes,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        UUID userId,
        List<UUID> fileIds
) {
}
