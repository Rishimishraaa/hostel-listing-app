package com.rk.serviceImpl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.rk.config.JwtService;
import com.rk.dto.UserDTO;
import com.rk.entity.User;
import com.rk.repository.UserRepository;
import com.rk.service.UserService;

import lombok.Data;

@Service
@Data
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final JwtService jwtService;

	@Override
	public UserDTO getUser(String jwt) throws Exception{
		jwt = jwt.substring(7);
		String username = jwtService.extractUsername(jwt);
		User user = userRepository.findByEmail(username).orElseThrow(()->new RuntimeException("user not found"));
		System.out.println("hello");
		return UserDTO.builder()
				.id(user.getId())
				.email(user.getEmail())
				.hostelId(user.getHostels()!=null? user.getHostels().getId():null)
				.fullName(user.getFullName())
				.phone(user.getPhone())
				.role(user.getRole())
				.build();

	}
}
