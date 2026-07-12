package uk.co.hogandhivecrafts.backend.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import uk.co.hogandhivecrafts.backend.entity.User;

/**
 * Repository interface for User entities.
 *
 * <p>Provides CRUD operations for User entities.
 * The JpaRepository base provides methods like save(), findById(), findAll(), delete(), etc.
 */
public interface UserRepository extends JpaRepository<User, UUID> {
}
