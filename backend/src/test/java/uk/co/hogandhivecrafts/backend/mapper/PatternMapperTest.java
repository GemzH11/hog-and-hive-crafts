package uk.co.hogandhivecrafts.backend.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import uk.co.hogandhivecrafts.backend.dto.GetAllPatternsItem;
import uk.co.hogandhivecrafts.backend.dto.GetAllPatternsResponse;
import uk.co.hogandhivecrafts.backend.dto.GetPatternByIdResponse;
import uk.co.hogandhivecrafts.backend.entity.Pattern;
import uk.co.hogandhivecrafts.backend.support.testdata.EntityTestData;

import java.util.List;
import java.util.UUID;

public class PatternMapperTest {
    private final PatternMapper mapper = new PatternMapper();

    @Test
    void toGetAllPatternsItem_mapsAllFields() {
        Pattern pattern = EntityTestData.buildDefaultPattern(UUID.randomUUID(), List.of(UUID.randomUUID(),
                UUID.randomUUID()), UUID.randomUUID());

        GetAllPatternsItem response = mapper.toGetAllPatternsItem(pattern);

        Assertions.assertThat(response.id()).isEqualTo(pattern.getId());
        Assertions.assertThat(response.name()).isEqualTo(pattern.getName());
        Assertions.assertThat(response.craftType()).isEqualTo(pattern.getCraftType());
        Assertions.assertThat(response.createdAt()).isEqualTo(pattern.getCreatedAt());
        Assertions.assertThat(response.updatedAt()).isEqualTo(pattern.getUpdatedAt());
        Assertions.assertThat(response.fileIds()).hasSameSizeAs(pattern.getFileIds());
        Assertions.assertThat(response.fileIds()).containsExactlyElementsOf(pattern.getFileIds());
    }

    @Test
    void toGetAllPatternsResponse_mapsAllFields() {
        Pattern pattern1 = EntityTestData.buildDefaultPattern(UUID.randomUUID(), List.of(UUID.randomUUID(),
                UUID.randomUUID()), UUID.randomUUID());
        Pattern pattern2 = EntityTestData.buildDefaultPattern(UUID.randomUUID(), List.of(UUID.randomUUID(),
                UUID.randomUUID()), UUID.randomUUID());

        Page<Pattern> page = new PageImpl<>(List.of(pattern1, pattern2), PageRequest.of(0, 20), 2);

        GetAllPatternsResponse response = mapper.toGetAllPatternsResponse(page);

        Assertions.assertThat(response.patterns()).hasSameSizeAs(page.getContent());
        Assertions.assertThat(response.patterns()).containsExactlyElementsOf(page.getContent().stream()
                .map(mapper::toGetAllPatternsItem)
                .toList());
        Assertions.assertThat(response.totalElements()).isEqualTo(page.getTotalElements());
        Assertions.assertThat(response.totalPages()).isEqualTo(page.getTotalPages());
        Assertions.assertThat(response.page()).isEqualTo(page.getNumber());
        Assertions.assertThat(response.size()).isEqualTo(page.getSize());
    }

    @Test
    void toGetPatternByIdResponse_mapsAllFields() {
        Pattern pattern = EntityTestData.buildDefaultPattern(UUID.randomUUID(), List.of(UUID.randomUUID(),
                UUID.randomUUID()), UUID.randomUUID());

        GetPatternByIdResponse response = mapper.toGetPatternByIdResponse(pattern);

        Assertions.assertThat(response.id()).isEqualTo(pattern.getId());
        Assertions.assertThat(response.name()).isEqualTo(pattern.getName());
        Assertions.assertThat(response.source()).isEqualTo(pattern.getSource());
        Assertions.assertThat(response.craftType()).isEqualTo(pattern.getCraftType());
        Assertions.assertThat(response.notes()).isEqualTo(pattern.getNotes());
        Assertions.assertThat(response.createdAt()).isEqualTo(pattern.getCreatedAt());
        Assertions.assertThat(response.updatedAt()).isEqualTo(pattern.getUpdatedAt());
        Assertions.assertThat(response.userId()).isEqualTo(pattern.getUserId());
        Assertions.assertThat(response.fileIds()).hasSameSizeAs(pattern.getFileIds());
        Assertions.assertThat(response.fileIds()).containsExactlyElementsOf(pattern.getFileIds());
    }
}
