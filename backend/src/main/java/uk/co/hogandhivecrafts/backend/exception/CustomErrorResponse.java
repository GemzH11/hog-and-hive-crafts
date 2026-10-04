package uk.co.hogandhivecrafts.backend.exception;

import java.util.List;

/**
 * Standard error payload returned by the API for failed requests.
 *
 * @param message summary of the error
 * @param path    request path that produced the error
 * @param errors  validation or resource-specific details, empty when no details are available
 */
public record CustomErrorResponse(String message, String path, List<String> errors) {
}
