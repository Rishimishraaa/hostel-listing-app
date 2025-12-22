package com.rk.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    
    
    // pending booking ke liye
    @Query("""
    	    SELECT COUNT(b)
    	    FROM Booking b
    	    WHERE b.hostel.id = :hostelId
    	      AND b.status = :status
    	""")
    	Long findPendingBooking(
    	        @Param("hostelId") Long hostelId,
    	        @Param("status") String status
    	);

   
    
    // Optional: particular room की bookings
    List<Booking> findByRoomId(Long roomId);
    
    
    // Active bookings whose period covers the given date
    @Query("SELECT b FROM Booking b " +
           "WHERE b.status = 'ACTIVE' " +
           "AND (b.endDate IS NULL OR b.endDate >= :date) " +
           "AND b.startDate <= :date")
    List<Booking> findActiveBookingsAt(@Param("date") LocalDate date);
    
    
    @Query("""
    	    SELECT 
    	        FUNCTION('MONTHNAME', MIN(b.startDate)) AS monthName,
    	        FUNCTION('YEAR', MIN(b.startDate)) AS year,
    	        COUNT(b.id) AS bookingCount
    	    FROM Booking b
    	    WHERE b.hostel.owner.email = :ownerEmail
    	    GROUP BY FUNCTION('YEAR', b.startDate), FUNCTION('MONTH', b.startDate)
    	    ORDER BY year ASC, FUNCTION('MONTH', b.startDate) ASC
    	""")
    	List<Object[]> getMonthlyBookingCount(@Param("ownerEmail") String ownerEmail);

    	 @Query("""
    		        SELECT COUNT(DISTINCT b.student.id)
    		        FROM Booking b
    		        WHERE b.hostel.owner.email = :ownerEmail
    		    """)
    		    Long countUsersByOwnerEmail(@Param("ownerEmail") String ownerEmail);
    	 
    	 
    	 @Query("""
 	            SELECT COALESCE(SUM(b.totalAmount), 0)
 	            FROM Booking b
 	            WHERE b.hostel.owner.email = :ownerEmail
 	        """)
 	        Double findTotalEarningsByOwnerEmail(@Param("ownerEmail") String ownerEmail);
    	 
    	 
    	 @Query("""
    			    SELECT COUNT(b)
    			    FROM Booking b
    			    WHERE b.hostel.id = :hostelId
    			      AND b.startDate BETWEEN :start AND :end
    			""")
    			Long countNewBookings(
    			        @Param("hostelId") Long hostelId,
    			        @Param("start") LocalDateTime start,
    			        @Param("end") LocalDateTime end
    			);

}
