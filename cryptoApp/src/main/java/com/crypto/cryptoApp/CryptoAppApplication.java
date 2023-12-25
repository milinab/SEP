package com.crypto.cryptoApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient

public class CryptoAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoAppApplication.class, args);
	}

}
