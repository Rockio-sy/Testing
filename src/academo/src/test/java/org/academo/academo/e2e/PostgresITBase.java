package org.academo.academo.e2e;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers(disabledWithoutDocker = true)
public abstract class PostgresITBase {

    protected static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withReuse(true)
                    .withDatabaseName("academo_it")
                    .withUsername("test")
                    .withPassword("test");

    static {
        POSTGRES.start(); // start before properties are read
    }

    @DynamicPropertySource
    static void registerProps(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        r.add("spring.datasource.username", POSTGRES::getUsername);
        r.add("spring.datasource.password", POSTGRES::getPassword);
        r.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
    }
}
