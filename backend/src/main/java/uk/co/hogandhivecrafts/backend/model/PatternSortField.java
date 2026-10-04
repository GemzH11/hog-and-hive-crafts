package uk.co.hogandhivecrafts.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum representing valid fields that can be used to sort patterns.
 *
 * <p>Each enum value maps to a database column name or JPA field name.
 */
@Getter
@AllArgsConstructor
public enum PatternSortField {
  /**
   * Sort by the pattern identifier.
   */
  ID("id"),
  /**
   * Sort alphabetically by pattern name.
   */
  NAME("name"),
  /**
   * Sort by the pattern creation timestamp.
   */
  CREATED_AT("createdAt"),
  /**
   * Sort by the pattern update timestamp.
   */
  UPDATED_AT("updatedAt");

  /**
   * The database/entity field name corresponding to this sort field.
   */
  private final String value;
}
