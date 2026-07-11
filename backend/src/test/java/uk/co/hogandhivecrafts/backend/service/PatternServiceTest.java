package uk.co.hogandhivecrafts.backend.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import uk.co.hogandhivecrafts.backend.configuration.PaginationProperties;
import uk.co.hogandhivecrafts.backend.dto.GetAllPatternsRequest;
import uk.co.hogandhivecrafts.backend.dto.GetAllPatternsResponse;
import uk.co.hogandhivecrafts.backend.dto.GetPatternByIdResponse;
import uk.co.hogandhivecrafts.backend.entity.Pattern;
import uk.co.hogandhivecrafts.backend.exception.PatternNotFoundException;
import uk.co.hogandhivecrafts.backend.mapper.PatternMapper;
import uk.co.hogandhivecrafts.backend.model.PatternSortField;
import uk.co.hogandhivecrafts.backend.repository.PatternRepository;
import uk.co.hogandhivecrafts.backend.support.assertions.PatternsDtoAssertions;
import uk.co.hogandhivecrafts.backend.support.testdata.EntityTestData;
import uk.co.hogandhivecrafts.backend.support.testdata.PatternsDtoTestData;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class PatternServiceTest {
    private static final int PAGE_DEFAULT = 20;
    private static final int PAGE = 1;
    private static final int SIZE = 10;
    private static final PatternSortField PATTERN_SORT_FIELD_DEFAULT = PatternSortField.CREATED_AT;
    private static final PatternSortField PATTERN_SORT_FIELD = PatternSortField.NAME;
    private static final Sort.Direction DIRECTION_DEFAULT = Sort.Direction.ASC;
    private static final Sort.Direction DIRECTION = Sort.Direction.DESC;
    private static final UUID DEFAULT_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    @Mock
    private PatternRepository patternRepository;

    @Mock
    private PatternMapper patternMapper;

    @Mock
    private PaginationProperties paginationProperties;

    @InjectMocks
    private PatternService patternService;

    @Test
    void getAllPatterns_usesProvidedPagination() {
        GetAllPatternsRequest request = PatternsDtoTestData.buildDefaultGetAllPatternsRequest();
        Page<Pattern> page = Page.empty();
        GetAllPatternsResponse response = PatternsDtoTestData.buildDefaultGetAllPatternsResponse();
        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);

        BDDMockito.given(patternRepository.findAll(ArgumentMatchers.any(Pageable.class))).willReturn(page);
        BDDMockito.given(patternMapper.toGetAllPatternsResponse(page)).willReturn(response);

        patternService.getAllPatterns(request);

        Mockito.verify(patternRepository).findAll(captor.capture());
        Pageable pageable = captor.getValue();

        Assertions.assertThat(pageable.getPageNumber()).isEqualTo(PAGE);
        Assertions.assertThat(pageable.getPageSize()).isEqualTo(SIZE);

        Sort sort = pageable.getSort();
        Assertions.assertThat(sort).isNotNull();
        Sort.Order order = sort.getOrderFor(PATTERN_SORT_FIELD.getValue());
        Assertions.assertThat(order).isNotNull();
        Assertions.assertThat(order.getDirection()).isEqualTo(DIRECTION);
    }

    @Test
    void getAllPatterns_usesDefaultPaginationWhenNull() {
        GetAllPatternsRequest request = PatternsDtoTestData.buildEmptyGetAllPatternsRequest();
        Page<Pattern> page = Page.empty();
        GetAllPatternsResponse response = PatternsDtoTestData.buildDefaultGetAllPatternsResponse();
        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);

        BDDMockito.given(patternRepository.findAll(ArgumentMatchers.any(Pageable.class))).willReturn(page);
        BDDMockito.given(paginationProperties.defaultPageSize()).willReturn(PAGE_DEFAULT);
        BDDMockito.given(paginationProperties.defaultPatternSortField()).willReturn(PATTERN_SORT_FIELD_DEFAULT);
        BDDMockito.given(paginationProperties.defaultSortDirection()).willReturn(DIRECTION_DEFAULT);
        BDDMockito.given(patternMapper.toGetAllPatternsResponse(page)).willReturn(response);

        patternService.getAllPatterns(request);

        Mockito.verify(patternRepository).findAll(captor.capture());
        Pageable pageable = captor.getValue();

        Assertions.assertThat(pageable.getPageNumber()).isZero();
        Assertions.assertThat(pageable.getPageSize()).isEqualTo(PAGE_DEFAULT);

        Sort sort = pageable.getSort();
        Assertions.assertThat(sort).isNotNull();
        Sort.Order order = sort.getOrderFor(PATTERN_SORT_FIELD_DEFAULT.getValue());
        Assertions.assertThat(order).isNotNull();
        Assertions.assertThat(order.getDirection()).isEqualTo(DIRECTION_DEFAULT);
    }

    @Test
    void getAllPatterns_callsRepositoryAndMapper() {
        GetAllPatternsRequest request = PatternsDtoTestData.buildDefaultGetAllPatternsRequest();
        Page<Pattern> page = Page.empty();
        GetAllPatternsResponse expected = PatternsDtoTestData.buildDefaultGetAllPatternsResponse();

        BDDMockito.given(patternRepository.findAll(ArgumentMatchers.any(Pageable.class))).willReturn(page);
        BDDMockito.given(patternMapper.toGetAllPatternsResponse(page)).willReturn(expected);

        GetAllPatternsResponse actual = patternService.getAllPatterns(request);

        Mockito.verify(patternRepository).findAll(ArgumentMatchers.any(Pageable.class));
        Mockito.verify(patternMapper).toGetAllPatternsResponse(page);
        PatternsDtoAssertions.assertGetAllPatternsResponseEquals(actual, expected);
    }

    @Test
    void getPatternById_callsRepositoryAndMapper() {
        GetPatternByIdResponse expected = PatternsDtoTestData.buildDefaultGetPatternByIdResponse();
        Optional<Pattern> optional = Optional.of(EntityTestData.buildDefaultPattern());

        BDDMockito.given(patternRepository.findById(ArgumentMatchers.any(UUID.class))).willReturn(optional);
        BDDMockito.given(patternMapper.toGetPatternByIdResponse(optional.get())).willReturn(expected);

        GetPatternByIdResponse actual = patternService.getPatternById(DEFAULT_ID);

        Mockito.verify(patternRepository).findById(ArgumentMatchers.any(UUID.class));
        Mockito.verify(patternMapper).toGetPatternByIdResponse(optional.get());
        PatternsDtoAssertions.assertGetPatternByIdResponseEquals(actual, expected);
    }

    @Test
    void getPatternById_notFound_throwsPatternNotFoundException() {
        BDDMockito.given(patternRepository.findById(ArgumentMatchers.any(UUID.class))).willReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(PatternNotFoundException.class)
                .isThrownBy(() -> patternService.getPatternById(DEFAULT_ID)).withMessage(String.format("Pattern not " +
                        "found with ID: %s", DEFAULT_ID));
    }

    @Test
    void deletePatternById_callsRepository() {
        BDDMockito.given(patternRepository.existsById(ArgumentMatchers.any(UUID.class))).willReturn(true);
        BDDMockito.doNothing().when(patternRepository).deleteById(ArgumentMatchers.any(UUID.class));

        patternService.deletePatternById(DEFAULT_ID);

        Mockito.verify(patternRepository).existsById(ArgumentMatchers.any(UUID.class));
        Mockito.verify(patternRepository).deleteById(ArgumentMatchers.any(UUID.class));
    }

    @Test
    void deletePatternById_notFound_throwsPatternNotFoundException() {
        BDDMockito.given(patternRepository.existsById(ArgumentMatchers.any(UUID.class))).willReturn(false);

        Assertions.assertThatExceptionOfType(PatternNotFoundException.class)
                .isThrownBy(() -> patternService.deletePatternById(DEFAULT_ID))
                .withMessage(String.format("Pattern not found with ID: %s", DEFAULT_ID));
    }
}
