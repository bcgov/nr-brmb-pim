package ca.bc.gov.mal.cirras.underwriting.listener.api.rest.v1.endpoints.test;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.mal.cirras.claims.data.models.ClaimCalculationBerries;
import ca.bc.gov.mal.cirras.claims.data.resources.ClaimCalculationSimpleRsrc;
import ca.bc.gov.mal.cirras.claims.data.resources.ClaimEventTypes;
import ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1.CirrasUnderwritingClaimsListenerService;
import ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1.model.factory.SyncUwClaimsFactory;
import ca.bc.gov.mal.cirras.underwriting.data.resources.ClaimSyncEventTypes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.SyncClaimCalculationSimpleRsrc;
import ca.bc.gov.mal.cirras.underwriting.listener.api.rest.test.EndpointsTest;
import ca.bc.gov.mal.cirras.underwriting.listener.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.listener.claims.event.listener.v1.CirrasUnderwritingClaimsEventListenerImpl;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;

public class UnderwritingClaimsEndpointsTest extends EndpointsTest {
	
	private static final Logger logger = LoggerFactory.getLogger(UnderwritingClaimsEndpointsTest.class);

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
	
	@Test
	public void testSyncClaimsUnderwritingFactoryUpdate() throws Throwable {
		
		logger.debug("<testSyncClaimsUnderwritingFactoryUpdate");

		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		String claimEventType = ClaimEventTypes.ClaimCalculationBerriesUpdated;
		String claimSyncEventTypes =  ClaimSyncEventTypes.ClaimCalculationBerriesUpdated;
		String claimCalculationGuid = "testClaimCalculationGuid";
		String claimCalculationBerriesGuid = "testClaimCalculationBerriesGuid";
		Integer contractId = 987654;
		Integer cropYear = 2026;
		Integer calculationVersion = 1;
		Integer cropCommodityId = 11111;
		String calculationStatusCode = "DRAFT";
		Double totalYieldForCalculation = 100.0;


		//Declared Yield Contract
		ClaimCalculationSimpleRsrc resource = new ClaimCalculationSimpleRsrc();
		resource.setContractId(contractId);
		resource.setCropYear(cropYear);
		resource.setClaimCalculationGuid(claimCalculationGuid);
		resource.setCalculationVersion(calculationVersion);
		resource.setCropCommodityId(cropCommodityId);
		resource.setCalculationStatusCode(calculationStatusCode);
		

		// Declared Yield Contract Commodity Berries
		ClaimCalculationBerries model = new ClaimCalculationBerries();

		model.setClaimCalculationBerriesGuid(claimCalculationBerriesGuid);
		model.setTotalYieldForCalculation(totalYieldForCalculation);

		resource.setClaimCalculationBerries(model);

		SyncUwClaimsFactory syncUwClaimsFactory = (SyncUwClaimsFactory)webApplicationContext.getBean("syncUwClaimsRsrcFactory");

		Instant timestamp = Instant.now();
		Date eventDate = Date.from(timestamp);
		SyncClaimCalculationSimpleRsrc syncClaimCalculationSimpleRsrc = syncUwClaimsFactory.getSyncClaimCalculationSimpleRsrc(resource, eventDate, claimEventType);

		Assert.assertNotNull(syncClaimCalculationSimpleRsrc);
		Assert.assertNotNull(syncClaimCalculationSimpleRsrc.getSyncClaimCalculationBerries());
		//claim calculation simple
		Assert.assertEquals("CropCommodityId", resource.getCropCommodityId(), syncClaimCalculationSimpleRsrc.getCropCommodityId());
		Assert.assertEquals("ContractId", resource.getContractId(), syncClaimCalculationSimpleRsrc.getContractId());
		Assert.assertEquals("CropYear", resource.getCropYear(), syncClaimCalculationSimpleRsrc.getCropYear());
		Assert.assertEquals("ClaimCalculationGuid", resource.getClaimCalculationGuid(), syncClaimCalculationSimpleRsrc.getClaimCalculationGuid());
		Assert.assertEquals("CalculationStatusCode", resource.getCalculationStatusCode(), syncClaimCalculationSimpleRsrc.getCalculationStatusCode());
		Assert.assertEquals("CalculationVersion", resource.getCalculationVersion(), syncClaimCalculationSimpleRsrc.getCalculationVersion());

		Assert.assertEquals("DataSyncTransDate", syncClaimCalculationSimpleRsrc.getDataSyncTransDate(), eventDate);
		Assert.assertEquals("TransactionType", syncClaimCalculationSimpleRsrc.getTransactionType(), claimSyncEventTypes);

		//claim calculation berries
		Assert.assertEquals("ClaimCalculationBerriesGuid", resource.getClaimCalculationBerries().getClaimCalculationBerriesGuid(), syncClaimCalculationSimpleRsrc.getSyncClaimCalculationBerries().getClaimCalculationBerriesGuid());
		Assert.assertEquals("TotalYieldForCalculation", resource.getClaimCalculationBerries().getTotalYieldForCalculation(), syncClaimCalculationSimpleRsrc.getSyncClaimCalculationBerries().getTotalYieldForCalculation());

		logger.debug(">testSyncClaimsUnderwritingFactoryUpdate");
	}
	
