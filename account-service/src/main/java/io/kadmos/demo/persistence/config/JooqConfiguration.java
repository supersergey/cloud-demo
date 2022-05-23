package io.kadmos.demo.persistence.config;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import static java.util.Objects.requireNonNull;

@Configuration
@Profile("!test")
public class JooqConfiguration {

    private final Environment env;

    @Autowired
    public JooqConfiguration(Environment env) {
        this.env = env;
    }

    @Bean
    public DSLContext jooqDslContext() {
        return DSL.using(
            requireNonNull(env.getProperty("spring.datasource.url")),
            requireNonNull(env.getProperty("spring.datasource.username")),
            requireNonNull(env.getProperty("spring.datasource.password"))
        );
    }
}
