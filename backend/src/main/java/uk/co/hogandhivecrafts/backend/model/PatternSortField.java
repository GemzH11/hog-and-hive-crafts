package uk.co.hogandhivecrafts.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PatternSortField {
    ID("id"),
    NAME("name"),
    CREATED_AT("createdAt"),
    UPDATED_AT("updatedAt");

    private final String value;
}
