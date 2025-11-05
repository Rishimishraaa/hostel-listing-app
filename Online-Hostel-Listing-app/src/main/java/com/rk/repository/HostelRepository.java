package com.rk.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rk.entity.Hostel;

public interface HostelRepository extends JpaRepository<Hostel, Long>{

	public Hostel findByOwnerId(Long id);
	
	
	 @Query("""
		        SELECT COALESCE(AVG(r.score), 0)
		        FROM Rating r
		        WHERE r.hostel.owner.email = :email
		    """)
		    Double findAverageRatingByHostelOwnerEmail(String email);
	 
	 
	 List<Hostel> findDistinctByRoomType_SharingType(Integer sharingType);
}
