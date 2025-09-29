package com.rk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rk.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long>{
	 // User के लिए सभी bookings
    List<Booking> findByStudentId(Long studentId);

    // User + Hostel के लिए bookings
    List<Booking> findByStudentIdAndHostelId(Long studentId, Long hostelId);

    // Latest booking के लिए order by startDate
    List<Booking> findByStudentIdOrderByStartDateDesc(Long studentId);
    

    // Hostel id से सभी bookings निकालने के लिए
    List<Booking> findByHostelId(Long hostelId);
    
   
    
    // Optional: particular room की bookings
    List<Booking> findByRoomId(Long roomId);
}
