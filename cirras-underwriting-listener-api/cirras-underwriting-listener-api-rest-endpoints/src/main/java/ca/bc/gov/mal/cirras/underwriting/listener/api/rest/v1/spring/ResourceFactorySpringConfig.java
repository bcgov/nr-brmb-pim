package ca.bc.gov.mal.cirras.underwriting.listener.api.rest.v1.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//import ca.bc.gov.mal.cirras.underwriting.listener.api.rest.v1.resource.factory.SyncUwLandManagementRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.listener.api.rest.v1.resource.factory.SyncUwPoliciesRsrcFactory;

@Configuration
public class ResourceFactorySpringConfig {

	private static final Logger logger = LoggerFactory.getLogger(ResourceFactorySpringConfig.class);
	
	public ResourceFactorySpringConfig() {
		logger.info("<ResourceFactorySpringConfig");
		
		logger.info(">ResourceFactorySpringConfig");
	}
	
	@Bean
	public SyncUwPoliciesRsrcFactory syncUwPoliciesRsrcFactory() {
		SyncUwPoliciesRsrcFactory result = new SyncUwPoliciesRsrcFactory();
		return result;
	}

/*
	PIM-1156: Disabling all code related to cirras-land-management-api so it can be shutdown.

	@Bean
	public SyncUwLandManagementRsrcFactory syncUwLandManagementRsrcFactory() {
		SyncUwLandManagementRsrcFactory result = new SyncUwLandManagementRsrcFactory();
		return result;
	}
*/
}
