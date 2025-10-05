package com.rk.service;

import com.rk.dto.BookingDTO;
import com.rk.dto.RazorpayOrderResponse;
import com.rk.entity.Payment;
import com.rk.response.PaymentResponse;

public interface PaymentService {

	public PaymentResponse createPaymentLink(Long paymentId) throws Exception;
	public Payment getPaymentById(Long id) throws Exception;
	public String handleStripeWebhook(String payload, String signHeader);
    public RazorpayOrderResponse createRozerpayOrder(Long paymentId) throws Exception;
    public boolean updateRazorpayWebhook(String payload, String sigHeader) throws Exception;
}
