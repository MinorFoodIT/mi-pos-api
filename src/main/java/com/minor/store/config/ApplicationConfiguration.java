package com.minor.store.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app")
public class ApplicationConfiguration {

	private boolean validateDopa = true;
	private SwaggerConfiguration swagger;

	@Data
	public static class SwaggerConfiguration {
		private String contactName;
		private String contactEmail;
		private String contactUrl;
		private String title;
		private String description;
		private String version;
		private String termOfServiceUrl;
		private String license;
		private String licenseUrl;
	}

}
