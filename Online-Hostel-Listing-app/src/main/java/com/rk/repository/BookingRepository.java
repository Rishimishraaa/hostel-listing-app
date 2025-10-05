package com.rk.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rk.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long>{
	 // User के लिए सभी bookings
    Booking findByStudentId(Long studentId);

    
    // Fetch all active bookings
    List<Booking> findAllByIsActiveTrue();
    
    // User + Hostel के लिए bookings
    Booking findByStudentIdAndHostelId(Long studentId, Long hostelId);

    // Latest booking के लिए order by startDate
    List<Booking> findByStudentIdOrderByStartDateDesc(Long studentId);
    

    // Hostel id से सभी bookings निकालने के लिए
    List<Booking> findByHostelId(Long hostelId);
    
   
    
    // Optional: particular room की bookings
    List<Booking> findByRoomId(Long roomId);
    
    
    // Active bookings whose period covers the given date
    @Query("SELECT b FROM Booking b " +
           "WHERE b.status = 'ACTIVE' " +
           "AND (b.endDate IS NULL OR b.endDate >= :date) " +
           "AND b.startDate <= :date")
    List<Booking> findActiveBookingsAt(@Param("date") LocalDate date);
}
