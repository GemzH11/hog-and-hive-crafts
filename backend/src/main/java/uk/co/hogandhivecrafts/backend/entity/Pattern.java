package uk.co.hogandhivecrafts.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import uk.co.hogandhivecrafts.backend.model.CraftType;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "patterns",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_patterns_user_id_name", columnNames = {"user_id", "name"})
        }
)
@ToString(onlyExplicitlyIncluded = true)
public class Pattern {

    @Id
    @GeneratedValue
    @ToString.Include
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "name", nullable = false)
    @ToString.Include
    private String name;

    @Column(name = "source")
    @ToString.Include
    private String source;

    @Column(name = "craft_type", nullable = false)
    @ToString.Include
    private CraftType craftType;

    @Column(name = "notes")
    @ToString.Include
    private String notes;

    // insertable = false means that when saving a new user, created_at and updated_at are not included
    // This results in the database setting the fields automatically using now()
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    @ToString.Include
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false)
    @ToString.Include
    private OffsetDateTime updatedAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @EqualsAndHashCode.Exclude
    private User user;

    @OneToMany(mappedBy = "pattern", cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    private List<File> files;

    // Safe debug helper (no recursion risk)
    @ToString.Include(name = "userId")
    public UUID getUserId() {
        return user != null ? user.getId() : null;
    }

    @ToString.Include(name = "fileIds")
    public List<UUID> getFileIds() {
        return files != null ? files.stream().map(File::getId).toList() : List.of();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pattern other)) return false;

        return id != null && id.equals(other.id);
    }
}