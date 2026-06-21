package uk.co.hogandhivecrafts.backend.exception;

import java.util.UUID;

/**
 * Exception thrown when a requested pattern is not found in the database.
 * <p>
 * This is a runtime exception that indicates the pattern with the specified ID
 * does not exist. It is caught by the global exception handler and converted to
 * an HTTP 404 (Not Found) response.
 */
public class PatternNotFoundException extends RuntimeException {
    /**
     * Constructs a new PatternNotFoundException with a message indicating the pattern ID.
     *
     * @param id the ID of the pattern that was not found
     */
    public PatternNotFoundException(UUID id) {
        super(String.format("Pattern not found with ID: %s", id));
    }
}
