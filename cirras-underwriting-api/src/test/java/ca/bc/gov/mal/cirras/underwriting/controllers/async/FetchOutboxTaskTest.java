package ca.bc.gov.mal.cirras.underwriting.controllers.async;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public class FetchOutboxTaskTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(FetchOutboxTaskTest.class);
	
	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
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
	public void testFetchOutboxTask() throws InterruptedException {
		logger.debug("<testFetchOutboxTask");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		// Not really a unit test, but can use to just run the web server for x minutes. Useful for testing the AsynchronousProcessesService and its associated threads.
		synchronized (this) { 
			this.wait(10*60*1000);  // 10 minutes.
		}

		// TODO: Might be able to test the actual outbox processing.
		
		logger.debug(">testFetchOutboxTask");
	}

}
