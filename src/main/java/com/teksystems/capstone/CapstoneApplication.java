package com.teksystems.capstone;

import com.teksystems.capstone.configuration.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(FileStorageProperties.class)
@SpringBootApplication
public class CapstoneApplication {

	public static void main(String[] args) {
		SpringApplication.run(CapstoneApplication.class, args);
	}


}
