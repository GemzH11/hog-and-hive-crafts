package uk.co.hogandhivecrafts.backend.support.assertions;

import uk.co.hogandhivecrafts.backend.dto.GetAllPatternsRequest;
import uk.co.hogandhivecrafts.backend.dto.GetAllPatternsResponse;
import uk.co.hogandhivecrafts.backend.dto.GetSinglePatternResponse;

import static org.assertj.core.api.Assertions.assertThat;

public final class PatternsDtoAssertions {
    private PatternsDtoAssertions() {
        // prevent instantiation
    }

    public static void assertGetAllPatternsRequestEquals(GetAllPatternsRequest actual, GetAllPatternsRequest expected) {
        assertThat(actual.page()).isEqualTo(expected.page());
        assertThat(actual.size()).isEqualTo(expected.size());
    }

    public static void assertGetAllPatternsResponseEquals(GetAllPatternsResponse actual, GetAllPatternsResponse expected) {
        assertThat(actual.patterns()).hasSameSizeAs(expected.patterns());
        assertThat(actual.patterns()).containsExactlyElementsOf(expected.patterns());
        assertThat(actual.totalElements()).isEqualTo(expected.totalElements());
        assertThat(actual.totalPages()).isEqualTo(expected.totalPages());
        assertThat(actual.page()).isEqualTo(expected.page());
        assertThat(actual.size()).isEqualTo(expected.size());
    }

    public static void assertGetSinglePatternResponseEquals(GetSinglePatternResponse actual, GetSinglePatternResponse expected) {
        assertThat(actual.id()).isEqualTo(expected.id());
        assertThat(actual.name()).isEqualTo(expected.name());
        assertThat(actual.source()).isEqualTo(expected.source());
        assertThat(actual.craftType()).isEqualTo(expected.craftType());
        assertThat(actual.notes()).isEqualTo(expected.notes());
        assertThat(actual.createdAt()).isEqualTo(expected.createdAt());
        assertThat(actual.updatedAt()).isEqualTo(expected.updatedAt());
        assertThat(actual.userId()).isEqualTo(expected.userId());
        assertThat(actual.fileIds().size()).isEqualTo(expected.fileIds().size());
        assertThat(actual.fileIds()).containsExactlyElementsOf(expected.fileIds());
    }
}
