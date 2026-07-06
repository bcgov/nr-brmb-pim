package ca.bc.gov.mal.cirras.underwriting.controllers;

import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.clients.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.clients.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.clients.ValidationException;
import ca.bc.gov.mal.cirras.underwriting.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.models.SyncClaimCalculationBerries;
import ca.bc.gov.mal.cirras.underwriting.data.resources.ClaimSyncEventTypes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.ContactRsrc;
import ca.bc.gov.mal.cirras.underwriting.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;
import ca.bc.gov.mal.cirras.underwriting.data.resources.PoliciesSyncEventTypes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.SyncClaimCalculationSimpleRsrc;


public class SyncClaimCalculationSimpleEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(SyncClaimCalculationSimpleEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.UPDATE_SYNC_UNDERWRITING,
		Scopes.DELETE_SYNC_UNDERWRITING,
		Scopes.GET_CLAIM_CALCULATION_SIMPLE
	};
	
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;
	private String claimCalculationBerriesGuid = "testccBerriesGuid15621";

	@Before
	public void prepareTests() throws CirrasUnderwritingServiceException, Oauth2ClientException, NotFoundDaoException, DaoException{
		service = getService(SCOPES);
		topLevelEndpoints = service.getTopLevelEndpoints();

		delete();

	}

	@After 
	public void cleanUp() throws CirrasUnderwritingServiceException, NotFoundDaoException, DaoException {
		delete();
	}
	
	private void delete() throws NotFoundDaoException, DaoException, CirrasUnderwritingServiceException{
		
		service.deleteSyncClaimCalculationSimple(topLevelEndpoints, claimCalculationBerriesGuid);
		
	}
	
	@Test
	public void testCreateUpdateDeleteSyncClaimCalculationSimple() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testCreateUpdateSyncClaimCalculationSimple");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 because they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);

		//CREATE Claim Calculation Berries
		SyncClaimCalculationSimpleRsrc resource = new SyncClaimCalculationSimpleRsrc();
		resource.setCropCommodityId(10);
		resource.setContractId(12345666);
		resource.setCropYear(2025);
		resource.setClaimCalculationGuid("testClaimCalculationGuid");
		resource.setCalculationStatusCode("DRAFT");
		resource.setCalculationVersion(1);

		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(ClaimSyncEventTypes.ClaimCalculationBerriesCreated);
		
		SyncClaimCalculationBerries model = new SyncClaimCalculationBerries();
		model.setClaimCalculationBerriesGuid(claimCalculationBerriesGuid);
		model.setTotalYieldForCalculation(100.0);
		resource.setSyncClaimCalculationBerries(model);

		service.synchronizeClaimCalculationSimple(resource);
		
		SyncClaimCalculationSimpleRsrc fetchedResource = service.getSyncClaimCalculationSimple(topLevelEndpoints, claimCalculationBerriesGuid); 

		Assert.assertNotNull(fetchedResource);
		Assert.assertNotNull(fetchedResource.getSyncClaimCalculationBerries());
		Assert.assertEquals("CropCommodityId", resource.getCropCommodityId(), fetchedResource.getCropCommodityId());
		Assert.assertEquals("ContractId", resource.getContractId(), fetchedResource.getContractId());
		Assert.assertEquals("CropYear", resource.getCropYear(), fetchedResource.getCropYear());
		Assert.assertEquals("ClaimCalculationGuid", resource.getClaimCalculationGuid(), fetchedResource.getClaimCalculationGuid());
		Assert.assertEquals("CalculationStatusCode", resource.getCalculationStatusCode(), fetchedResource.getCalculationStatusCode());
		Assert.assertEquals("CalculationVersion", resource.getCalculationVersion(), fetchedResource.getCalculationVersion());
		Assert.assertEquals("ClaimCalculationBerriesGuid", resource.getSyncClaimCalculationBerries().getClaimCalculationBerriesGuid(), fetchedResource.getSyncClaimCalculationBerries().getClaimCalculationBerriesGuid());
		Assert.assertEquals("TotalYieldForCalculation", resource.getSyncClaimCalculationBerries().getTotalYieldForCalculation(), fetchedResource.getSyncClaimCalculationBerries().getTotalYieldForCalculation());

		Assert.assertTrue("DataSyncTransDate 1", resource.getDataSyncTransDate().compareTo(fetchedResource.getDataSyncTransDate()) == 0);
		
		//UPDATE Claim Calculation Berries
		fetchedResource.setCalculationStatusCode("APPROVED");
		fetchedResource.getSyncClaimCalculationBerries().setTotalYieldForCalculation(200.0);

		fetchedResource.setDataSyncTransDate(addSeconds(transactionDate, +1));
		fetchedResource.setTransactionType(ClaimSyncEventTypes.ClaimCalculationBerriesUpdated);

		service.synchronizeClaimCalculationSimple(fetchedResource);
		
		SyncClaimCalculationSimpleRsrc updatedResource = service.getSyncClaimCalculationSimple(topLevelEndpoints, claimCalculationBerriesGuid); 

		Assert.assertNotNull(updatedResource);
		Assert.assertNotNull(updatedResource.getSyncClaimCalculationBerries());
		Assert.assertEquals("CalculationStatusCode", fetchedResource.getCalculationStatusCode(), updatedResource.getCalculationStatusCode());
		Assert.assertEquals("TotalYieldForCalculation", fetchedResource.getSyncClaimCalculationBerries().getTotalYieldForCalculation(), updatedResource.getSyncClaimCalculationBerries().getTotalYieldForCalculation());
		Assert.assertTrue("DataSyncTransDate 2", fetchedResource.getDataSyncTransDate().compareTo(updatedResource.getDataSyncTransDate()) == 0);
		
		//CLEAN UP: DELETE Claim Calculation Berries
		delete();
		
		logger.debug(">testCreateUpdateSyncClaimCalculationSimple");
	}

	
	@Test
	public void testUpdateSyncClaimCalculationSimpleWithoutRecordNoUpdate() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testUpdateSyncClaimCalculationSimpleWithoutRecordNoUpdate");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);

		//CREATE Claim Calculation Berries
		SyncClaimCalculationSimpleRsrc resource = new SyncClaimCalculationSimpleRsrc();
		resource.setCropCommodityId(10);
		resource.setContractId(12345666);
		resource.setCropYear(2025);
		resource.setClaimCalculationGuid("testClaimCalculationGuid");
		resource.setCalculationStatusCode("DRAFT");
		resource.setCalculationVersion(1);
		resource.setDataSyncTransDate(createTransactionDate);

		SyncClaimCalculationBerries model = new SyncClaimCalculationBerries();
		model.setClaimCalculationBerriesGuid(claimCalculationBerriesGuid);
		model.setTotalYieldForCalculation(100.0);
		resource.setSyncClaimCalculationBerries(model);
		
		//TRY TO DELETE A record THAT DOESN'T EXIST (NO ERROR EXPECTED)
		resource.setTransactionType(ClaimSyncEventTypes.ClaimCalculationBerriesDeleted);
		service.synchronizeClaimCalculationSimple(resource);

		//SHOULD RESULT IN AN INSERT
		resource.setTransactionType(ClaimSyncEventTypes.ClaimCalculationBerriesUpdated);
		service.synchronizeClaimCalculationSimple(resource);

		SyncClaimCalculationSimpleRsrc fetchedResource = service.getSyncClaimCalculationSimple(topLevelEndpoints, claimCalculationBerriesGuid); 

		Assert.assertNotNull(fetchedResource);
		Assert.assertNotNull(fetchedResource.getSyncClaimCalculationBerries());
		Assert.assertEquals("CropCommodityId", resource.getCropCommodityId(), fetchedResource.getCropCommodityId());
		Assert.assertEquals("ContractId", resource.getContractId(), fetchedResource.getContractId());
		Assert.assertEquals("CropYear", resource.getCropYear(), fetchedResource.getCropYear());
		Assert.assertEquals("ClaimCalculationGuid", resource.getClaimCalculationGuid(), fetchedResource.getClaimCalculationGuid());
		Assert.assertEquals("CalculationStatusCode", resource.getCalculationStatusCode(), fetchedResource.getCalculationStatusCode());
		Assert.assertEquals("CalculationVersion", resource.getCalculationVersion(), fetchedResource.getCalculationVersion());
		Assert.assertEquals("ClaimCalculationBerriesGuid", resource.getSyncClaimCalculationBerries().getClaimCalculationBerriesGuid(), fetchedResource.getSyncClaimCalculationBerries().getClaimCalculationBerriesGuid());
		Assert.assertEquals("TotalYieldForCalculation", resource.getSyncClaimCalculationBerries().getTotalYieldForCalculation(), fetchedResource.getSyncClaimCalculationBerries().getTotalYieldForCalculation());

		Assert.assertTrue("DataSyncTransDate 1", resource.getDataSyncTransDate().compareTo(fetchedResource.getDataSyncTransDate()) == 0);

		
		//NO UPDATE EXPECTED BECAUSE TRANSACTION DATE IS EARLIER THAN STORED ONE
		fetchedResource.setDataSyncTransDate(addSeconds(createTransactionDate, -1));
		fetchedResource.setTransactionType(ClaimSyncEventTypes.ClaimCalculationBerriesUpdated);
		service.synchronizeClaimCalculationSimple(fetchedResource);
		
		SyncClaimCalculationSimpleRsrc notUpdatedResource = service.getSyncClaimCalculationSimple(topLevelEndpoints, claimCalculationBerriesGuid); 

		Assert.assertTrue("DataSyncTransDate 2", resource.getDataSyncTransDate().compareTo(notUpdatedResource.getDataSyncTransDate()) == 0);

		
		//UPDATE Claim Calculation Berries
		notUpdatedResource.setCalculationStatusCode("APPROVED");
		notUpdatedResource.getSyncClaimCalculationBerries().setTotalYieldForCalculation(200.0);

		//UPDATE EXPECTED BECAUSE RECORD EXISTS IT WILL UPDATE IT
		notUpdatedResource.setTransactionType(ClaimSyncEventTypes.ClaimCalculationBerriesCreated);
		notUpdatedResource.setDataSyncTransDate(addSeconds(createTransactionDate, 10));
		service.synchronizeClaimCalculationSimple(notUpdatedResource);
		
		SyncClaimCalculationSimpleRsrc updatedResource = service.getSyncClaimCalculationSimple(topLevelEndpoints, claimCalculationBerriesGuid); 

		Assert.assertNotNull(updatedResource);
		Assert.assertNotNull(updatedResource.getSyncClaimCalculationBerries());
		Assert.assertEquals("CalculationStatusCode", notUpdatedResource.getCalculationStatusCode(), updatedResource.getCalculationStatusCode());
		Assert.assertEquals("TotalYieldForCalculation", notUpdatedResource.getSyncClaimCalculationBerries().getTotalYieldForCalculation(), updatedResource.getSyncClaimCalculationBerries().getTotalYieldForCalculation());
		Assert.assertTrue("DataSyncTransDate 2", notUpdatedResource.getDataSyncTransDate().compareTo(updatedResource.getDataSyncTransDate()) == 0);

		//CLEAN UP: DELETE CODE
		delete();		
		
		logger.debug(">testUpdateSyncClaimCalculationSimpleWithoutRecordNoUpdate");
	}

	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}
	

}
