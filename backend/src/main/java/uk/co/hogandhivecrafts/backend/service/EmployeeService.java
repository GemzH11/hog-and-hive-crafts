package uk.co.hogandhivecrafts.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.co.hogandhivecrafts.backend.entity.Employee;
import uk.co.hogandhivecrafts.backend.exception.EmployeeNotFoundException;
import uk.co.hogandhivecrafts.backend.repository.EmployeeRepository;

import java.util.List;

/**
 * Service layer is where all the business logic lies
 */
@Service
@RequiredArgsConstructor
@Slf4j // Automates creation of logger within class to facilitate message logging
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    /**
     * Fetches all employees
     *
     * @return List of Employees
     */
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    /**
     * @param id - employee id
     * @return Employee with the given id
     * @throws EmployeeNotFoundException - if employee with the given id is not found
     */
    public Employee getEmployeeById(Integer id) throws EmployeeNotFoundException {
        return employeeRepository.findById(id).orElseThrow(() -> {
            log.warn("Employee with id: {} not found", id);
            return new EmployeeNotFoundException(id);
        });
    }

    /**
     * Saves an Employee entity
     *
     * @param employee - Employee entity to be created
     * @return Saved Employee entity
     */
    public Employee saveEmployee(Employee employee) {
        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Saved employee: {}", savedEmployee);
        return savedEmployee;
    }

    /**
     * Updates an Employee entity
     *
     * @param employee - Employee entity to be updated
     * @return Updated employee
     * @throws EmployeeNotFoundException - if employee with the given id is not found
     */
    @Transactional
    public Employee updateEmployee(Employee employee) throws EmployeeNotFoundException {
        Employee existingEmployee = employeeRepository.findById(employee.getId()).orElseThrow(() -> {
            log.warn("Employee with id: {} not found", employee.getId());
            return new EmployeeNotFoundException(employee.getId());
        });

        existingEmployee.setFirstName(employee.getFirstName());
        existingEmployee.setLastName(employee.getLastName());
        employeeRepository.save(existingEmployee);
        log.info("Updated employee: {}", existingEmployee);
        return existingEmployee;
    }

    /**
     * Deletes an Employee entity
     *
     * @param id - employee id
     * @throws EmployeeNotFoundException - if employee with the given id is not found
     */
    @Transactional
    public void deleteEmployeeById(Integer id) throws EmployeeNotFoundException {
        Employee existingEmployee = employeeRepository.findById(id).orElseThrow(() -> {
            log.warn("Employee with id: {} not found", id);
            return new EmployeeNotFoundException(id);
        });

        employeeRepository.delete(existingEmployee);
        log.info("Deleted employee: {}", existingEmployee);
    }
}
