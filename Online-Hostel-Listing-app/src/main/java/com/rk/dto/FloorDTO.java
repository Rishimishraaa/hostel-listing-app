package com.rk.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FloorDTO {

	private Long id;
	private int floorNumber;
	 private List<RoomDTO> rooms;
	 private Long hostelId;
}
