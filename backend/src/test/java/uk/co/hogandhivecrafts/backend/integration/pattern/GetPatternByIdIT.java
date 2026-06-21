package uk.co.hogandhivecrafts.backend.integration.pattern;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import uk.co.hogandhivecrafts.backend.entity.File;
import uk.co.hogandhivecrafts.backend.entity.Pattern;
import uk.co.hogandhivecrafts.backend.entity.User;
import uk.co.hogandhivecrafts.backend.integration.AbstractIT;
import uk.co.hogandhivecrafts.backend.integration.support.FileITData;
import uk.co.hogandhivecrafts.backend.integration.support.PatternITData;
import uk.co.hogandhivecrafts.backend.integration.support.UserITData;
import uk.co.hogandhivecrafts.backend.repository.FileRepository;
import uk.co.hogandhivecrafts.backend.repository.PatternRepository;
import uk.co.hogandhivecrafts.backend.repository.UserRepository;

import java.util.UUID;

import static io.restassured.RestAssured.given;

public class GetPatternByIdIT extends AbstractIT {
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

    @Test
    void getPatternById_patternExists_returns200AndPattern() {
        User user = UserITData.buildMinimal();
        Pattern pattern = PatternITData.buildDefault(0, user);
        File file = FileITData.buildMinimal(0, pattern);

        user = userRepository.save(user);
        pattern = patternRepository.save(pattern);
        file = fileRepository.save(file);

        given().when().get(String.format("/api/patterns/v1/%s", pattern.getId()))
                .then().statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", Matchers.is(pattern.getId().toString()))
                .body("name", Matchers.is(pattern.getName()))
                .body("source", Matchers.is(pattern.getSource()))
                .body("craftType", Matchers.is(pattern.getCraftType()))
                .body("notes", Matchers.is(pattern.getNotes()))
                .body("createdAt", Matchers.is(pattern.getCreatedAt().toString()))
                .body("updatedAt", Matchers.is(pattern.getUpdatedAt().toString()))
                .body("userId", Matchers.is(user.getId().toString()))
                .body("fileIds", Matchers.hasSize(1))
                .body("fileIds[0]", Matchers.is(file.getId().toString()));
    }

    @Test
    void getPatternById_patternNotFound_returns404() {
        given().when().get(String.format("/api/patterns/v1/%s", DEFAULT_ID))
                .then().statusCode(404)
                .contentType(ContentType.JSON)
                .body("message", Matchers.is(String.format("Pattern not found with ID: %s", DEFAULT_ID)));
    }

    @Test
    void getPatternById_InvalidId_returns400() {
        given().when().get("/api/patterns/v1/INVALID")
                .then().statusCode(400)
                .contentType(ContentType.JSON)
                .body("message", Matchers.is("Invalid request"))
                .body("errors", Matchers.hasSize(1))
                .body("errors[0]", Matchers.is("Invalid value for ID path parameter: INVALID (expected UUID)"));
    }
}
