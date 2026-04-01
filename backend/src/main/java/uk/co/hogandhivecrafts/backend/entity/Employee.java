package uk.co.hogandhivecrafts.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * An entity class represents a table in a relational database
 */
@NoArgsConstructor // Automatically generate default constructor
@AllArgsConstructor // Automatically generate all-args constructor
@Data // Automatically generate getters, setters, `toString()` and other essential methods
@Entity
@Table(name = "employees")
public class Employee {

    @Id // Declare the primary key of an entity class
    @GeneratedValue // auto-generate unique ID for entity
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "surname")
    private String lastName;

}