	@Test
	public void testSyncClaimsUnderwritingFactoryDelete() throws Throwable {
		
		logger.debug("<testSyncClaimsUnderwritingFactoryDelete");

		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		String claimEventType = ClaimEventTypes.ClaimCalculationBerriesDeleted;
		String claimSyncEventTypes =  ClaimSyncEventTypes.ClaimCalculationBerriesDeleted;
		String claimCalculationBerriesGuid = "testClaimCalculationBerriesGuid";

		
		SyncUwClaimsFactory syncUwClaimsFactory = (SyncUwClaimsFactory)webApplicationContext.getBean("syncUwClaimsRsrcFactory");

		Instant timestamp = Instant.now();
		Date eventDate = Date.from(timestamp);
		SyncClaimCalculationSimpleRsrc syncClaimCalculationSimpleRsrc = syncUwClaimsFactory.getDeleteSyncClaimCalculationSimpleRsrc(claimCalculationBerriesGuid, eventDate, claimEventType);

		//claim calculation simple
		Assert.assertEquals("DataSyncTransDate", syncClaimCalculationSimpleRsrc.getDataSyncTransDate(), eventDate);
		Assert.assertEquals("TransactionType", syncClaimCalculationSimpleRsrc.getTransactionType(), claimSyncEventTypes);

		//claim calculation berries
		Assert.assertEquals("ClaimCalculationBerriesGuid", claimCalculationBerriesGuid, syncClaimCalculationSimpleRsrc.getSyncClaimCalculationBerries().getClaimCalculationBerriesGuid());
		
		logger.debug(">testSyncClaimsUnderwritingFactoryDelete");
	}

