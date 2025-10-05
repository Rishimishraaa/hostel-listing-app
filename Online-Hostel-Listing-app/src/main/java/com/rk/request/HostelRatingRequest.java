package com.rk.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HostelRatingRequest {

	private Integer score;
	private String category;
	private Long studentId;
	private Long hostelId;
	private String comment;
}
