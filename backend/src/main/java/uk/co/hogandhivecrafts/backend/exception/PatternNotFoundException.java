package uk.co.hogandhivecrafts.backend.exception;

public class PatternNotFoundException extends RuntimeException {
    public PatternNotFoundException(Integer id) {
        super(String.format("Pattern with id: %s not found", id));
    }
}
