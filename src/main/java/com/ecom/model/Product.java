package com.ecom.model;

public class Product {

	private final Integer id;
	private final String name;
	private final String category;
	private final String description;
	private final double price;
	private final String image;
	private final double rating;
	private final boolean eggless;
	private final boolean featured;

	public Product(Integer id, String name, String category, String description, double price, String image,
			double rating, boolean eggless, boolean featured) {
		this.id = id;
		this.name = name;
		this.category = category;
		this.description = description;
		this.price = price;
		this.image = image;
		this.rating = rating;
		this.eggless = eggless;
		this.featured = featured;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getCategory() {
		return category;
	}

	public String getDescription() {
		return description;
	}

	public double getPrice() {
		return price;
	}

	public String getImage() {
		return image;
	}

	public double getRating() {
		return rating;
	}

	public boolean isEggless() {
		return eggless;
	}

	public boolean isFeatured() {
		return featured;
	}
}
