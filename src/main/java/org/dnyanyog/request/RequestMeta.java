package org.dnyanyog.request;

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
	
	public String getRequestMethod() {
		return requestMethod;
	}
	public String getEndPoint() {
		return endPoint;
	}


	private Map<String, String> headers;
	
	public String getRequestBody() {
		return requestBody;
	}
	public String getRequestHttpType() {
		return requestMethod;
	}
	public String getRequestFormat() {
		return requestFormat;
	}
	public Map<String, String> getHeaders() {
		return headers;
	}
	
	
	public static RequestMeta buildMeta(String request, Map<String,String> header)
	{
		RequestMeta requestMeta = new RequestMeta();
		
		requestMeta.headers= header;
		requestMeta.requestFormat=Utils.getRequestFormatType(request);
		requestMeta.requestBody=request;
		requestMeta.requestMethod=header.get("method");
		requestMeta.endPoint=header.get("uri");
		
		return requestMeta;
	}
	

}
