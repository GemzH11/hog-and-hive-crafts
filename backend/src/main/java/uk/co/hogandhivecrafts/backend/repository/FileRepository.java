package uk.co.hogandhivecrafts.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.co.hogandhivecrafts.backend.entity.File;

import java.util.UUID;

public interface FileRepository extends JpaRepository<File, UUID> {
}
