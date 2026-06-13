package com.ecom.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.ecom.model.Product;

@Service
public class CatalogService {

	private final List<Product> products = List.of(
			new Product(1, "Chocolate Truffle Cake", "Chocolate", "Rich chocolate sponge with silky ganache layers.",
					699, "/img/category/cocolate.jpeg", 4.9, false, true),
			new Product(2, "Strawberry Celebration Cake", "Birthday",
					"Fresh cream, strawberry glaze, and a party-ready finish.", 749, "/img/category/strawberry.jpeg", 4.8, true,
					true),
			new Product(3, "Vanilla Bloom Cake", "Anniversary", "Soft vanilla sponge with floral whipped frosting.",
					599, "/img/category/vanilla.jpeg", 4.7, true, false),
			new Product(4, "Butterscotch Crunch Cake", "Best Seller",
					"Crunchy praline layers with caramel cream and premium nuts.", 799, "/img/category/butters.jpeg", 4.9, false,
					true),
			new Product(5, "Red Velvet Heart Cake", "Romance", "Moist red velvet base finished with cream cheese swirls.",
					899, "/img/category/redvelvet.jpeg", 4.8, true, false),
			new Product(6, "Custom Photo Cake", "Custom", "Personalized cake design for birthdays, events, and surprises.",
					1199, "/img/category/wedding.jpeg", 4.6, true, true));

	public List<Product> findAll() {
		return products;
	}

	public List<Product> featuredProducts() {
		return products.stream().filter(Product::isFeatured).toList();
	}

	public Set<String> categories() {
		return products.stream().map(Product::getCategory).collect(java.util.stream.Collectors.toCollection(java.util.LinkedHashSet::new));
	}

	public Map<String, String> categoryImages() {
		return Map.of(
				"Chocolate", "/img/category/cocolate.jpeg",
				"Birthday", "/img/category/strawberry.jpeg",
				"Anniversary", "/img/category/vanilla.jpeg",
				"Best Seller", "/img/category/butters.jpeg",
				"Romance", "/img/category/redvelvet.jpeg",
				"Custom", "/img/category/wedding.jpeg");
	}

	public Optional<Product> findById(Integer id) {
		return products.stream().filter(product -> product.getId().equals(id)).findFirst();
	}

	public List<Product> findByCategory(String category) {
		if (category == null || category.isBlank()) {
			return products;
		}
		return products.stream().filter(product -> product.getCategory().equalsIgnoreCase(category)).toList();
	}
}
