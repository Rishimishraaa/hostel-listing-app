package com.rk.request;
import lombok.Data;

@Data
public class UserLoginRequest {
	private String email;
	private String password;
}
