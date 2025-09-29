package com.rk.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rk.config.CustomUserDetailsService;
import com.rk.config.JwtService;
import com.rk.entity.Hostel;
import com.rk.entity.Role;
import com.rk.entity.User;
import com.rk.repository.UserRepository;
import com.rk.request.UserLoginRequest;
import com.rk.request.UserRegisterRequest;
import com.rk.response.AuthResponse;

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
	

	@GetMapping
	public ResponseEntity<?> welcome(){
		return ResponseEntity.ok("welcome my website : ");
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> register(@RequestBody UserRegisterRequest request) throws Exception{
		
		Optional<User> byEmail = userRepo.findByEmail(request.getEmail());
		
		if(byEmail.isPresent()) {
		   throw new Exception("email already exists!");
		}
		
		User user = User.builder().fullName(request.getFullName())
				.isActive(true)
					  .email(request.getEmail())
					  .phone(request.getPhone())
					  .role(request.getRole())
					  .password(passwordEncoder.encode(request.getPassword()))
					  .ratingsGiven(new ArrayList<>())
					 .reviews(new ArrayList<>())
					 .favorites(new ArrayList<>())
					  .build();
		
		User save = userRepo.save(user);
		save.setPassword(null);
		return ResponseEntity.ok("register successfully");
	}
	
	
	@PostMapping("signin")
	public ResponseEntity<?> login(@RequestBody UserLoginRequest request){
		AuthResponse response = new AuthResponse();
		try {
			
			Authentication authentication = authenticationManager
					.authenticate(
					new UsernamePasswordAuthenticationToken(
							request.getEmail(),request.getPassword()));
			UserDetails userDetails =(UserDetails) authentication.getPrincipal();
			
			Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
			
			String role = authorities.isEmpty()?null : authorities.iterator().next().getAuthority();
			
			String jwt = jwtService.generateToken(userDetails);
			
			
	
			response.setJwt(jwt);
			response.setRole(Role.valueOf(role));
			response.setMessage("Login Success");
			response.setUsername(userDetails.getUsername());

		} catch (Exception e) {
	
			throw new BadCredentialsException("invalid credentials");
		}
		
		return new ResponseEntity<AuthResponse>(response,HttpStatus.OK);
	}
}
