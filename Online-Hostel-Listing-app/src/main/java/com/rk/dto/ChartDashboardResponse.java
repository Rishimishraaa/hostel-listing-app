package com.rk.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChartDashboardResponse {

    private List<BarData> barData;
    private List<PieData> pieData;
    private List<RatingsData> ratingsData;
    
    
	@Data
	@AllArgsConstructor
	public static class BarData {

		String month;
		int year;
		Long orders;
	}

	@Data
	@AllArgsConstructor
	public static class PieData {
		String name;
		
		Long value;
	}

	@Data
	@AllArgsConstructor
	public static class RatingsData {

		String name;
		Long value;
	}

}
