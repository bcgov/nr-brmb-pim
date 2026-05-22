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
import ca.bc.gov.mal.cirras.underwriting.controllers.async.AsynchronousTimerTask;
import ca.bc.gov.mal.cirras.underwriting.controllers.async.FetchOutboxTask;
import ca.bc.gov.mal.cirras.underwriting.controllers.async.OutboxProcessor;
import ca.bc.gov.mal.cirras.underwriting.services.CirrasDataSyncService;
import ca.bc.gov.mal.cirras.underwriting.services.FailOverService;
import ca.bc.gov.mal.cirras.underwriting.services.utils.EmailUtils;


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

	@Value("${EMAIL_HOST_NAME}")
	private String emailHostName;
	
	
	@Value("${CIRRAS_UNDERWRITING_REST_CLIENT_ID}")
	private String webadeOauth2ClientId;

	@Value("${CIRRAS_UNDERWRITING_REST_SECRET}")
	private String webadeOauth2ClientSecret;

	@Value("${EMAIL_HOST_NAME}")
	private String emailHostName;
	
	@Value("${EMAIL_PORT}")
	private String emailPort;

	@Value("${EMAIL_FROM_ADDRESS}")
	private String emailFromAddress;
	
	@Value("${EMAIL_MASTER_TASK_SYNCH_ERROR_SUBJECT}")
	private String emailMasterTaskSynchErrorSubject;
	
	@Value("${EMAIL_OUTBOX_SYNCH_ERROR_SUBJECT}")
	private String emailOutboxSynchErrorSubject;

	@Value("${EMAIL_ADMIN_ADDRESS}")
	private String emailAdminAddress;
	
	@Value("${EMAIL_ERROR_SEND_FREQUENCY}")
	private String emailErrorSendFrequency;

	@Value("${APPLICATION_ENVIRONMENT_NAME}")
	private String applicationEnvironmentName;
	
	@Value("${POLLING_OUTBOX_ENABLED}")
	private String pollingOutboxEnabled;

	@Value("${POLLING_OUTBOX_SECONDS_FREQUENCY}")
	private String pollingOutboxSecondsFrequency;
	
	@Value("${POLLING_OUTBOX_MAX_RECORDS}")
	private String pollingOutboxMaxRecords;

	@Value("${POLLING_OUTBOX_MAX_ITERATIONS}")
	private String pollingOutboxMaxIterations;
	
	
	@Bean
	UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken() {
		UsernamePasswordAuthenticationToken result;
		
		result = new UsernamePasswordAuthenticationToken(webadeOauth2ClientId, webadeOauth2ClientSecret);
		
		return result;
	}

	@Bean
	Properties asyncProcessProperties() {

		Properties props = new Properties();

		addPropertyIfSet(EmailUtils.EMAIL_HOST_NAME_PROPERTY, emailHostName, props);
		addPropertyIfSet(EmailUtils.EMAIL_PORT_PROPERTY, emailPort, props);
		addPropertyIfSet(EmailUtils.EMAIL_FROM_ADDRESS_PROPERTY, emailFromAddress, props);
		addPropertyIfSet(AsyncMasterTask.EMAIL_SUBJECT_PROPERTY_KEY, emailMasterTaskSynchErrorSubject, props);
		addPropertyIfSet(FetchOutboxTask.EMAIL_SUBJECT_PROPERTY_KEY, emailOutboxSynchErrorSubject, props);
		addPropertyIfSet(AsynchronousTimerTask.EMAIL_ERROR_TO_KEY, emailAdminAddress, props);
		addPropertyIfSet(AsynchronousTimerTask.EMAIL_ERROR_SEND_FREQUENCY_KEY, emailErrorSendFrequency, props);
		
		addPropertyIfSet(AsynchronousTimerTask.ENVIRONMENT_KEY, applicationEnvironmentName, props);

		addPropertyIfSet(AsynchronousTimerTask.OUTBOX_POLLING_ENABLED_KEY, pollingOutboxEnabled, props);
		addPropertyIfSet(AsynchronousTimerTask.OUTBOX_POLLING_SECONDS_FREQUENCY_KEY, pollingOutboxSecondsFrequency, props);
		addPropertyIfSet(OutboxProcessor.OUTBOX_POLLING_MAX_RECORDS_KEY, pollingOutboxMaxRecords, props);
		addPropertyIfSet(OutboxProcessor.OUTBOX_POLLING_MAX_ITERATIONS_KEY, pollingOutboxMaxIterations, props);
		
		return props;
	}
	
	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	AsyncMasterTask asyncMasterTask() throws AddressException {

		if ( emailHostName == null ) {
			logger.info("TEST EMAIL HOST NAME: NULL");
		} else {
			logger.info("TEST EMAIL HOST NAME: " + emailHostName);
		}
		
		AsyncMasterTask result;
		
		result = new AsyncMasterTask(asyncProcessProperties());
		result.setUsernamePasswordAuthenticationToken(usernamePasswordAuthenticationToken());
		result.setAuthenticationProvider(securitySpringConfig.authenticationProvider());
		result.setFailOverService(failOverService);

		return result;
	}

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	FetchOutboxTask fetchOutboxTask() throws AddressException {
		FetchOutboxTask result;
		
		result = new FetchOutboxTask(asyncProcessProperties());
		result.setUsernamePasswordAuthenticationToken(usernamePasswordAuthenticationToken());
		result.setAuthenticationProvider(securitySpringConfig.authenticationProvider());
		result.setCirrasDataSyncService(cirrasDataSyncService);
		
		return result;
	}

	@Bean
	AsynchronousProcessesService asynchronousProcessesService() throws AddressException {
		AsynchronousProcessesService result;
		
		result = new AsynchronousProcessesService();
		result.setApplicationProperties(asyncProcessProperties());
		result.setAsyncMasterTask(asyncMasterTask());

		return result;
	}

	// Add key/value to props if value is not null.
	private void addPropertyIfSet(String key, String value, Properties props) {
		if ( value != null ) {
			props.setProperty(key, value);
		}
	}
}
