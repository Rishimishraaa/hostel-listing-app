package com.rk;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.stripe.Stripe;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@SpringBootApplication
@EnableScheduling
public class OnlineHostelListingApplication implements CommandLineRunner{

	@Value("${stripe.api.key}")
	private String stripeSecreteKey;

	@PostConstruct
	public void init() {

		Stripe.apiKey = stripeSecreteKey;
	}
	
	
	public static void main(String[] args) {
		
		/*	   // Load .env file from project root
		Dotenv dotenv = Dotenv.configure()
		                       .ignoreIfMissing()   // agar .env na ho to ignore kare
		                       .load();
		
		// Set System properties so Spring Boot can pick them in application.properties
		System.setProperty("SPRING_APP_NAME", dotenv.get("SPRING_APP_NAME", "Online-Hostel-Listing-app"));
		System.setProperty("DB_URL", dotenv.get("DB_URL"));
		System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
		System.setProperty("DB_DRIVER", dotenv.get("DB_DRIVER"));
		System.setProperty("SERVER_PORT", dotenv.get("SERVER_PORT", "8081"));
		System.setProperty("SERVER_ADDRESS", dotenv.get("SERVER_ADDRESS", "0.0.0.0"));
		
		System.setProperty("MAIL_HOST", dotenv.get("MAIL_HOST"));
		System.setProperty("MAIL_PORT", dotenv.get("MAIL_PORT"));
		System.setProperty("MAIL_USERNAME", dotenv.get("MAIL_USERNAME"));
		System.setProperty("MAIL_PASSWORD", dotenv.get("MAIL_PASSWORD"));
		
		System.setProperty("STRIPE_API_KEY", dotenv.get("STRIPE_API_KEY"));
		System.setProperty("STRIPE_WEBHOOK_KEY", dotenv.get("STRIPE_WEBHOOK_KEY"));
		
		System.setProperty("RAZORPAY_KEY_ID", dotenv.get("RAZORPAY_KEY_ID"));
		System.setProperty("RAZORPAY_KEY_SECRET", dotenv.get("RAZORPAY_KEY_SECRET"));
		System.setProperty("RAZORPAY_WEBHOOK_SECRET", dotenv.get("RAZORPAY_WEBHOOK_SECRET"));
		
		System.setProperty("FAST2SMS_API_KEY", dotenv.get("FAST2SMS_API_KEY"));
		*/
		SpringApplication.run(OnlineHostelListingApplication.class, args);
	}

	
	@Override
	public void run(String... args) throws Exception {
		System.out.println("h");
		
	}
	

}
