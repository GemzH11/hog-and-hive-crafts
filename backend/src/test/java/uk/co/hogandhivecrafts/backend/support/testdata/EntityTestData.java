package uk.co.hogandhivecrafts.backend.support.testdata;

import java.util.List;
import java.util.UUID;
import uk.co.hogandhivecrafts.backend.entity.File;
import uk.co.hogandhivecrafts.backend.entity.Pattern;
import uk.co.hogandhivecrafts.backend.model.CraftType;

/**
 * Creates deterministic entity instances for unit tests.
 */
public class EntityTestData {
  /**
   * Builds a pattern with associated files using the supplied index.
   *
   * @param index suffix used to generate distinct fixture values
   * @return the generated pattern
   */
  public static Pattern buildPattern(int index) {
    Pattern pattern = new Pattern();
    pattern.setId(UUID.fromString(String.format(TestDataConstants.PATTERN_ID_STRING, index)));
    pattern.setName(String.format(TestDataConstants.PATTERN_NAME, index));
    pattern.setSource(String.format(TestDataConstants.PATTERN_SOURCE, index));
    pattern.setCraftType(CraftType.OTHER);
    pattern.setNotes(String.format(TestDataConstants.PATTERN_NOTES, index));
    pattern.setCreatedAt(TestDataConstants.DATE);
    pattern.setUpdatedAt(TestDataConstants.DATE);

    File file1 = new File();
    File file2 = new File();
    file1.setId(UUID.fromString(String.format(TestDataConstants.FILE_ID_STRING, index)));
    file2.setId(UUID.fromString(String.format(TestDataConstants.FILE_ID_STRING, index + 1)));
    pattern.setFiles(List.of(file1, file2));

    return pattern;
  }
}
