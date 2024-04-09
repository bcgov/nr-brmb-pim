package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints;

import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.ValidationException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SyncCodeRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;
import ca.bc.gov.mal.cirras.policies.model.v1.CodeTableTypes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.PoliciesSyncEventTypes;


public class SyncPolicyStatusCodeEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(SyncPolicyStatusCodeEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.UPDATE_SYNC_UNDERWRITING,
		Scopes.DELETE_SYNC_UNDERWRITING
	};
	
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;
	private String testPolicyStatusCode = "TEST_CODE"; 
	private String testDescription = "Test Policy Status"; 
	
	@Before
	public void prepareTests() throws CirrasUnderwritingServiceException, Oauth2ClientException, NotFoundDaoException, DaoException{
		service = getService(SCOPES);
		topLevelEndpoints = service.getTopLevelEndpoints();

		deletePolicyStatusCode();

	}

	@After 
	public void cleanUp() throws CirrasUnderwritingServiceException, NotFoundDaoException, DaoException {
		deletePolicyStatusCode();
	}

	
	private void deletePolicyStatusCode() throws NotFoundDaoException, DaoException, CirrasUnderwritingServiceException{

		delete(testPolicyStatusCode);

	}
	
	private void delete(String policyStatusCode) throws NotFoundDaoException, DaoException, CirrasUnderwritingServiceException{

		service.deleteSyncCode(topLevelEndpoints, CodeTableTypes.PolicyStatusCode, policyStatusCode);
		
	}
	
	@Test
	public void testCreateUpdateDeletePolicyStatusCode() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testCreateUpdatePolicyStatusCode");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		Date transactionDate = new Date();
		Date createTransactionDate = addSeconds(transactionDate, -1);

		//CREATE CODE
		SyncCodeRsrc resource = new SyncCodeRsrc();
		
		resource.setCodeTableType(CodeTableTypes.PolicyStatusCode);
		resource.setUniqueKeyString(testPolicyStatusCode);
		resource.setDescription(testDescription);
		resource.setIsActive(true);
		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.CodeCreated);

		service.synchronizeCode(resource);

		//UPDATE CODE
		resource.setDescription(testDescription + " 2");
		resource.setDataSyncTransDate(addSeconds(transactionDate, +1));
		resource.setTransactionType(PoliciesSyncEventTypes.CodeUpdated);
		
		service.synchronizeCode(resource);

		//DELETE CODE (SET INACTIVE)
		resource.setDataSyncTransDate(addSeconds(transactionDate, +2));
		resource.setTransactionType(PoliciesSyncEventTypes.CodeDeleted);

		service.synchronizeCode(resource);

		//CLEAN UP: DELETE CODE
		delete(testPolicyStatusCode);
		
		logger.debug(">testCreateUpdatePolicyStatusCode");
	}

	
	@Test
	public void testUpdatePolicyStatusCodeWithoutRecordNoUpdate() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testUpdatePolicyStatusCodeWithoutRecordNoUpdate");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		Date transactionDate = new Date();
		Date createTransactionDate = addSeconds(transactionDate, -1);

		//CREATE CODE
		SyncCodeRsrc resource = new SyncCodeRsrc();

		resource.setCodeTableType(CodeTableTypes.PolicyStatusCode);
		resource.setUniqueKeyString(testPolicyStatusCode);
		resource.setDescription(testDescription);
		resource.setIsActive(false);
		resource.setDataSyncTransDate(createTransactionDate);

		
		//TRY TO DELETE A Code THAT DOESN'T EXIST (NO ERROR EXECTED)
		resource.setTransactionType(PoliciesSyncEventTypes.CodeDeleted);
		service.synchronizeCode(resource);

		//SHOULD RESULT IN AN INSERT
		resource.setTransactionType(PoliciesSyncEventTypes.CodeUpdated);

		//Expect insert (should be detected)
		service.synchronizeCode(resource);

		//UPDATE COMMODITY
		resource.setDescription(testDescription + " 2");
		resource.setDataSyncTransDate(addSeconds(transactionDate, -1));
		resource.setTransactionType(PoliciesSyncEventTypes.CodeUpdated);

		//NO UPDATE EXPECTED BECAUSE TRANSACTION DATE IS EARLIER THAN STORED ONE
		service.synchronizeCode(resource);
		
		//UPDATE CODE AND ACTIVATE --> USE CREATED TYPE
		resource.setDataSyncTransDate(addSeconds(transactionDate, 1));
		resource.setIsActive(true);
		resource.setTransactionType(PoliciesSyncEventTypes.CodeCreated);

		//UPDATE EXPECTED BECAUSE RECORD EXISTS IT WILL UPDATE IT
		service.synchronizeCode(resource);		
		
		//CLEAN UP: DELETE COMMODITY
		delete(testPolicyStatusCode);
		
		logger.debug(">testUpdatePolicyStatusCodeWithoutRecordNoUpdate");
	}
	
	@Test
	public void testCreateActivateInactivatePolicyStatusCode() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testInactivatePolicyStatusCode");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		Date transactionDate = new Date();
		Date createTransactionDate = addSeconds(transactionDate, -1);

		//CREATE CODE
		SyncCodeRsrc resource = new SyncCodeRsrc();
		
		resource.setCodeTableType(CodeTableTypes.PolicyStatusCode);
		resource.setUniqueKeyString(testPolicyStatusCode);
		resource.setDescription(testDescription);
		resource.setIsActive(true);
		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.CodeCreated);

		service.synchronizeCode(resource);

		//INACTIVATE (expiry date set to now())
		resource.setIsActive(false);
		resource.setDataSyncTransDate(addSeconds(transactionDate, +1));
		resource.setTransactionType(PoliciesSyncEventTypes.CodeUpdated);
		
		service.synchronizeCode(resource);

		//ACTIVATE (set effective date set to now() and expiry date to maxdate)
		resource.setIsActive(true);
		resource.setDataSyncTransDate(addSeconds(transactionDate, +2));
		resource.setTransactionType(PoliciesSyncEventTypes.CodeUpdated);
		
		service.synchronizeCode(resource);

		//CLEAN UP: DELETE CODE
		delete(testPolicyStatusCode);
		
		logger.debug(">testInactivatePolicyStatusCode");
	}
	
	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}


}
