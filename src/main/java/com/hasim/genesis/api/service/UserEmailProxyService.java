package com.hasim.genesis.api.service;

import org.springframework.stereotype.Service;

import com.hasim.genesis.api.data.UserEmailResponse;

@Service
public interface UserEmailProxyService {
	
	public UserEmailResponse getUserEmailDetails(String userId);

}
