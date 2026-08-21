package uk.co.hogandhivecrafts.backend.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.util.HtmlUtils;

/**
 * Handles application exceptions raised by MVC controllers and maps them to consistent JSON error
 * responses.
 *
 * <p>This advice centralizes request validation, type-conversion, not-found, and unexpected
 * failure handling so API clients receive predictable HTTP status codes and payload shapes.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
  public static final String INVALID_REQUEST = "Invalid request";
  public static final String RESOURCE_NOT_FOUND = "Resource not found";
  public static final String UNEXPECTED_ERROR = "An unexpected error occurred";
  public static final String MALFORMED_JSON_REQUEST_BODY = "Malformed JSON request body";

  /**
   * Handles Bean Validation failures on request arguments.
   *
   * @param ex      the thrown exception
   * @param request the request that contains the invalid argument
   * @return a formatted error response that can be returned to the client with a 400 status code
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<CustomErrorResponse> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, HttpServletRequest request) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    String path = HtmlUtils.htmlEscape(request.getRequestURI());
    List<String> errors = ex.getBindingResult()
                            .getFieldErrors()
                            .stream()
                            .map(error -> error.getDefaultMessage() == null
                                          ? "Invalid request parameter"
                                          : error.getDefaultMessage())
                            .toList();

    return buildResponse(status, INVALID_REQUEST, path, errors);
  }

  /**
   * Handles not-found domain exceptions.
   *
   * @param ex      the thrown exception
   * @param request the request that caused the exception to be thrown
   * @return a formatted error response that can be returned to the client with a 404 status code
   */
  @ExceptionHandler({UserNotFoundException.class,
      PatternNotFoundException.class,
      FileNotFoundException.class})
  public ResponseEntity<CustomErrorResponse> handleNotFound(RuntimeException ex,
                                                            HttpServletRequest request) {
    HttpStatus status = HttpStatus.NOT_FOUND;
    String path = HtmlUtils.htmlEscape(request.getRequestURI());
    List<String> errors = ex.getMessage() == null || ex.getMessage().isBlank()
                          ? List.of()
                          : List.of(ex.getMessage());

    return buildResponse(status, RESOURCE_NOT_FOUND, path, errors);
  }

  /**
   * Handle MethodArgumentTypeMismatchException separately so we can provide a useful errors[] entry
   * (for example when a path variable cannot be converted to UUID).
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<CustomErrorResponse> handleTypeMismatch(
      MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    String path = HtmlUtils.htmlEscape(request.getRequestURI());

    String rejectedValue = ex.getValue() == null ? "null" : ex.getValue().toString();
    List<String> errors = List.of(
        String.format("Invalid value for '%s': %s", ex.getName(), rejectedValue));

    return buildResponse(status, INVALID_REQUEST, path, errors);
  }

  /**
   * Handles malformed or unreadable JSON request bodies with stable 400 error details.
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<CustomErrorResponse> handleNotReadable(HttpMessageNotReadableException ex,
                                                               HttpServletRequest request) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    String path = HtmlUtils.htmlEscape(request.getRequestURI());
    List<String> errors = List.of(MALFORMED_JSON_REQUEST_BODY);

    return buildResponse(status, INVALID_REQUEST, path, errors);
  }

  /**
   * Default exception handler for unhandled errors.
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<CustomErrorResponse> handleGenericException(Exception ex,
                                                                    HttpServletRequest request) {
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    String path = HtmlUtils.htmlEscape(request.getRequestURI());

    return buildResponse(status, UNEXPECTED_ERROR, path, List.of());
  }

  private ResponseEntity<CustomErrorResponse> buildResponse(HttpStatus status, String message,
                                                            String path, List<String> errors) {
    List<String> safeErrors = errors == null ? List.of() : errors;
    return ResponseEntity.status(status)
                         .contentType(MediaType.APPLICATION_JSON)
                         .body(new CustomErrorResponse(message, status.value(), path, safeErrors));
  }

  /**
   * Represents a standardized error response returned to clients when an exception occurs.
   *
   * <p>Includes a human-readable message, HTTP status code, request path, and optional list of
   * specific errors.
   */
  public record CustomErrorResponse(String message, int status, String path, List<String> errors) {
  }
}