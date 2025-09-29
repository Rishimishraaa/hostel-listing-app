package com.rk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDTO {
	private Long id;
	private String studentName;
	private String phone;
	private String room;
	private String month;
	private Double amount;
	private String status;
	private String paidOn;
	private Long studentId;
	private Long bookingId;
	
	

}
