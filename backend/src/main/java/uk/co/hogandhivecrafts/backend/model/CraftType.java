package uk.co.hogandhivecrafts.backend.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum representing the different types of crafts that patterns can be associated with.
 * <p>
 * Each craft type has a human-readable display name. The enum is serialized to JSON
 * using the display value via the {@link JsonValue} annotation.
 */
@Getter
@AllArgsConstructor
public enum CraftType {
    /**
     * Knitting craft type
     */
    KNITTING("Knitting"),
    /**
     * Crochet craft type
     */
    CROCHET("Crochet"),
    /**
     * Sewing craft type
     */
    SEWING("Sewing"),
    /**
     * Embroidery craft type
     */
    EMBROIDERY("Embroidery"),
    /**
     * Cross-stitch craft type
     */
    CROSS_STITCH("CrossStitch"),
    /**
     * Quilting craft type
     */
    QUILTING("Quilting"),
    /**
     * Felting craft type
     */
    FELTING("Felting"),
    /**
     * Macramé craft type
     */
    MACRAME("Macrame"),
    /**
     * Other craft type (catch-all category)
     */
    OTHER("Other");

    /**
     * The human-readable display value for this craft type, used in JSON serialization.
     */
    private final String value;

    /**
     * Returns the display value for this craft type for use in JSON serialization.
     *
     * @return the display value of this craft type
     */
    @JsonValue
    public String getValue() {
        return value;
    }
}
