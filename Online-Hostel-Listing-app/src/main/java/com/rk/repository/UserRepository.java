package com.rk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rk.entity.Hostel;
import com.rk.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

	public Optional<User> findByEmail(String email);
	
	@Query("SELECT b.student FROM Booking b WHERE b.hostel.id = :hostelId")
	List<User> findAllUsersByHostelId(@Param("hostelId") Long hostelId);


	
}
