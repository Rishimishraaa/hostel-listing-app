package com.rk.response;

import com.rk.entity.Role;

import lombok.Data;

@Data
public class AuthResponse {
	private String username;
	private String message;
	private Role role;
	private String jwt;
}
