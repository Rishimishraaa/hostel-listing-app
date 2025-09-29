package com.rk.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rk.repository.UserRepository;

import lombok.Data;

@Service
@Data
public class CustomUserDetailsService implements UserDetailsService{
	
	
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return new CustomUserDetails(userRepository.findByEmail(username).get());
	}

}
