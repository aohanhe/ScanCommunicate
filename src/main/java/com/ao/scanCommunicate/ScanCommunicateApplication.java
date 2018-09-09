package com.ao.scanCommunicate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.ao","ao"})
public class ScanCommunicateApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScanCommunicateApplication.class, args);
	}
}
