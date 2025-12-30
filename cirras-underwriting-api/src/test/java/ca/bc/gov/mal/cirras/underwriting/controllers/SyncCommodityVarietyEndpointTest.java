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
import ca.bc.gov.mal.cirras.underwriting.data.resources.LegalLandRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.SyncCommodityVarietyRsrc;
import ca.bc.gov.mal.cirras.underwriting.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;
import ca.bc.gov.mal.cirras.underwriting.data.resources.PoliciesSyncEventTypes;


public class SyncCommodityVarietyEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(SyncCommodityVarietyEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.CREATE_SYNC_UNDERWRITING,
		Scopes.UPDATE_SYNC_UNDERWRITING,
		Scopes.DELETE_SYNC_UNDERWRITING,
		Scopes.GET_COMMODITY_VARIETY
	};
	
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;
	private Integer testCommodityId = 99999;
	private Integer testVarietyId = 77777;
	
	@Before
	public void prepareTests() throws CirrasUnderwritingServiceException, Oauth2ClientException, NotFoundDaoException, DaoException{
		service = getService(SCOPES);
		topLevelEndpoints = service.getTopLevelEndpoints();

		deleteCropVarietyCommodity();

	}

	@After 
	public void cleanUp() throws CirrasUnderwritingServiceException, NotFoundDaoException, DaoException {
		deleteCropVarietyCommodity();
	}

	
	private void deleteCropVarietyCommodity() throws NotFoundDaoException, DaoException, CirrasUnderwritingServiceException{

		//Delete variety
		delete(testVarietyId);
		
		//Delete Commodity
		delete(testCommodityId);

	}
	
	private void delete(Integer cropId) throws NotFoundDaoException, DaoException, CirrasUnderwritingServiceException{

		service.deleteSyncCommodityVariety(topLevelEndpoints, cropId.toString());
		
	}
	
	//
	//COMMODITY TESTS
	//
	@Test
	public void testCreateUpdateDeleteCommodity() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testCreateUpdateDeleteCommodity");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);

		//CREATE COMMODITY
		SyncCommodityVarietyRsrc resource = new SyncCommodityVarietyRsrc();

		resource.setCropId(testCommodityId);
		resource.setCropName("TEST COMMODITY");
		resource.setParentCropId(null);
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
		resource.setIsInventoryCrop(true);
		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.CommodityVarietyCreated);

		service.synchronizeCommodityVariety(resource);

		SyncCommodityVarietyRsrc fetchedResource = service.getSyncCommodityVariety(topLevelEndpoints, testCommodityId.toString()); 

		Assert.assertEquals("CropId 1", resource.getCropId(), fetchedResource.getCropId());
		Assert.assertNull("ParentCropId 1", fetchedResource.getParentCropId());
		Assert.assertEquals("CropName 1", resource.getCropName(), fetchedResource.getCropName());
		Assert.assertEquals("InsurancePlanId 1", resource.getInsurancePlanId(), fetchedResource.getInsurancePlanId());
		Assert.assertEquals("ShortLabel 1", resource.getShortLabel(), fetchedResource.getShortLabel());
		Assert.assertEquals("PlantDurationTypeCode 1", resource.getPlantDurationTypeCode(), fetchedResource.getPlantDurationTypeCode());
		Assert.assertEquals("IsInventoryCropInd 1", resource.getIsInventoryCrop(), fetchedResource.getIsInventoryCrop());
		Assert.assertEquals("IsYieldCropInd 1", resource.getIsYieldCrop(), fetchedResource.getIsYieldCrop());
		Assert.assertEquals("IsUnderwritingCropInd 1", resource.getIsUnderwritingCrop(), fetchedResource.getIsUnderwritingCrop());
		Assert.assertEquals("IsProductInsurableInd 1", resource.getIsProductInsurableInd(), fetchedResource.getIsProductInsurableInd());
		Assert.assertEquals("IsCropInsuranceEligibleInd 1", resource.getIsCropInsuranceEligibleInd(), fetchedResource.getIsCropInsuranceEligibleInd());
		Assert.assertEquals("IsPlantInsuranceEligibleInd 1", resource.getIsPlantInsuranceEligibleInd(), fetchedResource.getIsPlantInsuranceEligibleInd());
		Assert.assertEquals("IsOtherInsuranceEligibleInd 1", resource.getIsOtherInsuranceEligibleInd(), fetchedResource.getIsOtherInsuranceEligibleInd());
		Assert.assertEquals("YieldMeasUnitTypeCode 1", resource.getYieldMeasUnitTypeCode(), fetchedResource.getYieldMeasUnitTypeCode());
		Assert.assertEquals("YieldDecimalPrecision 1", resource.getYieldDecimalPrecision(), fetchedResource.getYieldDecimalPrecision());
		Assert.assertTrue("DataSyncTransDate 1", resource.getDataSyncTransDate().compareTo(fetchedResource.getDataSyncTransDate()) == 0);

		//UPDATE COMMODITY
		fetchedResource.setCropName("TEST COMMODITY 2");
		fetchedResource.setInsurancePlanId(5);
		fetchedResource.setShortLabel("SRT");
		fetchedResource.setPlantDurationTypeCode("PRENNIAL");
		fetchedResource.setIsInventoryCrop(false);
		fetchedResource.setIsYieldCrop(false);
		fetchedResource.setIsUnderwritingCrop(false);
		fetchedResource.setIsProductInsurableInd(false);
		fetchedResource.setIsCropInsuranceEligibleInd(false);
		fetchedResource.setIsPlantInsuranceEligibleInd(false);
		fetchedResource.setIsOtherInsuranceEligibleInd(false);
		fetchedResource.setYieldMeasUnitTypeCode("LBS");
		fetchedResource.setYieldDecimalPrecision(0);
		fetchedResource.setIsInventoryCrop(false);

		fetchedResource.setDataSyncTransDate(addSeconds(transactionDate, +1));
		fetchedResource.setTransactionType(PoliciesSyncEventTypes.CommodityVarietyUpdated);
		
		service.synchronizeCommodityVariety(fetchedResource);
		
		SyncCommodityVarietyRsrc updatedResource = service.getSyncCommodityVariety(topLevelEndpoints, testCommodityId.toString()); 

		Assert.assertEquals("CropName 2", fetchedResource.getCropName(), updatedResource.getCropName());
		Assert.assertEquals("InsurancePlanId 2", fetchedResource.getInsurancePlanId(), updatedResource.getInsurancePlanId());
		Assert.assertEquals("ShortLabel 2", fetchedResource.getShortLabel(), updatedResource.getShortLabel());
		Assert.assertEquals("PlantDurationTypeCode 2", fetchedResource.getPlantDurationTypeCode(), updatedResource.getPlantDurationTypeCode());
		Assert.assertEquals("IsInventoryCropInd 2", fetchedResource.getIsInventoryCrop(), updatedResource.getIsInventoryCrop());
		Assert.assertEquals("IsYieldCropInd 2", fetchedResource.getIsYieldCrop(), updatedResource.getIsYieldCrop());
		Assert.assertEquals("IsUnderwritingCropInd 2", fetchedResource.getIsUnderwritingCrop(), updatedResource.getIsUnderwritingCrop());
		Assert.assertEquals("IsProductInsurableInd 2", fetchedResource.getIsProductInsurableInd(), updatedResource.getIsProductInsurableInd());
		Assert.assertEquals("IsCropInsuranceEligibleInd 2", fetchedResource.getIsCropInsuranceEligibleInd(), updatedResource.getIsCropInsuranceEligibleInd());
		Assert.assertEquals("IsPlantInsuranceEligibleInd 2", fetchedResource.getIsPlantInsuranceEligibleInd(), updatedResource.getIsPlantInsuranceEligibleInd());
		Assert.assertEquals("IsOtherInsuranceEligibleInd 2", fetchedResource.getIsOtherInsuranceEligibleInd(), updatedResource.getIsOtherInsuranceEligibleInd());
		Assert.assertEquals("YieldMeasUnitTypeCode 2", fetchedResource.getYieldMeasUnitTypeCode(), updatedResource.getYieldMeasUnitTypeCode());
		Assert.assertEquals("YieldDecimalPrecision 2", fetchedResource.getYieldDecimalPrecision(), updatedResource.getYieldDecimalPrecision());
		Assert.assertTrue("DataSyncTransDate 2", fetchedResource.getDataSyncTransDate().compareTo(updatedResource.getDataSyncTransDate()) == 0);

		//DELETE COMMODITY (SET INACTIVE)
		updatedResource.setDataSyncTransDate(addSeconds(transactionDate, +2));
		updatedResource.setTransactionType(PoliciesSyncEventTypes.CommodityVarietyDeleted);

		service.synchronizeCommodityVariety(updatedResource);

		//CLEAN UP: DELETE COMMODITY
		delete(testCommodityId);
		
		logger.debug(">testCreateUpdateDeleteCommodity");
	}
	
	
	@Test
	public void testUpdateCommodityWithoutRecordNoUpdate() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testUpdateCommodityWithoutRecordNoUpdate");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);

		//CREATE COMMODITY
		SyncCommodityVarietyRsrc resource = new SyncCommodityVarietyRsrc();

		resource.setCropId(testCommodityId);
		resource.setCropName("TEST COMMODITY");
		resource.setParentCropId(null);
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
		resource.setIsInventoryCrop(true);

		resource.setDataSyncTransDate(createTransactionDate);
		
		//TRY TO DELETE A COMMODITY THAT DOESN'T EXIST (NO ERROR EXECTED)
		resource.setTransactionType(PoliciesSyncEventTypes.CommodityVarietyDeleted);
		service.synchronizeCommodityVariety(resource);

		//SHOULD RESULT IN AN INSERT
		resource.setTransactionType(PoliciesSyncEventTypes.CommodityVarietyUpdated);

		//Expect insert (should be detected)
		service.synchronizeCommodityVariety(resource);
		
		SyncCommodityVarietyRsrc fetchedResource = service.getSyncCommodityVariety(topLevelEndpoints, testCommodityId.toString()); 

		Assert.assertEquals("CropId 1", resource.getCropId(), fetchedResource.getCropId());
		Assert.assertNull("ParentCropId 1", fetchedResource.getParentCropId());
		Assert.assertEquals("CropName 1", resource.getCropName(), fetchedResource.getCropName());
		Assert.assertEquals("InsurancePlanId 1", resource.getInsurancePlanId(), fetchedResource.getInsurancePlanId());
		Assert.assertEquals("ShortLabel 1", resource.getShortLabel(), fetchedResource.getShortLabel());
		Assert.assertEquals("PlantDurationTypeCode 1", resource.getPlantDurationTypeCode(), fetchedResource.getPlantDurationTypeCode());
		Assert.assertEquals("IsInventoryCropInd 1", resource.getIsInventoryCrop(), fetchedResource.getIsInventoryCrop());
		Assert.assertEquals("IsYieldCropInd 1", resource.getIsYieldCrop(), fetchedResource.getIsYieldCrop());
		Assert.assertEquals("IsUnderwritingCropInd 1", resource.getIsUnderwritingCrop(), fetchedResource.getIsUnderwritingCrop());
		Assert.assertEquals("IsProductInsurableInd 1", resource.getIsProductInsurableInd(), fetchedResource.getIsProductInsurableInd());
		Assert.assertEquals("IsCropInsuranceEligibleInd 1", resource.getIsCropInsuranceEligibleInd(), fetchedResource.getIsCropInsuranceEligibleInd());
		Assert.assertEquals("IsPlantInsuranceEligibleInd 1", resource.getIsPlantInsuranceEligibleInd(), fetchedResource.getIsPlantInsuranceEligibleInd());
		Assert.assertEquals("IsOtherInsuranceEligibleInd 1", resource.getIsOtherInsuranceEligibleInd(), fetchedResource.getIsOtherInsuranceEligibleInd());
		Assert.assertEquals("YieldMeasUnitTypeCode 1", resource.getYieldMeasUnitTypeCode(), fetchedResource.getYieldMeasUnitTypeCode());
		Assert.assertEquals("YieldDecimalPrecision 1", resource.getYieldDecimalPrecision(), fetchedResource.getYieldDecimalPrecision());
		Assert.assertTrue("DataSyncTransDate 1", resource.getDataSyncTransDate().compareTo(fetchedResource.getDataSyncTransDate()) == 0);


		//UPDATE COMMODITY
		fetchedResource.setDataSyncTransDate(addSeconds(transactionDate, -1));
		fetchedResource.setTransactionType(PoliciesSyncEventTypes.CommodityVarietyUpdated);

		//NO UPDATE EXPECTED BECAUSE TRANSACTION DATE IS EARLIER THAN STORED ONE
		service.synchronizeCommodityVariety(fetchedResource);
		
		SyncCommodityVarietyRsrc notUpdatedResource = service.getSyncCommodityVariety(topLevelEndpoints, testCommodityId.toString()); 

		Assert.assertTrue("DataSyncTransDate 2", resource.getDataSyncTransDate().compareTo(notUpdatedResource.getDataSyncTransDate()) == 0);

		
		//UPDATE CODE --> USE CREATED TYPE
		notUpdatedResource.setCropName("TEST COMMODITY 2");
		notUpdatedResource.setDataSyncTransDate(addSeconds(transactionDate, 1));
		notUpdatedResource.setTransactionType(PoliciesSyncEventTypes.CommodityVarietyCreated);

		//UPDATE EXPECTED BECAUSE RECORD EXISTS IT WILL UPDATE IT
		service.synchronizeCommodityVariety(notUpdatedResource);		
		
		SyncCommodityVarietyRsrc updatedResource = service.getSyncCommodityVariety(topLevelEndpoints, testCommodityId.toString()); 

		Assert.assertEquals("CropName 3", notUpdatedResource.getCropName(), updatedResource.getCropName());
		Assert.assertTrue("DataSyncTransDate 3", notUpdatedResource.getDataSyncTransDate().compareTo(updatedResource.getDataSyncTransDate()) == 0);

				
		//CLEAN UP: DELETE COMMODITY
		delete(testCommodityId);
		
		logger.debug(">testUpdateCommodityWithoutRecordNoUpdate");
	}
	

	//
	//VARIETY TESTS
	//
	@Test
	public void testCreateUpdateDeleteVariety() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testCreateUpdateDeleteVariety");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);

		createParentCommodity(createTransactionDate);

		//CREATE VARIETY
		SyncCommodityVarietyRsrc resource = new SyncCommodityVarietyRsrc();

		resource.setCropId(testVarietyId);
		resource.setParentCropId(testCommodityId);
		resource.setCropName("TEST VARIETY");
		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.CommodityVarietyCreated);

		service.synchronizeCommodityVariety(resource);

		SyncCommodityVarietyRsrc fetchedResource = service.getSyncCommodityVariety(topLevelEndpoints, testVarietyId.toString()); 

		Assert.assertEquals("CropId 1", resource.getCropId(), fetchedResource.getCropId());
		Assert.assertEquals("ParentCropId 1", resource.getParentCropId(), fetchedResource.getParentCropId());
		Assert.assertEquals("CropName 1", resource.getCropName(), fetchedResource.getCropName());
		Assert.assertTrue("DataSyncTransDate 1", resource.getDataSyncTransDate().compareTo(fetchedResource.getDataSyncTransDate()) == 0);
		
		//UPDATE VARIETY
		fetchedResource.setCropName("TEST VARIETY 2");
		fetchedResource.setDataSyncTransDate(addSeconds(transactionDate, +1));
		fetchedResource.setTransactionType(PoliciesSyncEventTypes.CommodityVarietyUpdated);
		
		service.synchronizeCommodityVariety(fetchedResource);

		SyncCommodityVarietyRsrc updatedResource = service.getSyncCommodityVariety(topLevelEndpoints, testVarietyId.toString()); 

		Assert.assertEquals("CropName 2", fetchedResource.getCropName(), updatedResource.getCropName());
		Assert.assertTrue("DataSyncTransDate 2", fetchedResource.getDataSyncTransDate().compareTo(updatedResource.getDataSyncTransDate()) == 0);

		
		//DELETE VARIETY (SET INACTIVE)
		updatedResource.setDataSyncTransDate(addSeconds(transactionDate, +2));
		updatedResource.setTransactionType(PoliciesSyncEventTypes.CommodityVarietyDeleted);

		service.synchronizeCommodityVariety(updatedResource);
		
		//Check in database

		//CLEAN UP: DELETE VARIETY AND PARENT COMMODITY
		delete(testVarietyId);
		//delete(testCommodityId);
		
		logger.debug(">testCreateUpdateDeleteVariety");
	}

	@Test
	public void testUpdateVarietyWithoutRecordNoUpdate() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testUpdateVarietyWithoutRecordNoUpdate");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);
		
		createParentCommodity(createTransactionDate);

		//CREATE VARIETY
		SyncCommodityVarietyRsrc resource = new SyncCommodityVarietyRsrc();

		resource.setCropId(testVarietyId);
		resource.setParentCropId(testCommodityId);
		resource.setCropName("TEST VARIETY");
		resource.setDataSyncTransDate(createTransactionDate);
		
		//TRY TO DELETE A VARIETY THAT DOESN'T EXIST (NO ERROR EXECTED)
		resource.setTransactionType(PoliciesSyncEventTypes.CommodityVarietyDeleted);
		service.synchronizeCommodityVariety(resource);


		//SHOULD RESULT IN AN INSERT BECAUSE IT DOESN'T EXIST YET
		resource.setTransactionType(PoliciesSyncEventTypes.CommodityVarietyUpdated);
		service.synchronizeCommodityVariety(resource);
		
		SyncCommodityVarietyRsrc fetchedResource = service.getSyncCommodityVariety(topLevelEndpoints, testVarietyId.toString()); 

		Assert.assertEquals("CropId 1", resource.getCropId(), fetchedResource.getCropId());
		Assert.assertEquals("ParentCropId 1", resource.getParentCropId(), fetchedResource.getParentCropId());
		Assert.assertEquals("CropName 1", resource.getCropName(), fetchedResource.getCropName());
		Assert.assertTrue("DataSyncTransDate 1", resource.getDataSyncTransDate().compareTo(fetchedResource.getDataSyncTransDate()) == 0);


		//UPDATE VARIETY
		fetchedResource.setDataSyncTransDate(addSeconds(transactionDate, -1));
		fetchedResource.setTransactionType(PoliciesSyncEventTypes.CommodityVarietyUpdated);

		//NO UPDATE EXPECTED BECAUSE TRANSACTION DATE IS EARLIER THAN STORED ONE
		service.synchronizeCommodityVariety(fetchedResource);
		
		SyncCommodityVarietyRsrc notUpdatedResource = service.getSyncCommodityVariety(topLevelEndpoints, testVarietyId.toString()); 

		Assert.assertTrue("DataSyncTransDate 2", resource.getDataSyncTransDate().compareTo(notUpdatedResource.getDataSyncTransDate()) == 0);


		//UPDATE CODE --> USE CREATED TYPE
		notUpdatedResource.setCropName("TEST VARIETY 2");
		notUpdatedResource.setDataSyncTransDate(addSeconds(transactionDate, 1));
		notUpdatedResource.setTransactionType(PoliciesSyncEventTypes.CommodityVarietyCreated);

		//UPDATE EXPECTED BECAUSE RECORD EXISTS IT WILL UPDATE IT
		service.synchronizeCommodityVariety(notUpdatedResource);	
		
		SyncCommodityVarietyRsrc updatedResource = service.getSyncCommodityVariety(topLevelEndpoints, testVarietyId.toString()); 

		Assert.assertEquals("CropName 3", notUpdatedResource.getCropName(), updatedResource.getCropName());
		Assert.assertTrue("DataSyncTransDate 3", notUpdatedResource.getDataSyncTransDate().compareTo(updatedResource.getDataSyncTransDate()) == 0);

		
		//CLEAN UP: DELETE VARIETY AND PARENT COMMODITY
		delete(testVarietyId);
		//delete(testCommodityId);
		
		logger.debug(">testUpdateVarietyWithoutRecordNoUpdate");
	}
	
	private void createParentCommodity(Date createTransactionDate)
			throws CirrasUnderwritingServiceException, ValidationException {
		//CREATE PARENT COMMODITY
		SyncCommodityVarietyRsrc resource = new SyncCommodityVarietyRsrc();

		resource.setCropId(testCommodityId);
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
