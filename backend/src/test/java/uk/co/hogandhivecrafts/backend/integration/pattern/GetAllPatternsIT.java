package uk.co.hogandhivecrafts.backend.integration.pattern;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import uk.co.hogandhivecrafts.backend.dto.GetAllPatternsResponse;
import uk.co.hogandhivecrafts.backend.dto.GetPatternByIdResponse;
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

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;

import static io.restassured.RestAssured.given;

public class GetAllPatternsIT extends AbstractIT {
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
        File file = FileITData.buildMinimal(0, pattern0);

        user = userRepository.save(user);
        pattern0 = patternRepository.save(pattern0);
        pattern1 = patternRepository.save(pattern1);
        file = fileRepository.save(file);

        given().when().get("/api/patterns/v1")
                .then().statusCode(200)
                .contentType(ContentType.JSON)
                .body("patterns", Matchers.hasSize(2))
                .body("patterns[0].id", Matchers.is(pattern0.getId().toString()))
                .body("patterns[0].name", Matchers.is(pattern0.getName()))
                .body("patterns[0].source", Matchers.is(pattern0.getSource()))
                .body("patterns[0].craftType", Matchers.is(pattern0.getCraftType()))
                .body("patterns[0].notes", Matchers.is(pattern0.getNotes()))
                .body("patterns[0].createdAt", Matchers.is(pattern0.getCreatedAt().toString()))
                .body("patterns[0].updatedAt", Matchers.is(pattern0.getUpdatedAt().toString()))
                .body("patterns[0].userId", Matchers.is(user.getId().toString()))
                .body("patterns[0].fileIds", Matchers.hasSize(1))
                .body("patterns[0].fileIds[0]", Matchers.is(file.getId().toString()))
                .body("patterns[1].id", Matchers.is(pattern1.getId().toString()))
                .body("patterns[1].name", Matchers.is(pattern1.getName()))
                .body("patterns[1].userId", Matchers.is(user.getId().toString()))
                .body("patterns[1].fileIds", Matchers.hasSize(0));
    }

    @Test
    void getAllPatterns_paginationApplied_returnsRequestedPageSorted() {
        User user = UserITData.buildMinimal();
        List<Pattern> patterns = PatternITData.buildList(25, user);
        userRepository.save(user);
        patterns.forEach(patternRepository::save);

        // Capture the response so we can assert correct ordering
        GetAllPatternsResponse response = given().queryParam("page", 2)
                .queryParam("size", 10)
                .queryParam("sortField", "NAME")
                .queryParam("sortDirection", "DESC")
                .when().get("/api/patterns/v1")
                .then().statusCode(200)
                .contentType(ContentType.JSON)
                .body("patterns", Matchers.hasSize(5))
                .body("totalElements", Matchers.is(25))
                .body("totalPages", Matchers.is(3))
                .body("page", Matchers.is(2))
                .body("size", Matchers.is(10))
                .extract().as(GetAllPatternsResponse.class);

        List<String> actual =
                response.patterns().stream().map(GetPatternByIdResponse::name).toList();
        List<String> expected = actual.stream().sorted(Comparator.reverseOrder()).toList();
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getAllPatterns_noRequestParams_usesDefaults() {
        User user = UserITData.buildMinimal();
        List<Pattern> patterns = PatternITData.buildList(25, user);
        userRepository.save(user);
        patterns.forEach(patternRepository::save);

        GetAllPatternsResponse response = given().when().get("/api/patterns/v1")
                .then().statusCode(200)
                .contentType(ContentType.JSON)
                .body("patterns", Matchers.hasSize(20))
                .body("totalElements", Matchers.is(25))
                .body("totalPages", Matchers.is(2))
                .body("page", Matchers.is(0))
                .body("size", Matchers.is(20))
                .extract().as(GetAllPatternsResponse.class);

        List<OffsetDateTime> actual =
                response.patterns().stream().map(GetPatternByIdResponse::createdAt).toList();
        List<OffsetDateTime> expected = actual.stream().sorted().toList();
        Assertions.assertThat(actual).isEqualTo(expected);
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

    @Test
    void getAllPatterns_invalidSortDirection_returns400() {
        given().queryParam("sortDirection", "INVALID")
                .when().get("/api/patterns/v1")
                .then().statusCode(400)
                .contentType(ContentType.JSON)
                .body("message", Matchers.is("Invalid request"))
                .body("errors", Matchers.hasSize(1))
                .body("errors[0]", Matchers.is("Invalid value for 'sortDirection': INVALID. Allowed values: ASC, DESC"));
    }

    @Test
    void getAllPatterns_invalidSortField_returns400() {
        given().queryParam("sortField", "INVALID")
                .when().get("/api/patterns/v1")
                .then().statusCode(400)
                .contentType(ContentType.JSON)
                .body("message", Matchers.is("Invalid request"))
                .body("errors", Matchers.hasSize(1))
                .body("errors[0]", Matchers.is("Invalid value for 'sortField': INVALID. Allowed values: ID, NAME, CREATED_AT, UPDATED_AT"));
    }
}
