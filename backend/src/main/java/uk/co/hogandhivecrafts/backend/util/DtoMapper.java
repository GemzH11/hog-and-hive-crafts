package uk.co.hogandhivecrafts.backend.util;

import org.springframework.stereotype.Component;
import uk.co.hogandhivecrafts.backend.dto.CreateEmployeeRequest;
import uk.co.hogandhivecrafts.backend.dto.UpdateEmployeeRequest;
import uk.co.hogandhivecrafts.backend.entity.Employee;

@Component
public class DtoMapper {
    public Employee toEmployee(CreateEmployeeRequest request) {
        return Employee.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();
    }

    public Employee toEmployee(UpdateEmployeeRequest request) {
        return Employee.builder()
                .id(request.getId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();
    }
}
