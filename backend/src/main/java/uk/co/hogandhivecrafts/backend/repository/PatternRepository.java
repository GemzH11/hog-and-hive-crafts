package uk.co.hogandhivecrafts.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.co.hogandhivecrafts.backend.entity.Pattern;

import java.util.UUID;

public interface PatternRepository extends JpaRepository<Pattern, UUID> {
}
