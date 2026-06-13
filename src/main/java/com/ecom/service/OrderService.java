package com.ecom.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ecom.entity.OrderRecord;
import com.ecom.model.CartItem;
import com.ecom.model.CheckoutForm;
import com.ecom.repository.OrderRecordRepository;

@Service
public class OrderService {

	private final OrderRecordRepository orderRecordRepository;

	public OrderService(OrderRecordRepository orderRecordRepository) {
		this.orderRecordRepository = orderRecordRepository;
	}

	public OrderRecord createOrder(String orderNumber, CheckoutForm checkoutForm, List<CartItem> items, double subtotal,
			double deliveryCharge, double tax, double grandTotal, LocalDate deliveryDate) {
		OrderRecord orderRecord = new OrderRecord();
		orderRecord.setOrderNumber(orderNumber);
		orderRecord.setCustomerName(checkoutForm.getFullName());
		orderRecord.setEmail(checkoutForm.getEmail());
		orderRecord.setPhone(checkoutForm.getPhone());
		orderRecord.setAddress(checkoutForm.getAddress());
		orderRecord.setCity(checkoutForm.getCity());
		orderRecord.setCakeMessage(checkoutForm.getMessage());
		orderRecord.setPaymentMethod(checkoutForm.getPaymentMethod());
		orderRecord.setSubtotal(subtotal);
		orderRecord.setDeliveryCharge(deliveryCharge);
		orderRecord.setTax(tax);
		orderRecord.setGrandTotal(grandTotal);
		orderRecord.setDeliveryDate(deliveryDate);
		orderRecord.setOrderItems(items.stream()
				.map(item -> item.getProduct().getName() + " x" + item.getQuantity())
				.reduce((left, right) -> left + ", " + right)
				.orElse("No items"));
		return orderRecordRepository.save(orderRecord);
	}

	public List<OrderRecord> findAllOrders() {
		return orderRecordRepository.findAllByOrderByCreatedAtDesc();
	}
}
