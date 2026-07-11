package uk.co.hogandhivecrafts.backend.integration;

import org.assertj.core.api.Assertions;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class FlywayIT extends AbstractIT {
    @Autowired
    Flyway flyway;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void migrationsApplied() {
        String selectFlywayHistorySuccessCount = "SELECT count(*) FROM " + "flyway_schema_history WHERE success = true";
        Integer appliedMigrations = flyway.info().applied().length;
        Integer appliedRowCount = jdbcTemplate.queryForObject(selectFlywayHistorySuccessCount, Integer.class);
        Assertions.assertThat(appliedMigrations).isGreaterThan(0);
        Assertions.assertThat(appliedRowCount).isGreaterThan(0);
        Assertions.assertThat(appliedMigrations).isEqualTo(appliedRowCount);
    }
}
