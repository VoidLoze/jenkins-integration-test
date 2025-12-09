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
		System.out.println("Product Service запущен на порту 8081");
	}

	@GetMapping("/products")
	public List<Product> getProducts() {
		return Arrays.asList(
				new Product(1, "MacBook Pro", 1999.99),
				new Product(2, "iPhone 15", 999.99),
				new Product(3, "iPad Air", 599.99)
		);
	}

	@GetMapping("/health")
	public HealthStatus health() {
		return new HealthStatus("OK", "product-service");
	}

	@GetMapping("/products/{id}")
	public Product getProductById(@PathVariable int id) {
		List<Product> products = Arrays.asList(
				new Product(1, "MacBook Pro", 1999.99),
				new Product(2, "iPhone 15", 999.99),
				new Product(3, "iPad Air", 599.99)
		);

		return products.stream()
				.filter(p -> p.getId() == id)
				.findFirst()
				.orElse(new Product(id, "Unknown Product", 0.0));
	}

	@PostMapping("/products")
	public CreateResponse createProduct(@RequestBody ProductRequest request) {
		System.out.println("Создан новый продукт: " + request.getName());
		return new CreateResponse("Product created successfully", 201);
	}

	// Классы-модели
	static class Product {
		private int id;
		private String name;
		private double price;

		public Product(int id, String name, double price) {
			this.id = id;
			this.name = name;
			this.price = price;
		}

		public int getId() { return id; }
		public void setId(int id) { this.id = id; }

		public String getName() { return name; }
		public void setName(String name) { this.name = name; }

		public double getPrice() { return price; }
		public void setPrice(double price) { this.price = price; }
	}

	static class ProductRequest {
		private String name;
		private double price;

		public String getName() { return name; }
		public void setName(String name) { this.name = name; }

		public double getPrice() { return price; }
		public void setPrice(double price) { this.price = price; }
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

	static class CreateResponse {
		private String message;
		private int statusCode;

		public CreateResponse(String message, int statusCode) {
			this.message = message;
			this.statusCode = statusCode;
		}

		public String getMessage() { return message; }
		public void setMessage(String message) { this.message = message; }

		public int getStatusCode() { return statusCode; }
		public void setStatusCode(int statusCode) { this.statusCode = statusCode; }
	}
}