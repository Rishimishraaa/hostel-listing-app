package com.rk.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.rk.entity.Hostel;
import com.rk.entity.Room;
import com.rk.entity.RoomType;
import com.rk.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
	  private Long id;
	    private LocalDateTime startDate;
	    private LocalDateTime endDate;
	    private String status;
	    private Double totalAmount;
	    private Long studentId;
	    private Long hostelId;
	    private Long roomId;
	    private Long roomTypeId;
	    private HostelDTO hostel;
	    private RoomTypeDTO roomType;
	    private RoomDTO room;
	    private UserDTO student;
	    private List<PaymentDTO> payments;
}
