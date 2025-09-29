package com.rk.request;

import com.rk.dto.BookingDTO;
import com.rk.dto.StudentDTO;

import lombok.Data;

@Data
public class StudentBookingRequest {
	 private StudentDTO student;
	    private BookingDTO booking;
}
