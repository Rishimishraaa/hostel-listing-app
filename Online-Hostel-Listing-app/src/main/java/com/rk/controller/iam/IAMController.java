package com.rk.controller.iam;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rk.dto.HostelDTO;
import com.rk.entity.Hostel;
import com.rk.repository.HostelRepository;
import com.rk.serviceImpl.HostelServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/iam")
@RequiredArgsConstructor
public class IAMController {

	private final HostelServiceImpl hostelService;
	private final HostelRepository hostelRepository;
	

	
	@GetMapping("/fetch-hostels")
	public ResponseEntity<List<HostelDTO>> getAllHostels(){
		List<Hostel> all = hostelRepository.findAll();
		List<HostelDTO> list = all.stream().map(x->hostelService.convertHostelToDto(x)).toList();
		return  new ResponseEntity<List<HostelDTO>>(list, HttpStatus.OK);
	}
	
	@GetMapping("/approve-req/{hostelId}")
	public ResponseEntity<Long> approvedHostel(@PathVariable Long hostelId){
		Hostel hostel = hostelRepository.findById(hostelId).orElseThrow(()-> new RuntimeException("hostel not found..."));
		hostel.setIsApproved("APPROVED");
		hostelRepository.save(hostel);
		return ResponseEntity.ok(hostelId);
	}
	
	
	@GetMapping("/reject-req/{hostelId}")
	public ResponseEntity<Long> rejectHostel(@PathVariable Long hostelId){
		Hostel hostel = hostelRepository.findById(hostelId).orElseThrow(()-> new RuntimeException("hostel not found..."));
		hostel.setIsApproved("REJECTED");
		hostelRepository.save(hostel);
		return ResponseEntity.ok(hostelId);
	}
	
	
	@GetMapping("/delete-req/{hostelId}")
	public ResponseEntity<Long> deleteHostel(@PathVariable Long hostelId){
		Hostel hostel = hostelRepository.findById(hostelId).orElseThrow(()-> new RuntimeException("hostel not found..."));
		hostelRepository.delete(hostel);
		return ResponseEntity.ok(hostelId);
	}
	
	
}
