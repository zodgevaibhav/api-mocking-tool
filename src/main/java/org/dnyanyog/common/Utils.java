package org.dnyanyog.common;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.collections4.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {
	
    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

	
	public static String getRequestFormatType(String reqeust) {
		
		if(null!=reqeust && reqeust.contains("<") && reqeust.contains(">"))
			return "XML";
		else		
			return "JSON";
	}
	
	public static String joinMap(Map<String,String> map,String delimiter,String divider)
	{
		
		String joinedString="";
		Set<String> keySet = map.keySet();
		String[] keyArray = new String[keySet.size()];
		
		keySet.toArray(keyArray);
		
		
		joinedString=joinedString.concat(keyArray[0]+delimiter+map.get(keyArray[0]));
		if(keyArray.length>1)
		{
			for(int i=1;i<keyArray.length;i++)
			{
				joinedString=joinedString.concat(divider+keyArray[i]+delimiter+map.get(keyArray[i]));
			}
		}
		
		
		return joinedString;
	}
	
	
	
	

}
