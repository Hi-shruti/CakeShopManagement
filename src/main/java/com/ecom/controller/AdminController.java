package com.ecom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ecom.service.FeedbackService;
import com.ecom.service.OrderService;
import com.ecom.service.UserService;

@Controller
public class AdminController {

	private final UserService userService;
	private final FeedbackService feedbackService;
	private final OrderService orderService;

	public AdminController(UserService userService, FeedbackService feedbackService, OrderService orderService) {
		this.userService = userService;
		this.feedbackService = feedbackService;
		this.orderService = orderService;
	}

	@GetMapping("/admin")
	public String adminDashboard(Model model) {
		model.addAttribute("users", userService.findAllUsers());
		model.addAttribute("feedbackList", feedbackService.findAllFeedback());
		model.addAttribute("orders", orderService.findAllOrders());
		model.addAttribute("userCount", userService.findAllUsers().size());
		model.addAttribute("feedbackCount", feedbackService.findAllFeedback().size());
		model.addAttribute("adminCount", userService.countAdmins());
		model.addAttribute("orderCount", orderService.findAllOrders().size());
		return "admin";
	}
}
