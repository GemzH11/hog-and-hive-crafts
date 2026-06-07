package uk.co.hogandhivecrafts.backend.support.testdata;

import uk.co.hogandhivecrafts.backend.dto.GetAllPatternsResponse;
import uk.co.hogandhivecrafts.backend.dto.GetSinglePatternResponse;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PatternsDtoTestData {
    private static final Integer PAGE = 1;
    private static final Integer SIZE = 10;
    private static final Integer TOTAL_ELEMENTS = 45;
    private static final Integer TOTAL_PAGES = 5;
    private static final UUID PATTERN_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    private static final UUID USER_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID FILE_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final String PATTERN_NAME = "Test pattern";
    private static final String PATTERN_SOURCE = "web";
    private static final String PATTERN_CRAFT_TYPE = "craft";
    private static final String PATTERN_NOTES = "notes";
    private static final OffsetDateTime CREATED_DATE = OffsetDateTime.parse("2020-01-01T01:00:00Z");
    private static final OffsetDateTime UPDATED_DATE = OffsetDateTime.parse("2020-01-01T02:00:00Z");


    private PatternsDtoTestData() {
        // prevent instantiation
    }

    public static GetAllPatternsResponse buildDefaultGetAllPatternsResponse(List<UUID> patternIds) {
        List<GetSinglePatternResponse> responses = patternIds.stream()
                .map(PatternsDtoTestData::buildDefaultGetSinglePatternResponse)
                .toList();
        return new GetAllPatternsResponse(responses, TOTAL_ELEMENTS, TOTAL_PAGES, PAGE, SIZE);
    }

    public static GetAllPatternsResponse buildDefaultGetAllPatternsResponse() {
        return buildDefaultGetAllPatternsResponse(Collections.singletonList(PATTERN_ID));
    }

    public static GetAllPatternsResponse buildEmptyGetAllPatternsResponse() {
        return new GetAllPatternsResponse(Collections.emptyList(), TOTAL_ELEMENTS, TOTAL_PAGES, PAGE, SIZE);
    }

    public static GetSinglePatternResponse buildDefaultGetSinglePatternResponse(UUID patternId, UUID userId, List<UUID> fileIds) {
        return new GetSinglePatternResponse(patternId, PATTERN_NAME, PATTERN_SOURCE, PATTERN_CRAFT_TYPE,
                PATTERN_NOTES, CREATED_DATE, UPDATED_DATE, userId, fileIds);
    }

    public static GetSinglePatternResponse buildDefaultGetSinglePatternResponse(UUID patternId) {
        return buildDefaultGetSinglePatternResponse(patternId, USER_ID, List.of(FILE_ID));
    }

    public static GetSinglePatternResponse buildDefaultGetSinglePatternResponse() {
        return buildDefaultGetSinglePatternResponse(PATTERN_ID, USER_ID, List.of(FILE_ID));
    }
}
