package uk.co.hogandhivecrafts.backend.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.hogandhivecrafts.backend.entity.Employee;
import uk.co.hogandhivecrafts.backend.exception.EmployeeNotFoundException;
import uk.co.hogandhivecrafts.backend.repository.EmployeeRepository;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    private final Integer TEST_ID = 1;
    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void getEmployeeById_employeeExists_returnsEmployee() {
        // Test checks that when an employee with the given ID exists, the service returns the correct employee.
        Employee employee = new Employee(TEST_ID, "Joe", "Blogs");
        Mockito.when(employeeRepository.findById(TEST_ID))
                .thenReturn(Optional.of(employee));

        Employee result = employeeService.getEmployeeById(TEST_ID);

        Assertions.assertThat(result).isEqualTo(employee);

        Mockito.verify(employeeRepository).findById(TEST_ID);
    }

    @Test
    void getEmployeeById_employeeNotFound_throwsException() {
        // Test checks that when an employee with the given ID does not exist, the service throws an
        // EmployeeNotFoundException with the correct message.
        Mockito.when(employeeRepository.findById(TEST_ID))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> employeeService.getEmployeeById(TEST_ID)).isInstanceOf(EmployeeNotFoundException.class)
                .hasMessage("Employee with id: 1 not found");

        Mockito.verify(employeeRepository).findById(TEST_ID);
    }
}
