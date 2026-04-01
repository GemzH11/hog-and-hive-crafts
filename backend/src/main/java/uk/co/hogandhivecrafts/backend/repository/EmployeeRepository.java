package uk.co.hogandhivecrafts.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.co.hogandhivecrafts.backend.entity.Employee;

/**
 * Repository is an interface that provides access to data in a database
 */
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
