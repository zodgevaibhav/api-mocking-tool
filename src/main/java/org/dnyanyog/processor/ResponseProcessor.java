package org.dnyanyog.processor;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import javax.servlet.http.HttpServletResponse;

import org.dnyanyog.request.RequestMeta;
import org.dnyanyog.rule_engine.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseProcessor {
	private static final Logger logger = LoggerFactory.getLogger(ResponseProcessor.class);
	
	@Autowired
	RuleProcessor ruleProcessor;

	enum HttpMethods {
		POST, GET, DELETE, PUT, PATCH
	}

	public ResponseEntity<String> getResponse(RequestMeta reqeustMeta, HttpServletResponse response) {
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		
		if (!validateRequest(reqeustMeta)) {
			return new ResponseEntity<String>("{}", httpHeaders, HttpStatus.BAD_REQUEST);
		}

//###### Detect rule to be applied

		List<Rule> filteredRules = ruleProcessor.getFilteredRules(reqeustMeta);

		if (filteredRules.isEmpty()) {
			logger.error("!!!!!!! Unable to find any matching rule. Returning ERROR");
			return new ResponseEntity<String>("{}", httpHeaders, HttpStatus.BAD_REQUEST);
		}
		if (filteredRules.size() > 1) {
			logger.error(
					"!!!!!!! Unable to find unique rule for the request. Below are the ruleNames matched with the request");
			filteredRules.forEach(p -> System.out.println(p.getRuleName()));
			return new ResponseEntity<String>("{}", httpHeaders, HttpStatus.BAD_REQUEST);
		}

		Rule detectedRule = filteredRules.get(0);
		logger.info("****** Rule match : " + detectedRule.getRuleName());

//###### Set response header as per expectation		
		setResponseHeaderAsPerExpectedResponse(detectedRule.getExpectedResponseHeader(), httpHeaders);

//###### return response
		return new ResponseEntity<String>(filteredRules.get(0).getExpectedResponse(), httpHeaders,null==httpHeaders.get("status")?HttpStatus.OK:deriveHttpStatus(httpHeaders));
	}


	private HttpStatus deriveHttpStatus(HttpHeaders httpHeaders) {
			System.out.println(httpHeaders.get("status").get(0));
			return HttpStatus.valueOf(Integer.valueOf(httpHeaders.get("status").get(0)));
	}


	public Predicate<String> nameStartingWithPrefix(String nextLine) {
		return new Predicate<String>() {

			@Override
			public boolean test(String t) {
				// System.out.println(t);
				return t.startsWith(nextLine);
			}
		};
	}

	private boolean validateRequest(RequestMeta reqeustMeta) {
		if (null!=reqeustMeta.getRequestBody() && reqeustMeta.getRequestFormat().length() < 1) {
			logger.error("!!!!!!!!!! Reuqest Method is empty");
			return false;
		}

		if (reqeustMeta.getEndPoint().length() < 1) {
			logger.error("!!!!!!!!!! Reuqest Endpoint is empty");
			return false;
		}

		if (null!=reqeustMeta.getRequestBody()&&reqeustMeta.getRequestFormat().contains("POST")
				&& (reqeustMeta.getRequestBody() == null || reqeustMeta.getRequestBody().length() < 1)) {
			logger.error("!!!!!!!!!! Reuqest body is empty for Post Request");
			return false;

		}

		return true;
	}

	private void setResponseHeaderAsPerExpectedResponse(Map<String, String> map, HttpHeaders httpHeaders) {
		for (String s : map.keySet()) {
			switch (s) {
			case "Status":
			case "status":
				try {
					httpHeaders.add("status",Integer.valueOf(map.get(s)).toString());
				} catch (NumberFormatException exception) {
					exception.printStackTrace();
					logger.error(
							"!!!!!!!!!! Expected Response Header contains status code but which does not seems to be valid number please check. Value to be set was - "
									+ map.get(s));
				}
				break;
			default:
				httpHeaders.add(s, map.get(s));
				break;

			}
		}

	}
}
