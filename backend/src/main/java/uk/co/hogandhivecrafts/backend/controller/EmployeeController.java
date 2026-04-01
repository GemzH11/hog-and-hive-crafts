package uk.co.hogandhivecrafts.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uk.co.hogandhivecrafts.backend.entity.Employee;
import uk.co.hogandhivecrafts.backend.service.EmployeeService;

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
     * @param employee - Request body is an Employee entity
     * @return Saved Employee entity
     */
    @PostMapping()
    public ResponseEntity<Employee> saveEmployee(@RequestBody Employee employee) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.saveEmployee(employee));
    }

    /**
     * Updates an Employee entity
     *
     * @param employee - Employee entity to be updated
     * @return Updated employee
     */
    @PutMapping()
    public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee) {
        return ResponseEntity.ok().body(employeeService.updateEmployee(employee));
    }

    /**
     * Deletes an Employee entity
     *
     * @param id - employee id
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Employee> deleteEmployeeById(@PathVariable Integer id) {
        employeeService.deleteEmployeeById(id);
        return ResponseEntity.noContent().build();
    }
}
