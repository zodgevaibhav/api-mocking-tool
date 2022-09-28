package org.dnyanyog.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {
	
    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

	
	public static String getRequestFormatType(String reqeust) {
		
		if(reqeust.contains("<") && reqeust.contains(">"))
			return "XML";
		else		
			return "JSON";
	}
	
	

}
