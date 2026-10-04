package uk.co.hogandhivecrafts.backend.exception;

import java.util.UUID;

/**
 * Exception thrown when a requested file is not found in the database.
 *
 * <p>This is a runtime exception that indicates the file with the specified ID does not exist.
 * It is caught by the global exception handler and converted to an HTTP 404 (Not Found) response.
 */
public class FileNotFoundException extends RuntimeException {
  /**
   * Message template used when a file cannot be found.
   */
  private static final String FILE_NOT_FOUND = "File with ID %s not found.";

  /**
   * Constructs a new FileNotFoundException with a message indicating the file ID.
   *
   * @param id the ID of the file that was not found
   */
  public FileNotFoundException(UUID id) {
    super(String.format(FILE_NOT_FOUND, id));
  }
}
