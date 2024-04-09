package ca.bc.gov.mal.cirras.underwriting.listener.land.management.event.listener.v1.spring;

//import java.util.Properties;

//import javax.mail.Session;

//import org.apache.activemq.ActiveMQConnectionFactory;
//import org.apache.activemq.RedeliveryPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Import;

//import ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1.CirrasUnderwritingLandManagementListenerService;
//import ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1.spring.ServiceApiSpringConfig;
//import ca.bc.gov.mal.cirras.underwriting.listener.failover.service.api.v1.async.FailOverService;
//import ca.bc.gov.mal.cirras.underwriting.listener.land.management.event.listener.v1.CirrasUwLandManagementEventListenerImpl;
//import ca.bc.gov.mal.cirras.underwriting.listener.land.management.event.listener.v1.EventListener;
//import ca.bc.gov.mal.cirras.underwriting.listener.land.management.event.listener.v1.FailoverEventListener;

//PIM-1156: Disabling all code related to cirras-land-management-api so it can be shutdown.

//@Configuration
//@Import({ 
//	ServiceApiSpringConfig.class,
//	FailoverServiceSpringConfig.class
//})
public class LandMgmtMessageListenerSpringConfig {

	static final Logger logger = LoggerFactory.getLogger(LandMgmtMessageListenerSpringConfig.class);

/*
	@Value("${activemq.server.url}")
	private String brokerURL;

	@Value("${CIRRAS_UNDERWRITING_SYNC_USER}")
	private String userName;

	@Value("${CIRRAS_UNDERWRITING_SYNC_SECRET}")
	private String password;

    
	@Autowired
	private ServiceApiSpringConfig serviceApiSpringConfig;

	@Autowired
	private FailOverService landManagementFailOverService;
*/

	public LandMgmtMessageListenerSpringConfig() {
		logger.debug("<MessageListenerSpringConfig");

		logger.debug(">MessageListenerSpringConfig");
	}

/*
	@Bean 
	RedeliveryPolicy landManagementRedeliveryPolicy() {
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
	public ActiveMQConnectionFactory landManagementAmqConnectionFactory() {
		
		ActiveMQConnectionFactory result = new ActiveMQConnectionFactory(userName, password, brokerURL);
		
		result.setRedeliveryPolicy(landManagementRedeliveryPolicy());
		result.setNonBlockingRedelivery(true);
		
		return result;
	}

	@Value("${email.error.send.frequency}")
	private String emailFrequency;
	
	public long emailFrequency() {
		long result = 10*1000*60;
		
		if(emailFrequency!=null) {
			
			result = Long.valueOf(emailFrequency).longValue() * 60 * 1000;
		}
		
		return result;
	}

	@Value("${default.application.environment}")
	private String environment;
	
	private static final String ENVIRONMENT_PLACE_HOLDER = "%environment%";

	@Value("${email.underwriting.listener.synch.error.subject}")
	private String emailSubjectTemplate;
	
	private String emailSubject() {
		String result;
		
		result = emailSubjectTemplate.replace(ENVIRONMENT_PLACE_HOLDER, environment);
		
		return result;
	}

	@Value("${email.admin.address}")
	private String rawAddresses;

	@Value("${email.host.name}")
	private String emailHostName;

	@Value("${email.port}")
	private String emailPort;

	@Value("${email.from.address}")
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
	public EventListener landManagementEventListener() {
		CirrasUwLandManagementEventListenerImpl result;
		
		result = new CirrasUwLandManagementEventListenerImpl();
		
		result.setConnectionFactory(landManagementAmqConnectionFactory());
		result.setQueueName("Consumer.CIRRAS_UNDERWRITING_SYNC_REST.VirtualTopic.CIRRAS_LAND_MANAGEMENT.land-management-event-channel");
		result.setReceiveTimeout(500);
		result.setCirrasUnderwritingLandManagementListenerService(serviceApiSpringConfig.cirrasUnderwritingLandManagementListenerService());
		result.setEmailFrequency(emailFrequency());
		result.setEmailSession(emailSession());
		result.setEmailSubject(emailSubject());
		result.setRawAddresses(rawAddresses);
		result.setEmailFrom(emailFromAddress);
		
		return result;
	}
	
	@Bean
	public FailoverEventListener landManagementFailoverEventListener() {
		FailoverEventListener result;
		
		result = new FailoverEventListener(
				landManagementFailOverService, 
				landManagementEventListener());
		
		return result;
	}
*/
}
