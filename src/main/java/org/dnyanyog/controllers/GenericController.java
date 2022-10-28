package org.dnyanyog.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dnyanyog.common.Utils;
import org.dnyanyog.processor.ResponseProcessor;
import org.dnyanyog.request.RequestMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GenericController {
	
    private static final Logger logger = LoggerFactory.getLogger(GenericController.class);


	@Autowired
	ResponseProcessor processor;

	@RequestMapping(path = "/**")
	public ResponseEntity<String> genericController(@RequestBody String request, @RequestHeader Map<String, String> headers,
			HttpServletRequest httpRequest, HttpServletResponse response, @RequestParam Map<String,String> requestParam) {

		logger.info("**********"+httpRequest.getMethod()+" Reqeust Received on URI - "+ httpRequest.getRequestURI()+" - "+ request);

		headers.put("method", httpRequest.getMethod());
		headers.put("uri", generateEndPointByAppendingParameter(httpRequest.getRequestURI(),requestParam));

		RequestMeta requestMeta = RequestMeta.buildMeta(request, headers);

		return processor.getResponse(requestMeta, response);

	}
	
	@GetMapping(path = "/**")
	public ResponseEntity<String> genericControllerForGet(@RequestHeader Map<String, String> headers,
			HttpServletRequest httpRequest, HttpServletResponse response, @RequestParam Map<String,String> requestParam) {

		logger.info("**********"+httpRequest.getMethod()+" Reqeust Received on URI - "+ httpRequest.getRequestURI());

		headers.put("method", httpRequest.getMethod());
		headers.put("uri", generateEndPointByAppendingParameter(httpRequest.getRequestURI(),requestParam));

		RequestMeta requestMeta = RequestMeta.buildMeta(null, headers,requestParam);

		return processor.getResponse(requestMeta, response);

	}
	
	private String generateEndPointByAppendingParameter(String uri, Map<String,String> params)
	{        
		if(params.size()==0)
			return uri;
		else
			uri=uri+"?"+Utils.joinMap(params, "=", "&");
		
		return uri;
	}

}
