package org.dnyanyog.rule_engine;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuleHolder {

    private static final Logger logger = LoggerFactory.getLogger(RuleHolder.class);

    
	private static List<Rule> rules = new ArrayList<>();

	public static List<Rule> getRules() {
		return RuleHolder.rules;
	}

	public static void setRules(List<Rule> rules) {
		RuleHolder.rules = rules;
	}

	public static void loadRules()
	{

		try {
			ExcelDataReader.getExcelTableArray(rules);
		} catch (Exception e) {
			logger.error("!!!!!!!!!!! Rule loading failed - \n"+e.getMessage());
			e.printStackTrace();
		}
		
	}

}