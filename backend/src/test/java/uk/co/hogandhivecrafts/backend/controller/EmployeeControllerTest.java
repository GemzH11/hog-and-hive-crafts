package uk.co.hogandhivecrafts.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import uk.co.hogandhivecrafts.backend.entity.Employee;
import uk.co.hogandhivecrafts.backend.service.EmployeeService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @Test
    void getEmployeeById_employeeExists_returns200AndEmployee() throws Exception {
        Employee employee = new Employee(1, "Joe", "Blogs");

        given(employeeService.getEmployeeById(1)).willReturn(employee);

        mockMvc.perform(get("/employees/v1/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Joe"))
                .andExpect(jsonPath("$.lastName").value("Blogs"));
    }

    @Test
    void postEmployee_validInput_returns201AndSavedEmployee() throws Exception {
        Employee employee = new Employee(1, "Joe", "Blogs");

        given(employeeService.saveEmployee(any(Employee.class))).willReturn(employee);

        mockMvc.perform(post("/employees/v1").contentType(MediaType.APPLICATION_JSON).content("""
                        {
                            "firstName": "Joe",
                            "lastName": "Blogs"
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Joe"))
                .andExpect(jsonPath("$.lastName").value("Blogs"));
    }
}
