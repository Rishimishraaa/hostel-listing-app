package com.rk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rk.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Long>{
	@Query("SELECT r FROM Room r " +
		       "JOIN r.floor f " +
		       "JOIN f.hostel h " +
		       "WHERE h.id = :hostelId")
		List<Room> findAllRoomsByHostelId(@Param("hostelId") Long hostelId);
	
		public Optional<Room> findByRoomNumberAndRoomTypeHostelId(String roomNumber, Long hostelId);

		
		@Query("""
			    SELECT r.roomType.sharingType, COUNT(r.roomType.id)
			    FROM Room r
			    WHERE r.roomType.hostel.owner.email = :ownerEmail
			    GROUP BY r.roomType.sharingType
			""")
			List<Object[]> getRoomTypeCount(@Param("ownerEmail") String ownerEmail);
	
	
}
