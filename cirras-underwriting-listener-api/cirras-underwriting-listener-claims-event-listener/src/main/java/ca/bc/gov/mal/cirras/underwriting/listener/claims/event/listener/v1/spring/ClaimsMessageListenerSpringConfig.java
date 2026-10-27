package ca.bc.gov.mal.cirras.underwriting.listener.claims.event.listener.v1.spring;

import java.time.Duration;
import java.util.Properties;

import jakarta.mail.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1.spring.ServiceApiSpringConfig;
import ca.bc.gov.mal.cirras.underwriting.listener.failover.service.api.v1.async.FailOverService;
import ca.bc.gov.mal.cirras.underwriting.listener.claims.event.listener.v1.CirrasUnderwritingClaimsEventListenerImpl;
import ca.bc.gov.mal.cirras.underwriting.listener.claims.event.listener.v1.EventListener;
import ca.bc.gov.mal.cirras.underwriting.listener.claims.event.listener.v1.FailoverEventListener;
import ca.bc.gov.mal.cirras.underwriting.listener.claims.event.listener.v1.NatsAuthHandler;
import ca.bc.gov.mal.cirras.underwriting.listener.claims.event.listener.v1.NatsConnectionListener;
import ca.bc.gov.mal.cirras.underwriting.listener.claims.event.listener.v1.NatsErrorListener;
import io.nats.client.Options;

@Configuration
@Import({ 
	ServiceApiSpringConfig.class,
	ClaimsFailoverServiceSpringConfig.class
})
public class ClaimsMessageListenerSpringConfig {

	static final Logger logger = LoggerFactory.getLogger(ClaimsMessageListenerSpringConfig.class);

	@Value("${MESSAGE_QUEUE_URL}")
	private String natsURL;
	
	@Value("${MESSAGE_QUEUE_UNDERWRITING_REST_NKEY_SEED}")
	private String nkeySeed;

	
	@Autowired
	private ServiceApiSpringConfig serviceApiSpringConfig;

	@Autowired
	private FailOverService claimsFailOverService;

	public ClaimsMessageListenerSpringConfig() {
		logger.debug("<ClaimsMessageListenerSpringConfig");

		logger.debug(">ClaimsMessageListenerSpringConfig");
	}


	@Bean 
	Options natsConnectionOptions() {

		logger.info("NATS Server URL: " + natsURL);

		NatsAuthHandler authHandler = new NatsAuthHandler(nkeySeed);
		NatsConnectionListener connListener = new NatsConnectionListener();
		NatsErrorListener errListener = new NatsErrorListener();

		Options.Builder builder = new Options.Builder()
                .server(natsURL)
                .connectionTimeout(Duration.ofSeconds(10))
                .pingInterval(Duration.ofSeconds(30))
                .reconnectWait(Duration.ofSeconds(10))
                .authHandler(authHandler)
                .connectionListener(connListener)
                .errorListener(errListener)
                .maxReconnects(3);
		
        Options options = builder.build();

        return options;

	}
	
	@Value("${EMAIL_ERROR_SEND_FREQUENCY}")
	private String emailErrorSendFrequency;
	
	public long emailFrequency() {
		long result = 10*1000*60;
		
		if(emailErrorSendFrequency!=null) {
			
			result = Long.valueOf(emailErrorSendFrequency).longValue() * 60 * 1000;
		}
		
		return result;
	}

	@Value("${APPLICATION_ENVIRONMENT_NAME}")
	private String environment;
	
	private static final String ENVIRONMENT_PLACE_HOLDER = "%environment%";

	@Value("${EMAIL_UW_LISTENER_SYNCH_ERROR_SUBJECT}")
	private String emailSubjectTemplate;
	
	private String emailSubject() {
		String result;
		
		result = emailSubjectTemplate.replace(ENVIRONMENT_PLACE_HOLDER, environment);
		
		return result;
	}

	@Value("${EMAIL_ADMIN_ADDRESS}")
	private String rawAddresses;

	@Value("${EMAIL_HOST_NAME}")
	private String emailHostName;

	@Value("${EMAIL_PORT}")
	private String emailPort;

	@Value("${EMAIL_FROM_ADDRESS}")
	private String emailFromAddress;

	private Session emailSession() {
		logger.debug("<emailSession");
		Session result;
		
		Properties mailProperties = new Properties();
		mailProperties.setProperty("mail.smtp.host", emailHostName);
		mailProperties.setProperty("mail.smtp.port", emailPort);
		mailProperties.setProperty("mail.from.address", emailFromAddress);
		
		result = Session.getDefaultInstance(mailProperties);
		
		logger.debug(">emailSession");
		return result;
	}
	
	@Bean
	public EventListener claimsEventListener() {
		CirrasUnderwritingClaimsEventListenerImpl result;
		
		result = new CirrasUnderwritingClaimsEventListenerImpl();

		result.setMessageQueueOptions(natsConnectionOptions());
		result.setMessageQueueConsumer("underwriting_sync_rest_consumer");
		result.setMessageQueueStream("claims-event-channel");
		result.setInitialRedeliveryDelaySeconds(5);
		result.setReceiveTimeoutMilis(1000);
		result.setCirrasUnderwritingClaimsListenerService(serviceApiSpringConfig.cirrasUnderwritingClaimsListenerService());
		result.setEmailFrequency(emailFrequency());
		result.setEmailSession(emailSession());
		result.setEmailSubject(emailSubject());
		result.setRawAddresses(rawAddresses);
		result.setEmailFrom(emailFromAddress);
		
		return result;
	}
	
	@Bean
	public FailoverEventListener claimsFailoverEventListener() {
		FailoverEventListener result;
		
		result = new FailoverEventListener(
				claimsFailOverService, 
				claimsEventListener());
		
		return result;
	}

}
