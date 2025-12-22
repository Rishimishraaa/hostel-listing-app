package com.rk.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.rk.dto.ChartDashboardResponse;
import com.rk.dto.ChartDashboardResponse.BarData;
import com.rk.dto.ChartDashboardResponse.PieData;
import com.rk.dto.HostelDashboardDetailsDto;
import com.rk.entity.User;
import com.rk.repository.BookingRepository;
import com.rk.repository.HostelRepository;
import com.rk.repository.PaymentRepository;
import com.rk.repository.RoomRepository;
import com.rk.repository.UserRepository;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
@Service
public class DashboardService {

	private final BookingRepository bookingRepo;
	private final RoomRepository roomRepository;
	private final HostelRepository hostelRepository;
	private final PaymentRepository paymentRepository;
	private final UserRepository userRepository;

	public HostelDashboardDetailsDto getDashboardDetails(String ownerEmail) {

		User user = userRepository.findByEmail(ownerEmail).orElseThrow();

		ZoneId ist = ZoneId.of("Asia/Kolkata");

		LocalDate today = LocalDate.now(ist);

		LocalDateTime start = today.atStartOfDay();
		LocalDateTime end = today.atTime(23, 59, 59);
		try {
			Long countUsersByOwnerEmail = bookingRepo.countUsersByOwnerEmail(ownerEmail);

			Double averageRatingByHostelOwnerEmail = hostelRepository.findAverageRatingByHostelOwnerEmail(ownerEmail);

			Integer allReviews = hostelRepository.getAllReviews(user.getHostels().getId());
			
			Double totalEarningsByOwnerEmail = bookingRepo.findTotalEarningsByOwnerEmail(ownerEmail);

			Double todayRevanue = hostelRepository.getTodayRevenue(user.getHostels().getId(), start, end);

			// =================== montly revenue =======================
			// Month start
			LocalDateTime startM = today.withDayOfMonth(1).atStartOfDay();

			// Month end
			LocalDateTime endM = today.withDayOfMonth(today.lengthOfMonth()).atTime(23, 59, 59);

			Double monthlyRevenue = hostelRepository.getTodayRevenue(user.getHostels().getId(), startM, endM);

			//============================================================
			LocalDateTime startB =
			        today.withDayOfMonth(1).atStartOfDay();

			// Month end
			LocalDateTime endB =
			        today.withDayOfMonth(today.lengthOfMonth())
			             .atTime(23, 59, 59);

			Long newBookings = bookingRepo.countNewBookings(user.getHostels().getId(), startB, endB);
			
			Integer availableRooms = hostelRepository.getAvailableRoomCount(user.getHostels().getId());
			Integer allRooms = hostelRepository.getAllRoomCount(user.getHostels().getId());

			Long pendingBooking = bookingRepo.findPendingBooking(user.getHostels().getId(), "PENDING");

			Integer vaccantRooms = null;

			List<BarData> barDate = bookingRepo.getMonthlyBookingCount(ownerEmail).stream()
					.map(obj -> new ChartDashboardResponse.BarData((String) obj[0], (int) obj[1], (long) obj[2]))
					.toList();

			List<PieData> pieData = roomRepository.getRoomTypeCount(ownerEmail).stream()
					.map(obj -> new ChartDashboardResponse.PieData(new String("" + (int) obj[0]), (long) obj[1]))
					.toList();

			ChartDashboardResponse graph = ChartDashboardResponse.builder().barData(barDate).pieData(pieData).build();

			return HostelDashboardDetailsDto.builder()
					.students(countUsersByOwnerEmail)
					.monthlyRevanue(monthlyRevenue)
					.hostelListed(1L).todayRevanue(todayRevanue)
					.rating(averageRatingByHostelOwnerEmail)
					.totalReviews(allReviews)
					.pendingBooking(pendingBooking)
					.allRooms(allRooms)
					.availableRooms(availableRooms)
					.newBookings(newBookings)
					.totalRevenue(totalEarningsByOwnerEmail).graphResponse(graph).build();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
