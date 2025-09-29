package com.rk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
	 private String street;
	    private String landMark;
	    private String city;
	    private String state;
	    private Integer pincode;
}

