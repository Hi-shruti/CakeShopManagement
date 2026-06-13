package com.ecom.controller;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecom.model.CheckoutForm;
import com.ecom.service.CatalogService;
import com.ecom.service.OrderService;
import com.ecom.service.SessionCartService;

import jakarta.servlet.http.HttpSession;

@Controller
public class StoreController {

	private final CatalogService catalogService;
	private final SessionCartService sessionCartService;
	private final OrderService orderService;

	public StoreController(CatalogService catalogService, SessionCartService sessionCartService, OrderService orderService) {
		this.catalogService = catalogService;
		this.sessionCartService = sessionCartService;
		this.orderService = orderService;
	}

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("featuredProducts", catalogService.featuredProducts());
		model.addAttribute("categories", catalogService.categories());
		model.addAttribute("categoryImages", catalogService.categoryImages());
		return "index";
	}

	@GetMapping("/products")
	public String products(@RequestParam(required = false) String category, Model model) {
		model.addAttribute("selectedCategory", category);
		model.addAttribute("products", catalogService.findByCategory(category));
		model.addAttribute("categories", catalogService.categories());
		return "products";
	}

	@PostMapping("/cart/add/{id}")
	public String addToCart(@PathVariable Integer id, @RequestParam(defaultValue = "1") int quantity, HttpSession session) {
		sessionCartService.addToCart(session, id, quantity);
		return "redirect:/cart";
	}

	@GetMapping("/cart")
	public String cart(Model model, HttpSession session) {
		sessionCartService.populateCartModel(model, session);
		return "cart";
	}

	@PostMapping("/cart/update/{id}")
	public String updateCart(@PathVariable Integer id, @RequestParam int quantity, HttpSession session) {
		sessionCartService.updateCart(session, id, quantity);
		return "redirect:/cart";
	}

	@GetMapping("/checkout")
	public String checkout(Model model, HttpSession session) {
		sessionCartService.populateCartModel(model, session);
		model.addAttribute("checkoutForm", new CheckoutForm());
		return "checkout";
	}

	@PostMapping("/checkout")
	public String placeOrder(@ModelAttribute CheckoutForm checkoutForm, Model model, HttpSession session) {
		sessionCartService.populateCartModel(model, session);
		String orderNumber = "CK" + System.currentTimeMillis();
		LocalDate deliveryDate = LocalDate.now().plusDays(1);
		model.addAttribute("orderNumber", orderNumber);
		model.addAttribute("customerName",
				checkoutForm.getFullName() == null || checkoutForm.getFullName().isBlank() ? "Customer"
						: checkoutForm.getFullName());
		model.addAttribute("paymentMethod",
				checkoutForm.getPaymentMethod() == null || checkoutForm.getPaymentMethod().isBlank() ? "Card"
						: checkoutForm.getPaymentMethod());
		model.addAttribute("deliveryDate", deliveryDate);
		model.addAttribute("orderedItems", model.getAttribute("cartItems"));
		orderService.createOrder(orderNumber, checkoutForm, sessionCartService.getCartItems(session),
				(Double) model.getAttribute("subtotal"), (Double) model.getAttribute("deliveryCharge"),
				(Double) model.getAttribute("tax"), (Double) model.getAttribute("grandTotal"), deliveryDate);
		sessionCartService.clearCart(session);
		return "order-success";
	}
}
