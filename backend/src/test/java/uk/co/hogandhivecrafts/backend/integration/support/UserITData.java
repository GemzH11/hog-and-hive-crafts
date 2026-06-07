package uk.co.hogandhivecrafts.backend.integration.support;

import uk.co.hogandhivecrafts.backend.entity.User;

public class UserITData {
    private static final String USER_EMAIL = "test%s@example.com";
    private static final String USER_DISPLAY_NAME = "Test user%s";
    private static final String USER_AVATAR_URL = "avatar%s.png";

    private UserITData() {
        // prevent instantiation
    }

    public static User buildDefault(int index) {
        User user = new User();
        user.setEmail(String.format(USER_EMAIL, index));
        user.setDisplayName(String.format(USER_DISPLAY_NAME, index));
        user.setAvatarUrl(String.format(USER_AVATAR_URL, index));
        return user;
    }

    public static User buildMinimal(int index) {
        return new User();
    }
}
