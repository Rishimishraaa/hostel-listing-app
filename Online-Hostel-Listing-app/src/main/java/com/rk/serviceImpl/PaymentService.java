package com.rk.serviceImpl;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.rk.entity.Booking;

import com.rk.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import com.rk.repository.*;
import com.rk.entity.*;

@Service
@Data
@RequiredArgsConstructor
public class PaymentService {


	private final UserRepository userRepository;
	private final PaymentRepository paymentRepository;

	
	@Transactional
	public void createMonthlyPayment(Booking booking, LocalDate monthDate) {
	    if (booking.getStudent() == null || !booking.getStudent().getIsActive()) {
	        return; // skip this booking
	    }
	    
	   
	    boolean exists = paymentRepository.existsByStudentAndMonthAndYear(booking.getStudent().getId(), monthDate.getMonthValue(), monthDate.getYear());
	    
	 
	   
	    if(!exists) {
	    	User student = booking.getStudent();
	    	Payment payment = Payment.builder()
	    			.booking(booking)
	    			.month(monthDate)
	    			.amount(booking.getRoomType().getPricePerMonth())
	    			.status("PENDING")
	    			.uniqueKey(booking.getStudent().getId()+"-"+monthDate)
	    			.build();
	    	
	    	paymentRepository.save(payment);
	    	
	    	student.setPaymentStatus("PENDING");
	    	student.setLastPaymentDate(null);
	    	userRepository.save(student);
	    	
	    }

	}
}
