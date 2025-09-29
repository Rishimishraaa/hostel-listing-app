package com.rk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodInfoDTO {
	  private String foodType;
	    private Integer foodQualityRating;
	    private boolean breakFastIncluded;
	    private boolean lunchIncluded;
	    private boolean dinnerIncluded;
	    private Double messChargePerMonth;
}
