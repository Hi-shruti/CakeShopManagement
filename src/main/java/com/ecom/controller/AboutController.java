package com.ecom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecom.dto.FeedbackForm;
import com.ecom.service.FeedbackService;

import jakarta.validation.Valid;

@Controller
public class AboutController {

	private final FeedbackService feedbackService;

	public AboutController(FeedbackService feedbackService) {
		this.feedbackService = feedbackService;
	}

	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("feedbackList", feedbackService.findAllFeedback());
		return "about";
	}

	@GetMapping("/feedback-page")
	public String feedbackPage(Model model) {
		if (!model.containsAttribute("feedbackForm")) {
			model.addAttribute("feedbackForm", new FeedbackForm());
		}
		model.addAttribute("feedbackList", feedbackService.findAllFeedback());
		return "feedback";
	}

	@PostMapping("/feedback")
	public String submitFeedback(@Valid @ModelAttribute("feedbackForm") FeedbackForm feedbackForm,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute(
					"org.springframework.validation.BindingResult.feedbackForm", bindingResult);
			redirectAttributes.addFlashAttribute("feedbackForm", feedbackForm);
			redirectAttributes.addFlashAttribute("feedbackError", true);
			return "redirect:/feedback-page";
		}
		feedbackService.saveFeedback(feedbackForm);
		redirectAttributes.addFlashAttribute("feedbackSuccess", true);
		return "redirect:/feedback-page";
	}
}
