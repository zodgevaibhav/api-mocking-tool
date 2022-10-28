package org.dnyanyog.rule_engine;

import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Rule {
	
    private static final Logger logger = LoggerFactory.getLogger(Rule.class);


	private String ruleId;
	private String ruleName;
	private String requestFormat;
	private String requestType;
	private String endPoint;
	private Map<String, String> rulesOnReqBody = new HashedMap<>();
	private Map<String, String> rulesOnReqBodyHeader = new HashedMap<>();
	private Map<String, String> rulesOnReqParam = new HashedMap<>();
	private String expectedResponse;
	public String getExpectedResponse() {
		return expectedResponse;
	}
	

	public Map<String, String> getRulesOnReqParam() {
		return rulesOnReqParam;
	}

	public Map<String, String> getExpectedResponseHeader() {
		return expectedResponseHeader;
	}

	private Map<String, String> expectedResponseHeader = new HashedMap<>();


	public String getRuleId() {
		return ruleId;
	}

	public String getRuleName() {
		return ruleName;
	}

	public String getRequestFormat() {
		return requestFormat;
	}

	public String getRequestType() {
		return requestType;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public Map<String, String> getRulesOnBodyMap() {
		return rulesOnReqBody;
	}

	public Map<String, String> expectedResponseHeaders() {
		return expectedResponseHeader;
	}

	public Rule(String ruleId, String ruleName, String reqeustFormat, String requestType, String endPoints,
			String rulesOnRequestBody, String expectedResponse, String expectedResponseHeader,String rulesOnRequestHeader,String rulesOnRequestParam) {
		this.ruleId = ruleId;
		this.ruleName = ruleName;
		this.requestFormat = reqeustFormat;
		this.requestType = requestType;
		this.endPoint = endPoints;
		this.expectedResponse = expectedResponse;

		if (rulesOnRequestBody.length() > 4) {
			String[] rules = rulesOnRequestBody.split("\n");
			for (String rule : rules) {
				rulesOnReqBody.put(rule.split("->")[0], rule.split("->")[1]);
			}
		}
		
		if (expectedResponseHeader.length() > 4) {
			String[] expectedResponseHeadersArray = expectedResponseHeader.split("\n");
			for (String header : expectedResponseHeadersArray) {
				this.expectedResponseHeader.put(header.split("->")[0], header.split("->")[1]);
			}
		}
		
		if (rulesOnRequestHeader.length() > 4) {
			String[] rulesOnRequestHeaderArray = rulesOnRequestHeader.split("\n");
			for (String header : rulesOnRequestHeaderArray) {
				this.rulesOnReqBodyHeader.put(header.split("->")[0], header.split("->")[1]);
			}
		}
		
		if (rulesOnRequestParam.length() > 4) {
			String[] rulesOnRequestParamArray = rulesOnRequestParam.split("\n");
			for (String rule : rulesOnRequestParamArray) {
				this.rulesOnReqParam.put(rule.split("->")[0], rule.split("->")[1]);
			}
		}
	}

}
