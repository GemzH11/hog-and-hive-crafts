package uk.co.hogandhivecrafts.backend.integration.pattern;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import uk.co.hogandhivecrafts.backend.dto.pattern.GetPatternByIdResponse;
import uk.co.hogandhivecrafts.backend.entity.File;
import uk.co.hogandhivecrafts.backend.entity.Pattern;
import uk.co.hogandhivecrafts.backend.entity.User;
import uk.co.hogandhivecrafts.backend.exception.CustomErrorResponse;
import uk.co.hogandhivecrafts.backend.exception.GlobalExceptionHandler;
import uk.co.hogandhivecrafts.backend.exception.PatternNotFoundException;
import uk.co.hogandhivecrafts.backend.integration.AbstractIT;
import uk.co.hogandhivecrafts.backend.integration.support.ITAssertions;
import uk.co.hogandhivecrafts.backend.integration.support.ITTestData;
import uk.co.hogandhivecrafts.backend.repository.FileRepository;
import uk.co.hogandhivecrafts.backend.repository.PatternRepository;
import uk.co.hogandhivecrafts.backend.repository.UserRepository;
import uk.co.hogandhivecrafts.backend.support.testdata.TestDataConstants;

/**
 * Integration tests for retrieving a single pattern by ID.
 */
class GetPatternByIdIT extends AbstractIT {
  private static final String BASE_URL_TEMPLATE = "/api/patterns/%s";
  private static final String BASE_URL_VALID = String.format(BASE_URL_TEMPLATE,
                                                             TestDataConstants.DEFAULT_ID);
  private static final String BASE_URL_INVALID = String.format(BASE_URL_TEMPLATE, "INVALID");

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
   * Verifies that an existing pattern is returned with its full details, including file IDs.
   */
  @Test
  void getPatternById_patternExists_returns200AndPattern() {
    User user = ITTestData.buildUser(0);
    Pattern pattern = ITTestData.buildPattern(0, user);
    File file = ITTestData.buildFile(0, pattern);

    userRepository.save(user);
    pattern = patternRepository.save(pattern);
    file = fileRepository.save(file);

    GetPatternByIdResponse response = RestAssured.given()
                                                 .when()
                                                 .get(String.format(BASE_URL_TEMPLATE,
                                                                    pattern.getId()))
                                                 .then()
                                                 .statusCode(200)
                                                 .contentType(ContentType.JSON)
                                                 .extract()
                                                 .as(GetPatternByIdResponse.class);

    // Re-load entities from the database to ensure we have the correct state for comparison
    pattern = patternRepository.findById(pattern.getId()).orElseThrow();

    ITAssertions.assertGetPatternByIdResponseMatchesPattern(pattern, List.of(file.getId()),
                                                            response);
  }

  /**
   * Verifies that requesting a missing pattern ID returns a 404 response.
   */
  @Test
  void getPatternById_patternNotFound_returns404() {
    CustomErrorResponse response = RestAssured.given()
                                              .when()
                                              .get(BASE_URL_VALID)
                                              .then()
                                              .statusCode(404)
                                              .contentType(ContentType.JSON)
                                              .extract()
                                              .as(CustomErrorResponse.class);

    List<String> expectedErrors = List.of(
        String.format(PatternNotFoundException.PATTERN_NOT_FOUND, TestDataConstants.DEFAULT_ID));

    Assertions.assertThat(response.path()).isEqualTo(BASE_URL_VALID);
    Assertions.assertThat(response.message()).isEqualTo(GlobalExceptionHandler.RESOURCE_NOT_FOUND);
    Assertions.assertThat(response.errors()).containsExactlyElementsOf(expectedErrors);
  }

  /**
   * Verifies that an invalid UUID path parameter is rejected with a 400 response.
   */
  @Test
  void getPatternById_InvalidId_returns400() {
    CustomErrorResponse response = RestAssured.given()
                                              .when()
                                              .get(BASE_URL_INVALID)
                                              .then()
                                              .statusCode(400)
                                              .contentType(ContentType.JSON)
                                              .extract()
                                              .as(CustomErrorResponse.class);

    List<String> expectedErrors = List.of(
        String.format(GlobalExceptionHandler.TYPE_MISMATCH, "id", "INVALID"));

    Assertions.assertThat(response.path()).isEqualTo(BASE_URL_INVALID);
    Assertions.assertThat(response.message()).isEqualTo(GlobalExceptionHandler.INVALID_REQUEST);
    Assertions.assertThat(response.errors()).containsExactlyElementsOf(expectedErrors);
  }
}
