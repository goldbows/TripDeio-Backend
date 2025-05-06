package com.tripdeio.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.tripdeio.backend.entity")
@EnableJpaRepositories(basePackages = "com.tripdeio.backend.repository")
public class MytourguideApplication {

	public static void main(String[] args) {
		SpringApplication.run(MytourguideApplication.class, args);
	}

}
