package uk.co.hogandhivecrafts.backend.support.testdata;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import uk.co.hogandhivecrafts.backend.dto.GetAllPatternsItem;
import uk.co.hogandhivecrafts.backend.dto.GetAllPatternsRequest;
import uk.co.hogandhivecrafts.backend.dto.GetAllPatternsResponse;
import uk.co.hogandhivecrafts.backend.dto.GetPatternByIdResponse;
import uk.co.hogandhivecrafts.backend.model.CraftType;
import uk.co.hogandhivecrafts.backend.model.PatternSortField;

public class PatternsDtoTestData {
  private static final Integer PAGE = 1;
  private static final Integer SIZE = 10;
  private static final PatternSortField PATTERN_SORT_FIELD = PatternSortField.NAME;
  private static final Sort.Direction SORT_DIRECTION = Sort.Direction.DESC;
  private static final Integer TOTAL_ELEMENTS = 45;
  private static final Integer TOTAL_PAGES = 5;
  private static final UUID PATTERN_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");
  private static final UUID USER_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
  private static final UUID FILE_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
  private static final String PATTERN_NAME = "Test pattern";
  private static final String PATTERN_SOURCE = "web";
  private static final CraftType PATTERN_CRAFT_TYPE = CraftType.OTHER;
  private static final String PATTERN_NOTES = "notes";
  private static final OffsetDateTime CREATED_DATE = OffsetDateTime.parse("2020-01-01T01:00:00Z");
  private static final OffsetDateTime UPDATED_DATE = OffsetDateTime.parse("2020-01-01T02:00:00Z");


  private PatternsDtoTestData() {
    // prevent instantiation
  }

  /**
   * Builds a default GetAllPatternsRequest object with all fields populated
   *
   * @return a fully-populated GetAllPatternsRequest object with default properties
   */
  public static GetAllPatternsRequest buildDefaultGetAllPatternsRequest() {
    return new GetAllPatternsRequest(PAGE, SIZE, PATTERN_SORT_FIELD, SORT_DIRECTION);
  }

  /**
   * Builds an empty GetAllPatternsRequest object
   *
   * @return an empty GetAllPatternsRequest object
   */
  public static GetAllPatternsRequest buildEmptyGetAllPatternsRequest() {
    return new GetAllPatternsRequest(null, null, null, null);
  }

  /**
   * Builds a default GetAllPatternsItem object with all fields populated
   *
   * @param patternId the pattern ID to associate with the response
   * @param fileIds   the file IDs to associate with the pattern
   * @return a fully-populated GetAllPatternsItem object with default properties
   */
  public static GetAllPatternsItem buildDefaultGetAllPatternsItem(UUID patternId,
                                                                  List<UUID> fileIds) {
    return new GetAllPatternsItem(patternId, PATTERN_NAME, PATTERN_CRAFT_TYPE, CREATED_DATE,
                                  UPDATED_DATE, fileIds);
  }

  /**
   * Builds a default GetAllPatternsItem object using a custom pattern ID and default file IDs
   *
   * @param patternId the pattern ID to associate with the response
   * @return a fully-populated GetAllPatternsItem object with default properties
   */
  public static GetAllPatternsItem buildDefaultGetAllPatternsItem(UUID patternId) {
    return buildDefaultGetAllPatternsItem(patternId, List.of(FILE_ID));
  }

  /**
   * Builds a default GetAllPatternsItem object using a default set of IDs
   *
   * @return a fully-populated GetAllPatternsItem object with default properties
   */
  public static GetAllPatternsItem buildDefaultGetAllPatternsItem() {
    return buildDefaultGetAllPatternsItem(PATTERN_ID, List.of(FILE_ID));
  }

  /**
   * Builds a default GetAllPatternsResponse object with all fields populated
   *
   * @param patternIds the pattern IDs to associate with the response
   * @return a fully-populated GetAllPatternsResponse object with default properties
   */
  public static GetAllPatternsResponse buildDefaultGetAllPatternsResponse(List<UUID> patternIds) {
    List<GetAllPatternsItem> patterns = patternIds.stream()
                                                  .map(
                                                      PatternsDtoTestData::buildDefaultGetAllPatternsItem)
                                                  .toList();
    return new GetAllPatternsResponse(patterns, TOTAL_ELEMENTS, TOTAL_PAGES, PAGE, SIZE);
  }

  /**
   * Builds a default GetAllPatternsResponse object using a default set of IDs
   *
   * @return a fully-populated GetAllPatternsResponse object with default properties
   */
  public static GetAllPatternsResponse buildDefaultGetAllPatternsResponse() {
    return buildDefaultGetAllPatternsResponse(Collections.singletonList(PATTERN_ID));
  }

  /**
   * Builds an empty GetAllPatternsResponse object
   *
   * @return an empty GetAllPatternsResponse object
   */
  public static GetAllPatternsResponse buildEmptyGetAllPatternsResponse() {
    return new GetAllPatternsResponse(Collections.emptyList(), TOTAL_ELEMENTS, TOTAL_PAGES, PAGE,
                                      SIZE);
  }

  /**
   * Builds a default GetPatternByIdResponse object with all fields populated
   *
   * @param patternId the pattern ID to associate with the response
   * @param userId    the user ID to associate with the pattern
   * @param fileIds   the file IDs to associate with the pattern
   * @return a fully-populated GetPatternByIdResponse object with default properties
   */
  public static GetPatternByIdResponse buildDefaultGetPatternByIdResponse(UUID patternId,
                                                                          UUID userId,
                                                                          List<UUID> fileIds) {
    return new GetPatternByIdResponse(patternId, PATTERN_NAME, PATTERN_SOURCE, PATTERN_CRAFT_TYPE,
                                      PATTERN_NOTES, CREATED_DATE, UPDATED_DATE, userId, fileIds);
  }

  /**
   * Builds a default GetPatternByIdResponse object using a custom pattern ID and default user and
   * file IDs
   *
   * @param patternId the pattern ID to associate with the response
   * @return a fully-populated GetPatternByIdResponse object with default properties
   */
  public static GetPatternByIdResponse buildDefaultGetPatternByIdResponse(UUID patternId) {
    return buildDefaultGetPatternByIdResponse(patternId, USER_ID, List.of(FILE_ID));
  }

  /**
   * Builds a default GetPatternByIdResponse object using a default set of IDs
   *
   * @return a fully-populated GetPatternByIdResponse object with default properties
   */
  public static GetPatternByIdResponse buildDefaultGetPatternByIdResponse() {
    return buildDefaultGetPatternByIdResponse(PATTERN_ID, USER_ID, List.of(FILE_ID));
  }
}
