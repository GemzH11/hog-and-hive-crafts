package uk.co.hogandhivecrafts.backend.support.testdata;

import java.time.OffsetDateTime;
import java.util.UUID;
import uk.co.hogandhivecrafts.backend.model.CraftType;

/**
 * Shared deterministic values used to construct test fixtures.
 */
public class TestDataConstants {
  /**
   * UUID template for test users, formatted with a three-digit index.
   */
  public static final String USER_ID_STRING = "00000000-0000-0000-0000-000000000%03d";

  /**
   * Email template for test users, formatted with a three-digit index.
   */
  public static final String USER_EMAIL = "test%03d@example.com";

  /**
   * Display-name template for test users, formatted with a three-digit index.
   */
  public static final String USER_DISPLAY_NAME = "Test user%03d";

  /**
   * Avatar URL template for test users, formatted with a three-digit index.
   */
  public static final String USER_AVATAR_URL = "avatar%03d.png";

  /**
   * UUID template for test patterns, formatted with a three-digit index.
   */
  public static final String PATTERN_ID_STRING = "11111111-1111-1111-1111-111111111%03d";

  /**
   * Name template for test patterns, formatted with a three-digit index.
   */
  public static final String PATTERN_NAME = "pattern%03d";

  /**
   * Source template for test patterns, formatted with a three-digit index.
   */
  public static final String PATTERN_SOURCE = "source%03d";

  /**
   * Craft type assigned to test patterns.
   */
  public static final CraftType PATTERN_CRAFT_TYPE = CraftType.OTHER;

  /**
   * Notes template for test patterns, formatted with a three-digit index.
   */
  public static final String PATTERN_NOTES = "notes%03d";

  /**
   * Fixed timestamp used in test pattern fixtures.
   */
  public static final OffsetDateTime DATE = OffsetDateTime.parse("2025-01-01T12:00:00Z");

  /**
   * UUID template for test files, formatted with a three-digit index.
   */
  public static final String FILE_ID_STRING = "22222222-2222-2222-2222-222222222%03d";

  /**
   * Role template for test files, formatted with a three-digit index.
   */
  public static final String FILE_ROLE = "role%03d";

  /**
   * Display-name template for test files, formatted with a three-digit index.
   */
  public static final String FILE_DISPLAY_NAME = "file%03d";

  /**
   * Storage-path template for test files, formatted with a three-digit index.
   */
  public static final String FILE_STORAGE_PATH = "/tmp/file%03d.pdf";

  /**
   * Description template for test files, formatted with a three-digit index.
   */
  public static final String FILE_DESCRIPTION = "description%03d";

  /**
   * Content-type template for test files, formatted with a three-digit index.
   */
  public static final String FILE_CONTENT_TYPE = "type%03d";

  /**
   * Size in bytes assigned to test files.
   */
  public static final Long FILE_SIZE = 123L;

  /**
   * Checksum assigned to test files.
   */
  public static final String FILE_CHECKSUM = "abc123";

  /**
   * UUID used when a test needs an existing-looking but absent resource ID.
   */
  public static final UUID DEFAULT_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

  /**
   * Default first page index used by tests.
   */
  public static final int DEFAULT_PAGE_NUMBER = 0;

  /**
   * Default page size configured for the application.
   */
  public static final int DEFAULT_PAGE_SIZE = 20;
}
