package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@RestController
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
		System.out.println("User Service запущен на порту 8080");
	}

	@GetMapping("/users")
	public List<User> getUsers() {
		return Arrays.asList(
				new User(1, "Alice"),
				new User(2, "Bob")
		);
	}

	@GetMapping("/health")
	public HealthStatus health() {
		return new HealthStatus("OK", "user-service");
	}

	@GetMapping("/users/{id}")
	public User getUserById(@PathVariable int id) {
		return new User(id, "User " + id);
	}

	// Классы-модели
	static class User {
		private int id;
		private String name;

		public User(int id, String name) {
			this.id = id;
			this.name = name;
		}

		public int getId() { return id; }
		public void setId(int id) { this.id = id; }

		public String getName() { return name; }
		public void setName(String name) { this.name = name; }
	}

	static class HealthStatus {
		private String status;
		private String service;

		public HealthStatus(String status, String service) {
			this.status = status;
			this.service = service;
		}

		public String getStatus() { return status; }
		public void setStatus(String status) { this.status = status; }

		public String getService() { return service; }
		public void setService(String service) { this.service = service; }
	}
}
