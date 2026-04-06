package uk.co.hogandhivecrafts.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uk.co.hogandhivecrafts.backend.dto.CreateEmployeeRequest;
import uk.co.hogandhivecrafts.backend.dto.UpdateEmployeeRequest;
import uk.co.hogandhivecrafts.backend.entity.Employee;
import uk.co.hogandhivecrafts.backend.service.EmployeeService;
import uk.co.hogandhivecrafts.backend.util.DtoMapper;

import java.util.List;

/**
 * Controller class is where all the user requests are handled and required/appropriate responses are sent
 */
@RestController
@RequestMapping("/employees/v1")
@RequiredArgsConstructor
@Validated
public class EmployeeController {

    private final EmployeeService employeeService;

    private final DtoMapper dtoMapper;

    /**
     * Fetches all employees
     *
     * @return List of Employees
     */
    @GetMapping()
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok().body(employeeService.getAllEmployees());
    }

    /**
     * Fetches the employee with the given id
     *
     * @param id - employee id
     * @return Employee with the given id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(employeeService.getEmployeeById(id));
    }

    /**
     * Saves an Employee entity
     *
     * @param createEmployeeRequest - Request body is an Employee entity
     * @return Saved Employee entity
     */
    @PostMapping()
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody CreateEmployeeRequest createEmployeeRequest) {
        Employee employee = dtoMapper.toEmployee(createEmployeeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createEmployee(employee));
    }

    /**
     * Updates an Employee entity
     *
     * @param updateEmployeeRequest - Employee entity to be updated
     * @return Updated employee
     */
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Integer id,
                                                   @Valid @RequestBody UpdateEmployeeRequest updateEmployeeRequest) {
        Employee employee = dtoMapper.toEmployee(updateEmployeeRequest, id);
        return ResponseEntity.ok().body(employeeService.updateEmployee(employee));
    }

    /**
     * Deletes an Employee entity
     *
     * @param id - employee id
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployeeById(@PathVariable Integer id) {
        employeeService.deleteEmployeeById(id);
        return ResponseEntity.noContent().build();
    }
}
