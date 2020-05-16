package org.nzvirtual.api;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
@EnableScheduling
public class ApiApplication {
	private static String version;

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);

		try {
			setVersion(new String(Files.readAllBytes(Paths.get("/app/gitinfo"))));
		} catch (IOException e) {
			setVersion("Unknown");
		}
	}

	public static String getVersion() {
		return version;
	}

	public static void setVersion(String version) {
		ApiApplication.version = version.trim();
	}

	@Bean
	public OpenAPI customOpenAPI(@Value("${app.version}") String appVersion) {
		return new OpenAPI()
				.components(new Components().addSecuritySchemes("bearerAuth", new SecurityScheme().type(SecurityScheme.Type.HTTP).bearerFormat("JWT").scheme("bearer")))
				.info(new Info()
						.title("FlyAK API")
						.version(appVersion)
						.description("This is the API for interfacing with the FlyAK Web Services")
						.license(new License().name("MIT")));
	}
}
