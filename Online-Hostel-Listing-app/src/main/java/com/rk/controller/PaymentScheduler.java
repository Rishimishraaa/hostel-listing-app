package com.rk.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.rk.entity.Booking;
import com.rk.repository.BookingRepository;
import com.rk.repository.PaymentRepository;
import com.rk.serviceImpl.PaymentService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentScheduler {

	private final BookingRepository bookingRepository;
	private final PaymentService paymentService;
	private final PaymentRepository paymentRepository;
	
	  // Run every day at 1 AM
	@Scheduled(cron = "0 0 1 * * *") // हर दिन सुबह 1 बजे check
	public void generateMonthlyPayments() {
		
		LocalDate today = LocalDate.now();
		
	    List<Booking> bookings = bookingRepository.findAllByIsActiveTrue();

	    
	    for (Booking booking : bookings) {
	        LocalDate startDate = booking.getStartDate().toLocalDate();
	        
	        LocalDate nextDue = startDate;
	        
	        while(nextDue.isBefore(today) || nextDue.isEqual(today)) {
	        	paymentService.createMonthlyPayment(booking, today);
	        	nextDue = nextDue.plusMonths(1);
	        }
	        
	        

	    }
	}

}
