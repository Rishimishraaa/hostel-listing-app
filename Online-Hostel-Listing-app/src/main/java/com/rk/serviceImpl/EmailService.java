package com.rk.serviceImpl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

	private final JavaMailSender mailSender;
	
	
	public void sendOtpEmail(String to, String otp) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(to);
		msg.setSubject("Password Reset OTP");
		msg.setText("Your OTP for password reset is : "+otp+"\n It is valid for 5 minites.");
		mailSender.send(msg);
	}
}
