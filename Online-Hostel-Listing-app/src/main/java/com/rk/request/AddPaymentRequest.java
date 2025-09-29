package com.rk.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddPaymentRequest {

	private String month;
	private Double amount;
	private String status;
	private Long studentId;
	
	private Long roomId;
	private Long hostelId;
}
