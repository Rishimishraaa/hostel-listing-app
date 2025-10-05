package com.rk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RazorpayOrderResponse {
    private String orderId;
    private Double amount;
    private String currency;
    private Long paymentId;
    private String studentName;
    private String studentEmail;
    private String studentPhone;
}