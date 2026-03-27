package com.example.bookVault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BookVaultApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookVaultApplication.class, args);
	}

}
