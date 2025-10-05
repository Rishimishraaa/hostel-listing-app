package com.rk.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.rk.config.JwtService;
import com.rk.dto.AddressDTO;
import com.rk.dto.BookingDTO;
import com.rk.dto.ContactInformationDTO;
import com.rk.dto.FacilitiesDTO;
import com.rk.dto.FloorDTO;
import com.rk.dto.FoodInfoDTO;
import com.rk.dto.HostelDTO;
import com.rk.dto.HostelImageDTO;
import com.rk.dto.PaymentDTO;
import com.rk.dto.PaymentHistoryDto;
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
import com.rk.entity.Hostel;
import com.rk.entity.HostelImage;
import com.rk.entity.Payment;
import com.rk.entity.Policies;
import com.rk.entity.Rating;
import com.rk.entity.ReviewCategory;
import com.rk.entity.Role;
import com.rk.entity.Room;
import com.rk.entity.RoomType;
import com.rk.entity.RoomTypeImage;
import com.rk.entity.User;
import com.rk.repository.BookingRepository;
import com.rk.repository.FloorRepository;
import com.rk.repository.HostelRepository;
import com.rk.repository.PaymentRepository;
import com.rk.repository.RoomRepository;
import com.rk.repository.RoomTypeRepository;
import com.rk.repository.UserRepository;
import com.rk.request.AddPaymentRequest;
import com.rk.request.StudentBookingRequest;
import com.rk.response.MessageResponse;
import com.rk.service.AdminService;

import jakarta.transaction.Transactional;
import lombok.Data;

@Service
@Data
public class AdminServiceImpl implements AdminService {

	private final UserRepository userRepository;
	private final HostelRepository hostelRepository;
	private final JwtService jwtService;
	private final RoomTypeRepository roomTypeRepository;
	private final RoomRepository roomRepository;
	private final BookingRepository bookingRepository;
	private final PaymentRepository paymentRepository;
	private final FloorRepository floorRepository;

	@Override
	public HostelDTO getHostelsByOwnerId(Long hostelId) throws Exception {
		Hostel hostels = hostelRepository.findById(hostelId)
				.orElseThrow(() -> new RuntimeException("invalid hostel id or hostel not found"));
		HostelDTO convertHostelToDto = convertHostelToDto(hostels);
		return convertHostelToDto;

	}

	@Transactional
	@Override
	public HostelDTO createHostel(Hostel hostel, String jwt) throws Exception {
		jwt = jwt.substring(7);
		String username = jwtService.extractUsername(jwt);
		User owner = userRepository.findByEmail(username).get();
		hostel.setOwner(owner);

		if (hostel.getRatings() == null) {
			hostel.setRatings(new ArrayList<Rating>());
		}

		if (hostel.getImages() != null) {
			hostel.getImages().forEach(img -> img.setHostel(hostel));
		}

		Hostel save = hostelRepository.save(hostel);
		owner.setHostels(save);
		userRepository.save(owner);
		HostelDTO convertHostelToDto = convertHostelToDto(save);

		return convertHostelToDto;
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
				.map(rating -> RatingDTO.builder().comment(rating.getComment()).hostelName(rating.getHostel().getName())
						.id(rating.getId()).createdAt(rating.getCreatedAt()).category(rating.getCategory().toString())
						.hostelId(h.getId()).studentId(owner.getId()).score(rating.getScore()).build())
				.toList();

		List<HostelImageDTO> imageDto = images.stream().map(
				img -> HostelImageDTO.builder().id(img.getId()).imageUrl(img.getImageUrl()).hostelId(h.getId()).build())
				.toList();

		HostelDTO hostelDTO = HostelDTO.builder().id(h.getId()).gender(h.getGender()).owner(userDTO).name(h.getName())
				.status(h.isStatus()).description(h.getDescription()).address(addressDTO)
				.contactInfo(contactInformationDTO).policies(policiesDTO).facilities(facilitiesDTO).images(imageDto)
				.ratings(ratingDto).foodInfo(foodInfoDTO).floors(roomTypeDto).build();

		return hostelDTO;
	}

