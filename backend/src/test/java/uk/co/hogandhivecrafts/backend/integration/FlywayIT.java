package uk.co.hogandhivecrafts.backend.integration;

import org.assertj.core.api.Assertions;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;

@ActiveProfiles("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FlywayIT {

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private final String SELECT_FLYWAY_HISTORY_SUCCESS_COUNT = "SELECT count(*) FROM flyway_schema_history WHERE success = true";

    @BeforeEach
    void setUp() {
        // Ensure a fresh database state for the tests
        Flyway flyway = Flyway.configure().dataSource(dataSource).cleanDisabled(false).load();
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void migrationsApplied() {
        Integer applied = jdbcTemplate.queryForObject(SELECT_FLYWAY_HISTORY_SUCCESS_COUNT, Integer.class);
        Assertions.assertThat(applied).isGreaterThan(0);
    }
}
