package uk.co.hogandhivecrafts.backend.integration.support;

import uk.co.hogandhivecrafts.backend.entity.File;
import uk.co.hogandhivecrafts.backend.entity.Pattern;

public class FileITData {
    private static final String FILE_ROLE = "role%s";
    private static final String FILE_DISPLAY_NAME = "file%03d";
    private static final String FILE_STORAGE_PATH = "/tmp/file%03d.pdf";
    private static final String FILE_DESCRIPTION = "description%03d";
    private static final String FILE_CONTENT_TYPE = "type%03d";
    private static final Long FILE_SIZE = 123L;
    private static final String FILE_CHECKSUM = "abc123";

    private FileITData() {
        // prevent instantiation
    }

    /**
     * Builds a default File entity with all fields populated,
     * using the provided index to generate unique values for each field.
     *
     * @param index   the index to allow uniquely identifying the generated file properties (e.g. role1, role2, etc.)
     * @param pattern The pattern object that the file should be associated with.
     *                This allows the generated file to be linked to a specific pattern in the test data setup.
     * @return a fully-populated File entity with default properties based on the provided index and associated with the given pattern.
     */
    public static File buildDefault(int index, Pattern pattern) {
        File file = new File();
        file.setRole(String.format(FILE_ROLE, index));
        file.setDisplayName(String.format(FILE_DISPLAY_NAME, index));
        file.setStoragePath(String.format(FILE_STORAGE_PATH, index));
        file.setDescription(String.format(FILE_DESCRIPTION, index));
        file.setContentType(String.format(FILE_CONTENT_TYPE, index));
        file.setSizeBytes(FILE_SIZE);
        file.setChecksumSha256(FILE_CHECKSUM);
        file.setPattern(pattern);
        return file;

    }

    /**
     * Builds a minimal File entity with only the required fields populated,
     * using the provided index to generate unique values for each field.
     *
     * @param index   the index to allow uniquely identifying the generated file properties (e.g. role1, role2, etc.)
     * @param pattern The pattern object that the file should be associated with.
     *                This allows the generated file to be linked to a specific pattern in the test data setup.
     * @return a minimally-populated File entity with required properties based on the provided index and associated with the given pattern.
     */
    public static File buildMinimal(int index, Pattern pattern) {
        File file = new File();
        file.setRole(String.format(FILE_ROLE, index));
        file.setStoragePath(String.format(FILE_STORAGE_PATH, index));
        file.setPattern(pattern);
        return file;

    }
}
