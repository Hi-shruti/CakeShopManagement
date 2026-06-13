package com.ecom.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecom.dto.RegistrationForm;
import com.ecom.entity.AppUser;
import com.ecom.entity.UserRole;
import com.ecom.repository.AppUserRepository;

@Service
public class UserService {

	private final AppUserRepository appUserRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
		this.appUserRepository = appUserRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public AppUser registerCustomer(RegistrationForm registrationForm) {
		AppUser appUser = new AppUser();
		appUser.setFullName(registrationForm.getFullName());
		appUser.setUsername(registrationForm.getUsername());
		appUser.setEmail(registrationForm.getEmail());
		appUser.setPhone(registrationForm.getPhone());
		appUser.setPassword(passwordEncoder.encode(registrationForm.getPassword()));
		appUser.setRole(UserRole.USER);
		return appUserRepository.save(appUser);
	}

	public boolean isUsernameTaken(String username) {
		return appUserRepository.existsByUsernameIgnoreCase(username);
	}

	public boolean isEmailTaken(String email) {
		return appUserRepository.existsByEmailIgnoreCase(email);
	}

	public List<AppUser> findAllUsers() {
		return appUserRepository.findAll();
	}

	public long countAdmins() {
		return appUserRepository.countByRole(UserRole.ADMIN);
	}
}
