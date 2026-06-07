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

    public static Pattern buildDefault(int index, User user) {
        Pattern pattern = new Pattern();
        pattern.setName(String.format(PATTERN_NAME, index));
        pattern.setSource(String.format(PATTERN_SOURCE, index));
        pattern.setCraftType(String.format(PATTERN_CRAFT_TYPE, index));
        pattern.setNotes(String.format(PATTERN_NOTES, index));
        pattern.setUser(user);
        return pattern;
    }

    public static Pattern buildMinimal(int index, User user) {
        Pattern pattern = new Pattern();
        pattern.setName(String.format(PATTERN_NAME, index));
        pattern.setUser(user);
        return pattern;
    }

    public static List<Pattern> buildList(int count, User user) {
        List<Pattern> patterns = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            patterns.add(buildMinimal(i, user));
        }
        return patterns;
    }
}
