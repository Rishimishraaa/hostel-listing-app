package com.rk.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String paymentStatus;
    private String currentRoomNumber;
    private List<PaymentDTO> payments;
    private String joiningDate;
    private String lastPaymentDate;
    private Long hostelId;
    private Long roomId;
    private String imageUrl;
}
