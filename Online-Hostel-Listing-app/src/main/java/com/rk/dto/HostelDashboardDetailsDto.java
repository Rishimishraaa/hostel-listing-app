package com.rk.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HostelDashboardDetailsDto {

	private Long students ;
	private Double totalRevenue;
	private Long hostelListed;
	private double rating;
	private Integer totalReviews;
	
	private Double todayRevanue;
	private Double monthlyRevanue;
	private Integer availableRooms;
	private Integer allRooms;
	
	private Long newBookings;
	
	private Long pendingBooking;
	
	private Integer vaccantRooms;
	
	private ChartDashboardResponse graphResponse;
	
}
