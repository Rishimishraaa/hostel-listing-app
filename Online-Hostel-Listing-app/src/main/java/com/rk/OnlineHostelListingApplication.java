package com.rk;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.CommandLinePropertySource;

import com.rk.entity.User;
import com.rk.repository.UserRepository;
import com.stripe.Stripe;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@SpringBootApplication
public class OnlineHostelListingApplication implements CommandLineRunner{

	@Value("${stripe.api.key}")
	private String stripeSecreteKey;

	@PostConstruct
	public void init() {

		Stripe.apiKey = stripeSecreteKey;
	}
	
	
	
	
	public static void main(String[] args) {
		SpringApplication.run(OnlineHostelListingApplication.class, args);
	}
	
	private final UserRepository userRepo;

	@Override
	public void run(String... args) throws Exception {
	
	}

}
