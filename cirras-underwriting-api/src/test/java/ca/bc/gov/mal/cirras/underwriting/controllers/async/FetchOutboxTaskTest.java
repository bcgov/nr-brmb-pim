package ca.bc.gov.mal.cirras.underwriting.controllers.async;

import java.util.Map;
import java.util.Properties;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.mal.cirras.underwriting.clients.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.clients.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.controllers.publisher.EventPublisherException;
import ca.bc.gov.mal.cirras.underwriting.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.DopYieldContractSimpleRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.services.CirrasUnderwritingOutboxService;
import ca.bc.gov.mal.cirras.underwriting.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;
import jakarta.mail.internet.AddressException;


public class FetchOutboxTaskTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(FetchOutboxTaskTest.class);

	private static final String[] SCOPES = {
			Scopes.GET_TOP_LEVEL
	};
		
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;
	
	
	@Before
	public void prepareTests() throws CirrasUnderwritingServiceException, Oauth2ClientException, NotFoundDaoException, DaoException{
		service = getService(SCOPES);
		topLevelEndpoints = service.getTopLevelEndpoints();

		delete();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException {
		delete();
	}

		
	private void delete() throws NotFoundDaoException, DaoException {
	}

	// Ensure that enableAsyncProcs=true in super class before running this test.
	@Test
	public void testFetchOutboxTask() throws InterruptedException, AddressException {
		logger.debug("<testFetchOutboxTask");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		Assert.assertTrue(enableAsyncProcs);
		
		// Not really a unit test, but can use to just run the web server for x minutes. Useful for testing the AsynchronousProcessesService and its associated threads.
//		synchronized (this) { 
//			this.wait(10*60*1000);  // 10 minutes.
//		}
		//CirrasUnderwritingOutboxService cirrasUnderwritingOutboxService = (CirrasUnderwritingOutboxService)webApplicationContext.getBean("cirrasUnderwritingOutboxService");

		FetchOutboxTask fetchOutboxTask = (FetchOutboxTask)webApplicationContext.getBean("fetchOutboxTask");

		Assert.assertNotNull(fetchOutboxTask);
		
//Error because: this.declaredYieldContractCommodityBerriesOutboxDao" is null
		fetchOutboxTask.setCirrasUnderwritingOutboxService(new CirrasUnderwritingOutboxService(){
			@Override
			public void deleteDeclaredYieldContractCommodityBerriesOutbox(
					Integer declaredYieldContractCommodityBerriesOutboxId) throws DaoException, NotFoundDaoException {
				//Do nothing: This method would normally delete the outbox record
			}
			
			@Override
			public void publishDopYieldContractSimple(String eventType, DopYieldContractSimpleRsrc beforeDopYieldContractSimpleRsrc,
					DopYieldContractSimpleRsrc afterDopYieldContractSimpleRsrc, Map<String, String> sourceIdentifiers)
					throws EventPublisherException {
				//Do nothing: This method would normally push the message to the messaging queue
			}
		});
			
		fetchOutboxTask.init();
		fetchOutboxTask.run();
		
		logger.debug(">testFetchOutboxTask");
	}
	

}
