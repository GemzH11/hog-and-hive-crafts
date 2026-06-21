package uk.co.hogandhivecrafts.backend.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Integer id) {
        super(String.format("User not found with ID: %s", id));
    }
}
