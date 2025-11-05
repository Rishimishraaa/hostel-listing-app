package com.rk.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rk.dto.ChartDashboardResponse;
import com.rk.dto.ChartDashboardResponse.BarData;
import com.rk.dto.ChartDashboardResponse.PieData;
import com.rk.dto.HostelDashboardDetailsDto;
import com.rk.repository.BookingRepository;
import com.rk.repository.HostelRepository;
import com.rk.repository.PaymentRepository;
import com.rk.repository.RoomRepository;

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
	
	
	public HostelDashboardDetailsDto getDashboardDetails(String ownerEmail) {
		
		
		try {
			 Long countUsersByOwnerEmail = bookingRepo.countUsersByOwnerEmail(ownerEmail);
			 
			 
				
				Double averageRatingByHostelOwnerEmail = hostelRepository.findAverageRatingByHostelOwnerEmail(ownerEmail);
				
				Double totalEarningsByOwnerEmail = bookingRepo.findTotalEarningsByOwnerEmail(ownerEmail);
				
				
				List<BarData> barDate = bookingRepo.getMonthlyBookingCount(ownerEmail)
				.stream()
				.map(obj-> new ChartDashboardResponse
						.BarData((String)obj[0],(int)obj[1],(long) obj[2]
								))
				.toList();
				
				
				
				List<PieData> pieData = roomRepository.getRoomTypeCount(ownerEmail)
				.stream()
				.map(obj-> new ChartDashboardResponse
						.PieData(new String(""+(int)obj[0]),(long) obj[1]))
				.toList();
				
				
				ChartDashboardResponse graph = ChartDashboardResponse.builder()
				.barData(barDate)
				.pieData(pieData)
				.build();
				
				
				return HostelDashboardDetailsDto.builder()
				.students(countUsersByOwnerEmail)
				.hostelListed(1L)
				.rating(averageRatingByHostelOwnerEmail)
				.totalRevenue(totalEarningsByOwnerEmail)
				.graphResponse(graph)
				.build();
				
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
