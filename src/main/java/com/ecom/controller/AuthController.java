package com.ecom.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecom.dto.RegistrationForm;
import com.ecom.service.UserService;

import jakarta.validation.Valid;

@Controller
public class AuthController {

	private final UserService userService;

	public AuthController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/login")
	public String login(@RequestParam(required = false) Boolean error, @RequestParam(required = false) Boolean logout,
			@RequestParam(required = false) Boolean registered, @RequestParam(required = false) Boolean expired,
			@RequestParam(required = false) Boolean denied,
			Authentication authentication, Model model) {
		if (authentication != null && authentication.isAuthenticated()
				&& !(authentication instanceof AnonymousAuthenticationToken)) {
			boolean isAdmin = authentication.getAuthorities().stream()
					.anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
			return "redirect:" + (isAdmin ? "/admin" : "/");
		}
		model.addAttribute("showLoginError", Boolean.TRUE.equals(error));
		model.addAttribute("showRegistrationSuccess", Boolean.TRUE.equals(registered));
		model.addAttribute("showLogoutSuccess", Boolean.TRUE.equals(logout));
		model.addAttribute("showSessionExpired", Boolean.TRUE.equals(expired));
		model.addAttribute("showAccessDenied", Boolean.TRUE.equals(denied));
		return "login";
	}

	@GetMapping("/register")
	public String register(Model model) {
		if (!model.containsAttribute("registrationForm")) {
			model.addAttribute("registrationForm", new RegistrationForm());
		}
		return "register";
	}

	@PostMapping("/register")
	public String registerUser(@Valid @ModelAttribute("registrationForm") RegistrationForm registrationForm,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		boolean duplicateEntry = false;
		if (registrationForm.getUsername() != null && !registrationForm.getUsername().isBlank()
				&& userService.isUsernameTaken(registrationForm.getUsername())) {
			bindingResult.rejectValue("username", "duplicate.username", "This email/username is already registered.");
			duplicateEntry = true;
		}
		if (registrationForm.getEmail() != null && !registrationForm.getEmail().isBlank()
				&& userService.isEmailTaken(registrationForm.getEmail())) {
			bindingResult.rejectValue("email", "duplicate.email", "This email/username is already registered.");
			duplicateEntry = true;
		}
		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute(
					"org.springframework.validation.BindingResult.registrationForm", bindingResult);
			redirectAttributes.addFlashAttribute("registrationForm", registrationForm);
			redirectAttributes.addFlashAttribute("showRegisterError", true);
			redirectAttributes.addFlashAttribute("registerDuplicate", duplicateEntry);
			return "redirect:/register";
		}

		userService.registerCustomer(registrationForm);
		return "redirect:/login?registered=true";
	}

	@GetMapping("/logout-confirm")
	public String logoutConfirm() {
		return "logout";
	}

	@GetMapping("/forgot-password")
	public String forgotPassword() {
		return "forgot-password";
	}

	@PostMapping("/forgot-password")
	public String forgotPasswordRequest() {
		return "redirect:/email-sent";
	}

	@GetMapping("/email-sent")
	public String emailSent() {
		return "email-sent";
	}

}
