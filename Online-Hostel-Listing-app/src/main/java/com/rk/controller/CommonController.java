package com.rk.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.PutExchange;

import com.rk.dto.HostelDTO;
import com.rk.dto.UserDTO;
import com.rk.entity.Hostel;
import com.rk.entity.User;
import com.rk.repository.UserRepository;
import com.rk.request.UpdateUserProfileRequest;
import com.rk.service.HostelService;
import com.rk.service.UserService;

import lombok.Data;

@Data
@RestController
@RequestMapping("/api/common")
public class CommonController {
	
	private final HostelService hostelService;
	private final UserService userService;
	private final UserRepository userRepository;

	@GetMapping("/fetch-hostels")
	public ResponseEntity<?> getAllHostels() throws Exception{
		List<HostelDTO> allHostels = hostelService.getAllHostels();
		if(allHostels.isEmpty()) {
			throw new Exception("hostel not found");
		}
		System.out.println("Hostels hai idhar");
		return ResponseEntity.ok(allHostels);
	}
	
	@GetMapping("/user-profile")
	public ResponseEntity<?> getUser(@RequestHeader("Authorization") String jwt) throws Exception{
		 UserDTO user = userService.getUser(jwt);
		if(user==null) {
			throw new Exception("user not found first login");
		}
		return ResponseEntity.ok(user);
	}
	
	
	@PutMapping("/update-profile")
	public ResponseEntity<?> updateUserProfile(@RequestBody UpdateUserProfileRequest req){
		User user = userRepository.findById(req.getId()).orElseThrow(()-> new RuntimeException("invalid user id"));
		
		if(req.getFullName()!=null) {
			user.setFullName(req.getFullName());
		}
		
		if(req.getEmail()!=null) {
			user.setEmail(req.getEmail());
			
		}
		
		if(req.getPhone()!=null) {
			user.setPhone(req.getPhone());
		}
		
		User save = userRepository.save(user);
		return ResponseEntity.ok(save);
		
	}
	
}
