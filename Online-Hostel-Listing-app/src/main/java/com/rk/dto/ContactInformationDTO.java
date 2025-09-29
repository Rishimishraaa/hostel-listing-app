package com.rk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactInformationDTO {
	  private String contactNumber;
	    private String email;
	    private String website;
}
