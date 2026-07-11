package uk.co.hogandhivecrafts.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uk.co.hogandhivecrafts.backend.dto.GetAllPatternsRequest;
import uk.co.hogandhivecrafts.backend.dto.GetAllPatternsResponse;
import uk.co.hogandhivecrafts.backend.dto.GetPatternByIdResponse;
import uk.co.hogandhivecrafts.backend.service.PatternService;

import java.util.UUID;

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
     * Fetches all patterns using optional pagination parameters provided in the request,
     * and returns a paginated list of patterns in the response
     *
     * @return list of paginated patterns
     */
    @GetMapping("/v1")
    public ResponseEntity<GetAllPatternsResponse> getAllPatterns(@Valid GetAllPatternsRequest request) {
        return ResponseEntity.ok().body(patternService.getAllPatterns(request));
    }

    /**
     * Fetches a pattern by its ID and returns the pattern details in the response
     *
     * @param id the ID of the pattern to fetch
     * @return the GetPatternByIdResponse containing the pattern details
     */
    @GetMapping("/v1/{id}")
    public ResponseEntity<GetPatternByIdResponse> getPatternById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok().body(patternService.getPatternById(id));
    }

    /**
     * Deletes a pattern by its ID, including any associated files
     *
     * @param id the ID of the pattern to delete
     */
    @DeleteMapping("/v1/{id}")
    public ResponseEntity<Void> deletePatternById(@PathVariable("id") UUID id) {
        patternService.deletePatternById(id);
        return ResponseEntity.noContent().build();
    }
}