	private Hostel convertDtoToHostel(HostelDTO dto) {

		User owner = userRepository.findById(dto.getOwner().getId())
				.orElseThrow(() -> new RuntimeException("Owner not found"));

		Hostel hostel = Hostel.builder().id(dto.getId()).gender(dto.getGender()).name(dto.getName())
				.description(dto.getDescription()).owner(owner).build();

		AddressDTO address = dto.getAddress();
		ContactInformationDTO contactInfo = dto.getContactInfo();
		FacilitiesDTO facilities = dto.getFacilities();
		PoliciesDTO policies = dto.getPolicies();
		FoodInfoDTO foodInfo = dto.getFoodInfo();

		List<RatingDTO> ratings = dto.getRatings();
		List<HostelImageDTO> images = dto.getImages();

		Address addressDto = Address.builder().city(address.getCity()).state(address.getState())
				.landMark(address.getLandMark()).pincode(address.getPincode()).street(address.getStreet()).build();

		hostel.setAddress(addressDto);

		ContactInformation contactInfoDto = ContactInformation.builder().email(contactInfo.getEmail())
				.contactNumber(contactInfo.getContactNumber()).website(contactInfo.getWebsite()).build();

		hostel.setContactInfo(contactInfoDto);

		Facilities facilitiesDto = Facilities.builder().acAvailable(facilities.isAcAvailable())
				.wifiAvailable(facilities.isWifiAvailable()).cctvAvailable(facilities.isCctvAvailable())
				.commonRoom(facilities.isCommonRoom()).gymAvailable(facilities.isGymAvailable())
				.libraryAvailable(facilities.isLibraryAvailable()).waterHours(facilities.getWaterHours())
				.parkingAvailable(facilities.isParkingAvailable()).liftAvailable(facilities.isLiftAvailable())
				.powerBackup(facilities.isPowerBackup()).washingMachineAvailable(facilities.isWashingMachineAvailable())
				.build();

		hostel.setFacilities(facilitiesDto);

		Policies policiesDto = Policies.builder().checkInTime(policies.getCheckInTime())
				.checkOutTime(policies.getCheckOutTime()).cancellationPolicy(policies.getCancellationPolicy())
				.latePaymentPolicy(policies.getLatePaymentPolicy()).petsAllowed(policies.isPetsAllowed())
				.visitorPlocy(policies.getVisitorPlocy()).smokingAllowed(policies.isSmokingAllowed()).build();

		hostel.setPolicies(policiesDto);
		FoodInfo foodInfoDto = FoodInfo.builder().breakFastIncluded(foodInfo.isBreakFastIncluded())
				.lunchIncluded(foodInfo.isLunchIncluded()).dinnerIncluded(foodInfo.isDinnerIncluded())
				.foodType(foodInfo.getFoodType()).foodQualityRating(foodInfo.getFoodQualityRating())
				.messChargePerMonth(foodInfo.getMessChargePerMonth()).build();

		hostel.setFoodInfo(foodInfoDto);
		List<Floor> floors = dto.getFloors().stream().map(f -> {

			return Floor.builder().id(f.getId()).hostel(hostel).floorNumber(f.getFloorNumber()).build();
		}).toList();

		hostel.setFloors(floors);

		List<HostelImage> imagelist = images.stream()
				.map(img -> HostelImage.builder().id(img.getId()).imageUrl(img.getImageUrl()).hostel(hostel).build())
				.toList();

		hostel.setImages(imagelist);

		List<Rating> ratinglist = ratings.stream()
				.map(r -> Rating.builder().id(r.getId()).category(ReviewCategory.valueOf(r.getCategory()))
						.createdAt(r.getCreatedAt()).hostel(hostel).score(r.getScore()).student(owner).build())
				.toList();

		hostel.setRatings(ratinglist);

		return hostel;
	}

	@Override
	@Transactional
	public RoomTypeDTO createRoomTypeInHostel(RoomType room, Long hostelId, String jwt) throws Exception {
		Hostel hostel = hostelRepository.findById(hostelId).get();
		room.setHostel(hostel);
		if (room.getImages() != null) {
			room.getImages().forEach(img -> img.setRoomType(room));
		}

		room.setHostel(hostel);
		RoomType save = roomTypeRepository.save(room);

		List<RoomTypeImageDTO> roomTypeimgDto = save.getImages().stream().map(img -> {

			return RoomTypeImageDTO.builder().id(img.getId()).imageUrl(img.getImageUrl())

					.roomTypeId(save.getId()).build();
		}).toList();

		RoomTypeDTO roomTypeDTO = RoomTypeDTO.builder().id(save.getId()).sharingType(save.getSharingType())
				.hostelId(hostelId).isAvailable(save.isAvailable()).pricePerMonth(save.getPricePerMonth())
				.images(roomTypeimgDto).build();

		return roomTypeDTO;
	}

