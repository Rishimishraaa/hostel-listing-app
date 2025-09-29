package com.rk.serviceImpl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.rk.config.JwtService;
import com.rk.dto.FloorDTO;
import com.rk.dto.RoomDTO;
import com.rk.dto.RoomTypeDTO;
import com.rk.dto.RoomTypeImageDTO;
import com.rk.dto.StudentDTO;
import com.rk.entity.Booking;
import com.rk.entity.Hostel;
import com.rk.entity.Room;
import com.rk.entity.RoomType;
import com.rk.entity.User;
import com.rk.repository.BookingRepository;
import com.rk.repository.HostelRepository;
import com.rk.repository.PaymentRepository;
import com.rk.repository.RoomRepository;
import com.rk.repository.RoomTypeRepository;
import com.rk.repository.UserRepository;
import com.rk.service.HostelService;

import jakarta.transaction.Transactional;
import lombok.Data;

@Service
@Data
public class HostelServiceImpl implements HostelService {

	private final UserRepository userRepository;
	private final HostelRepository hostelRepository;
	private final JwtService jwtService;
	private final RoomTypeRepository roomTypeRepository;
	private final RoomRepository roomRepository;
	private final BookingRepository bookingRepository;
	private final PaymentRepository paymentRepository;

	@Override
	public List<Hostel> getAllHostels() {

		return hostelRepository.findAll();
	}

	@Override
	@Transactional
	public List<FloorDTO> fetchAllFloors(Long hostelId) {
	    Hostel hostel = hostelRepository.findById(hostelId)
	        .orElseThrow(() -> new RuntimeException("invalid hostel id or hostel not found"));

	    
	  return  hostel.getFloors().stream().map(floor->{
	    	
	    	List<RoomDTO> roomDtos = floor.getRooms().stream().map(room->{
	    		
	    		List<StudentDTO> studentDto = room.getBookings().stream().map(Booking::getStudent).filter(Objects::nonNull).map(std->{
	    			return StudentDTO.builder()
	    			.id(std.getId() !=null ? std.getId() : null)
	    			.currentRoomNumber(room.getRoomNumber())
	    			.fullName(std.getFullName())
	    			.email(std.getEmail())
	    			.hostelId(floor.getHostel()!=null ? floor.getHostel().getId():null)
	    			.phone(std.getPhone())
	    			.paymentStatus(std.getPaymentStatus())
	    			.joiningDate(std.getJoiningDate()!=null ? std.getJoiningDate().toString():null)
	    			.lastPaymentDate(std.getLastPaymentDate() !=null ? std.getLastPaymentDate().toString():null)
	    			.build();
	    		}).filter(Objects::nonNull).toList();
	    		
	    	return RoomDTO.builder()
	    		.id(room.getId())
	    		.capacity(room.getCapacity())
	    		.floorId(floor.getId())
	    		.floorNumber(floor.getFloorNumber())
	    		.roomNumber(room.getRoomNumber())
	    		.occupants(room.getOccupacy())
	    		.roomTypeId(room.getRoomType()!=null? room.getRoomType().getId(): null)
	    		.students(studentDto)
	    		.build();
	    	}).filter(Objects::nonNull).toList();
	    	
	    	
	    	
	    	return FloorDTO.builder()
	    			.id(floor.getId())
	    			.floorNumber(floor.getFloorNumber())
	    			.rooms(roomDtos)
	    			.hostelId(hostelId)
	    			.build();
	    	
	    }).toList();
	    
	    
	     
	     
	     
	     
	}

	@Override
	public List<RoomDTO> fetchAllRooms(Long hostelId) throws Exception {
		List<Room> rooms = roomRepository.findAllRoomsByHostelId(hostelId);

		List<RoomDTO> roomsDto = rooms.stream().map(r -> {
			return RoomDTO.builder().id(r.getId()).floorId(r.getFloor() != null ? r.getFloor().getId() : null)
					.capacity(r.getCapacity()).floorNumber(r.getFloorNumber()).occupants(r.getOccupacy())
					.roomNumber(r.getRoomNumber()).build();
		}).toList();
		return roomsDto;
	}

	@Override
	public List<RoomTypeDTO> getAllRoomTypeByHostelId(Long hostelId) throws Exception {
		List<RoomType> roomTypes = roomTypeRepository.findByHostelId(hostelId);

		return roomTypes.stream().map(rt -> {

			List<RoomTypeImageDTO> imgDtos = rt.getImages().stream().map(img -> {
				return RoomTypeImageDTO.builder().id(img.getId()).imageUrl(img.getImageUrl()).roomTypeId(rt.getId())
						.build();
			}).toList();

			return RoomTypeDTO.builder().id(rt.getId()).hostelId(rt.getHostel() != null ? rt.getHostel().getId() : null)
					.images(imgDtos).isAvailable(rt.isAvailable()).pricePerMonth(rt.getPricePerMonth())
					.sharingType(rt.getSharingType()).build();
		}).toList();

	}

}
