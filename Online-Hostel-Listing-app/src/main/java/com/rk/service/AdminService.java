package com.rk.service;
import java.util.List;

import com.rk.dto.FloorDTO;
import com.rk.dto.HostelDTO;
import com.rk.dto.PaymentDTO;
import com.rk.dto.PaymentHistoryDto;
import com.rk.dto.RoomDTO;
import com.rk.dto.RoomTypeDTO;
import com.rk.dto.StudentDTO;
import com.rk.entity.Hostel;
import com.rk.entity.RoomType;
import com.rk.request.AddPaymentRequest;
import com.rk.request.StudentBookingRequest;
import com.rk.response.MessageResponse;

public interface AdminService {

	HostelDTO getHostelsByOwnerId(Long hostelId) throws Exception;
	public HostelDTO createHostel(Hostel hostel, String jwt)throws Exception;
	public HostelDTO updateHostel(HostelDTO dto, String jwt) throws Exception;
	public MessageResponse updateHostelStatus(Long hostelId) throws Exception;
	public FloorDTO createFloor(FloorDTO floor);
	public RoomTypeDTO createRoomTypeInHostel(RoomType room,Long hostelId, String jwt) throws Exception;
	public RoomTypeDTO updateRoomType(Long roomId, RoomTypeDTO room)throws Exception;
	public MessageResponse deleteRoomType(Long roomId) throws Exception;
	public RoomDTO createRoom(RoomDTO dto)throws Exception;
	public StudentDTO addStudentInHostel(StudentBookingRequest request) throws Exception;	
	public MessageResponse removeStudentFromHostel(Long userId)throws Exception;
	public List<StudentDTO> getAllStudentInHostels(Long hostelId) throws Exception;
	public List<PaymentDTO> getPaymentsForHostel(Long hostelId) throws Exception;
	public MessageResponse confermStudentPayment(Long paymentId)throws Exception;
	public PaymentDTO AddStudentPayment(AddPaymentRequest request) throws Exception;

}
