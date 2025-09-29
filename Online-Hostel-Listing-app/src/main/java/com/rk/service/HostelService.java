package com.rk.service;

import java.util.List;

import com.rk.dto.BookingDTO;
import com.rk.dto.FloorDTO;
import com.rk.dto.HostelDTO;
import com.rk.dto.PaymentHistoryDto;
import com.rk.dto.RoomDTO;
import com.rk.dto.RoomTypeDTO;
import com.rk.dto.StudentDTO;
import com.rk.entity.Hostel;
import com.rk.entity.RoomType;
import com.rk.request.AddPaymentRequest;
import com.rk.request.StudentBookingRequest;
import com.rk.response.MessageResponse;

public interface HostelService {
	
	public List<FloorDTO> fetchAllFloors(Long hostelId) throws Exception;
	public List<RoomDTO> fetchAllRooms(Long hostelId) throws Exception;
	public List<Hostel> getAllHostels();
	public List<RoomTypeDTO> getAllRoomTypeByHostelId(Long hostelId)throws Exception;


	
}
