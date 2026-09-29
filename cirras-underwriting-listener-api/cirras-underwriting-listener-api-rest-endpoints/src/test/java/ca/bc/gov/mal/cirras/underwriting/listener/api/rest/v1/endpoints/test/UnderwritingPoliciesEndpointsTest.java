package ca.bc.gov.mal.cirras.underwriting.listener.api.rest.v1.endpoints.test;

import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.InsuranceClaimRsrc;
import ca.bc.gov.mal.cirras.policies.model.v1.PoliciesEventTypes;
import ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1.CirrasUnderwritingPoliciesListenerService;
import ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1.impl.CirrasUnderwritingPoliciesListenerServiceImpl;
import ca.bc.gov.mal.cirras.underwriting.listener.api.rest.test.EndpointsTest;
import ca.bc.gov.mal.cirras.underwriting.listener.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.listener.policies.event.listener.v1.CirrasUnderwritingPoliciesEventListenerImpl;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;

public class UnderwritingPoliciesEndpointsTest extends EndpointsTest {
	
	private static final Logger logger = LoggerFactory.getLogger(UnderwritingPoliciesEndpointsTest.class);

	private static ObjectMapper mapper = new ObjectMapper();
	
	private static final String[] SCOPES = {
			Scopes.GET_TOP_LEVEL,
			Scopes.CIRRAS_UNDERWRITING_SYNC_REST,
			Scopes.GET_SYNCRONIZATION_STATUS
		};

	
	private Object receivedLock = null;     // Use to synchronize access to receivedMessage and receivedCount by multiple threads.
	private String receivedMessage = null;
	private int receivedCount = 0;
	
	@Before
	public void prepareTests() {

		receivedLock = new Object();
		receivedMessage = null;
		receivedCount = 0;

	}
	
	@Test
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

	// Use ca.bc.gov.mal.cirras.policies.api.rest.v1.publisher.testClaimEventPublish() in Policies API to push 
	// the event consumed by this test. Note that although the event is for an InsuranceClaimRsrc, which is ignored 
	// by the UW Listener, it can still be used here to test just the event consume.
	@Test
	public void testClaimEventConsume() throws Throwable {
		logger.debug("<testClaimEventConsume");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		String messageText = getMessage();

		Assert.assertNotNull("No message received", messageText);
		
		Map<String, Object> policiesEventObjMap = mapper.readValue(messageText, new TypeReference<Map<String, Object>>() {});
		
		String eventType = (String)policiesEventObjMap.get("eventType");
		Assert.assertEquals("Wrong event type", PoliciesEventTypes.ClaimUpdated, eventType);
		
		String eventTimestamp = (String)policiesEventObjMap.get("eventTimestamp");
		Assert.assertNotNull(eventTimestamp);
			
		@SuppressWarnings("unchecked")
		Map<String, String> sourceIdentifiers = (Map<String, String>)policiesEventObjMap.get("sourceIdentifiers");

		String claimNumber = sourceIdentifiers.get("claimNumber");
		Assert.assertEquals("Wrong Claim Number", "28168", claimNumber);

		String sourceType = (String)policiesEventObjMap.get("sourceType");
		Assert.assertEquals("Wrong Source Type", InsuranceClaimRsrc.class.getName(), sourceType);

		String sourceLink = (String)policiesEventObjMap.get("sourceLink");
		Assert.assertNotNull(sourceLink);
		
		Object resourceBeforeUpdate = (String)policiesEventObjMap.get("resourceBeforeUpdate");
		Assert.assertNull(resourceBeforeUpdate);
		
		@SuppressWarnings("unchecked")
		Map<String, Object> resourceAfterUpdate = (Map<String, Object>) policiesEventObjMap.get("resourceAfterUpdate");
		String resourceAfterUpdateJson = mapper.writeValueAsString(resourceAfterUpdate);

		InsuranceClaimRsrc claimRsrc = mapper.readValue(resourceAfterUpdateJson, InsuranceClaimRsrc.class);
		Assert.assertEquals("Wrong Claim Resource", 28168, claimRsrc.getClaimNumber().intValue());
		
		logger.debug(">testClaimEventConsume");
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
		
		CirrasUnderwritingPoliciesListenerServiceImpl service = (CirrasUnderwritingPoliciesListenerServiceImpl)webApplicationContext.getBean("cirrasUnderwritingPoliciesListenerService");

		service.processCirrasUnderwritingPoliciesEvent(messageText, factoryContext);

		logger.debug(">testUnderwritingUpdateEventConsumeAndProcess");
	}

