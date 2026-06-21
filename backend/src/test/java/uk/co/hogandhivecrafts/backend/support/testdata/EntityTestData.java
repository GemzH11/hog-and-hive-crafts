package uk.co.hogandhivecrafts.backend.support.testdata;

import uk.co.hogandhivecrafts.backend.entity.File;
import uk.co.hogandhivecrafts.backend.entity.Pattern;
import uk.co.hogandhivecrafts.backend.entity.User;
import uk.co.hogandhivecrafts.backend.model.CraftType;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class EntityTestData {
    private static final OffsetDateTime CREATED_DATE = OffsetDateTime.parse("2020-01-01T01:00:00Z");
    private static final OffsetDateTime UPDATED_DATE = OffsetDateTime.parse("2020-01-01T02:00:00Z");

    private static final UUID PATTERN_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    private static final String PATTERN_NAME = "Test pattern";
    private static final String PATTERN_SOURCE = "web";
    private static final CraftType PATTERN_CRAFT_TYPE = CraftType.OTHER;
    private static final String PATTERN_NOTES = "notes";

    private static final UUID USER_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final String USER_EMAIL = "test@example.com";
    private static final String USER_DISPLAY_NAME = "Test user";
    private static final String USER_AVATAR_URL = "avatar.png";

    private static final UUID FILE_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final String FILE_ROLE = "image";
    private static final String FILE_DISPLAY_NAME = "file.png";
    private static final String FILE_STORAGE_PATH = "/tmp/file.png";
    private static final String FILE_DESCRIPTION = "test file";
    private static final String FILE_CONTENT_TYPE = "image/png";
    private static final Long FILE_SIZE = 123L;
    private static final String FILE_CHECKSUM = "abc123";

    private EntityTestData() {
        // prevent instantiation
    }

    /**
     * Builds a default User entity with all fields populated
     *
     * @param userId     the ID to use for the user
     * @param patternIds the pattern IDs to associate with the user
     * @return a fully-populated User entity with default properties
     */
    public static User buildDefaultUser(UUID userId, List<UUID> patternIds) {
        User user = new User();
        user.setId(userId);
        user.setEmail(USER_EMAIL);
        user.setDisplayName(USER_DISPLAY_NAME);
        user.setAvatarUrl(USER_AVATAR_URL);
        user.setCreatedAt(CREATED_DATE);
        user.setUpdatedAt(UPDATED_DATE);

        List<Pattern> patterns = patternIds.stream().map(EntityTestData::buildSimplePattern).toList();
        user.setPatterns(patterns);
        return user;
    }

    /**
     * Builds a default User entity using a default set of IDs
     *
     * @return a fully-populated User entity with default properties
     */
    public static User buildDefaultUser() {
        return buildDefaultUser(USER_ID, List.of(PATTERN_ID));
    }

    /**
     * Builds a simple User entity containing only an ID
     *
     * @param userId the ID to use for the user
     * @return a minimal User entity with only an ID
     */
    public static User buildSimpleUser(UUID userId) {
        User user = new User();
        user.setId(userId);
        return user;
    }

    /**
     * Builds a default File entity with all fields populated
     *
     * @param fileId    the ID to use for the file
     * @param patternId the ID of the pattern to associate with the file
     * @return a fully-populated File entity with default properties
     */
    public static File buildDefaultFile(UUID fileId, UUID patternId) {
        File file = new File();
        file.setId(fileId);
        file.setRole(FILE_ROLE);
        file.setDisplayName(FILE_DISPLAY_NAME);
        file.setStoragePath(FILE_STORAGE_PATH);
        file.setDescription(FILE_DESCRIPTION);
        file.setContentType(FILE_CONTENT_TYPE);
        file.setSizeBytes(FILE_SIZE);
        file.setChecksumSha256(FILE_CHECKSUM);
        file.setCreatedAt(CREATED_DATE);
        file.setUpdatedAt(UPDATED_DATE);

        Pattern pattern = buildSimplePattern(patternId);
        file.setPattern(pattern);
        return file;
    }

    /**
     * Builds a default File entity using a default set of IDs
     *
     * @return a fully-populated File entity with default properties
     */
    public static File buildDefaultFile() {
        return buildDefaultFile(FILE_ID, PATTERN_ID);
    }

    /**
     * Builds a simple File entity containing only an ID
     *
     * @param fileId the ID to use for the file
     * @return a minimal File entity with only an ID
     */
    public static File buildSimpleFile(UUID fileId) {
        File file = new File();
        file.setId(fileId);
        return file;
    }

    /**
     * Builds a default Pattern entity with all fields populated
     *
     * @param patternId the ID to use for the pattern
     * @param fileIds   the file IDs to associate with the pattern
     * @param userId    the ID of the user to associate with the pattern
     * @return a fully-populated Pattern entity with default properties
     */
    public static Pattern buildDefaultPattern(UUID patternId, List<UUID> fileIds, UUID userId) {
        Pattern pattern = new Pattern();
        pattern.setId(patternId);
        pattern.setName(PATTERN_NAME);
        pattern.setSource(PATTERN_SOURCE);
        pattern.setCraftType(PATTERN_CRAFT_TYPE);
        pattern.setNotes(PATTERN_NOTES);
        pattern.setCreatedAt(CREATED_DATE);
        pattern.setUpdatedAt(UPDATED_DATE);

        List<File> files = fileIds.stream().map(EntityTestData::buildSimpleFile).toList();
        pattern.setFiles(files);
        User user = buildSimpleUser(userId);
        pattern.setUser(user);
        return pattern;
    }

    /**
     * Builds a default Pattern entity using a default set of IDs
     *
     * @return a fully-populated Pattern entity with default properties
     */
    public static Pattern buildDefaultPattern() {
        return buildDefaultPattern(PATTERN_ID, List.of(FILE_ID), USER_ID);
    }

    /**
     * Builds a simple Pattern entity containing only an ID
     *
     * @param patternId the ID to use for the pattern
     * @return a minimal Pattern entity with only an ID
     */
    public static Pattern buildSimplePattern(UUID patternId) {
        Pattern pattern = new Pattern();
        pattern.setId(patternId);
        return pattern;
    }
}
