package uk.co.hogandhivecrafts.backend.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * An entity class represents a table in a relational database
 */
@Getter // Automatically generate getter methods for all fields in the class
@Setter // Automatically generate setter methods for all fields in the class
@NoArgsConstructor // Automatically generate constructor with no parameters
@AllArgsConstructor // Automatically generate constructor with parameters for all fields in the class
@Builder // Automatically generate builder pattern for the class, allowing for more flexible object creation
@Entity
@Table(name = "employees")
public class Employee {

    @Id // Declare the primary key of an entity class
    @GeneratedValue(strategy = GenerationType.AUTO) // auto-generate unique ID for entity
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "surname")
    private String lastName;

    @Override
    public String toString() {
        return String.format("Employee{id=%d, firstName='%s', lastName='%s'}", id, firstName, lastName);
    }
}
