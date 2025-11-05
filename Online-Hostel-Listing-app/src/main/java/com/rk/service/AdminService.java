package com.rk.service;
import java.util.List;

import com.rk.dto.BookingDTO;
import com.rk.dto.FloorDTO;
import com.rk.dto.HostelDTO;
import com.rk.dto.PaymentDTO;
import com.rk.dto.RoomDTO;
import com.rk.dto.RoomTypeDTO;
import com.rk.dto.StudentDTO;
import com.rk.entity.Hostel;
import com.rk.entity.RoomType;
import com.rk.exception.AppException;
import com.rk.request.AddPaymentRequest;
import com.rk.request.StudentBookingRequest;
import com.rk.response.MessageResponse;

public interface AdminService {

	HostelDTO getHostelsByOwnerId(Long hostelId) throws AppException;
	public HostelDTO createHostel(Hostel hostel, String jwt)throws AppException;
	public HostelDTO updateHostel(HostelDTO dto, String jwt) throws AppException;
	public MessageResponse updateHostelStatus(Long hostelId) throws AppException;
	public FloorDTO createFloor(FloorDTO floor);
	public RoomTypeDTO createRoomTypeInHostel(RoomType room,Long hostelId, String jwt) throws AppException;
	public RoomTypeDTO updateRoomType(Long roomId, RoomTypeDTO room)throws AppException;
	public MessageResponse deleteRoomType(Long roomId) throws AppException;
	public RoomDTO createRoom(RoomDTO dto)throws AppException;
	public StudentDTO addStudentInHostel(StudentBookingRequest request) throws AppException;	
	public MessageResponse removeStudentFromHostel(Long userId)throws AppException;
	public List<StudentDTO> getAllStudentInHostels(Long hostelId) throws AppException;
	public List<PaymentDTO> getPaymentsForHostel(Long hostelId) throws AppException;
	public MessageResponse confermStudentPayment(Long paymentId)throws AppException;
	public PaymentDTO AddStudentPayment(AddPaymentRequest request)throws AppException;
	public List<BookingDTO> getAllStudentBookings(Long hostelId)throws AppException;
	public void confermStudentBooking(Long bookingId)throws AppException;
	public void removeStudentBooking(Long bookingId)throws AppException;
	public RoomDTO asignRoomToStudent(Long bookingId, Long roomId)throws AppException;

}
