package uk.co.hogandhivecrafts.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uk.co.hogandhivecrafts.backend.configuration.PaginationProperties;
import uk.co.hogandhivecrafts.backend.dto.GetAllPatternsRequest;
import uk.co.hogandhivecrafts.backend.dto.GetAllPatternsResponse;
import uk.co.hogandhivecrafts.backend.dto.PatternSortField;
import uk.co.hogandhivecrafts.backend.entity.Pattern;
import uk.co.hogandhivecrafts.backend.mapper.PatternMapper;
import uk.co.hogandhivecrafts.backend.repository.PatternRepository;

/**
 * Service layer is where all the business logic lies
 */
@Service
@RequiredArgsConstructor
@Slf4j // Automates creation of logger within class to facilitate message logging

public class PatternService {
    private final PatternRepository patternRepository;

    private final PatternMapper patternMapper;

    private final PaginationProperties paginationProperties;

    /**
     * Fetches all patterns using optional pagination parameters provided in the request
     *
     * @param request the GetAllPatternsRequest containing pagination parameters (page number and page size)
     * @return a GetAllPatternsResponse containing a paginated list of patterns and pagination metadata
     */
    public GetAllPatternsResponse getAllPatterns(GetAllPatternsRequest request) {

        Pageable pageable = toPageable(request);
        Page<Pattern> patterns = patternRepository.findAll(pageable);

        return patternMapper.toGetAllPatternsResponse(patterns);
    }

    /**
     * Converts the GetAllPatternsRequest into a Pageable object, applying default pagination values if not provided
     *
     * @param request the GetAllPatternsRequest containing pagination parameters (page number and page size)
     * @return a Pageable object constructed from the request parameters, with defaults applied as necessary
     */
    private Pageable toPageable(GetAllPatternsRequest request) {
        int page = request.page() == null ? 0 : request.page();
        int size = request.size() == null ? paginationProperties.defaultPageSize() : request.size();
        Sort.Direction direction = request.sortDirection() == null ? paginationProperties.defaultSortDirection() : request.sortDirection();
        PatternSortField sortField = request.sortField() == null ? paginationProperties.defaultPatternSortField() : request.sortField();
        Sort sort;

        if (sortField == PatternSortField.ID) {
            sort = Sort.by(direction, sortField.getValue());
        } else {
            sort = Sort.by(direction, sortField.getValue()).and(Sort.by(direction, "id"));
        }

        return PageRequest.of(page, size, sort);
    }
}
