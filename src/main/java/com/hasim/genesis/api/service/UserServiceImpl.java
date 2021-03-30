package com.hasim.genesis.api.service;

import static com.hasim.genesis.api.constant.CommonConstant.CIRCUIT_BREAKER_CRTEATE_USER;
import static com.hasim.genesis.api.constant.CommonConstant.CIRCUIT_BREAKER_FETCH_USERS;
import static com.hasim.genesis.api.constant.CommonConstant.FALLBACK_CRTEATE_USER;
import static com.hasim.genesis.api.constant.CommonConstant.FALLBACK_FETCH_USERS;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hasim.genesis.api.entity.User;
import com.hasim.genesis.api.exception.ApplicationSQLException;
import com.hasim.genesis.api.model.UserVO;
import com.hasim.genesis.api.repository.UserRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class UserServiceImpl implements UserService {
	private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

	@Autowired
	UserRepository userRepository;
	
	@Autowired 
	UserEmailProxyService userEmailProxyService;
	
	public UserServiceImpl(UserRepository userRepository){
		this.userRepository = userRepository;
	}

	Function<User, UserVO> functionToConvertUserEntityTOUserVO = new Function<User, UserVO>() {

		@Override
		public UserVO apply(User user) {
			UserVO userVO =new UserVO();
			userVO.setId(user.getId());
			userVO.setName(user.getName());
			userVO.setUserId(user.getUserId());
			return userVO;
		}
	};

	Function<UserVO, User> functionToConvertUserVOTOUserEntity = new Function<UserVO, User>() {

		@Override
		public User apply(UserVO userVO) {
			User user =new User();
			user.setId(userVO.getId());
			user.setName(userVO.getName());
			user.setUserId(userVO.getUserId());
			return user;
		}
	};
	
	@Override
	@CircuitBreaker(name = CIRCUIT_BREAKER_FETCH_USERS, fallbackMethod=FALLBACK_FETCH_USERS)
	public List<UserVO> fetchUsers() {
		logger.info("getUsers started");
		List<User> users = userRepository.findAll();
		List<UserVO> userVOs = users.stream().map(functionToConvertUserEntityTOUserVO).collect(Collectors.toList());
		logger.info("getUsers finished");
		return userVOs;
	}
	public List<UserVO> fallbackFetchUsers(Throwable t) {
		throw new ApplicationSQLException("Error while geting users",t);
		
	}

	@Override
	@CircuitBreaker(name = CIRCUIT_BREAKER_CRTEATE_USER, fallbackMethod=FALLBACK_CRTEATE_USER)
	public UserVO createUser(UserVO userVO, String userEmail) {
		logger.info("createUser started");
		User user =userRepository.saveAndFlush(Optional.ofNullable(userVO).map(functionToConvertUserVOTOUserEntity).map(userEntity->{
			userEntity.setUserEmail(userEmail);
			return userEntity;}).get());
		UserVO createdUser = Optional.ofNullable(user).map(functionToConvertUserEntityTOUserVO).get();
		logger.info("createUser finished");
		return createdUser;
	}
	public UserVO fallbackCreateUser(UserVO userVO, String userEmail, Throwable t) {
		throw new ApplicationSQLException("Error while creating user",t);
		
	}

}
