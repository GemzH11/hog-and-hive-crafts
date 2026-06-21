package uk.co.hogandhivecrafts.backend.dto;

import uk.co.hogandhivecrafts.backend.model.CraftType;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record GetAllPatternsItem(
        UUID id,
        String name,
        CraftType craftType,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        List<UUID> fileIds
) {
}
