package uk.co.hogandhivecrafts.backend.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.co.hogandhivecrafts.backend.configuration.PaginationProperties;
import uk.co.hogandhivecrafts.backend.dto.GetAllPatternsRequest;
import uk.co.hogandhivecrafts.backend.dto.GetAllPatternsResponse;
import uk.co.hogandhivecrafts.backend.entity.Pattern;
import uk.co.hogandhivecrafts.backend.mapper.PatternMapper;
import uk.co.hogandhivecrafts.backend.repository.PatternRepository;
import uk.co.hogandhivecrafts.backend.support.assertions.PatternsDtoAssertions;
import uk.co.hogandhivecrafts.backend.support.testdata.PatternsDtoTestData;

@ExtendWith(MockitoExtension.class)
public class PatternServiceTest {
    private static final int PAGE_DEFAULT = 20;

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
        int page_param = 1;
        int size_param = 10;

        GetAllPatternsRequest request = new GetAllPatternsRequest(page_param, size_param);
        Page<Pattern> page = Page.empty();
        GetAllPatternsResponse response = PatternsDtoTestData.buildDefaultGetAllPatternsResponse();
        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);

        BDDMockito.given(patternRepository.findAll(ArgumentMatchers.any(Pageable.class))).willReturn(page);
        BDDMockito.given(patternMapper.toGetAllPatternsResponse(page)).willReturn(response);

        patternService.getAllPatterns(request);

        Mockito.verify(patternRepository).findAll(captor.capture());
        Pageable pageable = captor.getValue();

        Assertions.assertThat(pageable.getPageNumber()).isEqualTo(page_param);
        Assertions.assertThat(pageable.getPageSize()).isEqualTo(size_param);
    }

    @Test
    void getAllPatterns_usesDefaultPaginationWhenNull() {
        GetAllPatternsRequest request = new GetAllPatternsRequest(null, null);
        Page<Pattern> page = Page.empty();
        GetAllPatternsResponse response = PatternsDtoTestData.buildDefaultGetAllPatternsResponse();
        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);

        BDDMockito.given(patternRepository.findAll(ArgumentMatchers.any(Pageable.class))).willReturn(page);
        BDDMockito.given(paginationProperties.defaultPageSize()).willReturn(PAGE_DEFAULT);
        BDDMockito.given(patternMapper.toGetAllPatternsResponse(page)).willReturn(response);

        patternService.getAllPatterns(request);

        Mockito.verify(patternRepository).findAll(captor.capture());
        Pageable pageable = captor.getValue();

        Assertions.assertThat(pageable.getPageNumber()).isEqualTo(0);
        Assertions.assertThat(pageable.getPageSize()).isEqualTo(PAGE_DEFAULT);
    }

    @Test
    void getAllPatterns_callsRepositoryAndMapper() {
        GetAllPatternsRequest request = new GetAllPatternsRequest(null, null);
        Page<Pattern> page = Page.empty();
        GetAllPatternsResponse response = PatternsDtoTestData.buildDefaultGetAllPatternsResponse();

        BDDMockito.given(patternRepository.findAll(ArgumentMatchers.any(Pageable.class))).willReturn(page);
        BDDMockito.given(paginationProperties.defaultPageSize()).willReturn(PAGE_DEFAULT);
        BDDMockito.given(patternMapper.toGetAllPatternsResponse(page)).willReturn(response);

        GetAllPatternsResponse actual = patternService.getAllPatterns(request);

        Mockito.verify(patternRepository).findAll(ArgumentMatchers.any(Pageable.class));
        Mockito.verify(patternMapper).toGetAllPatternsResponse(page);
        PatternsDtoAssertions.assertGetAllPatternsResponseEquals(actual, response);
    }
}
