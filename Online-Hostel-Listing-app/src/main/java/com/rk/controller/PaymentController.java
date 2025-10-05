package com.rk.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rk.dto.RazorpayOrderResponse;
import com.rk.response.PaymentResponse;
import com.rk.service.PaymentService;

import lombok.Data;

@RestController
@RequestMapping("/api/payment")
@Data
public class PaymentController {
	
	private final PaymentService paymentService;
	
	
	@GetMapping
	public ResponseEntity<?> check(){
		return ResponseEntity.ok("hello dued");
	}
	
	@PostMapping("/create-checkout-session/{paymentId}")
	public ResponseEntity<?> createCheckoutSession(@PathVariable Long paymentId) throws Exception{
		System.out.println("paymentId is : "+paymentId);
		PaymentResponse paymentLink = paymentService.createPaymentLink(paymentId);
		return ResponseEntity.ok(paymentLink);
	}

	
	@PostMapping("/webhook")
	public ResponseEntity<String> stripeWebhook(
	        @RequestBody String payload, 
	        @RequestHeader("Stripe-Signature") String sigHeader) {
	    try {
	        String handleStripeWebhook = paymentService.handleStripeWebhook(payload, sigHeader);
	        System.out.println(handleStripeWebhook);

	        return ResponseEntity.ok("success");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("failure");
	    }
	}
	
	
	
	@PostMapping("/create-order/{paymentId}")
	public ResponseEntity<?> createRazorpayOrder(@PathVariable Long paymentId) throws Exception{
		RazorpayOrderResponse rozerpayOrder = paymentService.createRozerpayOrder(paymentId);
		return ResponseEntity.ok(rozerpayOrder);
	}
	
	
	@PostMapping("/r-webhook")
	public ResponseEntity<?> handleRazorpayWebhook(@RequestBody String payload, @RequestHeader("X-Razorpay-Signature") String signatur) throws Exception{
	
			boolean success = paymentService.updateRazorpayWebhook(payload, signatur);
			
			if(success) {
				return ResponseEntity.ok("success");
			}
			return ResponseEntity.status(500).body("failure");
	}
	
}
