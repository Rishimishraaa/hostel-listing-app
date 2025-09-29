package com.rk.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.rk.entity.Hostel;

public interface HostelRepository extends JpaRepository<Hostel, Long>{

	public Hostel findByOwnerId(Long id);
}
