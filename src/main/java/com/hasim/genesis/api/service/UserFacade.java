package com.hasim.genesis.api.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hasim.genesis.api.data.UserEmailResponse;
import com.hasim.genesis.api.model.UserVO;

@Component
public class UserFacade {
	private static final Logger logger = LogManager.getLogger(UserFacade.class);
	@Autowired
	UserService userService;
	
	@Autowired
	UserEmailProxyService userEmailProxyService;

	
	public List<UserVO> getUsers() {
		logger.info("getUsers started");
		List<UserVO> userVOs = userService.fetchUsers();
		logger.info("getUsers finished");
		return userVOs;
	}

	public UserVO createUser(UserVO userVO) {
		logger.info("createUser started");
		UserEmailResponse  userEmailResponse  = userEmailProxyService.getUserEmailDetails(userVO.getUserId());
		UserVO createdUser = userService.createUser(userVO,userEmailResponse!=null?userEmailResponse.getEmail():null);
		logger.info("createUser finished");
		return createdUser;
	}

}
