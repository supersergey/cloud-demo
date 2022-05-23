package io.kadmos.demo;

import io.kadmos.demo.configuration.ServiceProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableJdbcRepositories
@EnableConfigurationProperties(ServiceProperties.class)
public class AccountServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(AccountServiceApplication.class, args);
	}
	
}
