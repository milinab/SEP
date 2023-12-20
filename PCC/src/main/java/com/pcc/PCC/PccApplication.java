package com.pcc.PCC;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PccApplication {

	public static void main(String[] args) {
		SpringApplication.run(PccApplication.class, args);
	}

}
