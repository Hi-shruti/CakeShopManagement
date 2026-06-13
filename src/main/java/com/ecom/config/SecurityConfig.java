package com.ecom.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.ecom.service.CustomUserDetailsService;

@Configuration
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http, DaoAuthenticationProvider authenticationProvider)
			throws Exception {
		http.authenticationProvider(authenticationProvider)
				.authorizeHttpRequests(authorize -> authorize.requestMatchers("/css/**", "/js/**", "/img/**",
						"/h2-console/**", "/", "/products", "/about", "/register", "/login", "/feedback",
						"/forgot-password", "/email-sent", "/cart/**", "/checkout/**", "/logout-confirm")
						.permitAll().requestMatchers("/admin/**", "/admin").hasRole("ADMIN").anyRequest().authenticated())
				.formLogin(form -> form.loginPage("/login").loginProcessingUrl("/login")
						.successHandler(this::handleLoginSuccess).failureUrl("/login?error=true").permitAll())
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login?logout=true")
						.invalidateHttpSession(true).clearAuthentication(true).deleteCookies("JSESSIONID"))
				.sessionManagement(session -> session.invalidSessionUrl("/login?expired=true"))
				.exceptionHandling(exception -> exception
						.accessDeniedHandler((request, response, accessDeniedException) -> response.sendRedirect("/login?denied=true")))
				.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
				.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

		return http.build();
	}

	private void handleLoginSuccess(jakarta.servlet.http.HttpServletRequest request,
			jakarta.servlet.http.HttpServletResponse response, Authentication authentication) throws java.io.IOException {
		boolean isAdmin = authentication.getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
		response.sendRedirect(isAdmin ? "/admin" : "/");
	}

	@Bean
	DaoAuthenticationProvider authenticationProvider(CustomUserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder);
		return provider;
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
