package com.dnsotware.appfinanceiro_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AppfinanceiroApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppfinanceiroApiApplication.class, args);
	}

}
