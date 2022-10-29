package org.dnyanyog.request;

import java.util.HashMap;
import java.util.Map;

import org.dnyanyog.common.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RequestMeta {
	
    private static final Logger logger = LoggerFactory.getLogger(RequestMeta.class);

	
	private String requestBody;
	private String requestMethod;
	private String requestFormat;
	private String endPoint;
	private Map<String,String> requestParam = new HashMap<>();
	private Map<String,String> requestHeader = new HashMap<>();
	
	
	public String getRequestMethod() {
		return requestMethod;
	}
	public String getEndPoint() {
		return endPoint;
	}	
	public String getRequestBody() {
		return requestBody;
	}
	public String getRequestHttpType() {
		return requestMethod;
	}
	public String getRequestFormat() {
		return requestFormat;
	}
	public Map<String, String> getRequestHeaders() {
		return requestHeader;
	}
	
	
	public static RequestMeta buildMeta(String request, Map<String,String> header)
	{
		RequestMeta requestMeta = new RequestMeta();
		
		requestMeta.requestHeader= header;
		requestMeta.requestMethod=header.get("method");
		requestMeta.endPoint=header.get("uri");
		requestMeta.requestBody=request;
		if(null!=request)
		{
			requestMeta.requestFormat=Utils.getRequestFormatType(request);
		}else {
			requestMeta.requestFormat=null;
		}
		
		return requestMeta;
	}
	
	public static RequestMeta buildMeta(String request, Map<String,String> header, Map<String,String> requestParam)
	{
		RequestMeta requestMeta = new RequestMeta();
		
		requestMeta.requestHeader= header;
		requestMeta.requestParam= requestParam;
		requestMeta.requestMethod=header.get("method");
		requestMeta.endPoint=header.get("uri");
		requestMeta.requestBody=request;
		if(null!=request)
		{
			requestMeta.requestFormat=Utils.getRequestFormatType(request);
		}else {
			requestMeta.requestFormat=null;
		}
		
		return requestMeta;
	}
	public Map<String, String> getRequestParams() {
		return requestParam;
	}
}
