package uk.co.hogandhivecrafts.backend.support.testdata;

import uk.co.hogandhivecrafts.backend.entity.File;
import uk.co.hogandhivecrafts.backend.entity.Pattern;
import uk.co.hogandhivecrafts.backend.entity.User;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class EntityTestData {
    private static final OffsetDateTime CREATED_DATE = OffsetDateTime.parse("2020-01-01T01:00:00Z");
    private static final OffsetDateTime UPDATED_DATE = OffsetDateTime.parse("2020-01-01T02:00:00Z");

    private static final UUID PATTERN_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    private static final String PATTERN_NAME = "Test pattern";
    private static final String PATTERN_SOURCE = "web";
    private static final String PATTERN_CRAFT_TYPE = "craft";
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

    public static User buildDefaultUser(UUID UserId, List<UUID> patternIds) {
        User user = new User();
        user.setId(UserId);
        user.setEmail(USER_EMAIL);
        user.setDisplayName(USER_DISPLAY_NAME);
        user.setAvatarUrl(USER_AVATAR_URL);
        user.setCreatedAt(CREATED_DATE);
        user.setUpdatedAt(UPDATED_DATE);

        List<Pattern> patterns = patternIds.stream().map(EntityTestData::buildSimplePattern).toList();
        user.setPatterns(patterns);
        return user;
    }

    public static User buildDefaultUser() {
        return buildDefaultUser(USER_ID, List.of(PATTERN_ID));
    }

    public static User buildSimpleUser(UUID userId) {
        User user = new User();
        user.setId(userId);
        return user;
    }

    public static User buildSimpleUser() {
        return buildSimpleUser(USER_ID);
    }

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

    public static File buildDefaultFile() {
        return buildDefaultFile(FILE_ID, PATTERN_ID);
    }

    public static File buildSimpleFile(UUID fileId) {
        File file = new File();
        file.setId(fileId);
        return file;
    }

    public static File buildSimpleFile() {
        return buildSimpleFile(FILE_ID);
    }

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

    public static Pattern buildDefaultPattern() {
        return buildDefaultPattern(PATTERN_ID, List.of(FILE_ID), USER_ID);
    }

    public static Pattern buildSimplePattern(UUID patternId) {
        Pattern pattern = new Pattern();
        pattern.setId(patternId);
        return pattern;
    }

    public static Pattern buildSimplePattern() {
        return buildSimplePattern(PATTERN_ID);
    }
}
