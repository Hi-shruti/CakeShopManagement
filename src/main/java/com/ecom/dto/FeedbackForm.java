package com.ecom.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class FeedbackForm {

	private String name;

	@Email(message = "Enter a valid email.")
	private String email;

	@NotNull(message = "Please select a rating.")
	private Integer rating;

	@NotBlank(message = "Please choose a category.")
	@Pattern(regexp = "Bug|Suggestion|Complaint|Praise", message = "Please choose a valid category.")
	private String category;

	@NotBlank(message = "Feedback message is required.")
	@Size(min = 10, max = 1000, message = "Message should be between 10 and 1000 characters.")
	private String message;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
