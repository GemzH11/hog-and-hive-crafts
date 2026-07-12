package uk.co.hogandhivecrafts.backend.integration.support;

import uk.co.hogandhivecrafts.backend.entity.User;

public class UserITData {
  private static final String USER_EMAIL = "test%03d@example.com";
  private static final String USER_DISPLAY_NAME = "Test user%03d";
  private static final String USER_AVATAR_URL = "avatar%03d.png";

  private UserITData() {
    // prevent instantiation
  }

  /**
   * Builds a default User entity with all fields populated, using the provided index to generate
   * unique values for each field.
   *
   * @param index the index to allow uniquely identifying the generated user properties (e.g. user1,
   *              user2, etc.)
   * @return a fully-populated User entity with default properties based on the provided index.
   */
  public static User buildDefault(int index) {
    User user = new User();
    user.setEmail(String.format(USER_EMAIL, index));
    user.setDisplayName(String.format(USER_DISPLAY_NAME, index));
    user.setAvatarUrl(String.format(USER_AVATAR_URL, index));
    return user;
  }

  /**
   * Builds a minimal User entity with only the required fields populated, using the provided index
   * to generate unique values for each field.
   *
   * @param index the index to allow uniquely identifying the generated user properties (e.g. user1,
   *              user2, etc.)
   * @return a minimally-populated User entity with required properties based on the provided index.
   */
  public static User buildMinimal(int index) {
    User user = new User();
    user.setDisplayName(String.format(USER_DISPLAY_NAME, index));
    return user;
  }
}
