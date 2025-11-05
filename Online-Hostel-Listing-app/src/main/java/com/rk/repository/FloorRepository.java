package com.rk.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rk.entity.Floor;

public interface FloorRepository extends JpaRepository<Floor,Long>{
	
	Optional<Floor> findByFloorNumberAndHostelId(int floorNumber, Long hostelId);
}
