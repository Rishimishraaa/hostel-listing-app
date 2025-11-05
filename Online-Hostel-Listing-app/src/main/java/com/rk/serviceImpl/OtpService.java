package com.rk.serviceImpl;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.rk.entity.OtpToken;
import com.rk.repository.OtpTokenRepository;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OtpService {

	private final OtpTokenRepository otpRepository;
	private final EmailService emailService;
	private final SmsService smsService;
	
	
	private final int OTP_EXPIRY_MIN = 5;
	
	private final Random random = new java.security.SecureRandom();
	
	@Transactional
	public void generateAndSendOtpToEmail(String email) throws MessagingException {
		try {
			String otp = generateOtp();
			otpRepository.deleteAllByIdentifier(email);
			
			OtpToken token = new OtpToken();
			token.setIdentifier(email);
			token.setOtp(otp);
			token.setExpiresAt(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MIN));
			token.setUsed(false);
			otpRepository.save(token);
			
			emailService.sendOtpEmail(email, otp);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	
	@Transactional
	public void generateAndSendTopToPhone(String phone) {
		String otp = generateOtp();
		otpRepository.deleteAllByIdentifier(phone);
		
		OtpToken token = new OtpToken();
		token.setIdentifier(phone);
		token.setOtp(otp);
		token.setExpiresAt(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MIN));
		token.setUsed(false);
		otpRepository.save(token);
		smsService.sendOtpSms(phone, otp);
	}
	
	private String generateOtp() {
		int n = random.nextInt(900_000)+100_000;
		return String.valueOf(n);
	}
	
	public boolean verifyOtp(String identifier, String otp) {
		return otpRepository.findByIdentifierAndOtpAndUsedFalse(identifier, otp)
				.map(t-> {
					t.setUsed(true);
					otpRepository.save(t);
					return true;
				}).orElse(false);
	}
}
