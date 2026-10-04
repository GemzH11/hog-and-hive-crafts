package uk.co.hogandhivecrafts.backend.integration.pattern;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import uk.co.hogandhivecrafts.backend.dto.pattern.GetAllPatternsItem;
import uk.co.hogandhivecrafts.backend.dto.pattern.GetAllPatternsResponse;
import uk.co.hogandhivecrafts.backend.entity.File;
import uk.co.hogandhivecrafts.backend.entity.Pattern;
import uk.co.hogandhivecrafts.backend.entity.User;
import uk.co.hogandhivecrafts.backend.exception.CustomErrorResponse;
import uk.co.hogandhivecrafts.backend.exception.GlobalExceptionHandler;
import uk.co.hogandhivecrafts.backend.integration.AbstractIT;
import uk.co.hogandhivecrafts.backend.integration.support.ITAssertions;
import uk.co.hogandhivecrafts.backend.integration.support.ITTestData;
import uk.co.hogandhivecrafts.backend.repository.FileRepository;
import uk.co.hogandhivecrafts.backend.repository.PatternRepository;
import uk.co.hogandhivecrafts.backend.repository.UserRepository;

/**
 * Integration tests for listing patterns with pagination and validation.
 */
class GetAllPatternsIT extends AbstractIT {
  private static final String BASE_URL = "/api/patterns";

  @LocalServerPort
  protected int port;

  @Autowired
  private PatternRepository patternRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private FileRepository fileRepository;

  @BeforeEach
  void setUp() {
    RestAssured.port = port;
    patternRepository.deleteAll();
    userRepository.deleteAll();
    fileRepository.deleteAll();
  }

  /**
   * Verifies that the API returns an empty page when no patterns exist.
   */
  @Test
  void getAllPatterns_noPatterns_returns200AndEmptyList() {
    RestAssured.given()
               .when()
               .get(BASE_URL)
               .then()
               .statusCode(200)
               .contentType(ContentType.JSON)
               .body("patterns", Matchers.empty());
  }

  /**
   * Verifies that existing patterns are returned with their file IDs in the paged response.
   */
  @Test
  void getAllPatterns_patternsExist_returns200AndPagedResponse() {
    User user = ITTestData.buildUser(0);
    Pattern pattern0 = ITTestData.buildPattern(0, user);
    Pattern pattern1 = ITTestData.buildPattern(1, user);
    File file = ITTestData.buildFile(0, pattern0);

    userRepository.save(user);
    pattern0 = patternRepository.save(pattern0);
    pattern1 = patternRepository.save(pattern1);
    file = fileRepository.save(file);

    GetAllPatternsResponse response = RestAssured.given()
                                                 .when()
                                                 .get(BASE_URL)
                                                 .then()
                                                 .statusCode(200)
                                                 .contentType(ContentType.JSON)
                                                 .extract()
                                                 .as(GetAllPatternsResponse.class);

    // Re-load entities from the database to ensure we have the correct state for comparison
    pattern0 = patternRepository.findById(pattern0.getId()).orElseThrow();
    pattern1 = patternRepository.findById(pattern1.getId()).orElseThrow();

    HashMap<UUID, List<UUID>> expectedFileIds = new HashMap<>();
    expectedFileIds.put(pattern0.getId(), List.of(file.getId()));
    expectedFileIds.put(pattern1.getId(), List.of());

    ITAssertions.assertGetAllPatternsResponseMatchesPatternList(List.of(pattern0, pattern1),
                                                                expectedFileIds, response);
  }

  /**
   * Verifies that custom pagination and sort options are applied and returned correctly.
   */
  @Test
  void getAllPatterns_paginationApplied_returnsRequestedPageSorted() {
    User user = ITTestData.buildUser(0);
    List<Pattern> patterns = ITTestData.buildPatternList(25, user);

    userRepository.save(user);
    patterns.forEach(patternRepository::save);

    // Capture the response so we can assert correct ordering
    GetAllPatternsResponse response = RestAssured.given()
                                                 .queryParam("page", 2)
                                                 .queryParam("size", 10)
                                                 .queryParam("sortField", "NAME")
                                                 .queryParam("sortDirection", "DESC")
                                                 .when()
                                                 .get(BASE_URL)
                                                 .then()
                                                 .statusCode(200)
                                                 .contentType(ContentType.JSON)
                                                 .extract()
                                                 .as(GetAllPatternsResponse.class);

    // Assert pagination properties
    Assertions.assertThat(response.totalElements()).isEqualTo(25);
    Assertions.assertThat(response.totalPages()).isEqualTo(3);
    Assertions.assertThat(response.page()).isEqualTo(2);
    Assertions.assertThat(response.size()).isEqualTo(10);
    Assertions.assertThat(response.patterns()).hasSize(5);

    // Assert pagination ordering
    List<String> actual = response.patterns().stream().map(GetAllPatternsItem::name).toList();
    List<String> expected = actual.stream().sorted(Comparator.reverseOrder()).toList();
    Assertions.assertThat(actual).isEqualTo(expected);
  }

