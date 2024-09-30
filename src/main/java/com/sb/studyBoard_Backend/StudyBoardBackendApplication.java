package com.sb.studyBoard_Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class StudyBoardBackendApplication {

	public static void main(String[] args) {
	/*	Dotenv dotenv = Dotenv.load();
		System.setProperty("DATABASE_URL", dotenv.get("DATABASE_URL"));
		System.setProperty("DATABASE_USERNAME", dotenv.get("DATABASE_USERNAME"));
		System.setProperty("DATABASE_PASSWORD", dotenv.get("DATABASE_PASSWORD"));
		System.setProperty("GITHUB_CLIENTID", dotenv.get("GITHUB_CLIENTID"));
		System.setProperty("GITHUB_CLIENT-SECRET", dotenv.get("GITHUB_CLIENT-SECRET"));*/
		SpringApplication.run(StudyBoardBackendApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry
						.addMapping("/auth/github/callback")
						.allowedOrigins("http://localhost:4001/");
			}
		};
	}

}
