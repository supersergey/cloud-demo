package io.kadmos.demo.persistence.config;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

import static java.util.Objects.requireNonNull;

@Configuration
@Profile("!test")
public class JooqConfiguration {
    @Bean
    public DSLContext jooqDslContext(DataSource datasource) {
        return DSL.using(datasource, SQLDialect.POSTGRES);
    }
}
