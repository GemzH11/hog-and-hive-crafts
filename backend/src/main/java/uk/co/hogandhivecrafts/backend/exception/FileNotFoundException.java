package uk.co.hogandhivecrafts.backend.exception;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException(Integer id) {
        super(String.format("File with id: %s not found", id));
    }
}
