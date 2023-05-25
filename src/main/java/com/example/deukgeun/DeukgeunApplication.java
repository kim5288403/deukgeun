package com.example.deukgeun;

import com.example.deukgeun.global.config.AopConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
@EnableCaching
@EnableJpaAuditing
@Import(AopConfiguration.class)
@SpringBootApplication
public class DeukgeunApplication {
	public static void main(String[] args) {
		SpringApplication.run(DeukgeunApplication.class, args);
	}

}