	// Starts the event listener in another thread, waits for it to consume the message from the queue, stops the listener and returns the received 
	// message. It will not be sent to the UW API.
	private String getMessage() throws InterruptedException {

		String messageStr = null;
		
		CirrasUnderwritingPoliciesEventListenerImpl listener = (CirrasUnderwritingPoliciesEventListenerImpl)webApplicationContext.getBean("policiesEventListener");

		Assert.assertNotNull(listener);

		// Replace service with stub that just receives the message so it can be checked, rather than actually process it.
		listener.setCirrasUnderwritingPoliciesListenerService(new CirrasUnderwritingPoliciesListenerService() {
			
			@Override
			public void processCirrasUnderwritingPoliciesEvent(String eventMessageString, FactoryContext factoryContext)
					throws ServiceException, Throwable {
				synchronized (receivedLock) {
				receivedMessage = eventMessageString;
				receivedCount++;
				}				
			}
		});
				
		listener.startListening();

		// Wait 15 seconds for the event listener to receive the message in its thread then transfer it here.
		synchronized (this) {
			this.wait(15*1000); //15 seconds
			//this.wait(10*60*1000); //10 minutes
		}
		
		listener.stopListening();

		synchronized (receivedLock) {
			Assert.assertEquals(1, receivedCount);			
			Assert.assertNotNull(receivedMessage);
			messageStr = receivedMessage;
		}		

		return messageStr;
	}

	// Starts the event listener in another thread, waits for it to consume the message from the queue, throws an exception, repeats 
	// numFails times then stops the listener. 
	// This simulates a failure processing a message, which should result in the same message being processed multiple times, 
	// with a delay of 60 seconds, then 2*60 seconds, then 3*60 seconds, as so on, up to 60*60 seconds.
	// IMPORTANT: The overriden asyncCheckForMaster() in EndpointsTest has to be changed to return true instead of false for this test.
	//            Otherwise the event listener will be automatically stopped shortly after this test begins.
	@Test
	public void testFailedEventConsume() throws Throwable {
		logger.debug("<testFailedEventConsume");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		// Number of failures.
		int numFails = 3;
		
		// This should cause the test to stop after a max of 10 minutes, in case it doesn't stop on its own.
		int maxTimes = 120;

		CirrasUnderwritingPoliciesEventListenerImpl listener = (CirrasUnderwritingPoliciesEventListenerImpl)webApplicationContext.getBean("policiesEventListener");

		Assert.assertNotNull(listener);

		// Replace service with stub that just receives the message so it can be checked, rather than actually process it.
		listener.setCirrasUnderwritingPoliciesListenerService(new CirrasUnderwritingPoliciesListenerService() {
			
			@Override
			public void processCirrasUnderwritingPoliciesEvent(String eventMessageString, FactoryContext factoryContext)
					throws ServiceException, Throwable {

				synchronized (receivedLock) {
					receivedMessage = eventMessageString;
					receivedCount++;
				}
				
				throw new ServiceException("Failed to process message");
			}
		});
		
		listener.startListening();

		int i = 0;
		while (receivedCount < numFails && i < maxTimes) {
		
			// Sleep for 5 seconds.
			synchronized (this) {
				this.wait(5*1000);
			}
			
			i++;
		}

		listener.stopListening();

		synchronized (receivedLock) {
			Assert.assertEquals(numFails, receivedCount);
		}
		
		logger.debug(">testFailedEventConsume");
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
