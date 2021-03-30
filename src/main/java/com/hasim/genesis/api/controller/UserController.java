package com.hasim.genesis.api.controller;

import static com.hasim.genesis.api.constant.CommonConstant.REQUEST_TASK_EXECUTOR;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hasim.genesis.api.model.UserVO;
import com.hasim.genesis.api.service.UserFacade;

@Controller
public class UserController {
	private static final Logger logger = LogManager.getLogger(UserController.class);

	@Qualifier(REQUEST_TASK_EXECUTOR)
    @Autowired
    private ConcurrentTaskExecutor requestTaskExecutor;
	@Autowired
	UserFacade userFacade;
	
	@RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(OK)
	@ResponseBody
	public CompletableFuture<List<UserVO>> getUsers(){
		return CompletableFuture.supplyAsync(() -> {
		logger.info("getUsers Started");
		List<UserVO> users = userFacade.getUsers();
		logger.info("getUsers Finished");
		
		
		return users;
		}, requestTaskExecutor);
	}

	@RequestMapping(value = "/user", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public CompletableFuture<UserVO> createUser(@RequestBody UserVO userVO){
		return CompletableFuture.supplyAsync(() -> {
		logger.info("createUser Started");
		UserVO userVOOutput = userFacade.createUser(userVO);
		logger.info("createUser Finished");
		return userVOOutput;
		}, requestTaskExecutor);
	}
}
