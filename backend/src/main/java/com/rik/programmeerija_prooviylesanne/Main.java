package com.rik.programmeerija_prooviylesanne;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class Main {
	@Value("${cors.allowed-origins}")
	private String[] allowedOrigins;

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry
						.addMapping("/api/**")
						.allowedOrigins(allowedOrigins)
						.allowedMethods("*");
			}
		};
	}
}
