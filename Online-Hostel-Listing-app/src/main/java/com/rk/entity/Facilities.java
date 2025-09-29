package com.rk.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Facilities {

	private boolean wifiAvailable;
	
	private String waterHours;
	
	private boolean cctvAvailable;
	
	private boolean washingMachineAvailable;
	
	private boolean acAvailable;
	
	private boolean gymAvailable;
	
	private boolean parkingAvailable;
	
	private boolean powerBackup;
	
	private boolean commonRoom;
	
	private boolean liftAvailable;
	
	private boolean libraryAvailable;
}
