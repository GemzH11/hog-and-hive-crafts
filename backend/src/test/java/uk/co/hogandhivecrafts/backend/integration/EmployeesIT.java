package uk.co.hogandhivecrafts.backend.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import uk.co.hogandhivecrafts.backend.entity.Employee;
import uk.co.hogandhivecrafts.backend.repository.EmployeeRepository;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeesIT {

    @LocalServerPort
    int port;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
        employeeRepository.save(new Employee(null, "Joe", "Blogs"));
    }

    @Test
    void getEmployeeById_employeeExists_returns200AndEmployee() {
        given().port(port)
                .when().get("/employees/v1/1")
                .then().statusCode(200)
                .body("firstName", equalTo("Joe"));
    }

    @Test
    void postEmployee_validInput_returns201AndSavedEmployee() {
        given().port(port).contentType("application/json").body("""
                            {
                                "firstName": "Jane",
                                "surname": "Doe"
                            }
                        """)
                .when().post("/employees/v1")
                .then().statusCode(201)
                .body("firstName", equalTo("Jane"));
    }
}
