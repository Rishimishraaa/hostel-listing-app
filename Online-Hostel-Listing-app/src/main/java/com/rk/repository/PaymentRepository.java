package com.rk.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rk.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>{
	 List<Payment> findByBookingId(Long bookingId);
	  List<Payment> findByBooking_Hostel_Id(Long hostelId);
	 boolean existsByUniqueKey(String uniqueKey);
	   // Check if a payment already exists for a booking in a specific month

	    // Optional: find all pending payments before a date
	    List<Payment> findByStatusAndMonthBefore(String status, LocalDate month);
	    boolean existsByBookingIdAndMonth(Long bookingId, LocalDate month);
	    
	    @Query("SELECT COUNT(p) > 0 FROM Payment p " +
	            "WHERE p.booking.student.id = :studentId " +
	            "AND FUNCTION('MONTH', p.month) = :month " +
	            "AND FUNCTION('YEAR', p.month) = :year")
	     boolean existsByStudentAndMonthAndYear(@Param("studentId") Long studentId,
	                                            @Param("month") int month,
	                                            @Param("year") int year);
}
