package com.example.deukgeun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
@EnableJpaAuditing
@SpringBootApplication
public class DeukgeunApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeukgeunApplication.class, args);
	}

}
