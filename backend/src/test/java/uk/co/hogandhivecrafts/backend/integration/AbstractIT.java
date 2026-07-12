package uk.co.hogandhivecrafts.backend.integration;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.postgresql.PostgreSQLContainer;

/**
 * Base class for integration tests.
 *
 * <p>Uses the Testcontainers Singleton Container Pattern: the PostgreSQL container is started once
 * in a static initialiser and shared across all integration test classes in the same JVM run.
 * This ensures that Spring's application context cache always sees the same JDBC URL, preventing
 * connection failures that would otherwise occur when the container is restarted between test
 * classes and Spring reuses a cached context that points to the old (now stopped) container port.
 *
 * <p>The container is cleaned up automatically by the Testcontainers Ryuk resource reaper when the
 * JVM exits, so there is no need to annotate it with {@code @Container} or the class with
 * {@code @Testcontainers}.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractIT {
    static final PostgreSQLContainer pg = new PostgreSQLContainer("postgres:18-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpassword");

    static {
        pg.start();
    }

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", pg::getJdbcUrl);
        r.add("spring.datasource.username", pg::getUsername);
        r.add("spring.datasource.password", pg::getPassword);
    }
}