	@Test
	public void testUnderwritingClaimEventConsume() throws Throwable {
		logger.debug("<testUnderwritingClaimEventConsume");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//The unit test EventPublisherTest.testClaimCalculationBerriesEventPublish in the claims-api
		// is publishing this message
		
		//Expected Values
		String claimEventType = ClaimEventTypes.ClaimCalculationBerriesUpdated;
		String claimCalculationGuid = "testClaimCalculationGuid";
		String claimCalculationBerriesGuid = "testClaimCalculationBerriesGuid";
		Integer contractId = 987654;
		Integer cropYear = 2026;
		Integer calculationVersion = 1;
		Integer cropCommodityId = 11111;
		String calculationStatusCode = "DRAFT";
		Double totalYieldForCalculation = 100.0;

		
		String messageText = getMessage();

		Assert.assertNotNull("No message received", messageText);
		
		Map<String, Object> uwEventObjMap = mapper.readValue(messageText, new TypeReference<Map<String, Object>>() {});

		String eventType = (String)uwEventObjMap.get("eventType");
		Assert.assertEquals("Wrong event type", claimEventType, eventType);
		
		String eventTimestamp = (String)uwEventObjMap.get("eventTimestamp");
		Assert.assertNotNull(eventTimestamp);
			
		@SuppressWarnings("unchecked")
		Map<String, String> sourceIdentifiers = (Map<String, String>)uwEventObjMap.get("sourceIdentifiers");

		String sourceIdentifierClaimCalculationBerriesGuid = sourceIdentifiers.get("claimCalculationBerriesGuid");
		Assert.assertEquals("Wrong Claim Calculation Berries Guid", claimCalculationBerriesGuid, sourceIdentifierClaimCalculationBerriesGuid);

		Object resourceBeforeUpdate = (String)uwEventObjMap.get("resourceBeforeUpdate");
		Assert.assertNull(resourceBeforeUpdate);
		
		@SuppressWarnings("unchecked")
		Map<String, Object> resourceAfterUpdate = (Map<String, Object>) uwEventObjMap.get("resourceAfterUpdate");
		String resourceAfterUpdateJson = mapper.writeValueAsString(resourceAfterUpdate);

		ClaimCalculationSimpleRsrc resource = mapper.readValue(resourceAfterUpdateJson, ClaimCalculationSimpleRsrc.class);
		Assert.assertEquals("Wrong ClaimCalculationSimpleRsrc Resource", claimCalculationBerriesGuid, resource.getClaimCalculationBerries().getClaimCalculationBerriesGuid());
		
		Assert.assertNotNull(resource);
		Assert.assertNotNull(resource.getClaimCalculationBerries());
		
		//Check all values
		//claim calculation
		Assert.assertEquals("ClaimCalculationGuid", claimCalculationGuid, resource.getClaimCalculationGuid());
		Assert.assertEquals("ContractId", contractId, resource.getContractId());
		Assert.assertEquals("CropYear", cropYear, resource.getCropYear());
		Assert.assertEquals("CalculationStatusCode", calculationStatusCode, resource.getCalculationStatusCode());
		Assert.assertEquals("CropCommodityId", cropCommodityId, resource.getCropCommodityId());
		Assert.assertEquals("CalculationVersion", calculationVersion, resource.getCalculationVersion());

		
		//dop yield contract commodity berries
		ClaimCalculationBerries model = resource.getClaimCalculationBerries();
		Assert.assertEquals("ClaimCalculationBerriesGuid", claimCalculationBerriesGuid, model.getClaimCalculationBerriesGuid());
		Assert.assertEquals("TotalYieldForCalculation", totalYieldForCalculation, model.getTotalYieldForCalculation());
		
		logger.debug(">testUnderwritingClaimEventConsume");
	}


	// Starts the event listener in another thread, waits for it to consume the message from the queue, stops the listener and returns the received 
	// message. It will not be sent to the UW API.
	private String getMessage() throws InterruptedException {

		String messageStr = null;
		
		CirrasUnderwritingClaimsEventListenerImpl listener = (CirrasUnderwritingClaimsEventListenerImpl)webApplicationContext.getBean("claimsEventListener");

		Assert.assertNotNull(listener);

		// Replace service with stub that just receives the message so it can be checked, rather than actually process it.
		listener.setCirrasUnderwritingClaimsListenerService(new CirrasUnderwritingClaimsListenerService() {
			
			@Override
			public void processCirrasUnderwritingClaimsEvent(String eventMessageString, FactoryContext factoryContext)
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

		CirrasUnderwritingClaimsEventListenerImpl listener = (CirrasUnderwritingClaimsEventListenerImpl)webApplicationContext.getBean("claimsEventListener");

		Assert.assertNotNull(listener);

		// Replace service with stub that just receives the message so it can be checked, rather than actually process it.
		listener.setCirrasUnderwritingClaimsListenerService(new CirrasUnderwritingClaimsListenerService() {
			
			@Override
			public void processCirrasUnderwritingClaimsEvent(String eventMessageString, FactoryContext factoryContext)
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
