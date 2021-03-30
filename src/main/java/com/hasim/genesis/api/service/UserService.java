package com.hasim.genesis.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hasim.genesis.api.model.UserVO;
@Service
public interface UserService {
	
	public List<UserVO> fetchUsers();

	public UserVO createUser(UserVO userVO, String userEmail);

}
