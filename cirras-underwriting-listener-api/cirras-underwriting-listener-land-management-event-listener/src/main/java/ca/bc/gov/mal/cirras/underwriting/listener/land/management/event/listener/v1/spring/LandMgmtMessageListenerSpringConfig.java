package ca.bc.gov.mal.cirras.underwriting.listener.land.management.event.listener.v1.spring;

//import java.util.Properties;

//import jakarta.mail.Session;

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

	@Value("${EMAIL_ERROR_SEND_FREQUENCY}")
	private String emailFrequency;
	
	public long emailFrequency() {
		long result = 10*1000*60;
		
		if(emailFrequency!=null) {
			
			result = Long.valueOf(emailFrequency).longValue() * 60 * 1000;
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
	public EventListener landManagementEventListener() {
		CirrasUwLandManagementEventListenerImpl result;
		
		result = new CirrasUwLandManagementEventListenerImpl();
		
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
