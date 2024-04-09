package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints;

import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.ValidationException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SyncCommodityTypeCodeRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SyncCommodityVarietyRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.PoliciesSyncEventTypes;


public class SyncCommodityTypeCodeEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(SyncCommodityTypeCodeEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.UPDATE_SYNC_UNDERWRITING,
		Scopes.DELETE_SYNC_UNDERWRITING,
		Scopes.GET_COMMODITY_TYPE_CODE
	};
	
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;
	private String commodityTypeCode = "TESTCODE";
	private Integer cropCommodityId1 = 88888888;
	private Integer cropCommodityId2 = 99999999;
	
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

		// delete commodity type code
		service.deleteCommodityTypeCode(topLevelEndpoints, commodityTypeCode);

		// delete commodities
		service.deleteSyncCommodityVariety(topLevelEndpoints, cropCommodityId1.toString());
		service.deleteSyncCommodityVariety(topLevelEndpoints, cropCommodityId2.toString());
		
	}
	
	@Test
	public void testCreateUpdateDeleteCommodityTypeCode() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testCreateUpdateDeleteCommodityTypeCode");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);

		//Create Commodity
		createCommodity(createTransactionDate, cropCommodityId1);
		createCommodity(createTransactionDate, cropCommodityId2);

		String description = "Test Code";
		Integer cropCommodityId = cropCommodityId1;
		Boolean isActive = false;

		//CREATE Commodity Type Code
		SyncCommodityTypeCodeRsrc resource = new SyncCommodityTypeCodeRsrc();

		resource.setCommodityTypeCode(commodityTypeCode);
		resource.setCropCommodityId(cropCommodityId);
		resource.setDescription(description);
		resource.setIsActive(isActive);

		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.CommodityTypeCodeCreated);

		service.synchronizeCommodityTypeCode(resource);
		
		SyncCommodityTypeCodeRsrc fetchedResource = service.getCommodityTypeCode(topLevelEndpoints, commodityTypeCode); 
		
		Assert.assertEquals("CommodityTypeCode", resource.getCommodityTypeCode(), fetchedResource.getCommodityTypeCode());
		Assert.assertEquals("CropCommodityId", resource.getCropCommodityId(), fetchedResource.getCropCommodityId());
		Assert.assertEquals("Description", resource.getDescription(), fetchedResource.getDescription());
		//Inactive records mean that the expiry date is set to the current date
		Assert.assertTrue("EffectiveDate ExpiryDate 1", fetchedResource.getEffectiveDate().compareTo(fetchedResource.getExpiryDate()) == 0);
		Assert.assertTrue("DataSyncTransDate 1", resource.getDataSyncTransDate().compareTo(fetchedResource.getDataSyncTransDate()) == 0);
		
		//UPDATE CODE
		description = "Test Code 2";
		cropCommodityId = cropCommodityId2;
		isActive = true;
		
		fetchedResource.setDescription(description);
		fetchedResource.setCropCommodityId(cropCommodityId);
		fetchedResource.setIsActive(isActive);

		fetchedResource.setDataSyncTransDate(addSeconds(transactionDate, +1));
		fetchedResource.setTransactionType(PoliciesSyncEventTypes.CommodityTypeCodeUpdated);

		service.synchronizeCommodityTypeCode(fetchedResource);
		
		SyncCommodityTypeCodeRsrc updatedResource = service.getCommodityTypeCode(topLevelEndpoints, commodityTypeCode); 
		
		Assert.assertEquals("Description 2", fetchedResource.getDescription(), updatedResource.getDescription());
		Assert.assertEquals("CropCommodityId 2", fetchedResource.getCropCommodityId(), updatedResource.getCropCommodityId());
		//Active records mean that the expiry date is set to a date in the future
		Assert.assertTrue("EffectiveDate ExpiryDate 2", updatedResource.getEffectiveDate().compareTo(updatedResource.getExpiryDate()) < 0);
		Assert.assertTrue("DataSyncTransDate 2", fetchedResource.getDataSyncTransDate().compareTo(updatedResource.getDataSyncTransDate()) == 0);

		//Don't delete but set Inactive
		updatedResource.setDataSyncTransDate(addSeconds(transactionDate, +2));
		updatedResource.setTransactionType(PoliciesSyncEventTypes.CommodityTypeCodeDeleted);
		
		service.synchronizeCommodityTypeCode(updatedResource);
		
		SyncCommodityTypeCodeRsrc inactivatedResource = service.getCommodityTypeCode(topLevelEndpoints, commodityTypeCode); 
		
		Assert.assertTrue("EffectiveDate ExpiryDate 3", inactivatedResource.getEffectiveDate().compareTo(inactivatedResource.getExpiryDate()) == 0);
		Assert.assertTrue("DataSyncTransDate 3", updatedResource.getDataSyncTransDate().compareTo(inactivatedResource.getDataSyncTransDate()) == 0);

		//CLEAN UP: DELETE CODE
		delete();
		
		logger.debug(">testCreateUpdateDeleteCommodityTypeCode");
	}

	
	@Test
	public void testUpdateCommodityTypeCodeWithoutRecordNoUpdate() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testUpdateCommodityTypeCodeWithoutRecordNoUpdate");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);

		//Create Commodity
		createCommodity(createTransactionDate, cropCommodityId1);
		createCommodity(createTransactionDate, cropCommodityId2);

		String description = "Test Code";
		Integer cropCommodityId = cropCommodityId1;
		Boolean isActive = true;

		//CREATE Commodity Type Code
		SyncCommodityTypeCodeRsrc resource = new SyncCommodityTypeCodeRsrc();

		resource.setCommodityTypeCode(commodityTypeCode);
		resource.setCropCommodityId(cropCommodityId);
		resource.setDescription(description);
		resource.setIsActive(isActive);

		resource.setDataSyncTransDate(createTransactionDate);
		
		
		//TRY TO DELETE A Code THAT DOESN'T EXIST (NO ERROR EXPECTED)
		resource.setTransactionType(PoliciesSyncEventTypes.CommodityTypeCodeDeleted);
		service.synchronizeCommodityTypeCode(resource);
	
		//SHOULD RESULT IN AN INSERT
		resource.setTransactionType(PoliciesSyncEventTypes.CommodityTypeCodeUpdated);

		service.synchronizeCommodityTypeCode(resource);

		SyncCommodityTypeCodeRsrc fetchedResource = service.getCommodityTypeCode(topLevelEndpoints, commodityTypeCode); 

		Assert.assertEquals("CommodityTypeCode", resource.getCommodityTypeCode(), fetchedResource.getCommodityTypeCode());
		Assert.assertEquals("CropCommodityId", resource.getCropCommodityId(), fetchedResource.getCropCommodityId());
		Assert.assertEquals("Description", resource.getDescription(), fetchedResource.getDescription());
		//Active records mean that the expiry date is set to a date in the future
		Assert.assertTrue("EffectiveDate ExpiryDate 1", fetchedResource.getEffectiveDate().compareTo(fetchedResource.getExpiryDate()) < 0);
		Assert.assertTrue("DataSyncTransDate 1", resource.getDataSyncTransDate().compareTo(fetchedResource.getDataSyncTransDate()) == 0);
		
		//NO UPDATE EXPECTED BECAUSE TRANSACTION DATE IS EARLIER THAN STORED ONE
		fetchedResource.setIsActive(isActive);
		fetchedResource.setDataSyncTransDate(addSeconds(createTransactionDate, -1));
		fetchedResource.setTransactionType(PoliciesSyncEventTypes.CommodityTypeCodeUpdated);
		service.synchronizeCommodityTypeCode(fetchedResource);
		
		SyncCommodityTypeCodeRsrc notUpdatedResource = service.getCommodityTypeCode(topLevelEndpoints, commodityTypeCode); 

		Assert.assertTrue("DataSyncTransDate 2", resource.getDataSyncTransDate().compareTo(notUpdatedResource.getDataSyncTransDate()) == 0);

		//UPDATE CODE
		description = "Test Code 2";
		cropCommodityId = cropCommodityId2;
		isActive = false;
		
		notUpdatedResource.setDescription(description);
		notUpdatedResource.setCropCommodityId(cropCommodityId);
		notUpdatedResource.setIsActive(isActive);

		//UPDATE EXPECTED BECAUSE RECORD EXISTS IT WILL UPDATE IT
		notUpdatedResource.setDataSyncTransDate(addSeconds(transactionDate, +1));
		notUpdatedResource.setTransactionType(PoliciesSyncEventTypes.CommodityTypeCodeCreated);

		service.synchronizeCommodityTypeCode(notUpdatedResource);
		
		SyncCommodityTypeCodeRsrc updatedResource = service.getCommodityTypeCode(topLevelEndpoints, commodityTypeCode); 

		Assert.assertEquals("Description 2", notUpdatedResource.getDescription(), updatedResource.getDescription());
		Assert.assertEquals("CropCommodityId 2", notUpdatedResource.getCropCommodityId(), updatedResource.getCropCommodityId());
		//Inactive records mean that the expiry date is set to the current date
		Assert.assertTrue("EffectiveDate ExpiryDate 2", updatedResource.getEffectiveDate().compareTo(updatedResource.getExpiryDate()) == 0);
		Assert.assertTrue("DataSyncTransDate 2", notUpdatedResource.getDataSyncTransDate().compareTo(updatedResource.getDataSyncTransDate()) == 0);

		//CLEAN UP: DELETE CODE
		delete();		
		
		logger.debug(">testUpdateCommodityTypeCodeWithoutRecordNoUpdate");
	}

	private void createCommodity(Date createTransactionDate, Integer cropCommodityId)
			throws CirrasUnderwritingServiceException, ValidationException {
		//CREATE PARENT COMMODITY
		SyncCommodityVarietyRsrc resource = new SyncCommodityVarietyRsrc();

		resource.setCropId(cropCommodityId);
		resource.setCropName("TEST COMMODITY");
		resource.setInsurancePlanId(4);
		resource.setShortLabel("SHRT");
		resource.setPlantDurationTypeCode("ANNUAL");
		resource.setIsInventoryCrop(true);
		resource.setIsYieldCrop(true);
		resource.setIsUnderwritingCrop(true);
		resource.setIsProductInsurableInd(true);
		resource.setIsCropInsuranceEligibleInd(true);
		resource.setIsPlantInsuranceEligibleInd(true);
		resource.setIsOtherInsuranceEligibleInd(true);
		resource.setYieldMeasUnitTypeCode("TONNE");
		resource.setYieldDecimalPrecision(2);
		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.CommodityVarietyCreated);

		service.synchronizeCommodityVariety(resource);
	}

	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}
	

}
