package ca.bc.gov.mal.cirras.underwriting.listener.api.rest.v1.spring;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1.spring.ServiceApiSpringConfig;
import ca.bc.gov.mal.cirras.underwriting.listener.api.rest.v1.parameters.validation.ParameterValidator;
import ca.bc.gov.mal.cirras.underwriting.listener.policies.event.listener.v1.spring.PoliciesMessageListenerSpringConfig;
//import ca.bc.gov.mal.cirras.underwriting.listener.land.management.event.listener.v1.spring.LandMgmtMessageListenerSpringConfig;
import ca.bc.gov.nrs.wfone.common.checkhealth.CheckHealthValidator;
import ca.bc.gov.nrs.wfone.common.checkhealth.CompositeValidator;
import ca.bc.gov.nrs.wfone.common.utils.ApplicationContextProvider;

@Configuration
@Import({
	PropertiesSpringConfig.class,
	ServiceApiSpringConfig.class,
	ResourceFactorySpringConfig.class,
	SecuritySpringConfig.class,
	PoliciesMessageListenerSpringConfig.class,
	//LandMgmtMessageListenerSpringConfig.class,  PIM-1156: Disabling all code related to cirras-land-management-api so it can be shutdown.
	CirrasUnderwritingServiceSpringConfig.class
})
public class EndpointsSpringConfig {

	private static final Logger logger = LoggerFactory.getLogger(EndpointsSpringConfig.class);
	
	public EndpointsSpringConfig() {
		logger.info("<EndpointsSpringConfig");
		
		logger.info(">EndpointsSpringConfig");
	}
	
	@Bean
	public ApplicationContextProvider applicationContextProvider() {
		ApplicationContextProvider result;
		
		result = new ApplicationContextProvider();
		
		return result;
	}
	
	@Primary
	@Bean
	public DataSource failoverDataSource() {
		DataSource result;
		
		final JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
	    dsLookup.setResourceRef(true);
	    result = dsLookup.getDataSource("java:comp/env/jdbc/failover");
	    
	    return result;
	}

	@Bean
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource result;
		
		result = new ResourceBundleMessageSource();
		result.setBasename("messages");
		
		return result;
	}

	@Bean
	public ParameterValidator parameterValidator() {
		ParameterValidator result;
		
		result = new ParameterValidator();
		result.setMessageSource(messageSource());
		
		return result;
	}

	@Bean(initMethod="init")
	public CompositeValidator checkHealthValidator() {
		CompositeValidator result;
		
		result = new CompositeValidator();
		result.setComponentIdentifier("CIRRAS_UNDERWRITING_LISTENER_API");
		result.setComponentName("CIRRAS Underwriting Listener API");
		result.setValidators(healthCheckValidators());
		
		return result;
	}

	@Bean()
	public List<CheckHealthValidator> healthCheckValidators() {
		List<CheckHealthValidator> result = new ArrayList<>();
		
		return result;
	}
	
}
