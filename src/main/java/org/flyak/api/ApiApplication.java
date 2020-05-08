package org.flyak.api;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
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
