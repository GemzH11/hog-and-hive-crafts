package uk.co.hogandhivecrafts.backend.exception;

import java.util.UUID;

public class PatternNotFoundException extends RuntimeException {
    public PatternNotFoundException(UUID id) {
        super(String.format("Pattern not found with ID: %s", id));
    }
}
