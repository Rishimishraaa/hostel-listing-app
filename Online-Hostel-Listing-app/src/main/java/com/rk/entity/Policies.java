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
public class Policies {

	private String checkInTime;
	
	private String checkOutTime;
	
	private boolean petsAllowed;
	
	private boolean smokingAllowed;
	
	private String visitorPlocy;
	
	private String cancellationPolicy;
	
	private String latePaymentPolicy;
	
}
