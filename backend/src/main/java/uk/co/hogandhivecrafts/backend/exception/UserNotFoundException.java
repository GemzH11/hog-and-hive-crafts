package uk.co.hogandhivecrafts.backend.exception;

import java.util.UUID;

/**
 * Exception thrown when a requested user is not found in the database.
 *
 * <p>This is a runtime exception that indicates the user with the specified ID
 * does not exist. It is caught by the global exception handler and converted to
 * an HTTP 404 (Not Found) response.
 */
public class UserNotFoundException extends RuntimeException {
  /**
   * Constructs a new UserNotFoundException with a message indicating the user ID.
   *
   * @param id the ID of the user that was not found
   */
  public UserNotFoundException(UUID id) {
    super(String.format("User not found with ID: %s", id));
  }
}
