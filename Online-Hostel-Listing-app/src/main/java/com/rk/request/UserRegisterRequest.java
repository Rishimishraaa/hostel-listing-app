package com.rk.request;

import com.rk.entity.Role;

import lombok.Data;

@Data
public class UserRegisterRequest {
	private String fullName;
	private String email;
	private String phone;
	private String password;
	private Role role;
}
