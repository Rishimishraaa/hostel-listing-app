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
	
		public Optional<Room> findByRoomNumber(String roomNumber);

	
	
}
