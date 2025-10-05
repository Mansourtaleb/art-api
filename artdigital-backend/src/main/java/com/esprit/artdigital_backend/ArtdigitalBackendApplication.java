package com.esprit.artdigital_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ArtdigitalBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArtdigitalBackendApplication.class, args);
		System.out.println("\n========================================");
		System.out.println("   ArtDigital Backend démarré avec succès!");
		System.out.println("   URL: http://localhost:8080");
		System.out.println("   Swagger: http://localhost:8080/swagger-ui.html");
		System.out.println("========================================\n");
	}
}