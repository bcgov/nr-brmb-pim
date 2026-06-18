package ca.bc.gov.mal.cirras.underwriting.spring;

import ca.bc.gov.nrs.wfone.common.persistence.code.dao.CodeHierarchyConfig;
import ca.bc.gov.nrs.wfone.common.persistence.code.spring.CodePersistenceSpringConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Import({
	CodePersistenceSpringConfig.class
})
public class CodeHierarchySpringConfig {

	private static final Logger logger = LoggerFactory.getLogger(CodeHierarchySpringConfig.class);

	public CodeHierarchySpringConfig() {
		logger.debug("<CodeHierarchySpringConfig");
		
		logger.debug(">CodeHierarchySpringConfig");
	}

	// Beans provided by EndpointsSpringConfig
	@Autowired ResourceBundleMessageSource messageSource;
	
	// Imported Spring Config
	@Autowired
    CodePersistenceSpringConfig codePersistenceSpringConfig;

	@Bean
	public List<CodeHierarchyConfig> codeHierarchyConfigs() {
		List<CodeHierarchyConfig> result = new ArrayList<>();
		return result;
	}
}
