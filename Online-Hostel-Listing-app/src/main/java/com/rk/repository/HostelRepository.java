package com.rk.repository;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rk.entity.Hostel;

public interface HostelRepository extends JpaRepository<Hostel, Long>, JpaSpecificationExecutor<Hostel>{

	 LocalDate date = LocalDate.now(ZoneId.of("Asia/Kolkata"));

	
	public Hostel findByOwnerId(Long id);
	
	
	 @Query("""
		        SELECT COALESCE(AVG(r.score), 0)
		        FROM Rating r
		        WHERE r.hostel.owner.email = :email
		    """)
		    Double findAverageRatingByHostelOwnerEmail(String email);
	 
	 
	 List<Hostel> findDistinctByRoomType_SharingType(Integer sharingType);
	 
	 
	 @Query("""
			    SELECT COALESCE(SUM(p.amount), 0)
			    FROM Payment p
			    JOIN p.booking b
			    JOIN b.room r
			    JOIN r.floor f
			    JOIN f.hostel h
			    WHERE h.id = :hostelId
			      AND p.status = 'PAID'
			      AND p.paidOn BETWEEN :start AND :end
			""")
			Double getTodayRevenue(
			        @Param("hostelId") Long hostelId,
			        @Param("start") LocalDateTime start,
			        @Param("end") LocalDateTime end
			);
	 
	 
	 @Query("""
			    SELECT COALESCE(SUM(p.amount), 0)
			    FROM Payment p
			    JOIN p.booking b
			    JOIN b.room r
			    JOIN r.floor f
			    JOIN f.hostel h
			    WHERE h.id = :hostelId
			      AND p.status = 'PAID'
			      AND p.month = :month
			""")
			Double getMonthlyRevenue(
			        @Param("hostelId") Long hostelId,
			        @Param("month") LocalDate month
			);
	 
	 
	 @Query("""
			    SELECT COUNT(r)
			    FROM Hostel h
			    JOIN h.floors f
			    JOIN f.rooms r
			    WHERE h.id =:hostelId
			      AND r.occupacy < r.capacity
			""")
			Integer getAvailableRoomCount(@Param("hostelId") Long hostelId);
	 
	 
	 @Query("""
			    SELECT COUNT(r)
			    FROM Hostel h
			    JOIN h.floors f
			    JOIN f.rooms r
			    WHERE h.id =:hostelId
			""")
			Integer getAllRoomCount(@Param("hostelId") Long hostelId);

	 
	 @Query("""
			    SELECT COUNT(r)
			    FROM Hostel h
			    JOIN h.ratings r
			    WHERE h.id =:hostelId
			""")
			Integer getAllReviews(@Param("hostelId") Long hostelId);

	 
	 
	 

	 
}
