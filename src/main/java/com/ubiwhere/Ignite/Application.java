package com.ubiwhere.Ignite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Application {

	public static final String INSTANCE_ID = System.getenv().getOrDefault("HOSTNAME", "instance1");

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
