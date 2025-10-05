package com.rk.request;

import lombok.Data;

@Data
public class UpdateUserProfileRequest {

	private Long id;
	private String fullName;
	private String email;
	private String phone;
}
