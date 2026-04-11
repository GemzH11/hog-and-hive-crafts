package uk.co.hogandhivecrafts.backend.integration;

import org.assertj.core.api.Assertions;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FlywayIT {

    @Autowired
    Flyway flyway;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private final String SELECT_FLYWAY_HISTORY_SUCCESS_COUNT = "SELECT count(*) FROM flyway_schema_history WHERE success = true";

    @Test
    void migrationsApplied() {
        Integer appliedMigrations = flyway.info().applied().length;
        Integer appliedRowCount = jdbcTemplate.queryForObject(SELECT_FLYWAY_HISTORY_SUCCESS_COUNT, Integer.class);
        Assertions.assertThat(appliedMigrations).isGreaterThan(0);
        Assertions.assertThat(appliedRowCount).isGreaterThan(0);
        Assertions.assertThat(appliedMigrations).isEqualTo(appliedRowCount);
    }
}
