package uk.co.hogandhivecrafts.backend.mapper;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import uk.co.hogandhivecrafts.backend.dto.pattern.GetAllPatternsItem;
import uk.co.hogandhivecrafts.backend.dto.pattern.GetAllPatternsResponse;
import uk.co.hogandhivecrafts.backend.dto.pattern.GetPatternByIdResponse;
import uk.co.hogandhivecrafts.backend.dto.pattern.PostPatternRequest;
import uk.co.hogandhivecrafts.backend.entity.Pattern;
import uk.co.hogandhivecrafts.backend.support.testdata.EntityTestData;
import uk.co.hogandhivecrafts.backend.support.testdata.PatternDtoTestData;

/**
 * Tests conversion between pattern entities and API DTOs.
 */
class PatternMapperTest {
  @Test
  void toGetAllPatternsItem_mapsAllFields() {
    Pattern pattern = EntityTestData.buildPattern(0);

    GetAllPatternsItem result = new PatternMapper().toGetAllPatternsItem(pattern);

    assertGetAllPatternsItemMatchesPattern(pattern, result);
  }

  @Test
  void toGetAllPatternsResponse_mapsAllFields() {
    Page<Pattern> page = new PageImpl<>(
        List.of(EntityTestData.buildPattern(0), EntityTestData.buildPattern(1)));

    GetAllPatternsResponse result = new PatternMapper().toGetAllPatternsResponse(page);

    Assertions.assertThat(result.patterns()).hasSize(2);
    assertGetAllPatternsItemMatchesPattern(page.getContent().get(0), result.patterns().get(0));
    assertGetAllPatternsItemMatchesPattern(page.getContent().get(1), result.patterns().get(1));

    Assertions.assertThat(result.totalElements()).isEqualTo(page.getTotalElements());
    Assertions.assertThat(result.totalPages()).isEqualTo(page.getTotalPages());
    Assertions.assertThat(result.page()).isEqualTo(page.getNumber());
    Assertions.assertThat(result.size()).isEqualTo(page.getSize());
  }

  @Test
  void toGetPatternByIdResponse_mapsAllFields() {
    Pattern pattern = EntityTestData.buildPattern(0);

    GetPatternByIdResponse result = new PatternMapper().toGetPatternByIdResponse(pattern);

    Assertions.assertThat(result.id()).isEqualTo(pattern.getId());
    Assertions.assertThat(result.name()).isEqualTo(pattern.getName());
    Assertions.assertThat(result.source()).isEqualTo(pattern.getSource());
    Assertions.assertThat(result.craftType()).isEqualTo(pattern.getCraftType());
    Assertions.assertThat(result.notes()).isEqualTo(pattern.getNotes());
    Assertions.assertThat(result.createdAt()).isEqualTo(pattern.getCreatedAt());
    Assertions.assertThat(result.updatedAt()).isEqualTo(pattern.getUpdatedAt());
    Assertions.assertThat(result.fileIds()).containsExactlyElementsOf(pattern.getFileIds());
  }

  @Test
  void toPattern_mapsAllFields() {
    PostPatternRequest request = PatternDtoTestData.buildPostPatternRequest(0);

    Pattern result = new PatternMapper().toPattern(request);

    Assertions.assertThat(result.getId()).isNull();
    Assertions.assertThat(result.getName()).isEqualTo(request.name());
    Assertions.assertThat(result.getSource()).isEqualTo(request.source());
    Assertions.assertThat(result.getCraftType()).isEqualTo(request.craftType());
    Assertions.assertThat(result.getNotes()).isEqualTo(request.notes());
    Assertions.assertThat(result.getCreatedAt()).isNull();
    Assertions.assertThat(result.getUpdatedAt()).isNull();
//    TODO: Assertions.assertThat(result.getUser()).isNull();
    Assertions.assertThat(result.getFileIds()).isEmpty();
  }

  private void assertGetAllPatternsItemMatchesPattern(Pattern expected, GetAllPatternsItem actual) {
    Assertions.assertThat(actual.id()).isEqualTo(expected.getId());
    Assertions.assertThat(actual.name()).isEqualTo(expected.getName());
    Assertions.assertThat(actual.craftType()).isEqualTo(expected.getCraftType());
    Assertions.assertThat(actual.createdAt()).isEqualTo(expected.getCreatedAt());
    Assertions.assertThat(actual.updatedAt()).isEqualTo(expected.getUpdatedAt());
    Assertions.assertThat(actual.fileIds())
              .containsExactlyInAnyOrderElementsOf(expected.getFileIds());
  }
}
