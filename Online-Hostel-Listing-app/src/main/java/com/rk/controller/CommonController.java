package com.rk.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rk.dto.UserDTO;
import com.rk.entity.Hostel;
import com.rk.entity.User;
import com.rk.service.HostelService;
import com.rk.service.UserService;

import lombok.Data;

@Data
@RestController
@RequestMapping("/api/common")
public class CommonController {
	
	private final HostelService hostelService;
	private final UserService userService;

	@GetMapping("/getAll-hostels")
	public ResponseEntity<?> getAllHostels() throws Exception{
		List<Hostel> allHostels = hostelService.getAllHostels();
		
		if(allHostels.isEmpty()) {
			throw new Exception("hostel not found");
		}
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
	
	
}
