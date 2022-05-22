package io.kadmos.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableJdbcRepositories
public class KadmosApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(KadmosApplication.class, args);
	}
	
}
