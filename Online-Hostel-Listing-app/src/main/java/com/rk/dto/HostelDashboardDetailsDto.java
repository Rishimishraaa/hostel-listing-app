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
	
	private ChartDashboardResponse graphResponse;
	
}
