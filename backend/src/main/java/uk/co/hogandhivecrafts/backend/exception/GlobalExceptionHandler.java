package uk.co.hogandhivecrafts.backend.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.util.HtmlUtils;
import uk.co.hogandhivecrafts.backend.dto.PatternSortField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        String path = HtmlUtils.htmlEscape(request.getRequestURI());
        String message = "Invalid request";
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatFieldError)
                .toList();

        return buildResponse(status, message, path, errors);
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
        String path = HtmlUtils.htmlEscape(request.getRequestURI());
        String message = ex.getMessage() == null || ex.getMessage().isBlank() ? "Not found" : ex.getMessage();

        return buildResponse(status, message, path, new ArrayList<>());
    }

    /**
     * Global exception handler for IllegalArgumentException. This often maps to bad input supplied by the
     * client (for example invalid IDs, malformed parameters, or missing JSON bodies).
     *
     * @param ex      the thrown exception
     * @param request the request that caused the exception to be thrown
     * @return a formatted error response that can be returned to the client with a 400 status code
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String path = HtmlUtils.htmlEscape(request.getRequestURI());
        String message = "Invalid request";
        List<String> errors = ex.getMessage() != null && !ex.getMessage()
                .isBlank() ? List.of(ex.getMessage()) : List.of();

        return buildResponse(status, message, path, errors);
    }

    /**
     * Handle MethodArgumentTypeMismatchException separately so we can provide a useful
     * errors[] entry (for example when a path variable cannot be converted to UUID).
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String path = HtmlUtils.htmlEscape(request.getRequestURI());

        String paramName = ex.getName();
        Object rejectedValue = ex.getValue();
        // Include the expected type (when available) to make the error more actionable
        String valueStr = rejectedValue == null ? "null" : rejectedValue.toString();
        String expected = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : null;
        String errorDetail = expected != null
                ? String.format("Invalid value for %s path parameter: %s (expected %s)", paramName.toUpperCase(), valueStr, expected)
                : String.format("Invalid value for %s path parameter: %s", paramName.toUpperCase(), valueStr);

        List<String> errors = List.of(errorDetail);
        String message = "Invalid request";

        return buildResponse(status, message, path, errors);
    }

    /**
     * Handle JSON parse / read errors and return a helpful errors[] entry when possible.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String path = HtmlUtils.htmlEscape(request.getRequestURI());

        ex.getMostSpecificCause();
        String causeMessage = ex.getMostSpecificCause().getMessage();
        String errorDetail = causeMessage != null && !causeMessage.isBlank() ? causeMessage : "Malformed request body";

        List<String> errors = List.of(errorDetail);
        String message = "Invalid request";

        return buildResponse(status, message, path, errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String path = HtmlUtils.htmlEscape(request.getRequestURI());
        String message = "An unexpected error occurred";

        return buildResponse(status, message, path, new ArrayList<>());
    }

    /**
     * Formats a FieldError into a user-friendly error message.
     *
     * @param fieldError The object containing the error
     * @return a formatted error string
     */
    private String formatFieldError(FieldError fieldError) {
        return switch (fieldError.getField()) {
            case "sortField" -> String.format("Invalid value for 'sortField': %s. Allowed values: %s",
                    fieldError.getRejectedValue(), Arrays.stream(PatternSortField.values())
                            .map(Enum::name)
                            .collect(Collectors.joining(", ")));
            case "sortDirection" -> String.format("Invalid value for 'sortDirection': %s. Allowed values: %s",
                    fieldError.getRejectedValue(), Arrays.stream(Sort.Direction.values())
                            .map(Enum::name)
                            .collect(Collectors.joining(", ")));
            default -> fieldError.getDefaultMessage();
        };
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message, String path, List<String> errors) {
        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse(message, status.value(), path, errors));
    }

    public record ErrorResponse(String message, int status, String path, List<String> errors) {
    }
}
