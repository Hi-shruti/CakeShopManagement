package com.ecom.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegistrationForm {

	@NotBlank(message = "Please fill in all details to register.")
	private String fullName;

	@NotBlank(message = "Please fill in all details to register.")
	private String username;

	@NotBlank(message = "Please fill in all details to register.")
	@Email(message = "Enter a valid email address.")
	private String email;

	@NotBlank(message = "Please fill in all details to register.")
	private String phone;

	@NotBlank(message = "Please fill in all details to register.")
	@Size(min = 6, message = "Password must be at least 6 characters.")
	private String password;

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
