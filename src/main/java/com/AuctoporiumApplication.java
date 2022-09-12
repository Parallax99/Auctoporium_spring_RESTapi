package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.service.FileServices.FileUploadProperties;

@SpringBootApplication
@EnableConfigurationProperties({ FileUploadProperties.class })
public class AuctoporiumApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuctoporiumApplication.class, args);
	}

}
