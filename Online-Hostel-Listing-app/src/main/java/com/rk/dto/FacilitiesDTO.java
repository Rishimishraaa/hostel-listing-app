package com.rk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FacilitiesDTO {
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
