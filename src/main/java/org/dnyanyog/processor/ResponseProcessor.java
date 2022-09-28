package org.dnyanyog.processor;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import org.dnyanyog.request.RequestMeta;
import org.dnyanyog.rule_engine.Rule;
import org.dnyanyog.rule_engine.RuleHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

@Component
public class ResponseProcessor {
	private static final Logger logger = LoggerFactory.getLogger(ResponseProcessor.class);

	enum HttpMethods {
		POST, GET, DELETE, PUT, PATCH
	}

	public String getResponse(RequestMeta reqeustMeta, HttpServletResponse response) {

		if (!validateRequest(reqeustMeta)) {
			return "ERROR";
		}

//###### Detect rule to be applied

		Stream<Rule> filteredRulesIterator = RuleHolder.getRules().stream()
				.filter(p -> p.getEndPoint().equals(reqeustMeta.getEndPoint()))
				.filter(p -> p.getRequestType().equals(reqeustMeta.getRequestHttpType()))
				.filter(p -> p.getRequestFormat().equals(reqeustMeta.getRequestFormat()))
				.filter(p -> ruleOnBodyMatched(p, reqeustMeta)).map(p -> null != p ? p : null);

		List<Rule> filteredRules = filteredRulesIterator.collect(Collectors.toList());

		if (filteredRules.isEmpty()) {
			logger.error("!!!!!!! Unable to find any matching rule. Returning ERROR");
			return "ERROR";
		}
		if (!filteredRules.isEmpty() && filteredRules.size() > 1) {
			logger.error(
					"!!!!!!! Unable to find unique rule for the request. Below are the ruleNames matched with the request");
			filteredRules.forEach(p -> System.out.println(p.getRuleName()));
			return "ERROR";
		}

		Rule detectedRule = filteredRules.get(0);
		logger.info("****** Rule match : " + detectedRule.getRuleName());

//###### Set response header as per expectation		
		setResponseHeaderAsPerExpectedResponse(detectedRule.getExpectedResponseHeader(), response);

//###### return response
		return filteredRules.get(0).getExpectedResponse();
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

	public static Predicate<String> nameStartingWithPrefix(String nextLine) {
		return new Predicate<String>() {

			@Override
			public boolean test(String t) {
				// System.out.println(t);
				return t.startsWith(nextLine);
			}
		};
	}

	private boolean validateRequest(RequestMeta reqeustMeta) {
		if (reqeustMeta.getRequestFormat().length() < 1) {
			logger.error("!!!!!!!!!! Reuqest Method is empty");
			return false;
		}

		if (reqeustMeta.getEndPoint().length() < 1) {
			logger.error("!!!!!!!!!! Reuqest Endpoint is empty");
			return false;
		}

		if (reqeustMeta.getRequestFormat().contains("POST")
				&& (reqeustMeta.getRequestBody() == null || reqeustMeta.getRequestBody().length() < 1)) {
			logger.error("!!!!!!!!!! Reuqest body is empty for Post Request");
			return false;

		}

		return true;
	}

	private static void setResponseHeaderAsPerExpectedResponse(Map<String, String> map, HttpServletResponse response) {
		for (String s : map.keySet()) {
			switch (s) {
			case "Status":
			case "status":
				try {
					response.setStatus(Integer.valueOf(map.get(s)));
				} catch (NumberFormatException exception) {
					exception.printStackTrace();
					logger.error(
							"!!!!!!!!!! Expected Response Header contains status code but which does not seems to be valid number please check. Value to be set was - "
									+ map.get(s));
				}
				break;
			default:
				response.setHeader(s, map.get(s));
				break;

			}
		}

	}
}