	@Override
	public RoomTypeDTO updateRoomType(Long roomId, RoomTypeDTO room) throws Exception {

		Optional<RoomType> byId = roomTypeRepository.findById(roomId);

		if (byId.isPresent()) {
			RoomType roomType = byId.get();

			roomType.setAvailable(room.isAvailable());
			roomType.setSharingType(room.getSharingType());
			roomType.setPricePerMonth(room.getPricePerMonth());
			RoomType save = roomTypeRepository.save(roomType);

			List<RoomTypeImageDTO> roomTypeimgDto = save.getImages().stream().map(img -> {
				return RoomTypeImageDTO.builder().id(img.getId()).imageUrl(img.getImageUrl()).roomTypeId(save.getId())
						.build();
			}).toList();

			RoomTypeDTO roomTypeDTO = RoomTypeDTO.builder().id(save.getId()).sharingType(save.getSharingType())
					.hostelId(room.getId()).isAvailable(save.isAvailable()).pricePerMonth(save.getPricePerMonth())
					.images(roomTypeimgDto).build();

			return roomTypeDTO;

		} else {
			throw new RuntimeException("invalid room id");
		}

	}

	@Override
	public MessageResponse deleteRoomType(Long roomId) {
		MessageResponse resp = new MessageResponse();
		Optional<RoomType> byId = roomTypeRepository.findById(roomId);
		if (byId.isEmpty()) {

			resp.setMessage("invalid room id");
			return resp;
		}

		roomTypeRepository.deleteById(roomId);
		resp.setMessage("room deleted successfully");
		resp.setId(roomId);
		return resp;
	}

	@Transactional
	@Override
	public HostelDTO updateHostel(HostelDTO dto, String jwt) throws Exception {

		Hostel hostel = convertDtoToHostel(dto);

		jwt = jwt.substring(7);
		String username = jwtService.extractUsername(jwt);

		User owner = userRepository.findByEmail(username).get();
		hostel.setOwner(owner);

		if (hostel.getImages() != null) {
			hostel.getImages().forEach(img -> img.setHostel(hostel));
		}

		hostelRepository.save(hostel);

		HostelDTO convertHostelToDto = convertHostelToDto(hostel);
		return convertHostelToDto;
	}

	@Override
	public MessageResponse updateHostelStatus(Long hostelId) throws Exception {
		Hostel hostel = hostelRepository.findById(hostelId)
				.orElseThrow(() -> new RuntimeException("invalid hostel id"));

		if (hostel.isStatus() == true) {
			hostel.setStatus(false);
		} else {
			hostel.setStatus(true);
		}

		hostelRepository.save(hostel);

		MessageResponse res = new MessageResponse();
		res.setId(hostelId);
		res.setMessage("hostel update successfully");

		return res;
	}

	@Override
	public RoomDTO createRoom(RoomDTO dto) throws Exception {

		Floor floors = floorRepository.findById(dto.getFloorId())
				.orElseThrow(() -> new RuntimeException("invalid floor id"));

		RoomType roomSharing = roomTypeRepository.findById(dto.getRoomTypeId())
				.orElseThrow(() -> new RuntimeException("invalid sharing type / capacity"));

		Optional<Room> roomOp = roomRepository.findByRoomNumber(dto.getRoomNumber());

		if (roomOp == null) {
			throw new RuntimeException("room already available");
		}

		Room room = Room.builder().roomNumber(dto.getRoomNumber()).capacity(dto.getCapacity()).floor(floors)
				.occupacy(dto.getOccupants()).floorNumber(floors.getFloorNumber()).roomType(roomSharing).build();

		Room save = roomRepository.save(room);
		RoomDTO roomDTO = RoomDTO.builder().id(save.getId()).capacity(save.getCapacity())
				.roomNumber(save.getRoomNumber()).floorId(floors.getId()).floorNumber(floors.getFloorNumber())
				.occupants(save.getOccupacy())
				.roomTypeId(save.getRoomType() != null ? save.getRoomType().getId() : null).build();
		return roomDTO;
	}

