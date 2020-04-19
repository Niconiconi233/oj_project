package com.yw.ojproject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication
public class OjprojectApplication {
	private static final Logger log = LoggerFactory.getLogger(OjprojectApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(OjprojectApplication.class, args);
	}
}
