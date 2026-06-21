package uk.co.hogandhivecrafts.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.co.hogandhivecrafts.backend.entity.User;

import java.util.UUID;

/**
 * Repository interface for User entities.
 * <p>
 * Provides CRUD operations for User entities.
 * The JpaRepository base provides methods like save(), findById(), findAll(), delete(), etc.
 */
public interface UserRepository extends JpaRepository<User, UUID> {
}
