package org.dnyanyog.processor;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.tomcat.util.digester.Rules;
import org.dnyanyog.request.RequestMeta;
import org.dnyanyog.rule_engine.Rule;
import org.dnyanyog.rule_engine.RuleHolder;
import org.springframework.stereotype.Component;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

@Component
public class RuleProcessor {

	public List<Rule> getFilteredRules(RequestMeta requestMetaData) {
		// **** Skip of rules on the basis of what information available
		Object[] arr = RuleHolder.getRules().toArray();
		Stream<Rule> filteredRulesIterator = RuleHolder.getRules().stream();
		
		
		//***** Request Format
		if (null != requestMetaData.getRequestFormat()) {
			System.out.println("********** Inside request format rule");

			filteredRulesIterator = filteredRulesIterator
					.filter(p -> p.getRequestFormat().equals(requestMetaData.getRequestFormat()))
					.map(p -> null != p ? p : null);
		}
		
		//***** Request End Point
		filteredRulesIterator = filteredRulesIterator.filter(p -> p.getEndPoint().equals(requestMetaData.getEndPoint()))
				.filter(p -> p.getRequestType().equals(requestMetaData.getRequestHttpType()))
				.map(p -> null != p ? p : null);

		//***** Request Param Rule
		filteredRulesIterator = filteredRulesIterator.filter(p -> matchRulesOnRequstParameter(p, requestMetaData))
				.map(p -> null != p ? p : null);
		
		//***** Request Header Rule
		filteredRulesIterator = filteredRulesIterator.filter(p -> matchRequestHeaderRules(p, requestMetaData))
				.map(p -> null != p ? p : null);
		
		
		//***** Request Body Rule		
		if (null != requestMetaData.getRequestBody()) {
			System.out.println("********** Inside body rule");
			filteredRulesIterator = filteredRulesIterator
										.filter(p -> ruleOnBodyMatched(p, requestMetaData))
										.sorted(Comparator.comparingInt(Rule::getBodyRulesCount)) //planning to pick Rule which higher number of rulesOnBody. Non functional yet
										.map(p -> null != p ? p : null);
		}

		List<Rule> filteredRules = filteredRulesIterator.collect(Collectors.toList());

		return filteredRules;
	}

	private boolean matchRequestHeaderRules(Rule p, RequestMeta requestMetaData) {
		if (p.getRulesOnReqBodyHeader().size() <= 0) // If there is no req param rule in rule in context then skip this check
			return true;
		
		if(requestMetaData.getRequestHeaders().size()<=0)
			return false;
			
		for (String keys : p.getRulesOnReqBodyHeader().keySet()) {
			try {

				if (requestMetaData.getRequestHeaders().containsKey(keys)) { // To apply current rule in context, req param must have the param
					if (!p.getRulesOnReqBodyHeader().get(keys).contains(requestMetaData.getRequestHeaders().get(keys))) { // Rule param value should match with req param value
						return false;
					}
				}else { // If the rule param is not present in request param then do not apply the rule
					return false;
				}
			} catch (Exception e) {
				System.out.println("!!!!!!!!!! Somthing wrong in applying request header rule for rule - " + p.getRuleName());
				return false;
			}
		}
		
		return true;
	}

	private boolean matchRulesOnRequstParameter(Rule p, RequestMeta requestMetaData) {
		
		if (p.getRulesOnReqParam().size() <= 0) // If there is no req param rule in rule in context then skip this check
			return true;

		if (requestMetaData.getRequestParams().size() <= 0)// If there is no req param in request then skip this rule
			return false;

		for (String keys : p.getRulesOnReqParam().keySet()) {
			try {

				if (requestMetaData.getRequestParams().containsKey(keys)) { // To apply current rule in context, req param must have the param
					if (!p.getRulesOnReqParam().get(keys).contains(requestMetaData.getRequestParams().get(keys))) { // Rule param value should match with req param value
						return false;
					}
				}else { // If the rule param is not present in request param then do not apply the rule
					return false;
				}
			} catch (Exception e) {
				System.out.println("!!!!!!!!!! Somthing wrong in applying request parameter rule for rule - " + p.getRuleName());
				return false;
			}
		}
		return true;
	}

	private boolean ruleOnBodyMatched(Rule rule, RequestMeta reqeustMeta) {

		switch (reqeustMeta.getRequestFormat()) {
		case "JSON":

			DocumentContext jsonContext = JsonPath.parse(reqeustMeta.getRequestBody());
			for (String key : rule.getRulesOnBodyMap().keySet()) {
				if (!jsonContext.read(key).toString().equals(rule.getRulesOnBodyMap().get(key)))
					return false;
			}
			break;

		case "XML":

			break;
		default:
			return false;

		}

		return true;
	}
	

}
