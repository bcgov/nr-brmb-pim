package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.spring;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import ca.bc.gov.webade.spring.bootstrap.BootstrapPropertiesFactory;

@Configuration
public class PropertiesSpringConfig {

	private static final Logger logger = LoggerFactory.getLogger(PropertiesSpringConfig.class);
	
	public PropertiesSpringConfig() {
		logger.info("<PropertiesSpringConfig");
		
		logger.info(">PropertiesSpringConfig");
	}

	// @Bean
	// public static DataSource bootstrapDataSource() {
	// 	DataSource result;
		
	// 	final JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
	//     dsLookup.setResourceRef(true);
	//     result = dsLookup.getDataSource("java:comp/env/jdbc/webade_bootstrap");
	    
	//     return result;
	// }
	
	// @Bean
	// public static BootstrapPropertiesFactory bootstrapPropertiesFactory() {
	// 	BootstrapPropertiesFactory result;
		
	// 	result = new BootstrapPropertiesFactory(bootstrapDataSource());
		
	// 	return result;
	// }

	// @Bean
	// public static Properties bootstrapProperties() throws SQLException {
	// 	Properties result;
		
	// 	result = bootstrapPropertiesFactory().getApplicationProperties("CIRRAS_UNDERWRITING", "bootstrap-config");
		
	// 	return result;
	// }

	@Bean
	public static Properties applicationProperties() throws IOException, SQLException {
		Properties result;
		
		PropertiesFactoryBean propertiesFactory = new PropertiesFactoryBean();
		propertiesFactory.setLocalOverride(true);
		// propertiesFactory.setPropertiesArray(bootstrapProperties());
		propertiesFactory.setLocation(new ClassPathResource("static.properties"));
		propertiesFactory.afterPropertiesSet();
		
	    result =  propertiesFactory.getObject();
		
		return result;
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() throws IOException, SQLException {
		PropertySourcesPlaceholderConfigurer result;
		
		result = new PropertySourcesPlaceholderConfigurer();
		result.setProperties(applicationProperties());
		
		return result;
	}

}
