package com.primaryBank.PrimaryBank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PrimaryBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrimaryBankApplication.class, args);
	}

}
