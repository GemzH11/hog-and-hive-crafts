package uk.co.hogandhivecrafts.backend.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uk.co.hogandhivecrafts.backend.dto.GetAllPatternsRequest;
import uk.co.hogandhivecrafts.backend.dto.GetAllPatternsResponse;
import uk.co.hogandhivecrafts.backend.dto.GetPatternByIdResponse;
import uk.co.hogandhivecrafts.backend.dto.PostPatternRequest;
import uk.co.hogandhivecrafts.backend.dto.PostPatternResponse;
import uk.co.hogandhivecrafts.backend.service.PatternService;

/**
 * Handles pattern-related HTTP requests and returns API responses.
 */
@RestController
@RequestMapping("/patterns")
@RequiredArgsConstructor
@Validated
public class PatternController {

  private final PatternService patternService;

  /**
   * Fetches all patterns using optional pagination parameters provided in the request, and returns
   * a paginated list of patterns in the response.
   *
   * @return list of paginated patterns
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<GetAllPatternsResponse> getAllPatterns(
      @Valid @ModelAttribute GetAllPatternsRequest request) {
    // @ModelAttribute tells Spring to bind the query parameters from the GET request
    // into the GetAllPatternsRequest record.
    return ResponseEntity.ok().body(patternService.getAllPatterns(request));
  }

  /**
   * Creates a new pattern and returns its generated ID in the response.
   *
   * @param request the user-specified details of the pattern to be created
   * @return ID of the created pattern
   */
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PostPatternResponse> savePattern(
      @Valid @RequestBody PostPatternRequest request) {
    PostPatternResponse response = patternService.savePattern(request);
    String resourcePath = ServletUriComponentsBuilder.fromCurrentRequestUri()
                                                     .path("/{id}")
                                                     .buildAndExpand(response.id())
                                                     .toUri()
                                                     .getPath();

    return ResponseEntity.created(URI.create(resourcePath)).body(response);
  }

  /**
   * Fetches a pattern by its ID and returns the pattern details in the response.
   *
   * @param id the ID of the pattern to fetch
   * @return the GetPatternByIdResponse containing the pattern details
   */
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<GetPatternByIdResponse> getPatternById(@PathVariable("id") UUID id) {
    return ResponseEntity.ok().body(patternService.getPatternById(id));
  }

  /**
   * Deletes a pattern by its ID, including any associated files.
   *
   * @param id the ID of the pattern to delete
   */
  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> deletePatternById(@PathVariable("id") UUID id) {
    patternService.deletePatternById(id);
    return ResponseEntity.noContent().build();
  }
}
