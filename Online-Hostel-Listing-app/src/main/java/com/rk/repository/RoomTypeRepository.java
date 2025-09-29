package com.rk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rk.entity.RoomType;

public interface RoomTypeRepository extends JpaRepository<RoomType, Long>{

	List<RoomType> findByHostelId(Long id);

}
