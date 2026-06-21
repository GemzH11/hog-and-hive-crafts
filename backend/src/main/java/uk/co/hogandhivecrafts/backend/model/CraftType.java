package uk.co.hogandhivecrafts.backend.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CraftType {
    KNITTING("Knitting"),
    CROCHET("Crochet"),
    SEWING("Sewing"),
    EMBROIDERY("Embroidery"),
    CROSS_STITCH("CrossStitch"),
    QUILTING("Quilting"),
    FELTING("Felting"),
    MACRAME("Macrame"),
    OTHER("Other");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }
}
