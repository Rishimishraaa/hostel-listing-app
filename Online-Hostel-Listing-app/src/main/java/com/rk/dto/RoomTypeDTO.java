package com.rk.dto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomTypeDTO {
	 private Long id;
	    private Integer sharingType;
	    private Double pricePerMonth;
	    private boolean isAvailable;
	    private Long hostelId;
	    private List<RoomTypeImageDTO> images;
	    private List<RoomDTO> rooms;
}
