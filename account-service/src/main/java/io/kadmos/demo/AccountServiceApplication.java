package io.kadmos.demo;

import io.kadmos.demo.configuration.ServiceProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableConfigurationProperties(ServiceProperties.class)
public class AccountServiceApplication {

    public static void main(final String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }
}
