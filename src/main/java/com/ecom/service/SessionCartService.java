package com.ecom.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.ecom.model.CartItem;

import jakarta.servlet.http.HttpSession;

@Service
public class SessionCartService {

	private static final String CART_SESSION_KEY = "cart";

	private final CatalogService catalogService;

	public SessionCartService(CatalogService catalogService) {
		this.catalogService = catalogService;
	}

	public void addToCart(HttpSession session, Integer id, int quantity) {
		Map<Integer, Integer> cart = getCart(session);
		cart.merge(id, Math.max(quantity, 1), Integer::sum);
		session.setAttribute(CART_SESSION_KEY, cart);
	}

	public void updateCart(HttpSession session, Integer id, int quantity) {
		Map<Integer, Integer> cart = getCart(session);
		if (quantity <= 0) {
			cart.remove(id);
		} else {
			cart.put(id, quantity);
		}
		session.setAttribute(CART_SESSION_KEY, cart);
	}

	public int countItems(HttpSession session) {
		return getCart(session).values().stream().mapToInt(Integer::intValue).sum();
	}

	public void clearCart(HttpSession session) {
		session.removeAttribute(CART_SESSION_KEY);
	}

	public void populateCartModel(Model model, HttpSession session) {
		List<CartItem> items = buildCartItems(session);
		double subtotal = items.stream().mapToDouble(CartItem::getLineTotal).sum();
		double deliveryCharge = items.isEmpty() ? 0 : (subtotal >= 1000 ? 0 : 99);
		double tax = subtotal * 0.05;
		model.addAttribute("cartItems", items);
		model.addAttribute("subtotal", subtotal);
		model.addAttribute("deliveryCharge", deliveryCharge);
		model.addAttribute("tax", tax);
		model.addAttribute("grandTotal", subtotal + deliveryCharge + tax);
	}

	public List<CartItem> getCartItems(HttpSession session) {
		return buildCartItems(session);
	}

	@SuppressWarnings("unchecked")
	private Map<Integer, Integer> getCart(HttpSession session) {
		Object cart = session.getAttribute(CART_SESSION_KEY);
		if (cart instanceof Map<?, ?> existingCart) {
			return (Map<Integer, Integer>) existingCart;
		}
		Map<Integer, Integer> newCart = new LinkedHashMap<>();
		session.setAttribute(CART_SESSION_KEY, newCart);
		return newCart;
	}

	private List<CartItem> buildCartItems(HttpSession session) {
		List<CartItem> items = new ArrayList<>();
		for (Map.Entry<Integer, Integer> entry : getCart(session).entrySet()) {
			catalogService.findById(entry.getKey()).ifPresent(product -> items.add(new CartItem(product, entry.getValue())));
		}
		return items;
	}
}
