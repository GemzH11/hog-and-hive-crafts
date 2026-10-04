package uk.co.hogandhivecrafts.backend.support.assertions;

import java.util.List;
import org.assertj.core.api.Assertions;
import uk.co.hogandhivecrafts.backend.dto.pattern.GetAllPatternsResponse;
import uk.co.hogandhivecrafts.backend.dto.pattern.GetPatternByIdResponse;
import uk.co.hogandhivecrafts.backend.dto.pattern.PostPatternResponse;
import uk.co.hogandhivecrafts.backend.exception.CustomErrorResponse;
import uk.co.hogandhivecrafts.backend.exception.GlobalExceptionHandler;

/**
 * Shared assertions for pattern DTOs and API error payloads in unit tests.
 */
public class PatternDtoAssertions {
  /**
   * Asserts that two paginated pattern responses have equal items and metadata.
   *
   * @param expected expected response
   * @param actual   response under test
   */
  public static void assertGetAllPatternsResponseMatches(
      GetAllPatternsResponse expected, GetAllPatternsResponse actual) {
    Assertions.assertThat(actual.patterns()).containsExactlyElementsOf(expected.patterns());
    Assertions.assertThat(actual.totalElements()).isEqualTo(expected.totalElements());
    Assertions.assertThat(actual.totalPages()).isEqualTo(expected.totalPages());
    Assertions.assertThat(actual.page()).isEqualTo(expected.page());
    Assertions.assertThat(actual.size()).isEqualTo(expected.size());
  }

  /**
   * Asserts that two detailed pattern responses are equal.
   *
   * @param expected expected response
   * @param actual   response under test
   */
  public static void assertGetPatternByIdResponseMatches(
      GetPatternByIdResponse expected, GetPatternByIdResponse actual) {
    Assertions.assertThat(actual).isEqualTo(expected);
  }

  /**
   * Asserts that two pattern creation responses are equal.
   *
   * @param expected expected response
   * @param actual   response under test
   */
  public static void assertPostPatternResponseMatches(
      PostPatternResponse expected, PostPatternResponse actual) {
    Assertions.assertThat(actual).isEqualTo(expected);
  }

  /**
   * Asserts that an error response describes the expected invalid request.
   *
   * @param actual        response under test
   * @param expectedPath  expected request path
   * @param expectedError expected error detail
   */
  public static void assertInvalidRequestMatches(CustomErrorResponse actual, String expectedPath,
                                                 String expectedError) {
    assertErrorResponseMatches(actual, expectedPath, GlobalExceptionHandler.INVALID_REQUEST,
                               List.of(expectedError));
  }

  /**
   * Asserts that an error response describes the expected missing resource.
   *
   * @param actual        response under test
   * @param expectedPath  expected request path
   * @param expectedError expected error detail
   */
  public static void assertResourceNotFoundMatches(CustomErrorResponse actual, String expectedPath,
                                                   String expectedError) {
    assertErrorResponseMatches(actual, expectedPath, GlobalExceptionHandler.RESOURCE_NOT_FOUND,
                               List.of(expectedError));
  }

  private static void assertErrorResponseMatches(CustomErrorResponse actual, String expectedPath,
                                                 String expectedMessage,
                                                 List<String> expectedErrors) {
    Assertions.assertThat(actual.path()).isEqualTo(expectedPath);
    Assertions.assertThat(actual.message()).isEqualTo(expectedMessage);
    Assertions.assertThat(actual.errors()).containsExactlyElementsOf(expectedErrors);
  }
}
