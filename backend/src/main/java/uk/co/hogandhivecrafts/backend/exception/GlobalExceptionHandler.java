package uk.co.hogandhivecrafts.backend.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.util.HtmlUtils;
import tools.jackson.core.exc.StreamReadException;
import tools.jackson.databind.exc.InvalidFormatException;

/**
 * Handles application exceptions raised by MVC controllers and maps them to consistent JSON error
 * responses.
 *
 * <p>This advice centralizes request validation, type-conversion, not-found, and unexpected
 * failure handling so API clients receive predictable HTTP status codes and payload shapes.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
  /**
   * Summary returned for request validation and conversion failures.
   */
  public static final String INVALID_REQUEST = "Invalid request";

  /**
   * Summary returned when a requested resource does not exist.
   */
  public static final String RESOURCE_NOT_FOUND = "Resource not found";

  /**
   * Summary returned when an unhandled server error occurs.
   */
  public static final String UNEXPECTED_ERROR = "An unexpected error occurred";

  /**
   * Detail returned when a request body cannot be read as JSON.
   */
  public static final String MALFORMED_JSON_REQUEST_BODY = "Malformed JSON request body";

  /**
   * Detail returned when a request body cannot be converted to its expected type.
   */
  public static final String INVALID_REQUEST_BODY = "Invalid request body";

  /**
   * Fallback detail for a validation error without a default message.
   */
  public static final String METHOD_ARGUMENT_NOT_VALID = "Invalid request parameter";

  /**
   * Message template for a value that cannot be converted to the target request type.
   */
  public static final String TYPE_MISMATCH = "Invalid value for '%s': %s";

  /**
   * Message template for a request body value that cannot be converted to its target type.
   */
  public static final String INVALID_FORMAT = "Invalid value for '%s': %s. Expected %s";

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
    List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(error -> {
      if (error.isBindingFailure()) {
        return String.format(TYPE_MISMATCH, error.getField(), error.getRejectedValue());
      }
      return error.getDefaultMessage() == null
             ? METHOD_ARGUMENT_NOT_VALID
             : error.getDefaultMessage();
    }).toList();

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
   * Handles request value conversion failures with the rejected value in the error details.
   *
   * @param ex      the exception raised while converting a request value
   * @param request the request that contained the invalid value
   * @return a 400 error response with the parameter name and rejected value
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<CustomErrorResponse> handleTypeMismatch(
      MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    String path = HtmlUtils.htmlEscape(request.getRequestURI());

    Object rejected = ex.getValue();
    String rejectedValue = rejected == null ? "null" : rejected.toString();
    List<String> errors = List.of(String.format(TYPE_MISMATCH, ex.getName(), rejectedValue));

    return buildResponse(status, INVALID_REQUEST, path, errors);
  }

  /**
   * Handles malformed JSON and request-body value conversion failures with stable 400 error details.
   *
   * @param ex      the exception raised while reading the request body
   * @param request the request containing the unreadable body
   * @return a 400 error response describing why the body could not be read
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<CustomErrorResponse> handleNotReadable(HttpMessageNotReadableException ex,
                                                               HttpServletRequest request) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    String path = HtmlUtils.htmlEscape(request.getRequestURI());
    InvalidFormatException invalidFormatException =
        findCause(ex, InvalidFormatException.class);
    String error = invalidFormatException != null
                   ? formatInvalidFormat(invalidFormatException)
                   : findCause(ex, StreamReadException.class) != null
                     ? MALFORMED_JSON_REQUEST_BODY
                     : INVALID_REQUEST_BODY;

    return buildResponse(status, INVALID_REQUEST, path, List.of(error));
  }

  private String formatInvalidFormat(InvalidFormatException ex) {
    String field = ex.getPath().stream()
                     .map(reference -> reference.getPropertyName())
                     .filter(Objects::nonNull)
                     .reduce((first, second) -> second)
                     .orElse("request body");
    Class<?> targetType = ex.getTargetType();
    String expected = targetType.isEnum()
                      ? "one of: [" + String.join(", ",
                          Arrays.stream(targetType.getEnumConstants()).map(String::valueOf).toList())
                          + "]"
                      : "a value of type " + targetType.getSimpleName();
    Object rejectedValue = ex.getValue();
    return String.format(INVALID_FORMAT, field,
                         rejectedValue == null ? "null" : rejectedValue, expected);
  }

  private <T extends Throwable> T findCause(Throwable exception, Class<T> causeType) {
    Throwable cause = exception;
    while (cause != null) {
      if (causeType.isInstance(cause)) {
        return causeType.cast(cause);
      }
      Throwable nextCause = cause.getCause();
      if (nextCause == cause) {
        break;
      }
      cause = nextCause;
    }
    return null;
  }

  /**
   * Returns a generic error response for exceptions without a more specific handler.
   *
   * @param ex      the unhandled exception
   * @param request the request being processed when the exception occurred
   * @return a 500 error response without exposing internal exception details
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
                         .body(new CustomErrorResponse(message, path, safeErrors));
  }
}