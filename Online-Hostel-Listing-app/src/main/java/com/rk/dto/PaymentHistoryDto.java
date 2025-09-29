package com.rk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentHistoryDto {
    private Long id;
    private String studentName;
    private String phone;
    private String room;
    private String month; // "YYYY-MM"
    private Double amount;
    private String status;
}