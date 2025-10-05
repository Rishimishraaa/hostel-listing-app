package com.rk.dto;

import java.time.LocalDateTime;

import com.rk.entity.ReviewCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingDTO {
	 private Long id;
	 private String hostelName;
	 private String comment;
	    private LocalDateTime createdAt;
	    private Integer score;
	    private String category;
	    private Long studentId;
	    private Long hostelId;
	    private String studentName;
}
