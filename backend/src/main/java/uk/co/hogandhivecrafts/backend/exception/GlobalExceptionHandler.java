package uk.co.hogandhivecrafts.backend.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Global exception handler for MethodArgumentNotValidExceptions
     *
     * @param ex      the thrown exception
     * @param request the request that contains the invalid argument
     * @return a formatted error response that can be returned to the client with a 400 status code
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        // Escape user-controlled URI to prevent potential XSS warnings from static analysis tools
        String path = HtmlUtils.htmlEscape(request.getRequestURI());
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        ErrorResponse error = new ErrorResponse(("Invalid request"), status.value(), path, errors);

        return ResponseEntity.status(status).contentType(MediaType.APPLICATION_JSON).body(error);
    }

    /**
     * Global exception handler for custom NotFoundExceptions
     *
     * @param ex      the thrown exception
     * @param request the request that caused the exception to be thrown
     * @return a formatted error response that can be returned to the client with a 400 status code
     */
    @ExceptionHandler({UserNotFoundException.class, PatternNotFoundException.class, FileNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        // Escape user-controlled URI to prevent potential XSS warnings from static analysis tools
        String path = HtmlUtils.htmlEscape(request.getRequestURI());
        ErrorResponse error = new ErrorResponse(ex.getMessage(), status.value(), path, new ArrayList<>());

        return ResponseEntity.status(status).contentType(MediaType.APPLICATION_JSON).body(error);
    }

    public record ErrorResponse(String message, int status, String path, List<String> errors) {
    }
}
