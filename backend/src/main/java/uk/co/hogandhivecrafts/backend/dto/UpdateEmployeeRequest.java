package uk.co.hogandhivecrafts.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UpdateEmployeeRequest {
    private Integer id;
    private String firstName;
    private String lastName;
}