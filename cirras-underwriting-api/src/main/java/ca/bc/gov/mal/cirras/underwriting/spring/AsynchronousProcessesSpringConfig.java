package ca.bc.gov.mal.cirras.underwriting.spring;

import java.util.Properties;

import jakarta.mail.internet.AddressException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import ca.bc.gov.mal.cirras.underwriting.controllers.async.AsyncMasterTask;
import ca.bc.gov.mal.cirras.underwriting.controllers.async.AsynchronousProcessesService;
import ca.bc.gov.mal.cirras.underwriting.controllers.async.FetchOutboxTask;
import ca.bc.gov.mal.cirras.underwriting.services.CirrasDataSyncService;
import ca.bc.gov.mal.cirras.underwriting.services.FailOverService;


@Configuration
@Import({
	SecuritySpringConfig.class
})
public class AsynchronousProcessesSpringConfig {

	private static final Logger logger = LoggerFactory.getLogger(AsynchronousProcessesSpringConfig.class);

	public AsynchronousProcessesSpringConfig() {
		logger.debug("<AsynchronousProcessesSpringConfig");
		
		logger.debug(">AsynchronousProcessesSpringConfig");
	}

	// Beans provided by EndpointsSpringConfig
	@Autowired Properties applicationProperties;
	
	// Imported Spring Config
	@Autowired SecuritySpringConfig securitySpringConfig;

	// Beans provided by ServiceApiSpringConfig
	@Autowired CirrasDataSyncService cirrasDataSyncService;
	@Autowired FailOverService failOverService;

	@Value("${CIRRAS_UNDERWRITING_REST_CLIENT_ID}")
	private String webadeOauth2ClientId;

	@Value("${CIRRAS_UNDERWRITING_REST_SECRET}")
	private String webadeOauth2ClientSecret;
		
	@Bean
	UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken() {
		UsernamePasswordAuthenticationToken result;
		
		result = new UsernamePasswordAuthenticationToken(webadeOauth2ClientId, webadeOauth2ClientSecret);
		
		return result;
	}

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	AsyncMasterTask asyncMasterTask() throws AddressException {
		AsyncMasterTask result;
		
		result = new AsyncMasterTask(applicationProperties);
		result.setUsernamePasswordAuthenticationToken(usernamePasswordAuthenticationToken());
		result.setAuthenticationProvider(securitySpringConfig.authenticationProvider());
		result.setFailOverService(failOverService);

		return result;
	}

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	FetchOutboxTask fetchOutboxTask() throws AddressException {
		FetchOutboxTask result;
		
		result = new FetchOutboxTask(applicationProperties);
		result.setUsernamePasswordAuthenticationToken(usernamePasswordAuthenticationToken());
		result.setAuthenticationProvider(securitySpringConfig.authenticationProvider());
		result.setCirrasDataSyncService(cirrasDataSyncService);
		
		return result;
	}

	@Bean
	AsynchronousProcessesService asynchronousProcessesService() throws AddressException {
		AsynchronousProcessesService result;
		
		result = new AsynchronousProcessesService();
		result.setApplicationProperties(applicationProperties);
		result.setAsyncMasterTask(asyncMasterTask());

		return result;
	}
}
