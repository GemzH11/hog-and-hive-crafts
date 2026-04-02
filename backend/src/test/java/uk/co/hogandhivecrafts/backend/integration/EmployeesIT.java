package uk.co.hogandhivecrafts.backend.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import uk.co.hogandhivecrafts.backend.entity.Employee;
import uk.co.hogandhivecrafts.backend.repository.EmployeeRepository;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.equalTo;

@ActiveProfiles("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeesIT {

    @LocalServerPort
    int port;

    @Autowired
    private EmployeeRepository employeeRepository;

    private Integer testEmployeeId;

    @BeforeEach
    void setUp() {
        // Clear the database and add a test employee before each test
        employeeRepository.deleteAll();
        testEmployeeId = employeeRepository.save(new Employee(null, "Joe", "Blogs")).getId();
    }

    @Test
    void getEmployeeById_employeeExists_returns200AndEmployee() {
        // Test checks that when an employee with the given ID exists, the endpoint returns a 200 status code and the
        // correct employee data in JSON format.
        given().port(port)
                .when().get("/employees/v1/" + testEmployeeId)
                .then().statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("id", equalTo(testEmployeeId))
                .body("firstName", equalTo("Joe"))
                .body("lastName", equalTo("Blogs"));
    }

    @Test
    void postEmployee_validInput_returns201AndSavedEmployee() {
        // Test checks that when a valid employee creation request is sent, the endpoint returns a 201 status code
        // and the correct employee data in JSON format, including an auto-generated ID.
        given().port(port).contentType(MediaType.APPLICATION_JSON_VALUE).body("""
                            {
                                "firstName": "Jane",
                                "lastName": "Doe"
                            }
                        """)
                .when().post("/employees/v1")
                .then().statusCode(201)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("id", any(Integer.class))
                .body("firstName", equalTo("Jane"))
                .body("lastName", equalTo("Doe"));
    }
}
