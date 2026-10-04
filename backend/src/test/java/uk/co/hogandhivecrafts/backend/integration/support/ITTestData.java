package uk.co.hogandhivecrafts.backend.integration.support;

import java.util.ArrayList;
import java.util.List;
import uk.co.hogandhivecrafts.backend.entity.File;
import uk.co.hogandhivecrafts.backend.entity.Pattern;
import uk.co.hogandhivecrafts.backend.entity.User;
import uk.co.hogandhivecrafts.backend.support.testdata.TestDataConstants;

/**
 * Builds deterministic entities for persistence and API integration tests.
 */
public class ITTestData {
  /**
   * Creates a user populated with test values for the supplied index.
   *
   * @param index suffix used to make the generated user values unique
   * @return a user populated with deterministic test data
   */
  public static User buildUser(int index) {
    User user = new User();
    user.setEmail(String.format(TestDataConstants.USER_EMAIL, index));
    user.setDisplayName(String.format(TestDataConstants.USER_DISPLAY_NAME, index));
    user.setAvatarUrl(String.format(TestDataConstants.USER_AVATAR_URL, index));
    return user;
  }

  /**
   * Creates a pattern associated with the supplied user.
   *
   * @param index suffix used to make the generated pattern values unique
   * @param user  owner of the generated pattern
   * @return a pattern populated with deterministic test data
   */
  public static Pattern buildPattern(int index, User user) {
    Pattern pattern = new Pattern();
    pattern.setName(String.format(TestDataConstants.PATTERN_NAME, index));
    pattern.setSource(String.format(TestDataConstants.PATTERN_SOURCE, index));
    pattern.setCraftType(TestDataConstants.PATTERN_CRAFT_TYPE);
    pattern.setNotes(String.format(TestDataConstants.PATTERN_NOTES, index));
    pattern.setUser(user);
    return pattern;
  }

  /**
   * Creates a file associated with the supplied pattern.
   *
   * @param index   suffix used to make the generated file values unique
   * @param pattern pattern associated with the generated file
   * @return a file populated with deterministic test data
   */
  public static File buildFile(int index, Pattern pattern) {
    File file = new File();
    file.setRole(String.format(TestDataConstants.FILE_ROLE, index));
    file.setDisplayName(String.format(TestDataConstants.FILE_DISPLAY_NAME, index));
    file.setStoragePath(String.format(TestDataConstants.FILE_STORAGE_PATH, index));
    file.setDescription(String.format(TestDataConstants.FILE_DESCRIPTION, index));
    file.setContentType(String.format(TestDataConstants.FILE_CONTENT_TYPE, index));
    file.setSizeBytes(TestDataConstants.FILE_SIZE);
    file.setChecksumSha256(TestDataConstants.FILE_CHECKSUM);
    file.setPattern(pattern);
    return file;
  }

  /**
   * Creates a list of patterns with sequential test values for one user.
   *
   * @param count number of patterns to create
   * @param user  owner assigned to every generated pattern
   * @return the generated patterns
   */
  public static List<Pattern> buildPatternList(int count, User user) {
    List<Pattern> patterns = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      patterns.add(buildPattern(i, user));
    }
    return patterns;
  }
}
