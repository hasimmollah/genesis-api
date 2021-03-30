package com.hasim.genesis.api.tdd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.hasim.genesis.api.entity.User;
import com.hasim.genesis.api.model.UserVO;
import com.hasim.genesis.api.repository.UserRepository;
import com.hasim.genesis.api.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("IntegrationTest")
public class UserServiceTest {
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserRepository userRepository;

	@Before
	public void setup() {
	}
	
	
	@Test
	public void shouldReturnAllUserWhenHitGetEndpoint() throws Exception{
		List<User> users = new ArrayList();
		User user1 = new User();
		users.add(user1);
		User user2 = new User();
		users.add(user2);
		userRepository.saveAll(users);
		userRepository.flush();
		assertEquals(userService.fetchUsers().size(), 2);
	}
	
	@Test
	public void shouldReturnAllUserWhenHitPostEndpoint() throws Exception{
		UserVO userVO = new UserVO();
		userVO.setName("hasim");
		assertNotNull(userService.createUser(userVO,"test@gs.com").getId());
	}


}
