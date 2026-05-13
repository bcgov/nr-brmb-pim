package ca.bc.gov.mal.cirras.underwriting.controllers.publisher;

import java.util.Map;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.mal.cirras.underwriting.clients.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.clients.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class EventPublisherTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(EventPublisherTest.class);

	private static ObjectMapper mapper = new ObjectMapper();
	
	private static final String[] SCOPES = {
			Scopes.GET_TOP_LEVEL, 
	};

	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;

		
	@Before
	public void prepareTests() throws CirrasUnderwritingServiceException, Oauth2ClientException {

		service = getService(SCOPES);
		topLevelEndpoints = service.getTopLevelEndpoints();
	}	
	
	@Test
	public void testSimplePublish() throws EventPublisherException {
		logger.debug("<testSimplePublish");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		EventPublisher eventPublisher = (EventPublisher)webApplicationContext.getBean("eventPublisher");
		eventPublisher.publish("HelloWorld", null, null, null);
				
		logger.debug(">testSimplePublish");		
	}
}
