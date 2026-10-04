package uk.co.hogandhivecrafts.backend.service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import uk.co.hogandhivecrafts.backend.configuration.PaginationProperties;
import uk.co.hogandhivecrafts.backend.dto.pattern.GetAllPatternsRequest;
import uk.co.hogandhivecrafts.backend.dto.pattern.GetAllPatternsResponse;
import uk.co.hogandhivecrafts.backend.dto.pattern.GetPatternByIdResponse;
import uk.co.hogandhivecrafts.backend.dto.pattern.PostPatternRequest;
import uk.co.hogandhivecrafts.backend.dto.pattern.PostPatternResponse;
import uk.co.hogandhivecrafts.backend.entity.Pattern;
import uk.co.hogandhivecrafts.backend.exception.PatternNotFoundException;
import uk.co.hogandhivecrafts.backend.mapper.PatternMapper;
import uk.co.hogandhivecrafts.backend.model.PatternSortField;
import uk.co.hogandhivecrafts.backend.repository.PatternRepository;

/**
 * Tests pattern service behavior and pagination defaults using mocked dependencies.
 */
@ExtendWith(MockitoExtension.class)
class PatternServiceTest {

  @Mock
  private PatternRepository patternRepository;

  @Mock
  private PatternMapper patternMapper;

  @Mock
  private PaginationProperties paginationProperties;

  @InjectMocks
  private PatternService patternService;

  @Test
  void getAllPatterns_callsRepositoryAndMapper() {
    GetAllPatternsRequest mockRequest = mock(GetAllPatternsRequest.class);
    Page<Pattern> mockPage = Page.empty();
    GetAllPatternsResponse mockResponse = mock(GetAllPatternsResponse.class);

    when(mockRequest.page()).thenReturn(1);
    when(mockRequest.size()).thenReturn(10);
    when(mockRequest.sortDirection()).thenReturn(Sort.Direction.ASC);
    when(mockRequest.sortField()).thenReturn(PatternSortField.NAME);

    when(patternRepository.findAll(Mockito.any(Pageable.class))).thenReturn(mockPage);
    when(patternMapper.toGetAllPatternsResponse(mockPage)).thenReturn(mockResponse);

    GetAllPatternsResponse actual = patternService.getAllPatterns(mockRequest);

    verify(patternRepository, times(1)).findAll(Mockito.any(Pageable.class));
    verify(patternMapper, times(1)).toGetAllPatternsResponse(mockPage);
    Assertions.assertThat(actual).isEqualTo(mockResponse);
  }

  @Test
  void getPatternById_callsRepositoryAndMapper() {
    UUID mockId = mock(UUID.class);
    Pattern mockPattern = mock(Pattern.class);
    GetPatternByIdResponse mockResponse = mock(GetPatternByIdResponse.class);

    when(patternRepository.findById(mockId)).thenReturn(Optional.of(mockPattern));
    when(patternMapper.toGetPatternByIdResponse(mockPattern)).thenReturn(mockResponse);

    GetPatternByIdResponse actual = patternService.getPatternById(mockId);

    verify(patternRepository, times(1)).findById(mockId);
    verify(patternMapper, times(1)).toGetPatternByIdResponse(mockPattern);
    Assertions.assertThat(actual).isEqualTo(mockResponse);
  }

  @Test
  void getPatternById_notFound_throwsPatternNotFoundException() {
    UUID mockId = mock(UUID.class);

    when(patternRepository.findById(mockId)).thenReturn(Optional.empty());

    Assertions.assertThatThrownBy(() -> patternService.getPatternById(mockId))
              .isInstanceOf(PatternNotFoundException.class)
              .hasMessageContaining(
                  String.format(PatternNotFoundException.PATTERN_NOT_FOUND, mockId));

    verify(patternRepository, times(1)).findById(mockId);
  }

  @Test
  void deletePatternById_callsRepository() {
    UUID mockId = mock(UUID.class);

    when(patternRepository.existsById(mockId)).thenReturn(true);
    doNothing().when(patternRepository).deleteById(mockId);

    patternService.deletePatternById(mockId);

    verify(patternRepository, times(1)).existsById(mockId);
    verify(patternRepository, times(1)).deleteById(mockId);
  }

