package org.dnyanyog.test;

import java.util.LinkedHashMap;
import java.util.Map;

import org.dnyanyog.common.Utils;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UtilTest {
	
	@Test
	public void testMapJoin()
	{		
			Map<String,String> hashMap = new LinkedHashMap<>();
			hashMap.put("firstName", "Vaibhav");
			hashMap.put("lastName", "Zodge");
			hashMap.put("age", "80");
			
			String expectedResult="firstName=Vaibhav&lastName=Zodge&age=80";
			String actualResult=Utils.joinMap(hashMap,"=","&");
			
			Assert.assertEquals(actualResult, expectedResult);
			
	}
	

}
