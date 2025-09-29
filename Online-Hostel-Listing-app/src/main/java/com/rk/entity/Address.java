package com.rk.entity;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Address {
	private String street;
	private String landMark;
	private String city;
	private String state;
	private Integer pincode;
}
