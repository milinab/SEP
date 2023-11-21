package com.psp.PSPBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PspBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PspBackendApplication.class, args);
	}

}
