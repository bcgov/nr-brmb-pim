package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints;

import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.PoliciesSyncEventTypes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.ValidationException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SyncCommodityTypeCodeRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SyncCommodityTypeVarietyXrefRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SyncCommodityVarietyRsrc;
import ca.bc.gov.mal.cirras.underwriting.model.v1.CropVariety;
import ca.bc.gov.mal.cirras.underwriting.model.v1.CropVarietyCommodityType;
import ca.bc.gov.mal.cirras.underwriting.model.v1.CropVarietyPlantInsurability;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.CropCommodityListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.CropCommodityRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class CropCommodityListEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(CropCommodityListEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.UPDATE_SYNC_UNDERWRITING,
		Scopes.DELETE_SYNC_UNDERWRITING
	};
	
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;
	private String commodityTypeCode = "TESTCODE";
	private String description = "Test Code";
	private Integer cropCommodityId = 88888888;
	private Integer cropVarietyId = 99999999;
	private Integer insurancePlanId = 4;


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

		// delete commodity type variety xref
		service.deleteCommodityTypeVarietyXref(topLevelEndpoints, commodityTypeCode, cropVarietyId.toString());

		// delete commodity type code
		service.deleteCommodityTypeCode(topLevelEndpoints, commodityTypeCode);

		// delete variety
		service.deleteSyncCommodityVariety(topLevelEndpoints, cropVarietyId.toString());

		// delete commodity
		service.deleteSyncCommodityVariety(topLevelEndpoints, cropCommodityId.toString());

	}

	@Test
	public void testGetCropCommodityList() throws CirrasUnderwritingServiceException, Oauth2ClientException {
		logger.debug("<testGetCropCommodityList");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		String cropYear = "2021";
		String commodityType = null;


		CropCommodityListRsrc searchResults = service.getCropCommodityList(
				topLevelEndpoints, 
				insurancePlanId.toString(), 
				cropYear, 
				commodityType,
				"N");

		Assert.assertNotNull(searchResults);
		Assert.assertTrue(searchResults.getCollection().size() > 0);

		//Return inventory commodities only
		commodityType = "INV";
		
		searchResults = service.getCropCommodityList(
				topLevelEndpoints, 
				insurancePlanId.toString(), 
				cropYear, 
				commodityType,
				"N");
		
		Assert.assertNotNull(searchResults);
		Assert.assertTrue(searchResults.getCollection().size() > 0);
		
		for (CropCommodityRsrc rsrc : searchResults.getCollection()) {
			Assert.assertEquals("IsInventoryCropInd", true, rsrc.getIsInventoryCropInd());
			Assert.assertNotNull(rsrc.getCropVariety());
			Assert.assertEquals("Crop Variety List", 0, rsrc.getCropVariety().size());
		}
		
		//Return yield commodities only
		commodityType = "YLD";
		
		searchResults = service.getCropCommodityList(
				topLevelEndpoints, 
				insurancePlanId.toString(), 
				cropYear, 
				commodityType,
				"N");
		
		Assert.assertNotNull(searchResults);
		Assert.assertTrue(searchResults.getCollection().size() > 0);
		
		for (CropCommodityRsrc rsrc : searchResults.getCollection()) {
			Assert.assertEquals("IsYieldCropInd", true, rsrc.getIsYieldCropInd());
			Assert.assertNotNull(rsrc.getCropVariety());
			Assert.assertEquals("Crop Variety List", 0, rsrc.getCropVariety().size());
		}
		
		//Return underwriting commodities only
		commodityType = "UW";
		
		searchResults = service.getCropCommodityList(
				topLevelEndpoints, 
				insurancePlanId.toString(), 
				cropYear, 
				commodityType,
				"N");
		
		Assert.assertNotNull(searchResults);
		Assert.assertTrue(searchResults.getCollection().size() > 0);
		
		for (CropCommodityRsrc rsrc : searchResults.getCollection()) {
			Assert.assertEquals("IsUnderwritingCropInd", true, rsrc.getIsUnderwritingCropInd());
			Assert.assertNotNull(rsrc.getCropVariety());
			Assert.assertEquals("Crop Variety List", 0, rsrc.getCropVariety().size());
		}		
		
		logger.debug(">testGetCropCommodityList");
	}
	
	@Test
	public void testGetCropCommodityListWithVarietyCommodtyType() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testGetCropCommodityListWithVarietyCommodtyType");

		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();
	
		//CREATE Commodity, Variety and Commodity Type Code
		createCommodity(transactionDate);
		createVariety(transactionDate);
		createCommodityTypeCode(transactionDate);
		createCommodityTypeVarietyXref(transactionDate);
		
		String commodityType = null;
		String cropYear = "2021";
		
		CropCommodityListRsrc searchResults = service.getCropCommodityList(
				topLevelEndpoints, 
				insurancePlanId.toString(), 
				cropYear, 
				commodityType,
				"Y");

		Assert.assertNotNull(searchResults);
		Assert.assertTrue(searchResults.getCollection().size() > 0);

		//Loop through commodities
		for (CropCommodityRsrc cropCommodityRsrc : searchResults.getCollection()) {
			
			if (cropCommodityRsrc.getCropVariety() != null && cropCommodityRsrc.getCropVariety().size() > 0) {
				//Loop through varieties
				for(CropVariety variety : cropCommodityRsrc.getCropVariety()) {

					Assert.assertEquals("Crop Commodity Id Different", cropCommodityRsrc.getCropCommodityId(), variety.getCropCommodityId());
					Assert.assertEquals("insurancePlanId", insurancePlanId, variety.getInsurancePlanId());
					
					//Loop through commodity types of the variety
					if (variety.getCropVarietyCommodityTypes() != null && variety.getCropVarietyCommodityTypes().size() > 0) {
						for(CropVarietyCommodityType varietyCommodityType : variety.getCropVarietyCommodityTypes()) {
							Assert.assertEquals("Crop Variety Id Different", variety.getCropVarietyId(), varietyCommodityType.getCropVarietyId());
							Assert.assertEquals("insurancePlanId Commodity Type", insurancePlanId, varietyCommodityType.getInsurancePlanId());
							if(variety.getCropVarietyId().equals(cropVarietyId)) {
								Assert.assertEquals("Commodity Type Code", commodityTypeCode, varietyCommodityType.getCommodityTypeCode());
								Assert.assertEquals("Commodity Type Description", description, varietyCommodityType.getDescription());
							} else {
								Assert.assertNotNull(varietyCommodityType.getCommodityTypeCode());
								Assert.assertNotNull(varietyCommodityType.getDescription());
							}
						}
					}
				}
			}
		}	

		//CLEAN UP: DELETE RECORDS
		delete();

		logger.debug(">testGetCropCommodityListWithVarietyCommodtyType");
	}

	@Test
	public void testGetCropCommodityListWithVarietyCommodtyTypeDeadlines() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testGetCropCommodityListWithVarietyCommodtyTypeDeadlines");

		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
				
		// There is no way to create a SeedingDeadline for a unit test since there is no endpoint, so hard-code known values.
		Integer cropYear = 2023;
		String commodityTypeWithDeadlines = "Forage Oat";

		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(cropYear, Calendar.MAY, 25);
		Date fullCoverageDeadline = cal.getTime();

		cal.clear();
		cal.set(cropYear, Calendar.JUNE, 5);
		Date finalCoverageDeadline = cal.getTime();
		
		CropCommodityListRsrc searchResults = service.getCropCommodityList(
				topLevelEndpoints, 
				insurancePlanId.toString(), 
				cropYear.toString(), 
				null,
				"Y");

		Assert.assertNotNull(searchResults);
		Assert.assertTrue(searchResults.getCollection().size() > 0);

		//Loop through commodities
		boolean found = false;
		for (CropCommodityRsrc cropCommodityRsrc : searchResults.getCollection()) {
			
			if (cropCommodityRsrc.getCropVariety() != null && cropCommodityRsrc.getCropVariety().size() > 0) {
				//Loop through varieties
				for(CropVariety variety : cropCommodityRsrc.getCropVariety()) {

					//Loop through commodity types of the variety
					if (variety.getCropVarietyCommodityTypes() != null && variety.getCropVarietyCommodityTypes().size() > 0) {
						for(CropVarietyCommodityType varietyCommodityType : variety.getCropVarietyCommodityTypes()) {
							if(varietyCommodityType.getCommodityTypeCode().equals(commodityTypeWithDeadlines)) {
								found = true;
								Assert.assertEquals(fullCoverageDeadline, varietyCommodityType.getFullCoverageDeadlineDate());
								Assert.assertEquals(finalCoverageDeadline, varietyCommodityType.getFinalCoverageDeadlineDate());
							}
						}
					}
				}
			}
		}

		if (!found) {
			Assert.fail("Commodity Type " + commodityTypeWithDeadlines + " missing.");
		}
				

		logger.debug(">testGetCropCommodityListWithVarietyCommodtyTypeDeadlines");
	}
	
	
	private void createCommodityTypeVarietyXref(Date createTransactionDate)
			throws CirrasUnderwritingServiceException, ValidationException {

		//CREATE Commodity Type Variety Xref
		SyncCommodityTypeVarietyXrefRsrc resource = new SyncCommodityTypeVarietyXrefRsrc();
		
		resource.setCommodityTypeCode(commodityTypeCode);
		resource.setCropVarietyId(cropVarietyId);
		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.CommodityTypeVarietyXrefCreated);

		service.synchronizeCommodityTypeVarietyXref(resource);

	}
	
	private void createCommodityTypeCode(Date createTransactionDate)
			throws CirrasUnderwritingServiceException, ValidationException {
		SyncCommodityTypeCodeRsrc resource = new SyncCommodityTypeCodeRsrc();

		resource.setCommodityTypeCode(commodityTypeCode);
		resource.setCropCommodityId(cropCommodityId);
		resource.setDescription(description);
		resource.setIsActive(true);
		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.CommodityTypeCodeCreated);

		service.synchronizeCommodityTypeCode(resource);

	}

	private void createVariety(Date createTransactionDate)
			throws CirrasUnderwritingServiceException, ValidationException {
		SyncCommodityVarietyRsrc resource = new SyncCommodityVarietyRsrc();

		resource.setCropId(cropVarietyId);
		resource.setParentCropId(cropCommodityId);
		resource.setCropName("TEST VARIETY");
		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.CommodityVarietyCreated);

		service.synchronizeCommodityVariety(resource);

	}

	private void createCommodity(Date createTransactionDate)
			throws CirrasUnderwritingServiceException, ValidationException {
		//CREATE PARENT COMMODITY
		SyncCommodityVarietyRsrc resource = new SyncCommodityVarietyRsrc();

		resource.setCropId(cropCommodityId);
		resource.setCropName("TEST COMMODITY");
		resource.setInsurancePlanId(insurancePlanId);
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
	
	
	// get existing varieties for FORAGE and check their plant insurance
	@Test
	public void testGetForageCropCommodityListWithVarietyPlantIns() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testGetForageCropCommodityListWithVarietyPlantIns");

		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		String commodityType = null;
		String cropYear = "2021";
		Integer forageInsurancePlanId = 5;
		
		CropCommodityListRsrc searchResults = service.getCropCommodityList(
				topLevelEndpoints, 
				forageInsurancePlanId.toString(),
				cropYear, 
				commodityType,
				"Y");

		Assert.assertNotNull(searchResults);
		Assert.assertTrue(searchResults.getCollection().size() > 0);

		//Loop through commodities
		for (CropCommodityRsrc cropCommodityRsrc : searchResults.getCollection()) {
			
			if (cropCommodityRsrc.getCropVariety() != null && cropCommodityRsrc.getCropVariety().size() > 0) {
				//Loop through varieties
				for(CropVariety variety : cropCommodityRsrc.getCropVariety()) {

					Assert.assertEquals("Crop Commodity Id Different", cropCommodityRsrc.getCropCommodityId(), variety.getCropCommodityId());
					Assert.assertEquals("insurancePlanId", forageInsurancePlanId, variety.getInsurancePlanId());
					
					Assert.assertNotNull(variety.getIsQuantityInsurableInd());
					Assert.assertNotNull(variety.getIsPlantInsurableInd());
					Assert.assertNotNull(variety.getIsUnseededInsurableInd());
					Assert.assertNotNull(variety.getIsAwpEligibleInd());
					Assert.assertNotNull(variety.getIsUnderseedingEligibleInd());
					Assert.assertNotNull(variety.getIsGrainUnseededDefaultInd());
					
					//Loop through plant insurability types of the variety
					if (variety.getCropVarietyPlantInsurabilities() != null && variety.getCropVarietyPlantInsurabilities().size() > 0) {
						
						for(CropVarietyPlantInsurability varietyPlantInsurability : variety.getCropVarietyPlantInsurabilities()) {
							
							Assert.assertEquals("Crop Variety Id Different", variety.getCropVarietyId(), varietyPlantInsurability.getCropVarietyId());
							Assert.assertNotNull(varietyPlantInsurability.getPlantInsurabilityTypeCode());
							Assert.assertNotNull(varietyPlantInsurability.getDescription());
							
						}
					}
				}
			}
		}	
		
		logger.debug(">testGetForageCropCommodityListWithVarietyPlantIns");
	}
	
	

}
