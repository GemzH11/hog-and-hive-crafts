package uk.co.hogandhivecrafts.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "files")
@ToString(onlyExplicitlyIncluded = true)
public class File {

    @Id
    @GeneratedValue
    @ToString.Include
    private UUID id;

    @Column(nullable = false)
    @ToString.Include
    private String role;

    @Column(name = "display_name")
    @ToString.Include
    private String displayName;

    @Column(name = "storage_path", nullable = false)
    @ToString.Include
    private String storagePath;

    @Column(name = "description")
    @ToString.Include
    private String description;

    @Column(name = "content_type")
    @ToString.Include
    private String contentType;

    @Column(name = "size_bytes")
    @ToString.Include
    private Long sizeBytes;

    @Column(name = "checksum_sha256")
    @ToString.Include
    private String checksumSha256;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pattern_id", nullable = false)
    private Pattern pattern;

    // Safe debug helper
    @ToString.Include(name = "patternId")
    public UUID getPatternId() {
        return pattern != null ? pattern.getId() : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof File other)) return false;

        return id != null && id.equals(other.id);
    }
}