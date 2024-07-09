package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.spring;

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

import ca.bc.gov.nrs.wfone.common.api.rest.code.endpoints.spring.CodeEndpointsSpringConfig;
import ca.bc.gov.nrs.wfone.common.checkhealth.CheckHealthValidator;
import ca.bc.gov.nrs.wfone.common.checkhealth.CompositeValidator;
import ca.bc.gov.nrs.wfone.common.checkhealth.DatabaseCheckHealthValidator;
import ca.bc.gov.nrs.wfone.common.utils.ApplicationContextProvider;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.spring.ServiceApiSpringConfig;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.parameters.validation.ParameterValidator;

@Configuration
@Import({
	PropertiesSpringConfig.class,
	ServiceApiSpringConfig.class,
	ResourceFactorySpringConfig.class,
	CodeEndpointsSpringConfig.class,
	SecuritySpringConfig.class,
	CirrasPolicyServiceSpringConfig.class
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
	public DataSource cirrasUnderwritingDataSource() {
		DataSource result;
		
		final JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
	    dsLookup.setResourceRef(true);
	    result = dsLookup.getDataSource("java:comp/env/jdbc/cirras_underwriting_rest");
	    
	    return result;
	}
	
	@Bean 
	public DataSource codeTableDataSource() {
		DataSource result;
		
	    result = cirrasUnderwritingDataSource();
	    
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
		result.setComponentIdentifier("CIRRAS_UNDERWRITING_REST");
		result.setComponentName("CIRRAS Underwriting Resource V1 Rest API");
		result.setValidators(healthCheckValidators());
		
		return result;
	}
	

	@Bean()
	public List<CheckHealthValidator> healthCheckValidators() {
		List<CheckHealthValidator> result = new ArrayList<>();

		result.add(databaseCheckHealthValidator());
		
		return result;
	}

	@Bean(initMethod="init")
	public DatabaseCheckHealthValidator databaseCheckHealthValidator() {
		DatabaseCheckHealthValidator result;
		
		result = new DatabaseCheckHealthValidator();
		result.setUsername("proxy_cuws_rest");
		result.setDescription("java:comp/env/jdbc/cirras_underwriting_rest");
		result.setDataSource(cirrasUnderwritingDataSource());
		
		return result;
	}

}
