package ca.bc.gov.mal.cirras.underwriting.listener.api.rest.v1.endpoints.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.mal.cirras.policies.model.v1.PoliciesEventTypes;
import ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1.impl.CirrasUnderwritingPoliciesListenerServiceImpl;
import ca.bc.gov.mal.cirras.underwriting.listener.api.rest.test.EndpointsTest;
import ca.bc.gov.mal.cirras.underwriting.listener.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.listener.policies.event.listener.v1.CirrasUnderwritingPoliciesEventListenerImpl;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;

public class UnderwritingPoliciesEndpointsTest extends EndpointsTest {
	
	private static final Logger logger = LoggerFactory.getLogger(UnderwritingPoliciesEndpointsTest.class);

	private static ObjectMapper mapper = new ObjectMapper();
	
	//private CirrasUnderwritingListenerService service;

	private static final String[] SCOPES = {
			Scopes.GET_TOP_LEVEL,
			Scopes.CIRRAS_UNDERWRITING_SYNC_REST,
			Scopes.GET_SYNCRONIZATION_STATUS
		};
	
//	@Before
//	public void prepareTests() throws CirrasUnderwritingServiceException, Oauth2ClientException{
//		service = getService(SCOPES);
//		//topLevelEndpoints = service.getTopLevelEndpoints();
//	}
	
	//@Test
	public void testSimpleConsume() throws Throwable {
		logger.debug("<testSimpleConsume");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		String messageText = getMessage();
		
		Assert.assertEquals("Got unexpected message from queue", "Hello World", messageText);
		
		logger.debug(">testSimpleConsume");
	}
	
	//@Test
	public void testUnderwritingEventConsume() throws Throwable {
		logger.debug("<testUnderwritingEventConsume");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		String messageText = getMessage();

		Assert.assertNotNull("No message received", messageText);
		
		Map<String, Object> policiesEventObjMap = mapper.readValue(messageText, new TypeReference<Map<String, Object>>() {});
		
//		String eventType = (String)policiesEventObjMap.get("eventType");
//		Assert.assertEquals("Wrong event type", PoliciesEventTypes.UnderwritingUpdated, eventType);
		
		String eventTimestamp = (String)policiesEventObjMap.get("eventTimestamp");
			
		@SuppressWarnings("unchecked")
		Map<String, String> sourceIdentifiers = (Map<String, String>)policiesEventObjMap.get("sourceIdentifiers");

		String underwritingNumber = sourceIdentifiers.get("underwritingNumber");
		Assert.assertEquals("Wrong Underwriting Number", "28168", underwritingNumber);

		//String sourceType = (String)policiesEventObjMap.get("sourceType");
		//Assert.assertEquals("Wrong Source Type", InsuranceUnderwritingRsrc.class.getName(), sourceType);
			
		@SuppressWarnings("unchecked")
		Map<String, Object> resourceAfterUpdate = (Map<String, Object>) policiesEventObjMap.get("resourceAfterUpdate");
		String resourceAfterUpdateJson = mapper.writeValueAsString(resourceAfterUpdate);

		//InsuranceUnderwritingRsrc underwritingRsrc = mapper.readValue(resourceAfterUpdateJson, InsuranceUnderwritingRsrc.class);
		//Assert.assertEquals("Wrong Underwriting Resource", 28168, underwritingRsrc.getUnderwritingNumber().intValue());
		
		logger.debug(">testUnderwritingEventConsume");
	}	
	
	
	//@Test
	public void testUnderwritingEventListener() throws Throwable {
		logger.debug("<testUnderwritingUpdateEventConsumeAndProcess");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasUnderwritingPoliciesEventListenerImpl service = (CirrasUnderwritingPoliciesEventListenerImpl)webApplicationContext.getBean("policiesEventListener");
		
		Assert.assertNotNull("Event Listner is NULL", service);
		
		//service.startListening();
		service.run();
		//service.stopListening();

	}
	
	//@Test
	public void testUnderwritingUpdateEventConsumeAndProcess() throws Throwable {
		logger.debug("<testUnderwritingUpdateEventConsumeAndProcess");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		FactoryContext factoryContext = new FactoryContext() {
			// do nothing
		};

		String messageText = getMessage();

		Assert.assertNotNull("No message received", messageText);
		
		String messageId = "testId";
		
		CirrasUnderwritingPoliciesListenerServiceImpl service = (CirrasUnderwritingPoliciesListenerServiceImpl)webApplicationContext.getBean("cirrasUnderwritingPoliciesListenerService");

		service.processCirrasUnderwritingPoliciesEvent(messageText, messageId, factoryContext);

		logger.debug(">testUnderwritingUpdateEventConsumeAndProcess");
	}
	
	private String getMessage() throws Throwable, JMSException {
		Properties appProperties = (Properties)webApplicationContext.getBean("applicationProperties");

		String userName = appProperties.getProperty("CIRRAS_UNDERWRITING_SYNC_USER");
		String password = appProperties.getProperty("CIRRAS_UNDERWRITING_SYNC_SECRET");
		String brokerURL = appProperties.getProperty("activemq.server.url");

		String messageText = null;
		
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(userName, password, brokerURL);

		Connection connection = null;
		try {
			
			// Create a Connection
			connection = connectionFactory.createQueueConnection();
			
			connection.start();

			// Create a Session
			Session session = connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);

			Queue destination = session.createQueue("Consumer.CIRRAS_UNDERWRITING_SYNC_REST.VirtualTopic.CIRRAS_CLAIMS.policies-event-channel");

			MessageConsumer messageConsumer = session.createConsumer(destination);
				
			Message message = null;
			try {

				// Only wait for 500 ms.
				message = messageConsumer.receive(500);
	
				if(message!=null) {
												
					logger.debug("message=" + message);
					logger.debug("message.class=" + message.getClass());
												
					TextMessage textMessage = (TextMessage) message;
						
					messageText = textMessage.getText();
					logger.debug("messageText=\n" + messageText);
						
					session.commit();
				}
					
			} catch(Throwable t) {
					
				logger.error(t.getMessage(), t);
										
				session.rollback();
					
				throw t;
			}
			
		} catch (JMSException e) {
			
			logger.error(e.getMessage(), e);
			throw e;

		} finally {
			if(connection!=null) {
				
				try {
					connection.close();
				} catch (JMSException e) {
					// do nothing
				}
			}
		}
		return messageText;
	}
	
	// Not really a unit test, but can use to just run the web server for x minutes. Useful for testing the AsynchronousProcessesService and its associated threads.
	//@Test
	public void testAsyncProc() throws InterruptedException {
		synchronized (this) { 
			this.wait(60*1000); //1 minute
			//this.wait(10*60*1000); //10 minutes
		}
	}
}
