package ca.bc.gov.mal.cirras.underwriting.controllers;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.clients.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.clients.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.clients.ValidationException;
import ca.bc.gov.mal.cirras.underwriting.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.SyncCodeRsrc;
import ca.bc.gov.mal.cirras.underwriting.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;
import ca.bc.gov.mal.cirras.policies.model.v1.CodeTableTypes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.PoliciesSyncEventTypes;


public class SyncCodeTableEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(SyncCodeTableEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.UPDATE_SYNC_UNDERWRITING,
		Scopes.DELETE_SYNC_UNDERWRITING
	};
	
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;
	private String testCode = "TEST_CODE"; 
	private String testDescription = "Test Description"; 
	
	@Before
	public void prepareTests() throws CirrasUnderwritingServiceException, Oauth2ClientException, NotFoundDaoException, DaoException{
		service = getService(SCOPES);
		topLevelEndpoints = service.getTopLevelEndpoints();

	}

	
	@Test
	public void testUnsupportedCodeTableType() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testUnsupportedCodeTableType");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		Date transactionDate = new Date();
		//CREATE CODE
		SyncCodeRsrc resource = new SyncCodeRsrc();
		
		resource.setCodeTableType(CodeTableTypes.PerilTypes);
		resource.setUniqueKeyString(testCode);
		resource.setDescription(testDescription);
		resource.setIsActive(true);
		resource.setDataSyncTransDate(transactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.CodeCreated);

		service.synchronizeCode(resource);
		
		//Expectation: No error but a log entry that the code table type is not supported
		//Search for "Ignoring codeTableType as it's not supported by the underwriting api" in the log

		logger.debug(">testUnsupportedCodeTableType");
	}

	
	

}
