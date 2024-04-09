package ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1.CirrasUnderwritingPoliciesListenerService;
import ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1.impl.CirrasUnderwritingPoliciesListenerServiceImpl;
import ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1.model.factory.SyncUwPoliciesFactory;

//PIM-1156: Disabling all code related to cirras-land-management-api so it can be shutdown.
//import ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1.CirrasUnderwritingLandManagementListenerService;
//import ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1.impl.CirrasUnderwritingLandManagementListenerServiceImpl;
//import ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1.model.factory.SyncUwLandManagementFactory;

import ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1.validation.ModelValidator;

@Configuration
public class ServiceApiSpringConfig {

	private static final Logger logger = LoggerFactory.getLogger(ServiceApiSpringConfig.class);
	
	public ServiceApiSpringConfig() {
		logger.debug("<ServiceApiSpringConfig");
		
		logger.debug(">ServiceApiSpringConfig");
	}

	// Beans provided by EndpointsSpringConfig
	@Autowired ResourceBundleMessageSource messageSource;
	
	@Autowired CirrasUnderwritingService cirrasUnderwritingService;
	@Autowired SyncUwPoliciesFactory syncUwPoliciesFactory;

	//PIM-1156: Disabling all code related to cirras-land-management-api so it can be shutdown.
	//@Autowired SyncUwLandManagementFactory syncUwLandManagementFactory;

	@Bean
	public ModelValidator modelValidator() {
		ModelValidator result;
		
		result = new ModelValidator();
		result.setMessageSource(messageSource);
		
		return result;
	}

	@Bean()
	public CirrasUnderwritingPoliciesListenerService cirrasUnderwritingPoliciesListenerService() {
		CirrasUnderwritingPoliciesListenerServiceImpl result = new CirrasUnderwritingPoliciesListenerServiceImpl();
		
		result.setCirrasUnderwritingService(cirrasUnderwritingService);
		
		result.setSyncUwPoliciesFactory(syncUwPoliciesFactory);
		
		return result;
	}

	//PIM-1156: Disabling all code related to cirras-land-management-api so it can be shutdown.
/*	
	@Bean()
	public CirrasUnderwritingLandManagementListenerService cirrasUnderwritingLandManagementListenerService() {
		CirrasUnderwritingLandManagementListenerServiceImpl result = new CirrasUnderwritingLandManagementListenerServiceImpl();
		
		result.setCirrasUnderwritingService(cirrasUnderwritingService);
		
		result.setSyncUnderwritingLandManagementFactory(syncUwLandManagementFactory);
		
		return result;
	}
*/
}
