package com.rk.service;

import com.rk.dto.UserDTO;
import com.rk.entity.User;

public interface UserService {

	public UserDTO getUser(String jwt) throws Exception;
}
