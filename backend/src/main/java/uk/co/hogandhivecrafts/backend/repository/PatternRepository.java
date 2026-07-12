package uk.co.hogandhivecrafts.backend.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import uk.co.hogandhivecrafts.backend.entity.Pattern;

/**
 * Repository interface for Pattern entities.
 *
 * <p>Provides CRUD operations and pagination support for Pattern entities.
 * The JpaRepository base provides methods like save(), findById(), findAll(), delete(), etc.
 */
public interface PatternRepository extends JpaRepository<Pattern, UUID> {
}
