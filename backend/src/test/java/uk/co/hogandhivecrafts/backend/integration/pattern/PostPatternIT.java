package uk.co.hogandhivecrafts.backend.integration.pattern;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import tools.jackson.databind.ObjectMapper;
import uk.co.hogandhivecrafts.backend.dto.pattern.PostPatternRequest;
import uk.co.hogandhivecrafts.backend.dto.pattern.PostPatternResponse;
import uk.co.hogandhivecrafts.backend.entity.Pattern;
import uk.co.hogandhivecrafts.backend.exception.CustomErrorResponse;
import uk.co.hogandhivecrafts.backend.exception.GlobalExceptionHandler;
import uk.co.hogandhivecrafts.backend.integration.AbstractIT;
import uk.co.hogandhivecrafts.backend.model.CraftType;
import uk.co.hogandhivecrafts.backend.repository.FileRepository;
import uk.co.hogandhivecrafts.backend.repository.PatternRepository;
import uk.co.hogandhivecrafts.backend.repository.UserRepository;

/**
 * Integration tests for creating a new pattern
 */
class PostPatternIT extends AbstractIT {
  private static final String BASE_URL = "/api/patterns";
  private static final String RESOURCE_URL_TEMPLATE = "/api/patterns/%s";

  @LocalServerPort
  protected int port;

  @Autowired
  private PatternRepository patternRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private FileRepository fileRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    RestAssured.port = port;
    patternRepository.deleteAll();
    userRepository.deleteAll();
    fileRepository.deleteAll();
  }

  /**
   * Verifies that a fully-populated POST request creates and persists a pattern.
   */
  @Test
  void savePattern_allProperties_returns201AndPersistsPattern() {
    PostPatternRequest request = new PostPatternRequest("pattern000", "source000", CraftType.OTHER,
                                                        "notes000");

    // Need to extract as full response so can assert on header
    Response response = RestAssured.given()
                                   .contentType(ContentType.JSON)
                                   .body(objectMapper.writeValueAsString(request))
                                   .when()
                                   .post(BASE_URL)
                                   .then()
                                   .statusCode(201)
                                   .contentType(ContentType.JSON)
                                   .extract()
                                   .response();

    PostPatternResponse actual = response.as(PostPatternResponse.class);
    Assertions.assertThat(response.header("Location"))
              .isEqualTo(String.format(RESOURCE_URL_TEMPLATE, actual.id()));

    Pattern expected = patternRepository.findById(actual.id()).orElseThrow();

    Assertions.assertThat(actual.id()).isEqualTo(expected.getId());
  }

  /**
   * Verifies that only the required POST fields are needed and optional fields remain null.
   */
  @Test
  void savePattern_onlyMandatory_returns201AndPersistsPattern() {
    PostPatternRequest request = new PostPatternRequest("pattern000", null, CraftType.OTHER, null);

    // Need to extract as full response so can assert on header
    Response response = RestAssured.given()
                                   .contentType(ContentType.JSON)
                                   .body(objectMapper.writeValueAsString(request))
                                   .when()
                                   .post(BASE_URL)
                                   .then()
                                   .statusCode(201)
                                   .contentType(ContentType.JSON)
                                   .extract()
                                   .response();

    PostPatternResponse actual = response.as(PostPatternResponse.class);
    Assertions.assertThat(response.header("Location"))
              .isEqualTo(String.format(RESOURCE_URL_TEMPLATE, actual.id()));

    Pattern expected = patternRepository.findById(actual.id()).orElseThrow();

    Assertions.assertThat(actual.id()).isEqualTo(expected.getId());
  }

  /**
   * Verifies that omitting the required name field returns the validation error payload.
   */
  @Test
  void savePattern_noName_returns400() {
    PostPatternRequest request = new PostPatternRequest(null, null, CraftType.OTHER, null);

    CustomErrorResponse response = RestAssured.given()
                                              .contentType(ContentType.JSON)
                                              .body(objectMapper.writeValueAsString(request))
                                              .when()
                                              .post(BASE_URL)
                                              .then()
                                              .statusCode(400)
                                              .contentType(ContentType.JSON)
                                              .extract()
                                              .as(CustomErrorResponse.class);

    List<String> expectedErrors = List.of("Name is required");

    Assertions.assertThat(response.path()).isEqualTo(BASE_URL);
    Assertions.assertThat(response.message()).isEqualTo(GlobalExceptionHandler.INVALID_REQUEST);
    Assertions.assertThat(response.errors()).containsExactlyElementsOf(expectedErrors);
  }

  /**
   * Verifies that omitting the required craftType field returns the validation error payload.
   */
  @Test
  void savePattern_noCraftType_returns400() {
    PostPatternRequest request = new PostPatternRequest("pattern000", null, null, null);

    CustomErrorResponse response = RestAssured.given()
                                              .contentType(ContentType.JSON)
                                              .body(objectMapper.writeValueAsString(request))
                                              .when()
                                              .post(BASE_URL)
                                              .then()
                                              .statusCode(400)
                                              .contentType(ContentType.JSON)
                                              .extract()
                                              .as(CustomErrorResponse.class);

    List<String> expectedErrors = List.of("Craft type is required");

    Assertions.assertThat(response.path()).isEqualTo(BASE_URL);
    Assertions.assertThat(response.message()).isEqualTo(GlobalExceptionHandler.INVALID_REQUEST);
    Assertions.assertThat(response.errors()).containsExactlyElementsOf(expectedErrors);
  }
}