	@Override
	public StudentDTO addStudentInHostel(StudentBookingRequest request) throws Exception {
		BookingDTO bookingDto = request.getBooking();
		StudentDTO studentDto = request.getStudent();
		User user = null;
		Optional<User> byEmail = userRepository.findByEmail(studentDto.getEmail());

		Hostel hostel = hostelRepository.findById(bookingDto.getHostelId())
				.orElseThrow(() -> new RuntimeException("invalid hostel id"));
		Room room = roomRepository.findById(bookingDto.getRoomId())
				.orElseThrow(() -> new RuntimeException("invalid room id"));
		RoomType roomType = room.getRoomType();

		if (room.getCapacity() < room.getOccupacy()) {
			throw new RuntimeException("room is full try another room!");
		}

		if (byEmail.isPresent()) {
			User user2 = byEmail.get();
			user = User.builder().paymentStatus(user2.getPaymentStatus()).lastPaymentDate(user2.getLastPaymentDate())
					.isActive(user2.getIsActive()).id(user2.getId()).fullName(studentDto.getFullName())
					.email(studentDto.getEmail()).phone(studentDto.getPhone()).role(Role.STUDENT).room(room)
					.hostels(hostel).joiningDate(LocalDate.now()).build();
		} else {
			user = User.builder().joiningDate(LocalDate.parse(bookingDto.getStartDate().toString().substring(0, 10)))
					.paymentStatus("PENDING").isActive(true).fullName(studentDto.getFullName())
					.email(studentDto.getEmail()).phone(studentDto.getPhone()).role(Role.STUDENT).room(room)
					.hostels(hostel).joiningDate(LocalDate.now()).build();
		}

		User save = userRepository.save(user);

		Booking booking = Booking.builder().isActive(true).status("CONFERM").student(save).hostel(hostel).room(room)
				.roomType(roomType).status(bookingDto.getStatus() != null ? bookingDto.getStatus() : "PENDING")
				.startDate(bookingDto.getStartDate() != null ? bookingDto.getStartDate() : LocalDateTime.now()).build();

		Booking saveBooking = bookingRepository.save(booking);

		if (room.getOccupacy() >= room.getCapacity()) {
			throw new RuntimeException("room is full cannot book!");
		}

		ArrayList<Booking> bookings = new ArrayList<>();
		bookings.add(saveBooking);
		room.setBookings(bookings);
		room.setOccupacy(room.getOccupacy() + 1);
		roomRepository.save(room);

		return StudentDTO.builder().id(save.getId()).fullName(save.getFullName()).email(save.getEmail())
				.phone(save.getPhone()).currentRoomNumber(save.getRoom().getRoomNumber())
				.joiningDate(save.getJoiningDate().toString()).hostelId(hostel.getId()).roomId(room.getId())
				.paymentStatus(save.getPaymentStatus()).build();

	}

	@Override
	@Transactional
	public MessageResponse removeStudentFromHostel(Long userId) throws Exception {
		User student = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Invalid user id"));

		// soft delete: just deactivate user
		student.setIsActive(false);

		if (student.getBookings() == null) {
			throw new RuntimeException("Student does not belong to this hostel");
		}

		userRepository.save(student);

		MessageResponse res = new MessageResponse();
		res.setMessage("student removed from hostel successfully!");
		res.setId(userId);
		return res;

	}

