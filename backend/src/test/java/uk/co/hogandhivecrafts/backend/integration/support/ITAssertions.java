package uk.co.hogandhivecrafts.backend.integration.support;

import org.assertj.core.api.Assertions;
import uk.co.hogandhivecrafts.backend.dto.GetAllPatternsItem;
import uk.co.hogandhivecrafts.backend.dto.GetPatternByIdResponse;
import uk.co.hogandhivecrafts.backend.entity.Pattern;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Helper assertions reused across integration tests to compare JPA entities with DTOs.
 * <p>
 * These helpers compare timestamps by Instant (point-in-time) to avoid failures caused by
 * differences in string formatting/precision between the database and JSON serialisation.
 * Overloads that accept file id lists are provided so callers can avoid traversing potentially-detached
 * entity collections (which would trigger LazyInitializationException).
 */
public final class ITAssertions {
    private ITAssertions() {
    }

    /**
     * Assert that a {@link Pattern} entity matches the details in a {@link GetPatternByIdResponse} DTO.
     * <p>
     * Timestamps are compared by {@link Instant} to avoid formatting/precision mismatches.
     * File IDs are supplied explicitly so this helper does not need to traverse a detached lazy collection.
     *
     * @param expected        the JPA {@link Pattern} entity persisted in the test
     * @param expectedFileIds the expected file IDs for the pattern
     * @param actual          the DTO returned by the API under test
     */
    public static void assertPatternEquals(Pattern expected, List<UUID> expectedFileIds,
                                           GetPatternByIdResponse actual) {
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.id()).isEqualTo(expected.getId());
        Assertions.assertThat(actual.name()).isEqualTo(expected.getName());
        Assertions.assertThat(actual.source()).isEqualTo(expected.getSource());
        Assertions.assertThat(actual.craftType()).isEqualTo(expected.getCraftType());
        Assertions.assertThat(actual.notes()).isEqualTo(expected.getNotes());

        if (expected.getCreatedAt() != null) {
            Instant exp = expected.getCreatedAt().toInstant();
            Instant act = actual.createdAt() == null ? null : actual.createdAt().toInstant();
            Assertions.assertThat(act).isEqualTo(exp);
        } else {
            Assertions.assertThat(actual.createdAt()).isNull();
        }

        if (expected.getUpdatedAt() != null) {
            Instant exp = expected.getUpdatedAt().toInstant();
            Instant act = actual.updatedAt() == null ? null : actual.updatedAt().toInstant();
            Assertions.assertThat(act).isEqualTo(exp);
        } else {
            Assertions.assertThat(actual.updatedAt()).isNull();
        }

        Assertions.assertThat(actual.userId()).isEqualTo(expected.getUserId());
        Assertions.assertThat(actual.fileIds()).isEqualTo(expectedFileIds);
    }

    /**
     * Assert that a {@link Pattern} entity matches a compact {@link GetAllPatternsItem} DTO used in paged responses.
     * <p>
     * Timestamps are compared by {@link Instant} to avoid formatting/precision mismatches.
     * File IDs are supplied explicitly so this helper does not need to traverse a detached lazy collection.
     *
     * @param expected        the JPA {@link Pattern} entity persisted in the test
     * @param expectedFileIds the expected file IDs for the pattern
     * @param actual          the DTO item returned in the paged response
     */
    public static void assertPatternItemEquals(Pattern expected, List<UUID> expectedFileIds, GetAllPatternsItem actual) {
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.id()).isEqualTo(expected.getId());
        Assertions.assertThat(actual.name()).isEqualTo(expected.getName());
        Assertions.assertThat(actual.craftType()).isEqualTo(expected.getCraftType());

        if (expected.getCreatedAt() != null) {
            Assertions.assertThat(actual.createdAt()).isNotNull();
            Assertions.assertThat(actual.createdAt().toInstant()).isEqualTo(expected.getCreatedAt().toInstant());
        } else {
            Assertions.assertThat(actual.createdAt()).isNull();
        }

        if (expected.getUpdatedAt() != null) {
            Assertions.assertThat(actual.updatedAt()).isNotNull();
            Assertions.assertThat(actual.updatedAt().toInstant()).isEqualTo(expected.getUpdatedAt().toInstant());
        } else {
            Assertions.assertThat(actual.updatedAt()).isNull();
        }

        Assertions.assertThat(actual.fileIds()).isEqualTo(expectedFileIds);
    }
}