package uk.co.hogandhivecrafts.backend.exception;

import java.util.UUID;

/**
 * Exception thrown when a requested pattern is not found in the database.
 *
 * <p>This is a runtime exception that indicates the pattern with the specified ID does not exist.
 * It is caught by the global exception handler and converted to an HTTP 404 (Not Found) response.
 */
public class PatternNotFoundException extends RuntimeException {
  /**
   * Message template used when a pattern cannot be found.
   */
  public static final String PATTERN_NOT_FOUND = "Pattern not found with ID: %s";

  /**
   * Constructs a new PatternNotFoundException with a message indicating the pattern ID.
   *
   * @param id the ID of the pattern that was not found
   */
  public PatternNotFoundException(UUID id) {
    super(String.format(PATTERN_NOT_FOUND, id));
  }
}