	@Transactional
	@Override
	public PaymentDTO AddStudentPayment(AddPaymentRequest request) throws Exception {

		// 1️⃣ Fetch student
		User student = userRepository.findById(request.getStudentId())
				.orElseThrow(() -> new RuntimeException("Student not found"));

		// 2️⃣ Fetch student's single booking
		Booking booking = bookingRepository.findByStudentId(student.getId());
//				.stream()
//				.max(Comparator.comparing(Booking::getStartDate)) // agar kabhi multiple bookings ho, latest le lo

		LocalDate monthDate = LocalDate.parse(request.getMonth());

		// 4️⃣ Generate unique key
		String uniqueKey = student.getId() + "-" + monthDate.toString();

		// 5️⃣ Check if payment already exists
		boolean exists = paymentRepository.existsByStudentAndMonthAndYear(student.getId(), monthDate.getMonthValue(), monthDate.getYear());
		
		if (exists) {
			throw new RuntimeException("Payment already exists for this student and month");
		}

		// 6️⃣ Create and save payment
		Payment payment = new Payment();
		payment.setBooking(booking);
		payment.setMonth(monthDate);
		payment.setAmount(request.getAmount());
		payment.setStatus(request.getStatus());
		payment.setUniqueKey(uniqueKey);

		Payment savedPayment = paymentRepository.save(payment);

		// 7️⃣ Update student last payment info
		student.setPaymentStatus(savedPayment.getStatus());
		student.setLastPaymentDate(savedPayment.getPaidOn());
		userRepository.save(student);

		// 8️⃣ Return DTO
		return PaymentDTO.builder().id(savedPayment.getId()).studentId(student.getId())
				.studentName(student.getFullName()).phone(student.getPhone()).amount(savedPayment.getAmount())
				.status(savedPayment.getStatus()).bookingId(booking.getId())
				.month(savedPayment.getMonth().getMonth().name())
				.room(booking.getRoom() != null ? booking.getRoom().getRoomNumber() : "-")
				.paidOn(savedPayment.getPaidOn() != null ? savedPayment.getPaidOn().toString() : null).build();
	}

	@Override
	public MessageResponse confermStudentPayment(Long paymentId) throws Exception {

		Payment payment = paymentRepository.findById(paymentId)
				.orElseThrow(() -> new RuntimeException("invalid payment id"));

		if (payment.getStatus().equals("PENDING")) {
			payment.setStatus("PAID");
		}

		payment.setPaidOn(LocalDateTime.now());

		Booking booking = payment.getBooking();

		User user = userRepository.findById(booking.getStudent().getId())
				.orElseThrow(() -> new RuntimeException("invalid student id / booking id"));

		Payment save = paymentRepository.save(payment);

		user.setLastPaymentDate(save.getPaidOn());
		user.setPaymentStatus(save.getStatus());

		userRepository.save(user);

		MessageResponse res = new MessageResponse();

		res.setMessage("payment status update successfully");
		res.setId(save.getId());
		return res;
	}

	@Override
	public List<PaymentDTO> getPaymentsForHostel(Long hostelId) throws Exception {
		List<Payment> payments = paymentRepository.findByBooking_Hostel_Id(hostelId);

		return payments.stream().map(p -> {
			Booking b = p.getBooking();
			PaymentDTO dto = new PaymentDTO();
			dto.setStudentId(b.getStudent().getId());
			dto.setId(p.getId());
			dto.setStudentName(b.getStudent().getFullName());
			dto.setPhone(b.getStudent().getPhone());
			dto.setRoom(b.getRoom() != null ? b.getRoom().getRoomNumber() : "-");
			dto.setMonth(p.getMonth().toString());
			dto.setAmount(p.getAmount());
			dto.setStatus(p.getStatus());
			dto.setPaidOn(p.getPaidOn() != null ? p.getPaidOn().toString() : null);
			return dto;
		}).toList();

	}

	@Override
	public List<StudentDTO> getAllStudentInHostels(Long hostelId) throws Exception {
		List<Booking> bookings = bookingRepository.findByHostelId(hostelId);

		if (bookings == null)
			return List.of();

		return bookings.stream()
				// skip bookings where room is null
				.filter(booking -> booking.getRoom() != null).map(booking -> {
					User student = booking.getStudent();
					if (student == null)
						return null;

					Room room = booking.getRoom();

					List<PaymentDTO> payments = (booking.getPayments() != null ? booking.getPayments()
							: List.<Payment>of())
							.stream().filter(Objects::nonNull)
							.map(payment -> PaymentDTO.builder().id(payment.getId()).studentId(student.getId())
									.status(payment.getStatus()).amount(payment.getAmount())
									.month(payment.getMonth() != null 
											? payment.getMonth().toString()
											: null)
									.paidOn(payment.getPaidOn() != null ? payment.getPaidOn().toString() : null)
									.build())
							.toList();

					return StudentDTO.builder().id(student.getId()).fullName(student.getFullName())
							.email(student.getEmail()).phone(student.getPhone())
							.paymentStatus(student.getPaymentStatus()).currentRoomNumber(room.getRoomNumber())
							.payments(payments)
							.joiningDate(student.getJoiningDate() != null ? student.getJoiningDate().toString() : null)
							.lastPaymentDate(
									student.getLastPaymentDate() != null ? student.getLastPaymentDate().toString()
											: null)
							.hostelId(hostelId).roomId(room.getId()).build();
				}).filter(Objects::nonNull) // still safe if student is null
				.toList();
	}

