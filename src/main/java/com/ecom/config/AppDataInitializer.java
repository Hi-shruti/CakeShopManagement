package com.ecom.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ecom.entity.AppUser;
import com.ecom.entity.UserRole;
import com.ecom.repository.AppUserRepository;

@Configuration
public class AppDataInitializer {

	@Bean
	CommandLineRunner seedAdminUser(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (appUserRepository.findByUsername("admin").isEmpty()) {
				AppUser admin = new AppUser();
				admin.setFullName("Cake Haven Admin");
				admin.setUsername("admin");
				admin.setEmail("admin@cakehaven.com");
				admin.setPhone("9000011223");
				admin.setPassword(passwordEncoder.encode("Admin@123"));
				admin.setRole(UserRole.ADMIN);
				appUserRepository.save(admin);
			}
		};
	}
}
