package com.rk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rk.entity.RoomType;

public interface RoomTypeRepository extends JpaRepository<RoomType, Long>{

	List<RoomType> findByHostelId(Long id);
	Optional<RoomType> findBySharingTypeAndHostelId(int sharingType, Long hostelId);

}
