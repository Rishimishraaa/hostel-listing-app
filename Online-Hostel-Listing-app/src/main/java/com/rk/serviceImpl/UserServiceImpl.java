package com.rk.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.rk.config.JwtService;
import com.rk.dto.BookingDTO;
import com.rk.dto.FavoriteDTO;
import com.rk.dto.HostelDTO;
import com.rk.dto.PaymentDTO;
import com.rk.dto.RatingDTO;
import com.rk.dto.RoomDTO;
import com.rk.dto.RoomTypeDTO;
import com.rk.dto.UserDTO;
import com.rk.entity.Booking;
import com.rk.entity.Favorite;
import com.rk.entity.Hostel;
import com.rk.entity.Payment;
import com.rk.entity.Rating;
import com.rk.entity.ReviewCategory;
import com.rk.entity.Room;
import com.rk.entity.RoomType;
import com.rk.entity.User;
import com.rk.repository.BookingRepository;
import com.rk.repository.FavoriteRepository;
import com.rk.repository.HostelRepository;
import com.rk.repository.RatingRepository;
import com.rk.repository.RoomTypeRepository;
import com.rk.repository.UserRepository;
import com.rk.request.HostelRatingRequest;
import com.rk.service.UserService;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Service
@Data
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final BookingRepository bookingRepository;
	private final RoomTypeRepository roomTypeRepository;
	private final HostelRepository hostelRepository;
	private final JwtService jwtService;
	private final RatingRepository ratingRepository;
	private final AdminServiceImpl adminServiceImpl;
	private final HostelServiceImpl hostelServiceImpl;
	private final FavoriteRepository favoriteRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public UserDTO getUser(String jwt) throws Exception {
		jwt = jwt.substring(7);
		String username = jwtService.extractUsername(jwt);
		User user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("user not found"));
		return UserDTO.builder().id(user.getId()).email(user.getEmail())
				.hostelId(user.getHostels() != null ? user.getHostels().getId() : null).fullName(user.getFullName())
				.phone(user.getPhone()).role(user.getRole()).build();

	}

	@Override
	public BookingDTO createBooking(BookingDTO booking) throws Exception {
		Hostel hostel = hostelRepository.findById(booking.getHostelId())
				.orElseThrow(() -> new RuntimeException("invalid hostel id"));

		RoomType roomType = roomTypeRepository.findById(booking.getRoomTypeId())
				.orElseThrow(() -> new RuntimeException("invalid room type id"));

		User user = userRepository.findById(booking.getStudentId())
				.orElseThrow(() -> new RuntimeException("invalid user id"));

		Booking bookings = bookingRepository.findByStudentId(user.getId());
		if (!ObjectUtils.isEmpty(bookings)) {
			throw new RuntimeException(
					"You have already booked in " + bookings.getHostel().getName() + " please delete current booking and try again!");
		}

		Booking booking1 = Booking.builder().hostel(hostel).student(user).status("PENDING")
				.startDate(LocalDateTime.now()).roomType(roomType).isActive(true).build();

		Booking save = bookingRepository.save(booking1);

		user.setBookings(bookings);
		user.setHostels(hostel);
		user.setJoiningDate(save.getStartDate().toLocalDate());
		User save2 = userRepository.save(user);

		return BookingDTO.builder().id(booking1.getId()).startDate(booking1.getStartDate()).hostelId(hostel.getId())
				.status(booking1.getStatus()).studentId(save2.getId()).roomTypeId(roomType.getId()).build();
	}

	@Override
	public RatingDTO addRating(HostelRatingRequest req) throws Exception {

		Hostel hostel = hostelRepository.findById(req.getHostelId())
				.orElseThrow(() -> new RuntimeException("invalid hostel id"));

		User user = userRepository.findById(req.getStudentId())
				.orElseThrow(() -> new RuntimeException("invalid user id"));

		Optional<Rating> r = ratingRepository.findByStudentIdAndHostelIdAndCategory(user.getId(), hostel.getId(), ReviewCategory.valueOf( req.getCategory()));
		
		if(r.isPresent()) {
			throw new RuntimeException("Rating already given by you! With Type "+req.getCategory()+" Try another category");
		}
		
		Rating rating = new Rating();
		rating.setCategory(ReviewCategory.valueOf(req.getCategory()));
		rating.setCreatedAt(LocalDateTime.now());
		rating.setHostel(hostel);
		rating.setScore(req.getScore());
		rating.setStudent(user);
		rating.setComment(req.getComment());

		Rating save = ratingRepository.save(rating);
		ArrayList<Rating> list = new ArrayList<>();
		list.add(save);
		user.setRatingsGiven(list);
		userRepository.save(user);

		hostel.setRatings(list);
		hostelRepository.save(hostel);

		return RatingDTO.builder().studentName(user.getFullName()).id(save.getId())
				.category(save.getCategory().toString()).score(save.getScore()).hostelId(hostel.getId())
				.studentId(user.getId()).createdAt(save.getCreatedAt()).build();

	}

	@Override
	public BookingDTO getBookings(String jwt) throws Exception {
		jwt = jwt.substring(7);
		String username = jwtService.extractUsername(jwt);
		User user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("invalid user"));

		Booking b = bookingRepository.findByStudentId(user.getId());
		
		if(b==null) throw new RuntimeException("booking not found");

		Hostel h = b.getHostel();
		User student = b.getStudent();
		Room room = b.getRoom();
		RoomType roomType = b.getRoomType();
		List<Payment> payments = b.getPayments();

		HostelDTO hostelDto = adminServiceImpl.convertHostelToDto(h);

		UserDTO userDTO = UserDTO.builder().id(student.getId()).fullName(student.getFullName())
				.email(student.getEmail()).phone(student.getPhone()).role(student.getRole()).hostelId(hostelDto.getId())
				.build();
		RoomDTO roomDTO = null;
		if (room != null) {
			roomDTO = RoomDTO.builder().id(room.getId())
					.roomNumber(room.getRoomNumber())
					.capacity(room.getCapacity())
					.floorId(room.getFloor()
							.getId())
					.floorNumber(room.getFloorNumber())
					.occupants(room.getOccupacy())
					.roomTypeId(room.getRoomType().getId())
					.build();
		}

		RoomTypeDTO roomTypeDTO = RoomTypeDTO.builder().id(roomType.getId()).sharingType(roomType.getSharingType())
				.hostelId(hostelDto.getId()).isAvailable(roomType.isAvailable())
				.pricePerMonth(roomType.getPricePerMonth()).build();

		List<PaymentDTO> list = payments.stream().map(pay -> {
			return PaymentDTO.builder()
					.id(pay.getId())
					.amount(pay.getAmount())
					.bookingId(pay.getBooking().getId())
					.month(pay.getMonth().toString())
					.paidOn(pay.getPaidOn() != null ? pay.getPaidOn().toString() : null)
					.studentName(student.getFullName()).studentId(student.getId()).status(pay.getStatus())
					.room(room != null ? room.getRoomNumber() : null).phone(student.getPhone()).build();
		}).toList();

		return BookingDTO
				.builder()
				.id(b.getId())
				.hostel(hostelDto)
				.startDate(b.getStartDate())
				.endDate(b.getEndDate())
				.room(roomDTO)
				.roomType(roomTypeDTO)
				.student(userDTO)
				.status(b.getStatus())
				.payments(list)
				.build();
	}

	@Override
	public List<PaymentDTO> getAllPayments(String jwt)throws Exception {
		jwt = jwt.substring(7);
		String username = jwtService.extractUsername(jwt);
		User user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("Invalid user id"));

		Booking booking = bookingRepository.findByStudentId(user.getId());

		if(booking==null) {
			throw new RuntimeException("booking not available");
		}
		List<Payment> payments = booking.getPayments();

		List<PaymentDTO> list = payments.stream().map(p -> {
			return PaymentDTO.builder().id(p.getId()).amount(p.getAmount()).bookingId(booking.getId())
					.hostelName(booking.getHostel().getName()).month(p.getMonth().toString())
					.studentName(booking.getStudent().getFullName()).phone(booking.getStudent().getPhone())
					.status(p.getStatus()).room(booking.getRoom()!=null ? booking.getRoom().getRoomNumber(): null)
					.paidOn(p.getPaidOn() != null ? p.getPaidOn().toString() : null).build();

		}).filter(Objects::nonNull).toList();

		return list;
	}

	@Override
	public List<RatingDTO> fetchAllRatings(String jwt)throws Exception {
		jwt = jwt.substring(7);
		String username = jwtService.extractUsername(jwt);
		User user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("Invalid user id"));

		List<Rating> rating = ratingRepository.findByStudentId(user.getId());
		List<RatingDTO> list = rating.stream().map(r -> {
			return RatingDTO.builder().id(r.getId()).score(r.getScore()).category(r.getCategory().toString())
					.createdAt(r.getCreatedAt()).comment(r.getComment()).hostelId(r.getHostel().getId())
					.studentId(r.getStudent().getId()).studentName(r.getStudent().getFullName())
					.hostelName(r.getHostel().getName()).build();
		}).toList();

		return list;
	}

	@Override
	public FavoriteDTO addToFavorite(String jwt, Long hostelId)throws Exception {
		jwt = jwt.substring(7);
		String username = jwtService.extractUsername(jwt);
		User user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("Invalid user id"));

		
		Hostel hostel = hostelRepository.findById(hostelId)
				.orElseThrow(() -> new RuntimeException("invalid hostel id!"));
		
		Favorite myfav = favoriteRepository.findByStudentIdAndHostelId(user.getId(), hostelId);
		
		if(myfav!=null) {
			throw new RuntimeException("favorite already happend!");
		}

		Favorite fav = new Favorite();
		fav.setCreatedAt(LocalDateTime.now());
		fav.setHostel(hostel);
		fav.setStudent(user);

		Favorite save = favoriteRepository.save(fav);

		HostelDTO convertHostelToDto = adminServiceImpl.convertHostelToDto(hostel);

		FavoriteDTO favoriteDTO = FavoriteDTO.builder().id(save.getId()).createdAt(save.getCreatedAt())
				.hostelId(hostel.getId()).studentId(user.getId()).hostel(convertHostelToDto).build();

		ArrayList<Favorite> f = new ArrayList<>();
		f.add(save);
		user.setFavorites(f);
		userRepository.save(user);

		return favoriteDTO;
	}

	@Override
	public List<FavoriteDTO> fetchAllFavorite(String jwt) throws Exception{
		jwt = jwt.substring(7);
		String username = jwtService.extractUsername(jwt);
		User user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("Invalid user id"));

		List<Favorite> fav = favoriteRepository.findByStudentId(user.getId());
		return fav.stream().map(f -> {
			HostelDTO HostelDto = adminServiceImpl.convertHostelToDto(f.getHostel());
			return FavoriteDTO.builder().id(f.getId()).createdAt(f.getCreatedAt())
					.hostelId(HostelDto.getId())
					.hostel(HostelDto)
					.studentId(user.getId()).build();
		}).toList();

	}

	@Override
	public Long removeToFavorite(String jwt, Long hostelId)throws Exception {
		jwt = jwt.substring(7);
		String username = jwtService.extractUsername(jwt);
		User user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("Invalid user id"));

		Favorite favorite = favoriteRepository.findByStudentIdAndHostelId(user.getId(), hostelId);
		if (favorite == null)
			throw new RuntimeException("favorite not found");
		List<Favorite> favorites = user.getFavorites();
		favorites.remove(favorite);
		
		userRepository.save(user);

		favoriteRepository.delete(favorite);
		
		return favorite.getId();
	}
	
	
	public Optional<User> findByEmail(String email)throws Exception{
		return userRepository.findByEmail(email);
	}
	
	public Optional<User> findByPhone(String phone)throws Exception{
		return userRepository.findByPhone(phone);
		}
	
	@Override
	public void updatePasswordByEmail(String email, String rawPassword)throws Exception {
		userRepository.findByEmail(email).ifPresent(u -> {
			u.setPassword(passwordEncoder.encode(rawPassword));
			userRepository.save(u);
			});
	}
	
	@Override
	public void updatePasswordByPhone(String phone, String rawPassword) throws Exception{
		userRepository.findByEmail(phone).ifPresent(u -> {
			u.setPassword(passwordEncoder.encode(rawPassword));
			userRepository.save(u);
			});
	}
	
	
	
	

}
