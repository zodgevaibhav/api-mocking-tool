package org.dnyanyog;

import org.dnyanyog.rule_engine.RuleHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class ApplicationMain {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationMain.class);

    public static void main(String[] args) {
    	
        SpringApplication.run(ApplicationMain.class, args);        
        logger.info("****** Loading rules...");
        RuleHolder.loadRules();
        
    }

}
