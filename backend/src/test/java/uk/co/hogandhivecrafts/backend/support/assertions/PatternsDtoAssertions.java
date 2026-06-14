package uk.co.hogandhivecrafts.backend.support.assertions;

import uk.co.hogandhivecrafts.backend.dto.GetAllPatternsItem;
import uk.co.hogandhivecrafts.backend.dto.GetAllPatternsRequest;
import uk.co.hogandhivecrafts.backend.dto.GetAllPatternsResponse;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(actual.page()).isEqualTo(expected.page());
        assertThat(actual.size()).isEqualTo(expected.size());
    }

    /**
     * Helper method to assert two GetAllPatternsResponse objects are equal
     *
     * @param actual   the GetAllPatternsResponse object built in the test
     * @param expected the GetAllPatternsResponse object that we are expecting
     */
    public static void assertGetAllPatternsResponseEquals(GetAllPatternsResponse actual, GetAllPatternsResponse expected) {
        assertThat(actual.patterns()).hasSameSizeAs(expected.patterns());
        assertThat(actual.patterns()).containsExactlyElementsOf(expected.patterns());
        assertThat(actual.totalElements()).isEqualTo(expected.totalElements());
        assertThat(actual.totalPages()).isEqualTo(expected.totalPages());
        assertThat(actual.page()).isEqualTo(expected.page());
        assertThat(actual.size()).isEqualTo(expected.size());
    }

    /**
     * Helper method to assert two GetAllPatternsItem objects are equal
     *
     * @param actual   the GetAllPatternsItem object built in the test
     * @param expected the GetAllPatternsItem object that we are expecting
     */
    public static void assertGetAllPatternsItemEquals(GetAllPatternsItem actual, GetAllPatternsItem expected) {
        assertThat(actual.id()).isEqualTo(expected.id());
        assertThat(actual.name()).isEqualTo(expected.name());
        assertThat(actual.craftType()).isEqualTo(expected.craftType());
        assertThat(actual.createdAt()).isEqualTo(expected.createdAt());
        assertThat(actual.updatedAt()).isEqualTo(expected.updatedAt());
        assertThat(actual.fileIds().size()).isEqualTo(expected.fileIds().size());
        assertThat(actual.fileIds()).containsExactlyElementsOf(expected.fileIds());
    }
}
