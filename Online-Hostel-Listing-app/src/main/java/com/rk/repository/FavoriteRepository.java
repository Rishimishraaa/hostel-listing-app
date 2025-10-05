package com.rk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rk.entity.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Long>{

	List<Favorite> findByStudentId(Long studentId);
	Favorite findByStudentIdAndHostelId(Long studentId, Long hostelId);
	
	
}
