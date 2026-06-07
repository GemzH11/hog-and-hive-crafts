package uk.co.hogandhivecrafts.backend.integration.support;

import uk.co.hogandhivecrafts.backend.entity.File;
import uk.co.hogandhivecrafts.backend.entity.Pattern;

public class FileITData {
    private static final String FILE_ROLE = "role%s";
    private static final String FILE_DISPLAY_NAME = "file%s";
    private static final String FILE_STORAGE_PATH = "/tmp/file%s.pdf";
    private static final String FILE_DESCRIPTION = "description%s";
    private static final String FILE_CONTENT_TYPE = "type%s";
    private static final Long FILE_SIZE = 123L;
    private static final String FILE_CHECKSUM = "abc123";

    private FileITData() {
        // prevent instantiation
    }

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

    public static File buildMinimal(int index, Pattern pattern) {
        File file = new File();
        file.setRole(String.format(FILE_ROLE, index));
        file.setStoragePath(String.format(FILE_STORAGE_PATH, index));
        file.setPattern(pattern);
        return file;

    }
}
