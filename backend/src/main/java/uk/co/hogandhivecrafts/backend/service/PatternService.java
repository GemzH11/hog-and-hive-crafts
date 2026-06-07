package uk.co.hogandhivecrafts.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.co.hogandhivecrafts.backend.configuration.PaginationProperties;
import uk.co.hogandhivecrafts.backend.dto.GetAllPatternsRequest;
import uk.co.hogandhivecrafts.backend.dto.GetAllPatternsResponse;
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
     * Fetches all patterns
     *
     * @return List of patterns
     */
    public GetAllPatternsResponse getAllPatterns(GetAllPatternsRequest request) {

        Pageable pageable = toPageable(request);
        Page<Pattern> patterns = patternRepository.findAll(pageable);

        return patternMapper.toGetAllPatternsResponse(patterns);
    }

    private Pageable toPageable(GetAllPatternsRequest request) {
        int page = request.page() == null ? 0 : request.page();
        int size = request.size() == null ? paginationProperties.defaultPageSize() : request.size();

        return PageRequest.of(page, size);
    }
}
