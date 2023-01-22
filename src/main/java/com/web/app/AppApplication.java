package com.web.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class AppApplication {
	Environment env;
	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
		
	}

}
