package uk.co.hogandhivecrafts.backend.dto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record GetPatternByIdResponse(
        UUID id,
        String name,
        String source,
        String craftType,
        String notes,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        UUID userId,
        List<UUID> fileIds
) {
}
