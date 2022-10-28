package org.dnyanyog.test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.dnyanyog.rule_engine.RuleHolder;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

@SpringBootTest
@AutoConfigureMockMvc
public class PrimaryFiltersTest extends AbstractTestNGSpringContextTests{
	
	@Autowired
	MockMvc mockMvc;
	
	@BeforeSuite
	public void beforeSuite()
	{
        RuleHolder.loadRules();
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void getRequestWithOneRquestParam1() throws Exception
	{
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/users?page=1");
		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(jsonPath("$.page").value("1")).andReturn();
		
		requestBuilder = MockMvcRequestBuilders.get("/api/users?page=2");
		result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(jsonPath("$.page").value("2")).andReturn();
		
	}
	
	@Test
	public void getRequestWithOneRquestParam2() throws Exception
	{
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/users?page=2");
		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(jsonPath("$.page").value("2")).andReturn();		
	}
	
	@Test
	public void getRequestWithEndPointParam() throws Exception
	{
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/users/2");
		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(jsonPath("$.data.email").value("janet.weaver@reqres.in")).andReturn();
	}

	@Test
	public void uniqPrimaryFilterExpectHeader() throws Exception
	{
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/users/22");
		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().is4xxClientError()).andReturn();
	}

	@Test
	public void uniqPrimaryFilterExpectMultipleHeader() throws Exception
	{
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/users/32");
		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().is4xxClientError()).andReturn();
		Assert.assertEquals(result.getResponse().getHeader("Age"),"4298","Custom header not found as per expectation");
	}
	
	@Test
	public void getRequestWithOneRquestParamSingleRuleOnReqParam1() throws Exception
	{
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/customer?page=200");
		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(jsonPath("$.page").value("200")).andReturn();		
	}
	
	@Test
	public void getRequestWithOneRquestParamSingleRuleOnReqParam2() throws Exception
	{
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/customer?page=100");
		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(jsonPath("$.page").value("100")).andReturn();		
	}
	
	@Test
	public void getRequestWithOneRquestParamSingleRuleOnReqDuplicateRuleCheck() throws Exception
	{
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/customer?page=150");
		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().is4xxClientError()).andReturn();		
	}

}
