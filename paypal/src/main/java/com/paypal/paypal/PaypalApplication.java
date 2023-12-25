package com.paypal.paypal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PaypalApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaypalApplication.class, args);
	}

}
