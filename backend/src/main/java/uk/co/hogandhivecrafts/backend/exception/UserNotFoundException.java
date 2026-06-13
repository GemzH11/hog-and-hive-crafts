package uk.co.hogandhivecrafts.backend.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Integer id) {
        super(String.format("User with id: %s not found", id));
    }
}
