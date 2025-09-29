package com.rk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PoliciesDTO {
	  private String checkInTime;
	    private String checkOutTime;
	    private boolean petsAllowed;
	    private boolean smokingAllowed;
	    private String visitorPlocy;
	    private String cancellationPolicy;
	    private String latePaymentPolicy;
}
