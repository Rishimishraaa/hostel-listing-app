package com.rk.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rk.dto.BookingDTO;
import com.rk.dto.FavoriteDTO;
import com.rk.dto.PaymentDTO;
import com.rk.dto.RatingDTO;
import com.rk.repository.RatingRepository;
import com.rk.request.HostelRatingRequest;
import com.rk.service.UserService;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {
	
	private final UserService userService;

	@GetMapping
	public ResponseEntity<?> index(){
		return ResponseEntity.ok("This is Student controller");
	}
	
	@PostMapping("/create-booking")
	public ResponseEntity<?> createBooking(@RequestBody BookingDTO dto) throws Exception{
		BookingDTO booking = userService.createBooking(dto);
		return ResponseEntity.ok(booking);
	}
	
	@GetMapping("/fetch-bookings")
	public ResponseEntity<?> fetchBookings(@RequestHeader("Authorization") String jwt) throws Exception{
		BookingDTO bookings = userService.getBookings(jwt);
		
		return ResponseEntity.ok(bookings);
	}

	@GetMapping("/fetch-payments")
	public ResponseEntity<?> fetchPayments(@RequestHeader("Authorization") String jwt) throws Exception{
		List<PaymentDTO> allPayments = userService.getAllPayments(jwt);
		return ResponseEntity.ok(allPayments);
	}
	
	@PostMapping("/add-rating")
	public ResponseEntity<?> giveRatingToHostel(@RequestBody HostelRatingRequest req) throws Exception{
		RatingDTO rating = userService.addRating(req);
		return ResponseEntity.ok(rating);
	}
	
	@GetMapping("/fetch-rating")
	public ResponseEntity<?> fetchRating(@RequestHeader("Authorization") String jwt) throws Exception{
		
		List<RatingDTO> fetchAllRatings = userService.fetchAllRatings(jwt);
		
		return ResponseEntity.ok(fetchAllRatings);
	}
	
	
	@GetMapping("/add-favorite/{hostelId}")
	public ResponseEntity<?> addToFavorite(@RequestHeader("Authorization") String jwt, @PathVariable Long hostelId) throws Exception{
		FavoriteDTO toFavorite = userService.addToFavorite(jwt, hostelId);
		return ResponseEntity.ok(toFavorite);
	}
	
	@GetMapping("/fetch-favorite")
	public ResponseEntity<?> fetchFavorite(@RequestHeader("Authorization") String jwt) throws Exception{
		return ResponseEntity.ok(userService.fetchAllFavorite(jwt));
	}
	
	@GetMapping("/remove-favorite/{hostelId}")
	public ResponseEntity<?> removeToFavorite(@RequestHeader("Authorization") String jwt, @PathVariable Long hostelId) throws Exception{
	
			Long id = userService.removeToFavorite(jwt, hostelId);
			return ResponseEntity.ok(id);
		
	
	
	}
}
