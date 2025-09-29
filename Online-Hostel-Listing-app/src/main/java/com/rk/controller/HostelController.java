package com.rk.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rk.dto.FloorDTO;
import com.rk.dto.RoomTypeDTO;
import com.rk.service.HostelService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/hostels")
@RestController
public class HostelController {

	private final HostelService hostelService;
	
	@GetMapping("/fetch-roomtype/{hostelId}")
	public ResponseEntity<?> getRoomTypeByHostelId(
			@PathVariable Long hostelId) throws Exception {
		List<RoomTypeDTO> roomType = hostelService.getAllRoomTypeByHostelId(hostelId);
		return ResponseEntity.ok(roomType);
	}
	
	
	@GetMapping("/fetch-floors/{hostelId}")
	public ResponseEntity<?> getFloorsByHostelId(@PathVariable Long hostelId) throws Exception{
		List<FloorDTO> fetchAllFloors = hostelService.fetchAllFloors(hostelId);
	return	ResponseEntity.ok(fetchAllFloors);
	}
	

	
}
