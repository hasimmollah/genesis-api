package com.hasim.genesis.api.tdd;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hasim.genesis.api.model.UserVO;
import com.hasim.genesis.api.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("IntegrationTest")
public class UserControllerTest {
	
	
	
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	UserService userService;

	@Before
	public void setup() {
	}
	
	
	@Test
	public void shouldReturnAllUserWhenHitGetEndpoint() throws Exception{
		
		List<UserVO> users = new ArrayList();
		UserVO user1 = new UserVO();
		user1.setName("hasim");
		users.add(user1);
		UserVO user2 = new UserVO();
		users.add(user2);
		user2.setName("abdul");
		
		Mockito.when(userService.fetchUsers()).thenReturn(users);
		final HttpHeaders outboundRequestHeaders = new HttpHeaders();
		 outboundRequestHeaders.set("Origin", "localhost");
		 final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users").header("Origin", "localhost").contentType(MediaType.APPLICATION_JSON)).andExpect(request().asyncStarted()).andReturn();
		mockMvc.perform(asyncDispatch(mvcResult)).andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$.[0].name",is("hasim")));;
	}
	
	
	@Test
	public void shouldCreateUserWhenHitPostEndpoint() throws Exception {
		UserVO userVO = new UserVO();
		userVO.setName("test");
		ObjectMapper objectMapper = new ObjectMapper();
		UserVO user2 = new UserVO();
		user2.setId(1);
		user2.setName("hasim");
		Mockito.when(userService.createUser(Mockito.any(UserVO.class), Mockito.any())).thenReturn(user2);
		 final HttpHeaders outboundRequestHeaders = new HttpHeaders();
		 outboundRequestHeaders.set("Origin", "localhost");
		 final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/user").headers(outboundRequestHeaders).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userVO))).andExpect(request().asyncStarted()).andReturn();
		mockMvc.perform(asyncDispatch(mvcResult)).andExpect(jsonPath("$.name", is(user2.getName())));
		
	}


}
