package uk.co.hogandhivecrafts.backend.integration.pattern;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.List;
import java.util.UUID;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import uk.co.hogandhivecrafts.backend.dto.GetPatternByIdResponse;
import uk.co.hogandhivecrafts.backend.entity.File;
import uk.co.hogandhivecrafts.backend.entity.Pattern;
import uk.co.hogandhivecrafts.backend.entity.User;
import uk.co.hogandhivecrafts.backend.integration.AbstractIT;
import uk.co.hogandhivecrafts.backend.integration.support.FileITData;
import uk.co.hogandhivecrafts.backend.integration.support.ITAssertions;
import uk.co.hogandhivecrafts.backend.integration.support.PatternITData;
import uk.co.hogandhivecrafts.backend.integration.support.UserITData;
import uk.co.hogandhivecrafts.backend.repository.FileRepository;
import uk.co.hogandhivecrafts.backend.repository.PatternRepository;
import uk.co.hogandhivecrafts.backend.repository.UserRepository;

/**
 * Integration tests for retrieving a single pattern by ID.
 */
class GetPatternByIdIT extends AbstractIT {
  private static final UUID DEFAULT_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

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
    User user = UserITData.buildMinimal(0);
    Pattern pattern = PatternITData.buildDefault(0, user);
    File file = FileITData.buildMinimal(0, pattern);

    userRepository.save(user);
    pattern = patternRepository.save(pattern);
    file = fileRepository.save(file);

    List<UUID> patternFileIds = List.of(file.getId());

    GetPatternByIdResponse response = RestAssured.given()
                                                 .when()
                                                 .get(String.format("/api/patterns/%s",
                                                                    pattern.getId()))
                                                 .then()
                                                 .statusCode(200)
                                                 .contentType(ContentType.JSON)
                                                 .extract()
                                                 .as(GetPatternByIdResponse.class);

    ITAssertions.assertPatternEquals(pattern, patternFileIds, response);
  }

  /**
   * Verifies that requesting a missing pattern ID returns a 404 response.
   */
  @Test
  void getPatternById_patternNotFound_returns404() {
    RestAssured.given()
               .when()
               .get(String.format("/api/patterns/%s", DEFAULT_ID))
               .then()
               .statusCode(404)
               .contentType(ContentType.JSON)
               .body("message",
                     Matchers.is(String.format("Pattern not found with ID: %s", DEFAULT_ID)));
  }

  /**
   * Verifies that an invalid UUID path parameter is rejected with a 400 response.
   */
  @Test
  void getPatternById_InvalidId_returns400() {
    RestAssured.given()
               .when()
               .get("/api/patterns/INVALID")
               .then()
               .statusCode(400)
               .contentType(ContentType.JSON)
               .body("message", Matchers.is("Invalid request"))
               .body("errors", Matchers.hasSize(1))
               .body("errors[0]",
                     Matchers.is("Invalid value for ID path parameter: INVALID (expected UUID)"));
  }
}
