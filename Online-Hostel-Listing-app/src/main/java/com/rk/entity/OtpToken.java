package com.rk.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "otp_tokens")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String identifier;
	
	@Column(nullable = false)
	private String otp;
	
	@Column(nullable = false)
	private LocalDateTime expiresAt;
	
	@Column(nullable =  false)
	private boolean used = false;
	
}
