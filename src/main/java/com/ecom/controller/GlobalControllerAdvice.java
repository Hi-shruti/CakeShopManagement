package com.ecom.controller;

import java.security.Principal;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.ecom.service.CatalogService;
import com.ecom.service.SessionCartService;

import jakarta.servlet.http.HttpSession;

@ControllerAdvice(annotations = Controller.class)
public class GlobalControllerAdvice {

	private final CatalogService catalogService;
	private final SessionCartService sessionCartService;

	public GlobalControllerAdvice(CatalogService catalogService, SessionCartService sessionCartService) {
		this.catalogService = catalogService;
		this.sessionCartService = sessionCartService;
	}

	@ModelAttribute
	public void addSharedAttributes(Model model, HttpSession session, Principal principal, Authentication authentication) {
		model.addAttribute("navCategories", catalogService.categories());
		model.addAttribute("cartCount", sessionCartService.countItems(session));
		boolean authenticated = authentication != null && authentication.isAuthenticated()
				&& !(authentication instanceof AnonymousAuthenticationToken);
		model.addAttribute("isAuthenticated", authenticated);
		model.addAttribute("currentUsername", principal != null ? principal.getName() : null);
		model.addAttribute("isAdmin", authenticated && authentication.getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN")));
	}
}
