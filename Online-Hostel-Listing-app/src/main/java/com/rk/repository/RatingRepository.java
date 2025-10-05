package com.rk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rk.entity.Rating;
import com.rk.entity.ReviewCategory;

public interface RatingRepository extends JpaRepository<Rating, Long>{

public List<Rating> findByStudentId(Long studentId);
Optional<Rating> findByStudentIdAndHostelIdAndCategory(Long studentId, Long hostelId, ReviewCategory category);

}
