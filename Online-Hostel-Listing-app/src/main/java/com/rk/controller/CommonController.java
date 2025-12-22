package com.rk.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rk.dto.HostelDTO;
import com.rk.dto.UserDTO;
import com.rk.entity.Gender;
import com.rk.entity.Notification;
import com.rk.entity.User;
import com.rk.exception.AppException;
import com.rk.repository.UserRepository;
import com.rk.request.UpdateUserProfileRequest;
import com.rk.service.HostelService;
import com.rk.service.UserService;
import com.rk.serviceImpl.NotificationService;

import lombok.Data;

@Data
@RestController
@RequestMapping("/api/common")
public class CommonController {
	
	private final HostelService hostelService;
	private final UserService userService;
	private final UserRepository userRepository;
	private final NotificationService notificationService;

	@GetMapping("/fetch-hostels")
	public ResponseEntity<?> getHostels(
	        @RequestParam(required = false) String city,
	        @RequestParam(required = false) Integer sharingType,
	        @RequestParam(required = false) String gender,
	        @RequestParam(required = false) Integer minRent,
	        @RequestParam(required = false) Integer maxRent,
	        @RequestParam(required = false) Integer rating,
	        @RequestParam(required = false) List<String> facilities,
	        @RequestParam(defaultValue = "pricePerMonth") String sortBy,
	        @RequestParam(defaultValue = "asc") String sortDir
	        ) throws Exception {

		Map<String,Object> filters = new HashMap<>();
		filters.put("city", city);
		filters.put("sharingType", sharingType);
		filters.put("gender", gender);
		filters.put("minRent", minRent);
		filters.put("maxRent", maxRent);
		filters.put("rating", rating);
		filters.put("facilities", facilities);
		filters.put("sortBy", sortBy);
		filters.put("sortDir", sortDir);
		List<HostelDTO> filteredHostels = hostelService.getFilteredHostels(filters);
	    return ResponseEntity.ok(filteredHostels);
	}

	
	@GetMapping("/user-profile")
	public ResponseEntity<UserDTO> getUser(@RequestHeader("Authorization") String jwt) throws Exception{
		 UserDTO user = userService.getUser(jwt);
		if(user==null) {
			throw new Exception("user not found first login");
		}
		return ResponseEntity.ok(user);
	}
	
	
	@PutMapping("/update-profile")
	public ResponseEntity<UserDTO> updateUserProfile(@RequestBody UpdateUserProfileRequest req){
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
		
		if(req.getImageUrl() !=null) {
			user.setImageUrl(req.getImageUrl());
		}
		
		
		if(req.getIdUrl()!=null) {
			user.setIdUrl(req.getIdUrl());
		}
		
		if(req.getGender()!=null) {
			user.setGender(Gender.valueOf(req.getGender()));
		}
		
		
		
		User save = userRepository.save(user);
		
		UserDTO dto = UserDTO.builder().id(save.getId()).email(save.getEmail())
				.imageUrl(save.getImageUrl()!=null ? save.getImageUrl() : null)
				.idUrl(save.getIdUrl()!=null ? save.getIdUrl() : null)
				.gender(save.getGender().name())
		.hostelId(save.getHostels() != null ? save.getHostels().getId() : null).fullName(save.getFullName())
		.phone(save.getPhone()).role(save.getRole()).build();
		
		
		return ResponseEntity.ok(dto);
		
	}
	
	
	@GetMapping("/fetch-notification")
	public ResponseEntity<List<Notification>> getNotifications(@RequestParam String email) throws Exception{ 
		Optional<User> byEmail = userService.findByEmail(email);
		
		if(byEmail.isEmpty()) throw new AppException("invalid email");
		
		User user = byEmail.get();
		
		List<Notification> notifications = notificationService.getNotificationsForUser(user);
		
		return ResponseEntity.ok(notifications);
		
	}
	
	
	@PatchMapping("/{id}/read")
	public ResponseEntity<Notification> markAsRead(@PathVariable Long id)throws AppException{
		Notification markAsRead = notificationService.markAsRead(id);
		return ResponseEntity.ok(markAsRead);
	}
	
	
	@PatchMapping("/read-all")
	public ResponseEntity<List<Notification>>  markAllAsRead(@RequestParam String email){
		List<Notification> markAllAsRead = notificationService.markAllAsRead(email);
		return ResponseEntity.ok(markAllAsRead);
	}
	
	
}
