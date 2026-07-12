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
  ID("id"),
  NAME("name"),
  CREATED_AT("createdAt"),
  UPDATED_AT("updatedAt");

  /**
   * The database/entity field name corresponding to this sort field.
   */
  private final String value;
}
