package uk.co.hogandhivecrafts.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
@ToString(onlyExplicitlyIncluded = true)
public class User {

    @Id
    // Postgres generates the UUID, so no generation strategy needs to be specified
    @GeneratedValue
    @ToString.Include
    private UUID id;

    @Column(name = "email")
    @ToString.Include
    private String email;

    @Column(name = "display_name", nullable = false)
    @ToString.Include
    private String displayName;

    @Column(name = "avatar_url")
    @ToString.Include
    private String avatarUrl;

    // insertable = false means that when saving a new user, created_at and updated_at are not included
    // This results in the database setting the fields automatically using now()
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    @Generated(event = EventType.INSERT)
    @ToString.Include
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false)
    @Generated(event = {EventType.INSERT, EventType.UPDATE})
    @ToString.Include
    private OffsetDateTime updatedAt;

    // Relationships
    // Pattern owns the relationship, User just reflects it
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pattern> patterns;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User other)) return false;
        return id != null && id.equals(other.id);
    }
}
