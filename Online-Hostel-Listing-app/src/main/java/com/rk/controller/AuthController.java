package com.rk.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rk.config.CustomUserDetailsService;
import com.rk.config.JwtService;
import com.rk.entity.Role;
import com.rk.entity.User;
import com.rk.exception.AppException;
import com.rk.repository.UserRepository;
import com.rk.request.OtpResponse;
import com.rk.request.RequestOtp;
import com.rk.request.UserLoginRequest;
import com.rk.request.UserRegisterRequest;
import com.rk.request.VerifyOtpRequest;
import com.rk.response.AuthResponse;
import com.rk.service.UserService;
import com.rk.serviceImpl.OtpService;

import lombok.Data;

@Data
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final UserRepository userRepo;
	private final JwtService jwtService;
	private final CustomUserDetailsService userDetailsService;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final OtpService otpService;
	private final UserService userService;

	@GetMapping
	public ResponseEntity<?> welcome() {
		return ResponseEntity.ok("welcome my website : ");
	}

	@PostMapping("/signup")
	public ResponseEntity<?> register(@RequestBody UserRegisterRequest request) throws Exception {

		Optional<User> byEmail = userRepo.findByEmail(request.getEmail());
		Optional<User> byPhone = userRepo.findByPhone(request.getPhone());

		if (byEmail.isPresent()) {
			throw new AppException("email already exists!");
		}

		if (byPhone.isPresent())
			throw new AppException("mobile no already exists");

		User user = User.builder().fullName(request.getFullName()).isActive(true)

				.email(request.getEmail()).phone(request.getPhone()).role(request.getRole())
				.password(passwordEncoder.encode(request.getPassword()))
				.imageUrl(request.getImageUrl() != null ? request.getIdUrl() : null)
				.idUrl(request.getIdUrl() != null ? request.getIdUrl() : null).ratingsGiven(new ArrayList<>())
				.reviews(new ArrayList<>()).favorites(new ArrayList<>()).build();

		User save = userRepo.save(user);
		return ResponseEntity.ok("register successfully");
	}

	@PostMapping("signin")
	public ResponseEntity<?> login(@RequestBody UserLoginRequest request) throws Exception {
		AuthResponse response = new AuthResponse();
		try {

			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();

			Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

			String role = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();

			String jwt = jwtService.generateToken(userDetails);

			response.setJwt(jwt);
			response.setRole(Role.valueOf(role));
			response.setMessage("Login Success");
			response.setUsername(userDetails.getUsername());

		} catch (Exception e) {

			throw new BadCredentialsException("invalid credentials");
		}

		return new ResponseEntity<AuthResponse>(response, HttpStatus.OK);
	}

	@PostMapping("/request-otp")
	public ResponseEntity<?> requestOtp(@RequestBody RequestOtp dto) throws Exception {
		if ((dto.getEmail() == null || dto.getEmail().isBlank())
				&& (dto.getPhone() == null || dto.getPhone().isBlank())) {
			return ResponseEntity.badRequest().body(new OtpResponse(false, "Provide email or phone"));

		}

		if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
			if (userService.findByEmail(dto.getEmail()).isEmpty()) {
				return ResponseEntity.ok(new OtpResponse(true, "If account exists, OTP has been send"));
			}
			otpService.generateAndSendOtpToEmail(dto.getEmail());
		}

		if (dto.getPhone() != null && !dto.getPhone().isBlank()) {
			if (userService.findByPhone(dto.getPhone()).isEmpty()) {
				return ResponseEntity.ok(new OtpResponse(true, "If account exists, OTP has been send"));
			}

			otpService.generateAndSendTopToPhone(dto.getPhone());
		}
		return ResponseEntity.ok(new OtpResponse(true, "If account exists, OTP has been sent"));
	}

	@PostMapping("/verify-otp")
	public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpRequest dto) throws Exception {

		if (dto.getIdentifier() == null || dto.getOtp() == null || dto.getNewPassword() == null) {
			return ResponseEntity.badRequest().body(new OtpResponse(false, "Missing fields"));
		}

		boolean ok = otpService.verifyOtp(dto.getIdentifier(), dto.getOtp());

		if (!ok) {
			return ResponseEntity.badRequest().body(new OtpResponse(false, "Invalid or expired OTP"));
		}

		if (dto.getIdentifier().contains("@")) {
			userService.updatePasswordByEmail(dto.getIdentifier(), dto.getNewPassword());
		} else {
			userService.updatePasswordByPhone(dto.getIdentifier(), dto.getNewPassword());
		}

		return ResponseEntity.ok(new OtpResponse(true, "Password updated successfully!"));
	}
}
