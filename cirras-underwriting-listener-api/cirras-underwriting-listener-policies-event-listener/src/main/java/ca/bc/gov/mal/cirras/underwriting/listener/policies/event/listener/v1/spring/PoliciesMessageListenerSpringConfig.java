package ca.bc.gov.mal.cirras.underwriting.listener.policies.event.listener.v1.spring;

import java.util.Properties;

import javax.mail.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1.spring.ServiceApiSpringConfig;
import ca.bc.gov.mal.cirras.underwriting.listener.failover.service.api.v1.async.FailOverService;
import ca.bc.gov.mal.cirras.underwriting.listener.policies.event.listener.v1.CirrasUnderwritingPoliciesEventListenerImpl;
import ca.bc.gov.mal.cirras.underwriting.listener.policies.event.listener.v1.EventListener;
import ca.bc.gov.mal.cirras.underwriting.listener.policies.event.listener.v1.FailoverEventListener;

@Configuration
@Import({ 
	ServiceApiSpringConfig.class,
	FailoverServiceSpringConfig.class
})
public class PoliciesMessageListenerSpringConfig {

	static final Logger logger = LoggerFactory.getLogger(PoliciesMessageListenerSpringConfig.class);

	@Value("${ACTIVEMQ_URL}")
	private String brokerURL;

	@Value("${CIRRAS_UNDERWRITING_SYNC_USER}")
	private String userName;

	@Value("${CIRRAS_UNDERWRITING_SYNC_SECRET}")
	private String password;

    
	@Autowired
	private ServiceApiSpringConfig serviceApiSpringConfig;

	@Autowired
	private FailOverService policiesFailOverService;

	public PoliciesMessageListenerSpringConfig() {
		logger.debug("<MessageListenerSpringConfig");

		logger.debug(">MessageListenerSpringConfig");
	}
	
	@Bean 
	RedeliveryPolicy policiesRedeliveryPolicy() {
		RedeliveryPolicy result;
		
		result = new RedeliveryPolicy();
		
		result.setInitialRedeliveryDelay(5000);
		result.setRedeliveryDelay(5000);
		result.setBackOffMultiplier(3);
		result.setUseExponentialBackOff(true);
		result.setMaximumRedeliveries(8);
		
		return result;
	}

	@Bean
	public ActiveMQConnectionFactory policiesAmqConnectionFactory() {
		
		ActiveMQConnectionFactory result = new ActiveMQConnectionFactory(userName, password, brokerURL);
		
		result.setRedeliveryPolicy(policiesRedeliveryPolicy());
		result.setNonBlockingRedelivery(true);
		
		return result;
	}

	@Value("${ACTIVEMQ_EMAIL_ERROR_FREQ}")
	private String emailFrequency;
	
	public long emailFrequency() {
		long result = 10*1000*60;
		
		if(emailFrequency!=null) {
			
			result = Long.valueOf(emailFrequency).longValue() * 60 * 1000;
		}
		
		return result;
	}

	@Value("${DEFAULT_APPLICATION_ENV}")
	private String environment;
	
	private static final String ENVIRONMENT_PLACE_HOLDER = "%environment%";

	@Value("${ACTIVEMQ_EMAIL_ERROR_SUBJECT}")
	private String emailSubjectTemplate;
	
	private String emailSubject() {
		String result;
		
		result = emailSubjectTemplate.replace(ENVIRONMENT_PLACE_HOLDER, environment);
		
		return result;
	}

	@Value("${ACTIVEMQ_ADMIN_EMAILS}")
	private String rawAddresses;

	@Value("${ACTIVEMQ_EMAIL_HOSTNAME}")
	private String emailHostName;

	@Value("${ACTIVEMQ_EMAIL_PORT}")
	private String emailPort;

	@Value("${ACTIVEMQ_EMAIL_FROM_ADDRESS}")
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
	public EventListener policiesEventListener() {
		CirrasUnderwritingPoliciesEventListenerImpl result;
		
		result = new CirrasUnderwritingPoliciesEventListenerImpl();
		
		result.setQueueName("Consumer.CIRRAS_UNDERWRITING_SYNC_REST.VirtualTopic.CIRRAS_CLAIMS.policies-event-channel");
		result.setConnectionFactory(policiesAmqConnectionFactory());
		result.setReceiveTimeout(500);
		result.setCirrasUnderwritingPoliciesListenerService(serviceApiSpringConfig.cirrasUnderwritingPoliciesListenerService());
		result.setEmailFrequency(emailFrequency());
		result.setEmailSession(emailSession());
		result.setEmailSubject(emailSubject());
		result.setRawAddresses(rawAddresses);
		result.setEmailFrom(emailFromAddress);
		
		return result;
	}
	
	@Bean
	public FailoverEventListener policiesFailoverEventListener() {
		FailoverEventListener result;
		
		result = new FailoverEventListener(
				policiesFailOverService, 
				policiesEventListener());
		
		return result;
	}

}
