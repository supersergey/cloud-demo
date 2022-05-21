package io.kadmos.demo.persistence;

import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jdbc.core.dialect.JdbcPostgresDialect;
import org.springframework.data.relational.core.dialect.Dialect;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

@TestConfiguration
public class JooqTestConfiguration {

    final static JdbcDatabaseContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:14.2")
        .withDatabaseName("kadmosdemo")
        .withUsername("postgres")
        .withPassword("postgres");

    @Bean
    @Primary
    public DataSource dataSource() {
        return DataSourceBuilder.create()
            .url(postgreSQLContainer.getJdbcUrl())
            .username(postgreSQLContainer.getUsername())
            .password(postgreSQLContainer.getPassword())
            .driverClassName("org.postgresql.Driver")
            .build();
    }

    @Bean
    @Primary
    public DSLContext jooqTestDsl(DataSource dataSource) {
        if (postgreSQLContainer.isRunning()) {
            return DSL.using(dataSource, SQLDialect.POSTGRES);
        } else {
            throw new RuntimeException("JDBC test container is not started");
        }
    }

    @Bean
    @Primary
    public Dialect dialect() {
        return JdbcPostgresDialect.INSTANCE;
    }
}
