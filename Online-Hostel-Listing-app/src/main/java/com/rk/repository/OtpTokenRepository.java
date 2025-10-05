package com.rk.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rk.entity.OtpToken;

public interface OtpTokenRepository extends JpaRepository<OtpToken, Long>{
	   Optional<OtpToken> findByIdentifierAndOtpAndUsedFalse(String identifier, String otp);
	    void deleteAllByIdentifier(String identifier);
}
