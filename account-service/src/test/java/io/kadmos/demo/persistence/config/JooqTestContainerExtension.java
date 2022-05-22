package io.kadmos.demo.persistence.config;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static io.kadmos.demo.persistence.config.JooqTestConfiguration.postgreSQLContainer;

public class JooqTestContainerExtension implements BeforeAllCallback, AfterAllCallback {
    @Override
    public void beforeAll(ExtensionContext context) {
        postgreSQLContainer.start();
        var flyway = Flyway.configure().dataSource(
            postgreSQLContainer.getJdbcUrl(),
            postgreSQLContainer.getUsername(),
            postgreSQLContainer.getPassword()
        )
            .schemas("main")
            .createSchemas(true).load();
        flyway.clean();
        flyway.migrate();
    }

    @Override
    public void afterAll(ExtensionContext context) {
        postgreSQLContainer.stop();
    }
}
