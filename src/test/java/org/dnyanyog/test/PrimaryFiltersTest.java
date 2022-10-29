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
		mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(jsonPath("$.page").value("1")).andReturn();
		
		requestBuilder = MockMvcRequestBuilders.get("/api/users?page=2");
		mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(jsonPath("$.page").value("2")).andReturn();
		
	}
	
	@Test
	public void getRequestWithOneRquestParam2() throws Exception
	{
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/users?page=2");
		mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(jsonPath("$.page").value("2")).andReturn();		
	}
	
	@Test
	public void getRequestWithEndPointParam() throws Exception
	{
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/users/2");
		mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(jsonPath("$.data.email").value("janet.weaver@reqres.in")).andReturn();
	}

	@Test
	public void uniqPrimaryFilterExpectHeader() throws Exception
	{
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/users/22");
		mockMvc.perform(requestBuilder).andExpect(status().is4xxClientError()).andReturn();
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
		mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(jsonPath("$.page").value("200")).andReturn();		
	}
	
	@Test
	public void getRequestWithOneRquestParamSingleRuleOnReqParam2() throws Exception
	{
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/customer?page=100");
		mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(jsonPath("$.page").value("100")).andReturn();		
	}
	
	@Test
	public void getRequestWithOneRquestParamSingleRuleOnReqDuplicateRuleCheck() throws Exception
	{
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/customer?page=150");
		mockMvc.perform(requestBuilder).andExpect(status().is4xxClientError()).andReturn();		
	}
	
	@Test
	public void getRequestWithOneRquestParamSingleRuleOnRequestHeader() throws Exception
	{
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/customer").header("token", "active");
		
		mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(jsonPath("$.token").value("active")).andReturn();		
	}
	
	@Test
	public void getRequestWithOneRquestParamTwoRuleOnRequestHeader() throws Exception
	{
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/customer").header("token", "locked").header("page", "100");
		
		mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(jsonPath("$.token").value("locked")).andExpect(jsonPath("$.page").value("100")).andReturn();		
	}
	
	@Test
	public void postRequestWithOneRuleOnBody() throws Exception
	{
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/customer").content("{\n"
				+ "  \"token\":\"inactive\",\n"
				+ "  \"page\": 130,\n"
				+ "  \"per_page\": 6,\n"
				+ "  \"total\": 12,\n"
				+ "  \"total_pages\": 2,\n"
				+ "  \"data\": [\n"
				+ "    {\n"
				+ "      \"id\": 1,\n"
				+ "      \"email\": \"george.bluth@reqres.in\",\n"
				+ "      \"first_name\": \"George\",\n"
				+ "      \"last_name\": \"Bluth\",\n"
				+ "      \"avatar\": \"https://reqres.in/img/faces/1-image.jpg\"\n"
				+ "    }\n"
				+ "  ],\n"
				+ "  \"support\": {\n"
				+ "    \"url\": \"https://reqres.in/#support-heading\",\n"
				+ "    \"text\": \"To keep ReqRes free, contributions towards server costs are appreciated!\"\n"
				+ "  }\n"
				+ "}");
		
		mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(jsonPath("$.token").value("inactive")).andExpect(jsonPath("$.page").value("130")).andReturn();		
	}

	@Test
	public void postRequestWithTwoRulesOnBody() throws Exception
	{
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/customer").content("{\n"
				+ "  \"token\":\"expired\",\n"
				+ "  \"page\": 125,\n"
				+ "  \"per_page\": 6,\n"
				+ "  \"total\": 12,\n"
				+ "  \"total_pages\": 2,\n"
				+ "  \"data\": [\n"
				+ "    {\n"
				+ "      \"id\": 1,\n"
				+ "      \"email\": \"george.bluth@reqres.in\",\n"
				+ "      \"first_name\": \"George\",\n"
				+ "      \"last_name\": \"Bluth\",\n"
				+ "      \"avatar\": \"https://reqres.in/img/faces/1-image.jpg\"\n"
				+ "    }\n"
				+ "  ],\n"
				+ "  \"support\": {\n"
				+ "    \"url\": \"https://reqres.in/#support-heading\",\n"
				+ "    \"text\": \"To keep ReqRes free, contributions towards server costs are appreciated!\"\n"
				+ "  }\n"
				+ "}");
		
		mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(jsonPath("$.token").value("inactive")).andExpect(jsonPath("$.page").value("130")).andReturn();		
	}
}
