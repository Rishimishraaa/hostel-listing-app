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
import com.rk.service.AdminService;
import com.rk.service.HostelService;

import lombok.Data;

@Data
@RestController
@RequestMapping("/api/admin")
public class AdminController {

	private final HostelService hostelService;
	private final AdminService adminService;

	@PostMapping("/create-hostel")
	public ResponseEntity<?> createHostel(@RequestHeader("Authorization") String jwt, @RequestBody Hostel hostel)
			throws Exception {
		HostelDTO hostel2 = adminService.createHostel(hostel, jwt);
		return ResponseEntity.ok(hostel2);
	}

	
	@PutMapping("/update-hostel")
	public ResponseEntity<?> updateHostel(@RequestHeader("Authorization") String jwt,@RequestBody HostelDTO dto)throws Exception{
		HostelDTO updateHostel = adminService.updateHostel(dto, jwt);
		return ResponseEntity.ok(updateHostel);
	}

	
	
	
	@GetMapping("/{hostelId}/fetch-hostel")
	public ResponseEntity<?> fetchHostelByOwnerId(@PathVariable Long hostelId) throws Exception {
		HostelDTO hostelsByOwnerId = adminService.getHostelsByOwnerId(hostelId);
		return ResponseEntity.ok(hostelsByOwnerId);
	}

	
	@GetMapping("/update-status/{hostelId}")
	public ResponseEntity<?> updateHostelStatus(@PathVariable Long hostelId) throws Exception{
		MessageResponse res = adminService.updateHostelStatus(hostelId);
		return ResponseEntity.ok(res);
	}

	@PostMapping("/create-roomtype/{hostelId}")
	public ResponseEntity<?> createRoomType(@RequestHeader("Authorization") String jwt, @PathVariable Long hostelId,
			@RequestBody RoomType dto) throws Exception {
		RoomTypeDTO roomTypeInHostel = adminService.createRoomTypeInHostel(dto, hostelId, jwt);
		return ResponseEntity.ok(roomTypeInHostel);
	}

	@PutMapping("/update-roomtype/{roomTypeId}")
	public ResponseEntity<?> updateRoomType(@PathVariable Long roomTypeId, @RequestBody RoomTypeDTO dto) throws Exception {
		RoomTypeDTO updateTypeRoom = adminService.updateRoomType(roomTypeId, dto);
		return ResponseEntity.ok(updateTypeRoom);
	}

	@DeleteMapping("/delete-roomtype/{roomId}")
	public ResponseEntity<?> deleteRoomType(@RequestHeader("Authorization") String jwt, @PathVariable Long roomId) throws Exception {

		MessageResponse deleteRoomType = adminService.deleteRoomType(roomId);

		return ResponseEntity.ok(deleteRoomType);
	}
	
	
	@PostMapping("/create-room")
	public ResponseEntity<?> createRoomInHostel(
		 @RequestBody RoomDTO roomDto
		) throws Exception{
		RoomDTO room = adminService.createRoom(roomDto);
		return ResponseEntity.ok(room);
	}
	
	
	
	@PostMapping("/add-user")
	public ResponseEntity<?> addStudentInHostel(@RequestHeader("Authorization") String jwt,
			@RequestBody StudentBookingRequest request
			) throws Exception{
		StudentDTO studentInHostel = adminService.addStudentInHostel(request);
		return ResponseEntity.ok(studentInHostel);
	}
	
	
	@DeleteMapping("/remove-student/{studentId}")
	public ResponseEntity<?> deleteStudentInHostel(@PathVariable Long studentId) throws Exception{
	
		MessageResponse response = adminService.removeStudentFromHostel(studentId);
		return ResponseEntity.ok(response);
	}
	
	
	@PostMapping("/create-floor")
	public ResponseEntity<?> addFloor(@RequestBody FloorDTO dto){

		FloorDTO floor = adminService.createFloor(dto);
		return ResponseEntity.ok(floor);
	}
	

	@GetMapping("/fetch-students/{hostelId}")
	public ResponseEntity<?> getAllStudentInHostel(@PathVariable Long hostelId) throws Exception{
		List<StudentDTO> students = adminService.getAllStudentInHostels(hostelId);
		return ResponseEntity.ok(students);
	}
	
	
	@GetMapping("/fetch-payment/{hostelId}")
	public ResponseEntity<?> getStudentPayementHistory(@PathVariable Long hostelId) throws Exception{
		List<PaymentDTO> paymentsForHostel = adminService.getPaymentsForHostel(hostelId);
		return ResponseEntity.ok(paymentsForHostel);
	}
	
	@PostMapping("/add-payment")
	public ResponseEntity<?> makeStudentPayment(@RequestBody AddPaymentRequest request) throws Exception{
		
		PaymentDTO addStudentPayment = adminService.AddStudentPayment(request);
		return ResponseEntity.ok(addStudentPayment);
	}
	
	 @GetMapping("/payment-status/{paymentId}")
	 public ResponseEntity<?> confermStudentPayment(@PathVariable Long paymentId) throws Exception{
		 MessageResponse confermStudentPayment = adminService.confermStudentPayment(paymentId);
		 return ResponseEntity.ok(confermStudentPayment);
	 }
}
