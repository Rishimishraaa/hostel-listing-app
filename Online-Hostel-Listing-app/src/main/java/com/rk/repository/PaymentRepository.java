package com.rk.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rk.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>{
	 List<Payment> findByBookingId(Long bookingId);
	  List<Payment> findByBooking_Hostel_Id(Long hostelId);
	 boolean existsByUniqueKey(String uniqueKey);
}
