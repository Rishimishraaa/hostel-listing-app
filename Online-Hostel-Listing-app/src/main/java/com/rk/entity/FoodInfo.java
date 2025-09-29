package com.rk.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FoodInfo {
	
	private String foodType;
	
	private Integer foodQualityRating; //1-5
	
	private boolean breakFastIncluded;
	
	private boolean lunchIncluded;
	
	private boolean dinnerIncluded;
	
	private Double messChargePerMonth;
	

}
