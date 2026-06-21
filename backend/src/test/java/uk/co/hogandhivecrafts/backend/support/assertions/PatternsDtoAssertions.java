package uk.co.hogandhivecrafts.backend.support.assertions;

import org.assertj.core.api.Assertions;
import uk.co.hogandhivecrafts.backend.dto.GetAllPatternsItem;
import uk.co.hogandhivecrafts.backend.dto.GetAllPatternsRequest;
import uk.co.hogandhivecrafts.backend.dto.GetAllPatternsResponse;
import uk.co.hogandhivecrafts.backend.dto.GetPatternByIdResponse;

public final class PatternsDtoAssertions {
    private PatternsDtoAssertions() {
        // prevent instantiation
    }

    /**
     * Helper method to assert two GetAllPatternRequest objects are equal
     *
     * @param actual   the GetAllPatternRequest object built in the test
     * @param expected the GetAllPatternRequest object that we are expecting
     */
    public static void assertGetAllPatternsRequestEquals(GetAllPatternsRequest actual, GetAllPatternsRequest expected) {
        Assertions.assertThat(actual.page()).isEqualTo(expected.page());
        Assertions.assertThat(actual.size()).isEqualTo(expected.size());
    }

    /**
     * Helper method to assert two GetAllPatternsResponse objects are equal
     *
     * @param actual   the GetAllPatternsResponse object built in the test
     * @param expected the GetAllPatternsResponse object that we are expecting
     */
    public static void assertGetAllPatternsResponseEquals(GetAllPatternsResponse actual, GetAllPatternsResponse expected) {
        Assertions.assertThat(actual.patterns()).hasSameSizeAs(expected.patterns());
        Assertions.assertThat(actual.patterns()).containsExactlyElementsOf(expected.patterns());
        Assertions.assertThat(actual.totalElements()).isEqualTo(expected.totalElements());
        Assertions.assertThat(actual.totalPages()).isEqualTo(expected.totalPages());
        Assertions.assertThat(actual.page()).isEqualTo(expected.page());
        Assertions.assertThat(actual.size()).isEqualTo(expected.size());
    }

    /**
     * Helper method to assert two GetAllPatternsItem objects are equal
     *
     * @param actual   the GetAllPatternsItem object built in the test
     * @param expected the GetAllPatternsItem object that we are expecting
     */
    public static void assertGetAllPatternsItemEquals(GetAllPatternsItem actual, GetAllPatternsItem expected) {
        Assertions.assertThat(actual.id()).isEqualTo(expected.id());
        Assertions.assertThat(actual.name()).isEqualTo(expected.name());
        Assertions.assertThat(actual.craftType()).isEqualTo(expected.craftType());
        Assertions.assertThat(actual.createdAt()).isEqualTo(expected.createdAt());
        Assertions.assertThat(actual.updatedAt()).isEqualTo(expected.updatedAt());
        Assertions.assertThat(actual.fileIds().size()).isEqualTo(expected.fileIds().size());
        Assertions.assertThat(actual.fileIds()).containsExactlyElementsOf(expected.fileIds());
    }

    public static void assertGetPatternByIdResponseEquals(GetPatternByIdResponse actual, GetPatternByIdResponse expected) {
        Assertions.assertThat(actual.id()).isEqualTo(expected.id());
        Assertions.assertThat(actual.name()).isEqualTo(expected.name());
        Assertions.assertThat(actual.craftType()).isEqualTo(expected.craftType());
        Assertions.assertThat(actual.createdAt()).isEqualTo(expected.createdAt());
        Assertions.assertThat(actual.updatedAt()).isEqualTo(expected.updatedAt());
        Assertions.assertThat(actual.fileIds().size()).isEqualTo(expected.fileIds().size());
        Assertions.assertThat(actual.fileIds()).containsExactlyElementsOf(expected.fileIds());
    }
}
