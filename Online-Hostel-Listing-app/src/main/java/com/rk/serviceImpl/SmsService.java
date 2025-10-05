package com.rk.serviceImpl;


import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class SmsService {

	@Value("${fast2sms.api.key}")
	private String apiKey;
	
	public void sendOtpSms(String phone, String otp) {
		String url = "https://www.fast2sms.com/dev/bulkV2";
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		
		headers.set("authorization", apiKey);
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		
		body.add("route", "v3");
		body.add("sender_id", "TXTIND");
		body.add("message", "Your OTP for password reset is "+otp);
		body.add("numbers", phone);
		
		
		HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(body,headers);
		
		try {
			restTemplate.postForObject(url, request, String.class);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.print("Fast2SMS error "+e.getMessage());
		}
	}
}
