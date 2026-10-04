package uk.co.hogandhivecrafts.backend.support.testdata;

import java.util.List;
import java.util.UUID;
import uk.co.hogandhivecrafts.backend.dto.pattern.GetAllPatternsItem;
import uk.co.hogandhivecrafts.backend.dto.pattern.GetPatternByIdResponse;
import uk.co.hogandhivecrafts.backend.dto.pattern.PostPatternRequest;

/**
 * Creates deterministic pattern DTO instances for unit tests.
 */
public class PatternDtoTestData {
  /**
   * Builds a pattern creation request using the supplied index.
   *
   * @param index suffix used to generate distinct fixture values
   * @return the generated request
   */
  public static PostPatternRequest buildPostPatternRequest(int index) {
    return new PostPatternRequest(String.format(TestDataConstants.PATTERN_NAME, index),
                                  String.format(TestDataConstants.PATTERN_SOURCE, index),
                                  TestDataConstants.PATTERN_CRAFT_TYPE,
                                  String.format(TestDataConstants.PATTERN_NOTES, index));
  }

  /**
   * Builds a pattern summary DTO using the supplied index.
   *
   * @param index suffix used to generate distinct fixture values
   * @return the generated summary item
   */
  public static GetAllPatternsItem buildGetAllPatternsItem(int index) {
    return new GetAllPatternsItem(
        UUID.fromString(String.format(TestDataConstants.PATTERN_ID_STRING, index)),
        String.format(TestDataConstants.PATTERN_NAME, index), TestDataConstants.PATTERN_CRAFT_TYPE,
        TestDataConstants.DATE, TestDataConstants.DATE,
        List.of(UUID.fromString(String.format(TestDataConstants.FILE_ID_STRING, index)),
                UUID.fromString(String.format(TestDataConstants.FILE_ID_STRING, index + 1))));
  }

  /**
   * Builds a detailed pattern response using the supplied index.
   *
   * @param index suffix used to generate distinct fixture values
   * @return the generated detailed response
   */
  public static GetPatternByIdResponse buildGetPatternByIdResponse(int index) {
    GetAllPatternsItem item = buildGetAllPatternsItem(index);
    return new GetPatternByIdResponse(item.id(), item.name(),
                                      String.format(TestDataConstants.PATTERN_SOURCE, index),
                                      item.craftType(),
                                      String.format(TestDataConstants.PATTERN_NOTES, index),
                                      item.createdAt(), item.updatedAt(), item.fileIds());
  }

}
