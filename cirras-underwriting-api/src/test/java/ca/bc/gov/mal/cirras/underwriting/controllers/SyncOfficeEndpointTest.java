package ca.bc.gov.mal.cirras.underwriting.controllers;

import java.util.Calendar;
import java.util.Date;

import org.junit.After;
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


public class SyncOfficeEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(SyncOfficeEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.UPDATE_SYNC_UNDERWRITING,
		Scopes.DELETE_SYNC_UNDERWRITING
	};
	
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;
	private Integer officeId = 999999999;
	private String testOffice = "Victoria";  
	
	@Before
	public void prepareTests() throws CirrasUnderwritingServiceException, Oauth2ClientException, NotFoundDaoException, DaoException{
		service = getService(SCOPES);
		topLevelEndpoints = service.getTopLevelEndpoints();

		deleteOffice();

	}

	@After 
	public void cleanUp() throws CirrasUnderwritingServiceException, NotFoundDaoException, DaoException {
		deleteOffice();
	}

	
	private void deleteOffice() throws NotFoundDaoException, DaoException, CirrasUnderwritingServiceException{

		service.deleteSyncCode(topLevelEndpoints, CodeTableTypes.Offices, officeId.toString());
		
	}
	
	@Test
	public void testCreateUpdateDeleteOffice() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testCreateUpdateOffice");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		Date transactionDate = new Date();
		Date createTransactionDate = addSeconds(transactionDate, -1);

		//CREATE CODE
		SyncCodeRsrc resource = new SyncCodeRsrc();
		
		resource.setCodeTableType(CodeTableTypes.Offices);
		resource.setUniqueKeyInteger(officeId);
		resource.setDescription(testOffice);
		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.CodeCreated);

		service.synchronizeCode(resource);

		//UPDATE CODE
		resource.setDescription(testOffice + " 2");
		resource.setDataSyncTransDate(addSeconds(transactionDate, +1));
		resource.setTransactionType(PoliciesSyncEventTypes.CodeUpdated);
		
		service.synchronizeCode(resource);

		//CLEAN UP: DELETE CODE
		deleteOffice();
		
		logger.debug(">testCreateUpdateOffice");
	}

	
	@Test
	public void testUpdateOfficeWithoutRecordNoUpdate() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testUpdateOfficeWithoutRecordNoUpdate");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		Date transactionDate = new Date();
		Date createTransactionDate = addSeconds(transactionDate, -1);

		//CREATE CODE
		SyncCodeRsrc resource = new SyncCodeRsrc();

		resource.setCodeTableType(CodeTableTypes.Offices);
		resource.setUniqueKeyInteger(officeId);
		resource.setDescription(testOffice);
		resource.setDataSyncTransDate(createTransactionDate);

		
		//TRY TO DELETE A Code THAT DOESN'T EXIST (NO ERROR EXECTED)
		resource.setTransactionType(PoliciesSyncEventTypes.CodeDeleted);
		service.synchronizeCode(resource);

		//SHOULD RESULT IN AN INSERT
		resource.setTransactionType(PoliciesSyncEventTypes.CodeUpdated);

		//Expect insert (should be detected)
		service.synchronizeCode(resource);

		//UPDATE COMMODITY
		resource.setDescription(testOffice + " 2");
		resource.setDataSyncTransDate(addSeconds(transactionDate, -1));
		resource.setTransactionType(PoliciesSyncEventTypes.CodeUpdated);

		//NO UPDATE EXPECTED BECAUSE TRANSACTION DATE IS EARLIER THAN STORED ONE
		service.synchronizeCode(resource);
		
		//UPDATE CODE AND ACTIVATE --> USE CREATED TYPE
		resource.setDataSyncTransDate(addSeconds(transactionDate, 1));
		resource.setTransactionType(PoliciesSyncEventTypes.CodeCreated);

		//UPDATE EXPECTED BECAUSE RECORD EXISTS IT WILL UPDATE IT
		service.synchronizeCode(resource);		
		
		//CLEAN UP: DELETE COMMODITY
		deleteOffice();
		
		logger.debug(">testUpdateOfficeWithoutRecordNoUpdate");
	}

	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}


}
