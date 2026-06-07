package uk.co.hogandhivecrafts.backend.mapper;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import uk.co.hogandhivecrafts.backend.dto.GetAllPatternsResponse;
import uk.co.hogandhivecrafts.backend.dto.GetSinglePatternResponse;
import uk.co.hogandhivecrafts.backend.entity.Pattern;

import java.util.List;

@Component
public class PatternMapper {
    public GetSinglePatternResponse toGetSinglePatternResponse(Pattern pattern) {
        return new GetSinglePatternResponse(
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

    public GetAllPatternsResponse toGetAllPatternsResponse(Page<Pattern> patterns) {
        List<GetSinglePatternResponse> content = patterns.getContent()
                .stream()
                .map(this::toGetSinglePatternResponse)
                .toList();
        return new GetAllPatternsResponse(
                content, patterns.getTotalElements(), patterns.getTotalPages(), patterns.getNumber(), patterns.getSize()
        );
    }
}
