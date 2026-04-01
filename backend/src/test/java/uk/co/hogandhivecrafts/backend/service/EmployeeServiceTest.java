package uk.co.hogandhivecrafts.backend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.hogandhivecrafts.backend.entity.Employee;
import uk.co.hogandhivecrafts.backend.exceptions.EmployeeNotFoundException;
import uk.co.hogandhivecrafts.backend.repository.EmployeeRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void getEmployeeById_employeeExists_returnsEmployee() {
        Employee employee = new Employee(1, "Joe", "Blogs");
        Mockito.when(employeeRepository.findById(1))
                .thenReturn(Optional.of(employee));

        Employee result = employeeService.getEmployeeById(1);

        assertThat(result).isEqualTo(employee);
    }

    @Test
    void getEmployeeById_employeeNotFound_throwsException() {
        Mockito.when(employeeRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.getEmployeeById(1)).isInstanceOf(EmployeeNotFoundException.class)
                .hasMessage("Employee with id: 1 not found");
    }
}
