package uk.co.hogandhivecrafts.backend.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import uk.co.hogandhivecrafts.backend.entity.File;

/**
 * Repository interface for File entities.
 *
 * <p>Provides CRUD operations for File entities.
 * The JpaRepository base provides methods like save(), findById(), findAll(), delete(), etc.
 */
public interface FileRepository extends JpaRepository<File, UUID> {
}
