package uk.co.hogandhivecrafts.backend.integration.support;

import uk.co.hogandhivecrafts.backend.entity.Pattern;
import uk.co.hogandhivecrafts.backend.entity.User;
import uk.co.hogandhivecrafts.backend.model.CraftType;

import java.util.ArrayList;
import java.util.List;

public class PatternITData {
    private static final String PATTERN_NAME = "pattern%03d";
    private static final String PATTERN_SOURCE = "source%03d";
    private static final CraftType PATTERN_CRAFT_TYPE = CraftType.OTHER;
    private static final String PATTERN_NOTES = "notes%03d";

    private PatternITData() {
        // prevent instantiation
    }

    /**
     * Builds a default {@link Pattern} entity with all fields populated using the provided index to generate
     * unique values for the name, source, and notes fields.
     *
     * @param index the index used to generate unique pattern properties
     * @param user  the user that the pattern should be associated with
     * @return a fully populated {@link Pattern} entity with unique values based on the provided index
     */
    public static Pattern buildDefault(int index, User user) {
        Pattern pattern = new Pattern();
        pattern.setName(String.format(PATTERN_NAME, index));
        pattern.setSource(String.format(PATTERN_SOURCE, index));
        pattern.setCraftType(PATTERN_CRAFT_TYPE);
        pattern.setNotes(String.format(PATTERN_NOTES, index));
        pattern.setUser(user);
        return pattern;
    }

    /**
     * Builds a minimal {@link Pattern} entity with only the required fields populated.
     *
     * @param index the index used to generate a unique pattern name
     * @param user  the user object that the pattern should be associated with
     * @return a minimally populated {@link Pattern} entity with the required properties set
     */
    public static Pattern buildMinimal(int index, User user) {
        Pattern pattern = new Pattern();
        pattern.setName(String.format(PATTERN_NAME, index));
        pattern.setCraftType(PATTERN_CRAFT_TYPE);
        pattern.setUser(user);
        return pattern;
    }

    /**
     * Builds a list of minimal {@link Pattern} entities with sequential indexes starting at {@code 0}.
     *
     * @param count the number of pattern entities to create
     * @param user  the user object that the patterns should be associated with
     * @return a list of minimally populated {@link Pattern} entities
     */
    public static List<Pattern> buildList(int count, User user) {
        List<Pattern> patterns = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            patterns.add(buildMinimal(i, user));
        }
        return patterns;
    }
}
