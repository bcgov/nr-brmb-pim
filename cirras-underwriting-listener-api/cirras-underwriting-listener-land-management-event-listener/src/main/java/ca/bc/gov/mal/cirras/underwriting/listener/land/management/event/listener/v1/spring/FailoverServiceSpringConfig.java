package ca.bc.gov.mal.cirras.underwriting.listener.land.management.event.listener.v1.spring;

//import javax.sql.DataSource;

//import org.springframework.aop.framework.ProxyFactoryBean;
//import org.springframework.aop.target.HotSwappableTargetSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Import;
//import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

//import ca.bc.gov.mal.cirras.underwriting.listener.failover.persistence.v1.spring.FailoverPersistenceSpringConfig;
//import ca.bc.gov.mal.cirras.underwriting.listener.failover.service.api.v1.async.FailOverService;
//import ca.bc.gov.mal.cirras.underwriting.listener.failover.service.api.v1.async.FailOverServiceImpl;

//PIM-1156: Disabling all code related to cirras-land-management-api so it can be shutdown.

//@Configuration
//@Import({ FailoverPersistenceSpringConfig.class })
public class FailoverServiceSpringConfig {

/*
	@Autowired private FailoverPersistenceSpringConfig failoverPersistenceSpringConfig;
	
	@Bean
	public FailOverService landManagementFailOverServiceImpl() {
		FailOverServiceImpl result;
		
		result = new FailOverServiceImpl();
		result.setSyncOwnershipDao(failoverPersistenceSpringConfig.syncOwnershipDao());
		
		return result;
	}

	// The client is made hot swappable to support JUNIT tests.
	@Bean
	public HotSwappableTargetSource landManagementSwappableFailOverService() {
		HotSwappableTargetSource result;

		result = new HotSwappableTargetSource(landManagementFailOverServiceImpl());
		return result;
	}

	// The swappable token client is proxied to support JUNIT tests.
	@Bean
	public ProxyFactoryBean landManagementFailOverService() {
		ProxyFactoryBean result;

		result =  new ProxyFactoryBean();
		result.setTargetSource(landManagementSwappableFailOverService());
		return result;
	}
*/
}
