package uk.co.hogandhivecrafts.backend.dto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import uk.co.hogandhivecrafts.backend.model.CraftType;

/**
 * Response DTO representing a single pattern item in a paginated list.
 * This DTO contains a summary of pattern information (without the source and notes fields).
 *
 * @param id        unique identifier of the pattern
 * @param name      name of the pattern
 * @param craftType type of craft this pattern is for
 * @param createdAt timestamp when the pattern was created
 * @param updatedAt timestamp when the pattern was last updated
 * @param fileIds   list of file IDs associated with this pattern
 */
public record GetAllPatternsItem(
        UUID id,
        String name,
        CraftType craftType,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        List<UUID> fileIds
) {
}