	@Override
	public FloorDTO createFloor(FloorDTO dto) {

		Hostel hostel = hostelRepository.findById(dto.getHostelId())
				.orElseThrow(() -> new RuntimeException("invalid hostel id"));

		Floor f = new Floor();
		f.setFloorNumber(dto.getFloorNumber());
		f.setHostel(hostel);
		Floor save = floorRepository.save(f);

		return FloorDTO.builder().id(save.getId()).floorNumber(save.getFloorNumber()).hostelId(save.getHostel().getId())
				.build();
	}

	@Override
	public List<BookingDTO> getAllStudentBookings(Long hostelId) throws Exception {

		List<Booking> bookings = bookingRepository.findByHostelId(hostelId);

		return bookings.stream().map(b -> {

			Hostel hostel = b.getHostel();
			User student = b.getStudent();
			Room room = b.getRoom();

			HostelDTO hostelDTO = HostelDTO.builder().id(hostel.getId()).name(hostel.getName()).build();

			UserDTO studentDTO = UserDTO.builder().id(student.getId()).fullName(student.getFullName())
					.phone(student.getPhone()).build();

			RoomDTO dto = null;

			if (room != null) {
				dto = RoomDTO.builder().id(room.getId()).roomNumber(room.getRoomNumber()).build();
			}

			return BookingDTO.builder().id(b.getId()).startDate(b.getStartDate()).endDate(b.getEndDate())
					.hostelId(b.getHostel().getId()).roomId(b.getRoom() != null ? b.getRoom().getId() : null)
					.totalAmount(b.getTotalAmount())
					.roomTypeId(b.getRoomType() != null ? b.getRoomType().getId() : null).status(b.getStatus())
					.studentId(b.getStudent().getId()).hostel(hostelDTO).student(studentDTO).room(dto).build();
		}).toList();

	}

	@Override
	public RoomDTO asignRoomToStudent(Long bookingId, Long roomId) throws Exception {
		Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("room not available!"));

		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new RuntimeException("booking not available"));

		User user = userRepository.findById(booking.getStudent().getId())
				.orElseThrow(() -> new RuntimeException("invalid user"));

		booking.setRoom(room);
		booking.setStatus("CONFIRMED");
		booking.setStartDate(LocalDateTime.now());
		Booking save = bookingRepository.save(booking);

		user.setBookings(save);
		userRepository.save(user);

		ArrayList<Booking> bookings = new ArrayList<>();
		bookings.add(save);
		room.setBookings(bookings);
		room.setOccupacy(room.getOccupacy() + 1);
		Room save2 = roomRepository.save(room);

		return RoomDTO.builder().id(roomId).bookingId(bookingId).capacity(save2.getCapacity())
				.floorId(save2.getFloor().getId()).floorNumber(save2.getFloorNumber()).occupants(save2.getCapacity())
				.build();
	}

	@Override
	public void confermStudentBooking(Long bookingId) throws Exception {
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new RuntimeException("booking not available"));

		booking.setStatus("CONFIRMED");

		bookingRepository.save(booking);

	}

	@Override
	public void removeStudentBooking(Long bookingId) throws Exception {

		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new RuntimeException("booking not available"));

		User user = userRepository.findById(booking.getStudent().getId())
				.orElseThrow(() -> new RuntimeException("user not available"));
		user.setBookings(null);
		userRepository.save(user);
		Room room = booking.getRoom();
		room.setOccupacy(room.getOccupacy() - 1);
		roomRepository.save(room);

		booking.setStudent(null);
		bookingRepository.delete(booking);
	}
}
