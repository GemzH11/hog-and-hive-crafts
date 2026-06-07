package uk.co.hogandhivecrafts.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.hogandhivecrafts.backend.dto.GetAllPatternsRequest;
import uk.co.hogandhivecrafts.backend.dto.GetAllPatternsResponse;
import uk.co.hogandhivecrafts.backend.service.PatternService;

/**
 * Controller class is where all the user requests are handled and required/appropriate responses are sent
 */
@RestController
@RequestMapping("/patterns")
@RequiredArgsConstructor
@Validated
public class PatternController {

    private final PatternService patternService;

    /**
     * Fetches all patterns
     *
     * @return List of patterns
     */
    @GetMapping("/v1")
    public ResponseEntity<GetAllPatternsResponse> getAllPatterns(@Valid GetAllPatternsRequest request) {
        return ResponseEntity.ok().body(patternService.getAllPatterns(request));
    }
}
