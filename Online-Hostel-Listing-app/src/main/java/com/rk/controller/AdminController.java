package com.rk.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rk.dto.BookingDTO;
import com.rk.dto.FloorDTO;
import com.rk.dto.HostelDTO;
import com.rk.dto.HostelDashboardDetailsDto;
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
import com.rk.service.AdminService;
import com.rk.service.HostelService;
import com.rk.serviceImpl.DashboardService;

import lombok.Data;

@Data
@RestController
@RequestMapping("/api/admin")
public class AdminController {

	private final HostelService hostelService;
	private final AdminService adminService;
	private final DashboardService dashboardService;

	@PostMapping("/create-hostel")
	public ResponseEntity<HostelDTO> createHostel(@RequestHeader("Authorization") String jwt, @RequestBody Hostel hostel)
			throws Exception {
		HostelDTO hostel2 = adminService.createHostel(hostel, jwt);
		return ResponseEntity.ok(hostel2);
	}

	@PutMapping("/update-hostel")
	public ResponseEntity<HostelDTO> updateHostel(@RequestHeader("Authorization") String jwt, @RequestBody HostelDTO dto)
			throws Exception {
		HostelDTO updateHostel = adminService.updateHostel(dto, jwt);
		return ResponseEntity.ok(updateHostel);
	}

	@GetMapping("/{hostelId}/fetch-hostel")
	public ResponseEntity<HostelDTO> fetchHostelByOwnerId(@PathVariable Long hostelId) throws Exception {
		HostelDTO hostelsByOwnerId = adminService.getHostelsByOwnerId(hostelId);
		return ResponseEntity.ok(hostelsByOwnerId);
	}

	@GetMapping("/update-status/{hostelId}")
	public ResponseEntity<MessageResponse> updateHostelStatus(@PathVariable Long hostelId) throws Exception {
		MessageResponse res = adminService.updateHostelStatus(hostelId);
		return ResponseEntity.ok(res);
	}

	@PostMapping("/create-roomtype/{hostelId}")
	public ResponseEntity<RoomTypeDTO> createRoomType(@RequestHeader("Authorization") String jwt, @PathVariable Long hostelId,
			@RequestBody RoomType dto) throws AppException {
		RoomTypeDTO roomTypeInHostel = adminService.createRoomTypeInHostel(dto, hostelId, jwt);
		return ResponseEntity.ok(roomTypeInHostel);
	}

	@PutMapping("/update-roomtype/{roomTypeId}")
	public ResponseEntity<RoomTypeDTO> updateRoomType(@PathVariable Long roomTypeId, @RequestBody RoomTypeDTO dto)
			throws Exception {
		RoomTypeDTO updateTypeRoom = adminService.updateRoomType(roomTypeId, dto);
		return ResponseEntity.ok(updateTypeRoom);
	}

	@DeleteMapping("/delete-roomtype/{roomId}")
	public ResponseEntity<MessageResponse> deleteRoomType(@RequestHeader("Authorization") String jwt, @PathVariable Long roomId)
			throws Exception {

		MessageResponse deleteRoomType = adminService.deleteRoomType(roomId);

		return ResponseEntity.ok(deleteRoomType);
	}

	@PostMapping("/create-room")
	public ResponseEntity<RoomDTO> createRoomInHostel(@RequestBody RoomDTO roomDto) throws AppException {
		RoomDTO room = adminService.createRoom(roomDto);
		return ResponseEntity.ok(room);
	}

	@PostMapping("/add-user")
	public ResponseEntity<StudentDTO> addStudentInHostel(@RequestHeader("Authorization") String jwt,
			@RequestBody StudentBookingRequest request) throws Exception {
		
		StudentDTO studentInHostel = adminService.addStudentInHostel(request);
		
		return ResponseEntity.ok(studentInHostel);
	}

	@DeleteMapping("/remove-student/{studentId}")
	public ResponseEntity<MessageResponse> deleteStudentInHostel(@PathVariable Long studentId) throws Exception {

		MessageResponse response = adminService.removeStudentFromHostel(studentId);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/create-floor")
	public ResponseEntity<FloorDTO> addFloor(@RequestBody FloorDTO dto) {

		FloorDTO floor = adminService.createFloor(dto);
		return ResponseEntity.ok(floor);
	}

	@GetMapping("/fetch-students/{hostelId}")
	public ResponseEntity<List<StudentDTO>> getAllStudentInHostel(@PathVariable Long hostelId) throws Exception {
		List<StudentDTO> students = adminService.getAllStudentInHostels(hostelId);
		return ResponseEntity.ok(students);
	}

	@GetMapping("/fetch-payment/{hostelId}")
	public ResponseEntity<List<PaymentDTO>> getStudentPayementHistory(@PathVariable Long hostelId) throws Exception {
		List<PaymentDTO> paymentsForHostel = adminService.getPaymentsForHostel(hostelId);
		return ResponseEntity.ok(paymentsForHostel);
	}

	@PostMapping("/add-payment")
	public ResponseEntity<PaymentDTO> makeStudentPayment(@RequestBody AddPaymentRequest request) throws Exception {

		PaymentDTO addStudentPayment = adminService.AddStudentPayment(request);
		return ResponseEntity.ok(addStudentPayment);
	}

	@GetMapping("/conferm-booking/{bookingId}")
	public ResponseEntity<?> confermStudent(@PathVariable Long bookingId) {
		return null;
	}

	@GetMapping("/asign-room/{bookingId}/{roomId}")
	public ResponseEntity<RoomDTO> asignRoomToStudent(@PathVariable Long bookingId, @PathVariable Long roomId)
			throws Exception {
		RoomDTO asignRoomToStudent = adminService.asignRoomToStudent(bookingId, roomId);
		return ResponseEntity.ok(asignRoomToStudent);
	}

	@DeleteMapping("/remove-booking-student/{bookingId}")
	public ResponseEntity<String> removeStudentFromBooking(@PathVariable Long bookingId) throws Exception {
		adminService.removeStudentBooking(bookingId);
		return ResponseEntity.ok("booking deleted successfully");
	}

	@GetMapping("/payment-status/{paymentId}")
	public ResponseEntity<MessageResponse> confermStudentPayment(@PathVariable Long paymentId) throws Exception {
		MessageResponse confermStudentPayment = adminService.confermStudentPayment(paymentId);
		return ResponseEntity.ok(confermStudentPayment);
	}

	@GetMapping("/fetch-bookings/{hostelId}")
	public ResponseEntity<List<BookingDTO>> getAllBookings(@PathVariable Long hostelId) throws Exception {
		List<BookingDTO> bookings = adminService.getAllStudentBookings(hostelId);
		return ResponseEntity.ok(bookings);

	}

	@GetMapping("/dashboard")
	public ResponseEntity<HostelDashboardDetailsDto> getDashboardData(@RequestParam String email) {
		HostelDashboardDetailsDto dashboardDetails = dashboardService.getDashboardDetails(email);
		return ResponseEntity.ok(dashboardDetails);
	}
}
