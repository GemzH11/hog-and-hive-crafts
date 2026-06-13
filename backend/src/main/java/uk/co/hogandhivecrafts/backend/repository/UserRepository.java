package uk.co.hogandhivecrafts.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.co.hogandhivecrafts.backend.entity.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
