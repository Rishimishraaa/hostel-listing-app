package com.rk.dto;

import java.util.List;

import com.rk.entity.Gender;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostelDTO {
	  private Long id;
	    private String name;
	    private boolean status;
	    private String isApproved;
	    private String description;
	    private AddressDTO address;
	    private ContactInformationDTO contactInfo;
	    private FacilitiesDTO facilities;
	    private PoliciesDTO policies;
	    private FoodInfoDTO foodInfo;
	    private Gender gender;
	    private UserDTO owner;
	    private Long ownerId;
	    private List<RatingDTO> ratings;
	    private List<FloorDTO> floors;
	    private List<HostelImageDTO> images;
	    private List<RoomTypeDTO> roomTypes;
}
