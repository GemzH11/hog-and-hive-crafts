package uk.co.hogandhivecrafts.backend.mapper;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import uk.co.hogandhivecrafts.backend.dto.GetAllPatternsItem;
import uk.co.hogandhivecrafts.backend.dto.GetAllPatternsResponse;
import uk.co.hogandhivecrafts.backend.dto.GetPatternByIdResponse;
import uk.co.hogandhivecrafts.backend.entity.Pattern;

import java.util.List;

@Component
public class PatternMapper {
    /**
     * Helper method to map a Page of Pattern entities to a GetAllPatternsResponse DTO,
     * which includes pagination metadata and a list of pattern summaries
     *
     * @param pattern Page the Page of Pattern entities to be mapped
     * @return a GetAllPatternsResponse DTO containing the mapped patterns and pagination information
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
     * Helper method to map a page of pattern entities to a GetAllPatternsResponse object
     * containing a list of patterns and pagination metadata
     *
     * @param patterns page of pattern entities to be mapped
     * @return mapped GetAllPatternsResponse object containing a list of patterns and pagination metadata
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