  @Test
  void deletePatternById_notFound_throwsPatternNotFoundException() {
    UUID mockId = mock(UUID.class);

    when(patternRepository.existsById(mockId)).thenReturn(false);

    Assertions.assertThatThrownBy(() -> patternService.deletePatternById(mockId))
              .isInstanceOf(PatternNotFoundException.class)
              .hasMessageContaining(
                  String.format(PatternNotFoundException.PATTERN_NOT_FOUND, mockId));

    verify(patternRepository, times(1)).existsById(mockId);
  }

  @Test
  void deletePatternById_raceCondition_throwsPatternNotFoundException() {
    UUID mockId = mock(UUID.class);

    when(patternRepository.existsById(mockId)).thenReturn(true);
    doThrow(new EmptyResultDataAccessException(1)).when(patternRepository).deleteById(mockId);

    Assertions.assertThatThrownBy(() -> patternService.deletePatternById(mockId))
              .isInstanceOf(PatternNotFoundException.class)
              .hasMessageContaining(
                  String.format(PatternNotFoundException.PATTERN_NOT_FOUND, mockId));

    verify(patternRepository, times(1)).existsById(mockId);
    verify(patternRepository, times(1)).deleteById(mockId);
  }

  @Test
  void savePattern_callsRepositoryAndMapper() {
    PostPatternRequest mockRequest = mock(PostPatternRequest.class);
    Pattern mockPattern = mock(Pattern.class);
    UUID mockId = mock(UUID.class);

    when(patternMapper.toPattern(mockRequest)).thenReturn(mockPattern);
    when(patternRepository.save(mockPattern)).thenReturn(mockPattern);
    when(mockPattern.getId()).thenReturn(mockId);

    PostPatternResponse response = patternService.savePattern(mockRequest);

    verify(patternMapper, times(1)).toPattern(mockRequest);
    verify(patternRepository, times(1)).save(mockPattern);
    verify(mockPattern, times(1)).getId();
    Assertions.assertThat(response.id()).isEqualTo(mockId);
  }

  @Test
  void toPageable_withRequestParams_returnsCorrectPageable() {
    GetAllPatternsRequest mockRequest = mock(GetAllPatternsRequest.class);

    when(mockRequest.page()).thenReturn(1);
    when(mockRequest.size()).thenReturn(10);
    when(mockRequest.sortDirection()).thenReturn(Sort.Direction.ASC);
    when(mockRequest.sortField()).thenReturn(PatternSortField.NAME);

    Pageable pageable = patternService.toPageable(mockRequest);

    Sort expectedSort = Sort.by(Sort.Direction.ASC, "name").and(Sort.by(Sort.Direction.ASC, "id"));

    verifyNoInteractions(paginationProperties);
    Assertions.assertThat(pageable.getPageNumber()).isEqualTo(1);
    Assertions.assertThat(pageable.getPageSize()).isEqualTo(10);
    Assertions.assertThat(pageable.getSort()).isEqualTo(expectedSort);
  }

  @Test
  void toPageable_noRequestParams_returnsDefaultPageable() {
    GetAllPatternsRequest mockRequest = mock(GetAllPatternsRequest.class);

    when(mockRequest.page()).thenReturn(null);
    when(mockRequest.size()).thenReturn(null);
    when(mockRequest.sortDirection()).thenReturn(null);
    when(mockRequest.sortField()).thenReturn(null);

    when(paginationProperties.defaultPageSize()).thenReturn(20);
    when(paginationProperties.defaultSortDirection()).thenReturn(Sort.Direction.DESC);
    when(paginationProperties.defaultPatternSortField()).thenReturn(PatternSortField.CREATED_AT);

    Pageable pageable = patternService.toPageable(mockRequest);

    Sort expectedSort = Sort.by(Sort.Direction.DESC, "createdAt")
                            .and(Sort.by(Sort.Direction.DESC, "id"));

    verify(paginationProperties, times(1)).defaultPageSize();
    verify(paginationProperties, times(1)).defaultSortDirection();
    verify(paginationProperties, times(1)).defaultPatternSortField();

    Assertions.assertThat(pageable.getPageNumber()).isZero();
    Assertions.assertThat(pageable.getPageSize()).isEqualTo(20);
    Assertions.assertThat(pageable.getSort()).isEqualTo(expectedSort);
  }
}
