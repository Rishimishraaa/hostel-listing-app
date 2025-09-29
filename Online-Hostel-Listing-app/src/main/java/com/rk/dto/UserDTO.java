package com.rk.dto;


import java.util.List;

import com.rk.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	  private Long id;
	    private String fullName;
	    private String email;
	    private String phone;
	    private Role role;
	    
	    private List<ReviewDTO> reviews;
	    private List<FavoriteDTO> favorites;
	    private List<BookingDTO> bookings;
	    private Long hostelId;
	    private List<RatingDTO> ratings;
}
