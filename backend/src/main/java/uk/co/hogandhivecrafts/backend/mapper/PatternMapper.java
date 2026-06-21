package uk.co.hogandhivecrafts.backend.mapper;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import uk.co.hogandhivecrafts.backend.dto.GetAllPatternsItem;
import uk.co.hogandhivecrafts.backend.dto.GetAllPatternsResponse;
import uk.co.hogandhivecrafts.backend.dto.GetPatternByIdResponse;
import uk.co.hogandhivecrafts.backend.entity.Pattern;

import java.util.List;

/**
 * Mapper component for converting Pattern entities to their corresponding DTOs.
 * <p>
 * This component handles the transformation of Pattern JPA entities into data transfer
 * objects (DTOs) used in API responses. It provides methods to map individual patterns,
 * paginated pattern lists, and detailed pattern information.
 */
@Component
public class PatternMapper {
    /**
     * Maps a single Pattern entity to a GetAllPatternsItem DTO for use in paginated list responses.
     * This DTO contains a summary of pattern information (without source and notes).
     *
     * @param pattern the Pattern entity to map
     * @return a GetAllPatternsItem DTO containing the pattern's summary information
     */
    public GetAllPatternsItem toGetAllPatternsItem(Pattern pattern) {
        return new GetAllPatternsItem(
                pattern.getId(),
                pattern.getName(),
                pattern.getCraftType(),
                pattern.getCreatedAt(),
                pattern.getUpdatedAt(),
                pattern.getFileIds()
        );
    }

    /**
     * Maps a page of Pattern entities to a GetAllPatternsResponse DTO containing
     * a list of pattern summaries along with pagination metadata.
     *
     * @param patterns a Page of Pattern entities to be mapped
     * @return a GetAllPatternsResponse DTO containing the mapped patterns and pagination information
     */
    public GetAllPatternsResponse toGetAllPatternsResponse(Page<Pattern> patterns) {
        List<GetAllPatternsItem> content = patterns.getContent()
                .stream()
                .map(this::toGetAllPatternsItem)
                .toList();
        return new GetAllPatternsResponse(
                content, patterns.getTotalElements(), patterns.getTotalPages(), patterns.getNumber(), patterns.getSize()
        );
    }

    /**
     * Maps a single Pattern entity to a GetPatternByIdResponse DTO for use in detailed
     * pattern retrieval responses. This DTO contains the complete pattern information
     * including source, notes, and all other fields.
     *
     * @param pattern the Pattern entity to map
     * @return a GetPatternByIdResponse DTO containing the full pattern information
     */
    public GetPatternByIdResponse toGetPatternByIdResponse(Pattern pattern) {
        return new GetPatternByIdResponse(
                pattern.getId(),
                pattern.getName(),
                pattern.getSource(),
                pattern.getCraftType(),
                pattern.getNotes(),
                pattern.getCreatedAt(),
                pattern.getUpdatedAt(),
                pattern.getUserId(),
                pattern.getFileIds()
        );
    }
}
