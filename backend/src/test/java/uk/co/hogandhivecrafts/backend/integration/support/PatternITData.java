package uk.co.hogandhivecrafts.backend.integration.support;

import uk.co.hogandhivecrafts.backend.entity.Pattern;
import uk.co.hogandhivecrafts.backend.entity.User;

import java.util.ArrayList;
import java.util.List;

public class PatternITData {
    private static final String PATTERN_NAME = "pattern%s";
    private static final String PATTERN_SOURCE = "source%s";
    private static final String PATTERN_CRAFT_TYPE = "craft%s";
    private static final String PATTERN_NOTES = "notes%s";

    private PatternITData() {
        // prevent instantiation
    }

    /**
     * Builds a default Pattern entity with all fields populated,
     * using the provided index to generate unique values for each field.
     *
     * @param index the index to allow uniquely identifying the generated pattern properties (e.g. source1,
     *              source2, etc.)
     * @param user  the user that the pattern should be associated with.
     * @return a fully populated Pattern entity with unique values based on the provided index.
     */
    public static Pattern buildDefault(int index, User user) {
        Pattern pattern = new Pattern();
        pattern.setName(String.format(PATTERN_NAME, index));
        pattern.setSource(String.format(PATTERN_SOURCE, index));
        pattern.setCraftType(String.format(PATTERN_CRAFT_TYPE, index));
        pattern.setNotes(String.format(PATTERN_NOTES, index));
        pattern.setUser(user);
        return pattern;
    }

    /**
     * Builds a minimal Pattern entity with only the required fields populated,
     * using the provided index to generate unique values for each field.
     *
     * @param index the index to allow uniquely identifying the generated pattern properties (e.g. source1, source2, etc.)
     * @param user  The user object that the pattern should be associated with.
     *              This allows the generated pattern to be linked to a specific user in the test data setup.
     * @return a minimally-populated Pattern entity with required properties based on the provided index and associated with the given user.
     */
    public static Pattern buildMinimal(int index, User user) {
        Pattern pattern = new Pattern();
        pattern.setName(String.format(PATTERN_NAME, index));
        pattern.setUser(user);
        return pattern;
    }

    /**
     * Builds a list of minimal Pattern entities with only the required fields populated,
     * using the provided index to generate unique values for each field.
     *
     * @param count the number of pattern entities to create.
     * @param user  The user object that the patterns should be associated with.
     * @return a list of minimally-populated Pattern entities with required properties based on the provided index and associated with the given user.
     */
    public static List<Pattern> buildList(int count, User user) {
        List<Pattern> patterns = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            patterns.add(buildMinimal(i, user));
        }
        return patterns;
    }
}
