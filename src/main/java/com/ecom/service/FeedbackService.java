package com.ecom.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecom.dto.FeedbackForm;
import com.ecom.entity.Feedback;
import com.ecom.repository.FeedbackRepository;

@Service
public class FeedbackService {

	private final FeedbackRepository feedbackRepository;

	public FeedbackService(FeedbackRepository feedbackRepository) {
		this.feedbackRepository = feedbackRepository;
	}

	public Feedback saveFeedback(FeedbackForm feedbackForm) {
		Feedback feedback = new Feedback();
		feedback.setName(feedbackForm.getName() == null || feedbackForm.getName().isBlank() ? "Anonymous"
				: feedbackForm.getName());
		feedback.setEmail(feedbackForm.getEmail() == null || feedbackForm.getEmail().isBlank() ? "Not provided"
				: feedbackForm.getEmail());
		feedback.setRating(feedbackForm.getRating());
		feedback.setCategory(feedbackForm.getCategory());
		feedback.setMessage(feedbackForm.getMessage());
		return feedbackRepository.save(feedback);
	}

	public List<Feedback> findAllFeedback() {
		return feedbackRepository.findAllByOrderByCreatedAtDesc();
	}
}
