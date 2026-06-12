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

import java.util.List;

import static io.restassured.RestAssured.given;

public class GetAllPatternsIT extends AbstractIT {
    private static final String DATE_TIME_REGEX = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d+Z$";
    private static final String UUID_REGEX = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    private static final String PATTERN_NAME = "pattern%s";
    private static final String PATTERN_SOURCE = "source%s";
    private static final String PATTERN_CRAFT_TYPE = "craft%s";
    private static final String PATTERN_NOTES = "notes%s";

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
    }

    @Test
    void getAllPatterns_noPatterns_returns200AndEmptyList() {
        given().when().get("/api/patterns/v1")
                .then().statusCode(200)
                .contentType(ContentType.JSON)
                .body("patterns", Matchers.empty());
    }

    @Test
    void getAllPatterns_patternsExist_returns200AndPagedResponse() {
        User user = UserITData.buildMinimal();
        Pattern pattern0 = PatternITData.buildDefault(0, user);
        Pattern pattern1 = PatternITData.buildDefault(1, user);
        File file1 = FileITData.buildMinimal(0, pattern0);
        userRepository.save(user);
        patternRepository.save(pattern0);
        patternRepository.save(pattern1);
        fileRepository.save(file1);

        given().when().get("/api/patterns/v1")
                .then().statusCode(200)
                .contentType(ContentType.JSON)
                .body("patterns", Matchers.hasSize(2))
                .body("patterns.id", Matchers.everyItem(Matchers.matchesPattern(UUID_REGEX)))
                .body("patterns.createdAt", Matchers.everyItem(Matchers.matchesPattern(DATE_TIME_REGEX)))
                .body("patterns.updatedAt", Matchers.everyItem(Matchers.matchesPattern(DATE_TIME_REGEX)))
                .body("patterns.userId", Matchers.everyItem(Matchers.matchesPattern(UUID_REGEX)))
                .body("patterns[0].name", Matchers.is(String.format(PATTERN_NAME, 0)))
                .body("patterns[0].source", Matchers.is(String.format(PATTERN_SOURCE, 0)))
                .body("patterns[0].craftType", Matchers.is(String.format(PATTERN_CRAFT_TYPE, 0)))
                .body("patterns[0].notes", Matchers.is(String.format(PATTERN_NOTES, 0)))
                .body("patterns[0].fileIds", Matchers.hasSize(1))
                .body("patterns[0].fileIds[0]", Matchers.matchesPattern(UUID_REGEX))
                .body("patterns[1].name", Matchers.is(String.format(PATTERN_NAME, 1)))
                .body("patterns[1].fileIds", Matchers.hasSize(0));
    }

    @Test
    void getAllPatterns_paginationApplied_returnsRequestedPage() {
        User user = UserITData.buildMinimal();
        List<Pattern> patterns = PatternITData.buildList(25, user);
        userRepository.save(user);
        patterns.forEach(patternRepository::save);

        given().queryParam("page", 2)
                .queryParam("size", 10)
                .when().get("/api/patterns/v1")
                .then().statusCode(200)
                .contentType(ContentType.JSON)
                .body("patterns", Matchers.hasSize(5))
                .body("totalElements", Matchers.is(25))
                .body("totalPages", Matchers.is(3))
                .body("page", Matchers.is(2))
                .body("size", Matchers.is(10));
    }

    @Test
    void getAllPatterns_noRequestParams_usesDefaults() {
        User user = UserITData.buildMinimal();
        List<Pattern> patterns = PatternITData.buildList(25, user);
        userRepository.save(user);
        patterns.forEach(patternRepository::save);

        given().when().get("/api/patterns/v1")
                .then().statusCode(200)
                .contentType(ContentType.JSON)
                .body("patterns", Matchers.hasSize(20))
                .body("totalElements", Matchers.is(25))
                .body("totalPages", Matchers.is(2))
                .body("page", Matchers.is(0))
                .body("size", Matchers.is(20));
    }

    @Test
    void getAllPatterns_negativePage_returns400() {
        given().queryParam("page", -1)
                .when().get("/api/patterns/v1")
                .then().statusCode(400)
                .contentType(ContentType.JSON)
                .body("message", Matchers.is("Invalid request"))
                .body("errors", Matchers.hasSize(1))
                .body("errors[0]", Matchers.is("Page must be greater than or equal to 0"));
    }

    @Test
    void getAllPatterns_negativeSize_returns400() {
        given().queryParam("size", 0)
                .when().get("/api/patterns/v1")
                .then().statusCode(400)
                .contentType(ContentType.JSON)
                .body("message", Matchers.is("Invalid request"))
                .body("errors", Matchers.hasSize(1))
                .body("errors[0]", Matchers.is("Size must be greater than or equal to 1"));
    }

    @Test
    void getAllPatterns_largeSize_returns400() {
        given().queryParam("size", 101)
                .when().get("/api/patterns/v1")
                .then().statusCode(400)
                .contentType(ContentType.JSON)
                .body("message", Matchers.is("Invalid request"))
                .body("errors", Matchers.hasSize(1))
                .body("errors[0]", Matchers.is("Size must be less than or equal to 100"));
    }
}
