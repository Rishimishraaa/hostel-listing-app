package com.rk.serviceImpl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.rk.config.JwtService;
import com.rk.dto.AddressDTO;
import com.rk.dto.ContactInformationDTO;
import com.rk.dto.FacilitiesDTO;
import com.rk.dto.FloorDTO;
import com.rk.dto.FoodInfoDTO;
import com.rk.dto.HostelDTO;
import com.rk.dto.HostelImageDTO;
import com.rk.dto.PoliciesDTO;
import com.rk.dto.RatingDTO;
import com.rk.dto.RoomDTO;
import com.rk.dto.RoomTypeDTO;
import com.rk.dto.RoomTypeImageDTO;
import com.rk.dto.StudentDTO;
import com.rk.dto.UserDTO;
import com.rk.entity.Address;
import com.rk.entity.Booking;
import com.rk.entity.ContactInformation;
import com.rk.entity.Facilities;
import com.rk.entity.Floor;
import com.rk.entity.FoodInfo;
import com.rk.entity.Gender;
import com.rk.entity.Hostel;
import com.rk.entity.HostelImage;
import com.rk.entity.Policies;
import com.rk.entity.Rating;
import com.rk.entity.Room;
import com.rk.entity.RoomType;
import com.rk.entity.RoomTypeImage;
import com.rk.entity.User;
import com.rk.repository.BookingRepository;
import com.rk.repository.HostelRepository;
import com.rk.repository.PaymentRepository;
import com.rk.repository.RatingRepository;
import com.rk.repository.RoomRepository;
import com.rk.repository.RoomTypeRepository;
import com.rk.repository.UserRepository;
import com.rk.service.HostelService;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
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
	private final RatingRepository ratingRepository;

	@Override
	public List<HostelDTO> getFilteredHostels(Map<String, Object> filters) throws Exception {

		
		 // 🔹 Convert gender string → Enum
	    if (filters.containsKey("gender") && filters.get("gender") != null) {
	        try {
	            String g = filters.get("gender").toString().toUpperCase().trim();
	            filters.put("gender", Gender.valueOf(g));
	        } catch (Exception ex) {
	            throw new IllegalArgumentException("Invalid gender value: " + filters.get("gender"));
	        }
	    }
	    
	    if (filters.get("rating") != null) {
	        try {
	            Double ratingValue = Double.valueOf(filters.get("rating").toString());
	            filters.put("rating", ratingValue);
	        } catch (Exception e) {
	            throw new IllegalArgumentException("Invalid rating value");
	        }
	    }


	    String sortBy = filters.get("sortBy").toString();
	    String sortDir = filters.get("sortDir").toString();

	    Sort sort = Sort.unsorted(); // default

	    // NORMAL SORT IF COLUMN EXISTS IN HOSTEL ENTITY
	    if (!sortBy.equals("pricePerMonth")) {
	        sort = sortDir.equals("asc")
	                ? Sort.by(sortBy).ascending()
	                : Sort.by(sortBy).descending();
	    }

	    
	    // FETCH FROM DB (filtered but unsorted if sorting = pricePerMonth)
	    List<Hostel> result = hostelRepository.findAll(
	            HostelSpecification.filter(filters),
	            sort
	    );

	    // MANUAL SORT (ONLY FOR pricePerMonth)
	    if (sortBy.equals("pricePerMonth")) {
	        result.sort((h1, h2) -> {
	            double p1 = h1.getRoomType().stream()
	                    .mapToDouble(RoomType::getPricePerMonth)
	                    .min().orElse(Double.MAX_VALUE);

	            double p2 = h2.getRoomType().stream()
	                    .mapToDouble(RoomType::getPricePerMonth)
	                    .min().orElse(Double.MAX_VALUE);

	            return sortDir.equals("asc")
	                    ? Double.compare(p1, p2)
	                    : Double.compare(p2, p1);
	        });
	    }
	    
	   

	    // CONVERT TO DTO
	    return result.stream()
	            .map(this::convertHostelToDto)
	            .collect(Collectors.toList());
	}

	


	@Override
	@Transactional
	public List<FloorDTO> fetchAllFloors(Long hostelId) {
		Hostel hostel = hostelRepository.findById(hostelId)
				.orElseThrow(() -> new RuntimeException("invalid hostel id or hostel not found"));

		return hostel.getFloors().stream().map(floor -> {

			List<RoomDTO> roomDtos = floor.getRooms().stream().map(room -> {

				List<StudentDTO> studentDto = room.getBookings().stream().map(Booking::getStudent)
						.filter(Objects::nonNull).map(std -> {
							return StudentDTO.builder().id(std.getId() != null ? std.getId() : null)
									.currentRoomNumber(room.getRoomNumber()).fullName(std.getFullName())
									.email(std.getEmail())
									.hostelId(floor.getHostel() != null ? floor.getHostel().getId() : null)
									.phone(std.getPhone()).paymentStatus(std.getPaymentStatus())
									.joiningDate(std.getJoiningDate() != null ? std.getJoiningDate().toString() : null)
									.lastPaymentDate(
											std.getLastPaymentDate() != null ? std.getLastPaymentDate().toString()
													: null)
									.imageUrl(std.getImageUrl())
									.idUrl(std.getIdUrl())
									.build();
						}).filter(Objects::nonNull).toList();

				return RoomDTO.builder().id(room.getId()).capacity(room.getCapacity()).floorId(floor.getId())
						.floorNumber(floor.getFloorNumber()).roomNumber(room.getRoomNumber())
						.occupants(room.getOccupacy())
						.roomTypeId(room.getRoomType() != null ? room.getRoomType().getId() : null).students(studentDto)
						.build();
			}).filter(Objects::nonNull).toList();

			return FloorDTO.builder().id(floor.getId()).floorNumber(floor.getFloorNumber()).rooms(roomDtos)
					.hostelId(hostelId).build();

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

	public HostelDTO convertHostelToDto(Hostel h) {

		if (h == null)
			return null;

		Address address = h.getAddress();
		ContactInformation contactInfo = h.getContactInfo();
		FoodInfo foodInfo = h.getFoodInfo();
		Facilities facilities = h.getFacilities();
		Policies policies = h.getPolicies();
		List<Floor> floors = h.getFloors();
		List<RoomType> roomTypes = h.getRoomType();

		List<Rating> ratings = h.getRatings();

		List<HostelImage> images = h.getImages();
		User owner = h.getOwner();

		UserDTO userDTO = UserDTO.builder().email(owner.getEmail()).fullName(owner.getFullName()).hostelId(h.getId())
				.id(owner.getId()).role(owner.getRole()).phone(owner.getPhone()).build();

		AddressDTO addressDTO = AddressDTO.builder().city(address.getCity()).state(address.getState())
				.landMark(address.getLandMark()).pincode(address.getPincode()).street(address.getStreet()).build();

		ContactInformationDTO contactInformationDTO = ContactInformationDTO.builder().email(contactInfo.getEmail())
				.contactNumber(contactInfo.getContactNumber()).website(contactInfo.getWebsite()).build();

		FoodInfoDTO foodInfoDTO = FoodInfoDTO.builder().breakFastIncluded(foodInfo.isBreakFastIncluded())
				.lunchIncluded(foodInfo.isLunchIncluded()).dinnerIncluded(foodInfo.isDinnerIncluded())
				.messChargePerMonth(foodInfo.getMessChargePerMonth()).foodType(foodInfo.getFoodType())
				.foodQualityRating(foodInfo.getFoodQualityRating()).build();

		FacilitiesDTO facilitiesDTO = FacilitiesDTO.builder().acAvailable(facilities.isAcAvailable())
				.cctvAvailable(facilities.isCctvAvailable()).commonRoom(facilities.isCommonRoom())
				.gymAvailable(facilities.isGymAvailable()).liftAvailable(facilities.isLiftAvailable())
				.wifiAvailable(facilities.isWifiAvailable()).libraryAvailable(facilities.isLibraryAvailable())
				.parkingAvailable(facilities.isParkingAvailable()).powerBackup(facilities.isPowerBackup())
				.washingMachineAvailable(facilities.isWashingMachineAvailable()).waterHours(facilities.getWaterHours())
				.build();

		PoliciesDTO policiesDTO = PoliciesDTO.builder().checkInTime(policies.getCheckInTime())
				.checkOutTime(policies.getCheckOutTime()).cancellationPolicy(policies.getCancellationPolicy())
				.latePaymentPolicy(policies.getLatePaymentPolicy()).petsAllowed(policies.isPetsAllowed())
				.smokingAllowed(policies.isSmokingAllowed()).visitorPlocy(policies.getVisitorPlocy()).build();

		List<FloorDTO> roomTypeDto = floors.stream().map(
				(f) -> FloorDTO.builder().id(f.getId()).hostelId(h.getId()).floorNumber(f.getFloorNumber()).build())
				.toList();

		List<RatingDTO> ratingDto = ratings.stream()
				.map(rating ->{
				
			return	RatingDTO.builder().id(rating.getId())
					.comment(rating.getComment())
					.createdAt(rating.getCreatedAt())
						.category(rating.getCategory().toString()).hostelId(h.getId()).studentId(rating.getStudent().getId())
						.score(rating.getScore())
						.studentName(rating.getStudent().getFullName())
						.build();
				})
				.toList();

		List<HostelImageDTO> imageDto = images.stream().map(
				img -> HostelImageDTO.builder().id(img.getId()).imageUrl(img.getImageUrl()).hostelId(h.getId()).build())
				.toList();

		List<RoomTypeDTO> rtdto = roomTypes.stream().map(r -> {
			List<RoomTypeImage> rimg = r.getImages();
			List<RoomTypeImageDTO> rtidto = rimg.stream().map(img -> {
				return RoomTypeImageDTO.builder().id(img.getId()).imageUrl(img.getImageUrl()).roomTypeId(r.getId())
						.build();
			}).toList();

			return RoomTypeDTO.builder().id(r.getId()).hostelId(h.getId()).images(rtidto).isAvailable(r.isAvailable())
					.sharingType(r.getSharingType()).pricePerMonth(r.getPricePerMonth()).build();
		}).toList();

		HostelDTO hostelDTO = HostelDTO.builder().id(h.getId()).gender(h.getGender()).owner(userDTO).name(h.getName())
				.status(h.isStatus()).isApproved(h.getIsApproved()).description(h.getDescription()).address(addressDTO)
				.contactInfo(contactInformationDTO).policies(policiesDTO).facilities(facilitiesDTO).images(imageDto)
				.ratings(ratingDto).foodInfo(foodInfoDTO).floors(roomTypeDto).ownerId(owner.getId()).roomTypes(rtdto)
				.build();

		return hostelDTO;
	}

	
}
