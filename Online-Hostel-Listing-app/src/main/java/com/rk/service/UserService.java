package com.rk.service;

import java.util.List;
import java.util.Optional;

import com.rk.dto.BookingDTO;
import com.rk.dto.FavoriteDTO;
import com.rk.dto.PaymentDTO;
import com.rk.dto.RatingDTO;
import com.rk.dto.UserDTO;
import com.rk.entity.User;
import com.rk.request.HostelRatingRequest;

public interface UserService {

	public UserDTO getUser(String jwt) throws Exception;
	
	public BookingDTO createBooking(BookingDTO dto) throws Exception;
	public RatingDTO addRating(HostelRatingRequest req) throws Exception;
	public BookingDTO getBookings(String jwt)throws Exception;
	public List<PaymentDTO> getAllPayments(String jwt)throws Exception;
	public List<RatingDTO> fetchAllRatings(String jwt)throws Exception;
	public List<FavoriteDTO> fetchAllFavorite(String jwt)throws Exception;
	public FavoriteDTO addToFavorite(String jwt, Long hostelId)throws Exception;
	public Long removeToFavorite(String jwt, Long hostelId)throws Exception;
	public void updatePasswordByEmail(String email, String rawPassword)throws Exception;
	public void updatePasswordByPhone(String phone, String rawPassword)throws Exception;
	public Optional<User> findByEmail(String email)throws Exception;
	public Optional<User> findByPhone(String phone)throws Exception;
	
}