  /**
   * Verifies that default pagination settings are used when no request parameters are provided.
   */
  @Test
  void getAllPatterns_noRequestParams_usesDefaults() {
    User user = ITTestData.buildUser(0);
    List<Pattern> patterns = ITTestData.buildPatternList(25, user);

    userRepository.save(user);
    patterns.forEach(patternRepository::save);

    GetAllPatternsResponse response = RestAssured.given()
                                                 .when()
                                                 .get(BASE_URL)
                                                 .then()
                                                 .statusCode(200)
                                                 .contentType(ContentType.JSON)
                                                 .extract()
                                                 .as(GetAllPatternsResponse.class);

    // Assert pagination properties
    Assertions.assertThat(response.totalElements()).isEqualTo(25);
    Assertions.assertThat(response.totalPages()).isEqualTo(2);
    Assertions.assertThat(response.page()).isZero();
    Assertions.assertThat(response.size()).isEqualTo(20);
    Assertions.assertThat(response.patterns()).hasSize(20);

    // Assert pagination ordering
    List<OffsetDateTime> actual = response.patterns()
                                          .stream()
                                          .map(GetAllPatternsItem::createdAt)
                                          .toList();
    List<OffsetDateTime> expected = actual.stream().sorted().toList();
    Assertions.assertThat(actual).isEqualTo(expected);
  }

  /**
   * Verifies that a negative page index is rejected with a 400 response.
   */
  @Test
  void getAllPatterns_negativePage_returns400() {
    CustomErrorResponse response = RestAssured.given()
                                              .queryParam("page", -1)
                                              .when()
                                              .get(BASE_URL)
                                              .then()
                                              .statusCode(400)
                                              .contentType(ContentType.JSON)
                                              .extract()
                                              .as(CustomErrorResponse.class);

    List<String> expectedErrors = List.of("Page must be greater than or equal to 0");

    Assertions.assertThat(response.path()).isEqualTo(BASE_URL);
    Assertions.assertThat(response.message()).isEqualTo(GlobalExceptionHandler.INVALID_REQUEST);
    Assertions.assertThat(response.errors()).containsExactlyElementsOf(expectedErrors);
  }

  /**
   * Verifies that a non-positive page size is rejected with a 400 response.
   */
  @Test
  void getAllPatterns_negativeSize_returns400() {
    CustomErrorResponse response = RestAssured.given()
                                              .queryParam("size", 0)
                                              .when()
                                              .get(BASE_URL)
                                              .then()
                                              .statusCode(400)
                                              .contentType(ContentType.JSON)
                                              .extract()
                                              .as(CustomErrorResponse.class);

    List<String> expectedErrors = List.of("Size must be greater than or equal to 1");

    Assertions.assertThat(response.path()).isEqualTo(BASE_URL);
    Assertions.assertThat(response.message()).isEqualTo(GlobalExceptionHandler.INVALID_REQUEST);
    Assertions.assertThat(response.errors()).containsExactlyElementsOf(expectedErrors);
  }

  /**
   * Verifies that a page size above the configured maximum is rejected with a 400 response.
   */
  @Test
  void getAllPatterns_largeSize_returns400() {
    CustomErrorResponse response = RestAssured.given()
                                              .queryParam("size", 101)
                                              .when()
                                              .get(BASE_URL)
                                              .then()
                                              .statusCode(400)
                                              .contentType(ContentType.JSON)
                                              .extract()
                                              .as(CustomErrorResponse.class);

    List<String> expectedErrors = List.of("Size must be less than or equal to 100");

    Assertions.assertThat(response.path()).isEqualTo(BASE_URL);
    Assertions.assertThat(response.message()).isEqualTo(GlobalExceptionHandler.INVALID_REQUEST);
    Assertions.assertThat(response.errors()).containsExactlyElementsOf(expectedErrors);
  }

  /**
   * Verifies that an invalid sort direction is rejected with a 400 response.
   */
  @Test
  void getAllPatterns_invalidSortDirection_returns400() {
    CustomErrorResponse response = RestAssured.given()
                                              .queryParam("sortDirection", "INVALID")
                                              .when()
                                              .get(BASE_URL)
                                              .then()
                                              .statusCode(400)
                                              .contentType(ContentType.JSON)
                                              .extract()
                                              .as(CustomErrorResponse.class);

    List<String> expectedErrors = List.of("Invalid value for 'sortDirection': INVALID");

    Assertions.assertThat(response.path()).isEqualTo(BASE_URL);
    Assertions.assertThat(response.message()).isEqualTo(GlobalExceptionHandler.INVALID_REQUEST);
    Assertions.assertThat(response.errors()).containsExactlyElementsOf(expectedErrors);
  }

  /**
   * Verifies that an invalid sort field is rejected with a 400 response.
   */
  @Test
  void getAllPatterns_invalidSortField_returns400() {
    CustomErrorResponse response = RestAssured.given()
                                              .queryParam("sortField", "INVALID")
                                              .when()
                                              .get(BASE_URL)
                                              .then()
                                              .statusCode(400)
                                              .contentType(ContentType.JSON)
                                              .extract()
                                              .as(CustomErrorResponse.class);

    List<String> expectedErrors = List.of("Invalid value for 'sortField': INVALID");

    Assertions.assertThat(response.path()).isEqualTo(BASE_URL);
    Assertions.assertThat(response.message()).isEqualTo(GlobalExceptionHandler.INVALID_REQUEST);
    Assertions.assertThat(response.errors()).containsExactlyElementsOf(expectedErrors);
  }
}
