package uk.co.hogandhivecrafts.backend.integration.support;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import uk.co.hogandhivecrafts.backend.dto.pattern.GetAllPatternsItem;
import uk.co.hogandhivecrafts.backend.dto.pattern.GetAllPatternsResponse;
import uk.co.hogandhivecrafts.backend.dto.pattern.GetPatternByIdResponse;
import uk.co.hogandhivecrafts.backend.entity.Pattern;

/**
 * Shared assertions for pattern API integration-test responses.
 */
public class ITAssertions {
  /**
   * Asserts that a paginated response contains the expected pattern summaries and file IDs.
   *
   * @param expected                 patterns expected in the response
   * @param expectedFileIdsByPattern expected file IDs keyed by pattern ID
   * @param actual                   response returned by the API
   */
  public static void assertGetAllPatternsResponseMatchesPatternList(List<Pattern> expected,
                                                                    Map<UUID, List<UUID>> expectedFileIdsByPattern,
                                                                    GetAllPatternsResponse actual) {
    // Check have same size
    Assertions.assertThat(actual.patterns()).hasSameSizeAs(expected);

    // Pair up and check each pattern matches
    for (Pattern expectedPattern : expected) {
      // Search through actual patterns to find a match by ID
      GetAllPatternsItem actualPattern = actual.patterns()
                                               .stream()
                                               .filter(p -> p.id().equals(expectedPattern.getId()))
                                               .findFirst()
                                               .orElseThrow(() -> new AssertionError(
                                                   "Pattern with ID " + expectedPattern.getId() + " not found in actual response"));

      assertGetAllPatternsItemMatchesPattern(expectedPattern,
                                             expectedFileIdsByPattern.get(expectedPattern.getId()),
                                             actualPattern);
    }
  }

  /**
   * Asserts that a detailed response matches the expected pattern and associated file IDs.
   *
   * @param expected        expected pattern entity
   * @param expectedFileIds expected file IDs
   * @param actual          response returned by the API
   */
  public static void assertGetPatternByIdResponseMatchesPattern(Pattern expected,
                                                                List<UUID> expectedFileIds,
                                                                GetPatternByIdResponse actual) {
    Assertions.assertThat(actual.id()).isEqualTo(expected.getId());
    Assertions.assertThat(actual.name()).isEqualTo(expected.getName());
    Assertions.assertThat(actual.source()).isEqualTo(expected.getSource());
    Assertions.assertThat(actual.craftType()).isEqualTo(expected.getCraftType());
    Assertions.assertThat(actual.notes()).isEqualTo(expected.getNotes());
    Assertions.assertThat(actual.createdAt()).isEqualTo(expected.getCreatedAt());
    Assertions.assertThat(actual.updatedAt()).isEqualTo(expected.getUpdatedAt());
    Assertions.assertThat(actual.fileIds()).containsExactlyInAnyOrderElementsOf(expectedFileIds);
  }

  private static void assertGetAllPatternsItemMatchesPattern(Pattern expected,
                                                             List<UUID> expectedFileIds,
                                                             GetAllPatternsItem actual) {
    Assertions.assertThat(actual.id()).isEqualTo(expected.getId());
    Assertions.assertThat(actual.name()).isEqualTo(expected.getName());
    Assertions.assertThat(actual.craftType()).isEqualTo(expected.getCraftType());
    Assertions.assertThat(actual.createdAt()).isEqualTo(expected.getCreatedAt());
    Assertions.assertThat(actual.updatedAt()).isEqualTo(expected.getUpdatedAt());
    Assertions.assertThat(actual.fileIds()).containsExactlyInAnyOrderElementsOf(expectedFileIds);

  }
}
