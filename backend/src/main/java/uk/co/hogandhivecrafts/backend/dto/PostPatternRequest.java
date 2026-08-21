package uk.co.hogandhivecrafts.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import uk.co.hogandhivecrafts.backend.model.CraftType;

/**
 * Request DTO containing user-entered information for a single pattern. This DTO allows creation of
 * a new Pattern entity.
 *
 * @param name      name of the pattern (required, non-blank)
 * @param source    original source or reference for the pattern
 * @param craftType type of craft this pattern is for (required)
 * @param notes     additional notes about the pattern
 */
public record PostPatternRequest(
    @NotBlank(message = "Name is required") @Size(max = 128, message = "Name must be 128 characters or fewer") String name,
    String source, @NotNull(message = "Craft type is required") CraftType craftType, String notes) {
}
