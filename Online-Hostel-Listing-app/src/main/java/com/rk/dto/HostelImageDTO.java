package com.rk.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostelImageDTO {
	   private Long id;
	    private String imageUrl;
	    private Long hostelId;
}
