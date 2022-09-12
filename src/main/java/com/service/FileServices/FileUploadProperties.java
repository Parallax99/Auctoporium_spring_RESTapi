package com.service.FileServices;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "file.upload")
@Data
public class FileUploadProperties {
	private String location;
}
