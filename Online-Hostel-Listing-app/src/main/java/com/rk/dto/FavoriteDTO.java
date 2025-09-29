package com.rk.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteDTO {
	 private Long id;
	    private LocalDateTime createdAt;
	    private Long studentId;
	    private Long hostelId;
}
