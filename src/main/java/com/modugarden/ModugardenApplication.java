package com.modugarden;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ModugardenApplication {

	public static void main(String[] args) {
		SpringApplication.run(ModugardenApplication.class, args);
	}

}
