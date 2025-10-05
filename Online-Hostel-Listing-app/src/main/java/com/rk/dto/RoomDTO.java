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
public class RoomDTO {
	private Long id;
	private String roomNumber; // e.g., "101", "102"
	private int floorNumber;
	private int capacity; // total allowed occupants
	  private int occupants; // current number of bookings
	private Long floorId;
	private Long roomTypeId;
	private List<StudentDTO> students;
	private Long bookingId;


}
