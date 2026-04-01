package uk.co.hogandhivecrafts.backend.util;

import uk.co.hogandhivecrafts.backend.dto.CreateEmployeeRequest;
import uk.co.hogandhivecrafts.backend.dto.UpdateEmployeeRequest;
import uk.co.hogandhivecrafts.backend.entity.Employee;

public class DtoMapper {
    public static Employee toEmployee(CreateEmployeeRequest request) {
        return Employee.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();
    }

    public static Employee toEmployee(UpdateEmployeeRequest request) {
        return Employee.builder()
                .id(request.getId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();
    }
}
