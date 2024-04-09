package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints;


import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.CropVarietyInsurabilityListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SyncCommodityVarietyRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UnderwritingYearRsrc;
import ca.bc.gov.mal.cirras.underwriting.model.v1.CropVarietyInsurability;
import ca.bc.gov.mal.cirras.underwriting.model.v1.CropVarietyPlantInsurability;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.common.wfone.rest.resource.CodeRsrc;
import ca.bc.gov.nrs.common.wfone.rest.resource.CodeTableListRsrc;
import ca.bc.gov.nrs.common.wfone.rest.resource.CodeTableRsrc;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class CropVarietyInsurabilityListEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(CropVarietyInsurabilityListEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.GET_CODE_TABLES,
		Scopes.GET_CROP_VARIETY_INSURABILITIES,
		Scopes.SAVE_CROP_VARIETY_INSURABILITIES,
		Scopes.UPDATE_SYNC_UNDERWRITING,
		Scopes.DELETE_SYNC_UNDERWRITING
	};
	
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;

	private Integer insurancePlanId = 5;

	private String cropCommodityName = "Commodity 12";
	private Integer cropCommodityId = 777777;
	
	private String cropVarietyName1 = "Variety 12";
	private Integer cropVarietyId1 = 888999;
	
	private String plantInsurabilityTypeCode1 = null;
	private String plantInsurabilityTypeCode2 = null;
	private String plantInsurabilityTypeCode3 = null;
	
	private String plantInsurabilityTypeDesc1 = null;
	private String plantInsurabilityTypeDesc2 = null;
	private String plantInsurabilityTypeDesc3 = null;
	


	@Before
	public void prepareTests() throws CirrasUnderwritingServiceException, Oauth2ClientException, NotFoundDaoException, DaoException, ValidationException{
		service = getService(SCOPES);
		topLevelEndpoints = service.getTopLevelEndpoints();

		delete();

	}

	@After 
	public void cleanUp() throws CirrasUnderwritingServiceException, NotFoundDaoException, DaoException, ValidationException {
		delete();
	}
	
	private void delete() throws NotFoundDaoException, DaoException, CirrasUnderwritingServiceException, ValidationException{

		//Delete crop variety insurability
		CropVarietyInsurabilityListRsrc searchResults = service.getCropVarietyInsurabilities(
				topLevelEndpoints, 
				insurancePlanId.toString(),
				"N"
		);
		
		deleteCropVarietyInsurability(searchResults, cropVarietyId1);


		// delete variety
		service.deleteSyncCommodityVariety(topLevelEndpoints, cropVarietyId1.toString());
		
		// delete commodity
		service.deleteSyncCommodityVariety(topLevelEndpoints, cropCommodityId.toString());

	}
	
	private void deleteCropVarietyInsurability(CropVarietyInsurabilityListRsrc searchResults, Integer cropVarietyId) throws CirrasUnderwritingServiceException, ValidationException {

		if(searchResults != null && searchResults.getCollection().size() > 0) {
			CropVarietyInsurability resource = getCropVarietyInsurability(searchResults, cropVarietyId);
			
			if(resource != null) {
				//only delete for added varieties
				resource.setDeletedByUserInd(true);
				service.saveCropVarietyInsurabilities(searchResults, insurancePlanId.toString(), "N");
			}
		}
	}
	
	private void loadPlantInsurabilityCodes() throws CirrasUnderwritingServiceException {

		CodeTableListRsrc codeTables = service.getCodeTables(topLevelEndpoints, "plant_insurability_type_code", LocalDate.now());
		Assert.assertNotNull(codeTables);
		CodeTableRsrc codeTable = codeTables.getCodeTableList().get(0);
		Assert.assertNotNull(codeTable);
		List<CodeRsrc> plantInsurabilityCodes = codeTable.getCodes();
		Assert.assertNotNull(plantInsurabilityCodes);
		Assert.assertTrue(plantInsurabilityCodes.size() >= 3);
		
		plantInsurabilityTypeCode1 = plantInsurabilityCodes.get(0).getCode();
		plantInsurabilityTypeDesc1 = plantInsurabilityCodes.get(0).getDescription();
		plantInsurabilityTypeCode2 = plantInsurabilityCodes.get(1).getCode();
		plantInsurabilityTypeDesc2 = plantInsurabilityCodes.get(1).getDescription();
		plantInsurabilityTypeCode3 = plantInsurabilityCodes.get(2).getCode();
		plantInsurabilityTypeDesc3 = plantInsurabilityCodes.get(2).getDescription();

		Assert.assertNotNull(plantInsurabilityTypeCode1);
		Assert.assertNotNull(plantInsurabilityTypeCode2);
		Assert.assertNotNull(plantInsurabilityTypeCode3);

	}
	
	@Test
	public void testCreateUpdateDeleteSelectCropVarietyInsurability() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testCreateUpdateDeleteSelectCropVarietyInsurability");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		loadPlantInsurabilityCodes();
		
		//Establish current record count
		CropVarietyInsurabilityListRsrc searchResults = service.getCropVarietyInsurabilities(
				topLevelEndpoints, 
				insurancePlanId.toString(),
				"N"
		);

		Assert.assertNotNull(searchResults);
		int totalExistingRecords = searchResults.getCollection().size();
		
		int expectedCount = totalExistingRecords + 1;

		//Create commodity
		createCommodity();
		
		//Create Variety
		createVariety(cropVarietyId1, cropVarietyName1);
		
		CropVarietyInsurabilityListRsrc insurabilityList = service.getCropVarietyInsurabilities(
				topLevelEndpoints, 
				insurancePlanId.toString(),
				"N"
		);
		
		Assert.assertNotNull(insurabilityList);
		Assert.assertEquals(expectedCount, insurabilityList.getCollection().size());

		//Add insurability for new variety
		CropVarietyInsurability cropVarietyInsurability1 = getCropVarietyInsurability(insurabilityList, cropVarietyId1);
		Assert.assertNotNull(cropVarietyInsurability1);
		
		setCropVarietyInsurability(cropVarietyInsurability1, true, false, true, false, true);
		
		//Add plant insurabilities
		cropVarietyInsurability1.getCropVarietyPlantInsurabilities().add(getNewCropVarietyPlantInsurability(plantInsurabilityTypeCode1, plantInsurabilityTypeDesc1, cropVarietyInsurability1.getCropVarietyInsurabilityGuid(), cropVarietyInsurability1.getCropVarietyId() ));
		cropVarietyInsurability1.getCropVarietyPlantInsurabilities().add(getNewCropVarietyPlantInsurability(plantInsurabilityTypeCode2, plantInsurabilityTypeDesc2, cropVarietyInsurability1.getCropVarietyInsurabilityGuid(), cropVarietyInsurability1.getCropVarietyId()));
		
		//Save and Fetch
		CropVarietyInsurabilityListRsrc cropVarietyInsurabilities = service.saveCropVarietyInsurabilities(
				insurabilityList, 
				insurancePlanId.toString(),
				"N"
		);
		 
		Assert.assertNotNull(cropVarietyInsurabilities);
		Assert.assertEquals(expectedCount, cropVarietyInsurabilities.getCollection().size());
		
		CropVarietyInsurability insertedRecord = getCropVarietyInsurability(cropVarietyInsurabilities, cropVarietyId1);
		Assert.assertNotNull(insertedRecord);
		
		assertCropVarietyInsurability(cropVarietyInsurability1, insertedRecord, false);
		
		Assert.assertNotNull(insertedRecord.getCropVarietyPlantInsurabilities());
		Assert.assertEquals(2, insertedRecord.getCropVarietyPlantInsurabilities().size());
		assertPlantInsurabilities(plantInsurabilityTypeCode1, cropVarietyInsurability1.getCropVarietyPlantInsurabilities(), insertedRecord.getCropVarietyPlantInsurabilities());
		assertPlantInsurabilities(plantInsurabilityTypeCode2, cropVarietyInsurability1.getCropVarietyPlantInsurabilities(), insertedRecord.getCropVarietyPlantInsurabilities());
		
		//Update same
		setCropVarietyInsurability(insertedRecord, false, true, false, true, false);
		
		//Add new plant insurablity and remove an existing one
		insertedRecord.getCropVarietyPlantInsurabilities().add(getNewCropVarietyPlantInsurability(plantInsurabilityTypeCode3, plantInsurabilityTypeDesc3, cropVarietyInsurability1.getCropVarietyInsurabilityGuid(), cropVarietyInsurability1.getCropVarietyId()));
		CropVarietyPlantInsurability plantInsurabilityToDelete = getCropVarietyPlantInsurability(insertedRecord.getCropVarietyPlantInsurabilities(), plantInsurabilityTypeCode2);
		Assert.assertNotNull(plantInsurabilityToDelete);
		plantInsurabilityToDelete.setDeletedByUserInd(true);
		
		//Save and Fetch
		cropVarietyInsurabilities = service.saveCropVarietyInsurabilities(
				cropVarietyInsurabilities, 
				insurancePlanId.toString(),
				"N"
		);	
		
		CropVarietyInsurability updatedRecord = getCropVarietyInsurability(cropVarietyInsurabilities, cropVarietyId1);
		Assert.assertNotNull(updatedRecord);
		
		assertCropVarietyInsurability(insertedRecord, updatedRecord, false);
		
		Assert.assertNotNull(updatedRecord.getCropVarietyPlantInsurabilities());
		Assert.assertEquals(2, updatedRecord.getCropVarietyPlantInsurabilities().size());
		assertPlantInsurabilities(plantInsurabilityTypeCode1, insertedRecord.getCropVarietyPlantInsurabilities(), updatedRecord.getCropVarietyPlantInsurabilities());
		assertPlantInsurabilities(plantInsurabilityTypeCode3, insertedRecord.getCropVarietyPlantInsurabilities(), updatedRecord.getCropVarietyPlantInsurabilities());

		//Delete
		updatedRecord.setDeletedByUserInd(true);

		cropVarietyInsurabilities = service.saveCropVarietyInsurabilities(
				cropVarietyInsurabilities, 
				insurancePlanId.toString(),
				"N"
		);	
		
		CropVarietyInsurability deletedRecord = getCropVarietyInsurability(cropVarietyInsurabilities, cropVarietyId1);
		Assert.assertNotNull(deletedRecord);
		Assert.assertNull(deletedRecord.getCropVarietyInsurabilityGuid());

		delete();

		logger.debug(">testCreateUpdateDeleteSelectCropVarietyInsurability");
		
	}	

	@Test
	public void testCropVarietyInsurabilityValidation() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testCropVarietyInsurabilityValidation");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//Establish current record count
		CropVarietyInsurabilityListRsrc searchResults = service.getCropVarietyInsurabilities(
				topLevelEndpoints, 
				insurancePlanId.toString(),
				"Y"
		);

		Assert.assertNotNull(searchResults);

		assertValidationCropVarietyInsurabilities(searchResults);

		logger.debug(">testCropVarietyInsurabilityValidation");
	}
	
	@Test
	public void testCropVarietyInsurabilitySaveValidation() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testCropVarietyInsurabilitySaveValidation");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		loadPlantInsurabilityCodes();

		//Create commodity
		createCommodity();
		
		//Create Variety
		createVariety(cropVarietyId1, cropVarietyName1);

		CropVarietyInsurabilityListRsrc insurabilityList = service.getCropVarietyInsurabilities(
				topLevelEndpoints, 
				insurancePlanId.toString(),
				"Y"
		);
		
		Assert.assertNotNull(insurabilityList);

		//Add insurability for new variety
		CropVarietyInsurability cropVarietyInsurability1 = getCropVarietyInsurability(insurabilityList, cropVarietyId1);
		Assert.assertNotNull(cropVarietyInsurability1);
		
		setCropVarietyInsurability(cropVarietyInsurability1, true, true, true, true, true);
		
		//Add plant insurability
		cropVarietyInsurability1.getCropVarietyPlantInsurabilities().add(getNewCropVarietyPlantInsurability(plantInsurabilityTypeCode1, plantInsurabilityTypeDesc1, cropVarietyInsurability1.getCropVarietyInsurabilityGuid(), cropVarietyInsurability1.getCropVarietyId() ));
		
		//Save and Fetch
		insurabilityList = service.saveCropVarietyInsurabilities(
				insurabilityList, 
				insurancePlanId.toString(),
				"Y"
		);		
		
		//Get insurability for new variety
		cropVarietyInsurability1 = getCropVarietyInsurability(insurabilityList, cropVarietyId1);
		Assert.assertNotNull(cropVarietyInsurability1);
		Assert.assertNotNull(cropVarietyInsurability1.getCropVarietyPlantInsurabilities());
		Assert.assertEquals(1, cropVarietyInsurability1.getCropVarietyPlantInsurabilities().size());
		
		//Update to false
		setCropVarietyInsurability(cropVarietyInsurability1, false, false, false, false, false);

		//remove plant insurability
		CropVarietyPlantInsurability plantInsurabilityToDelete = getCropVarietyPlantInsurability(cropVarietyInsurability1.getCropVarietyPlantInsurabilities(), plantInsurabilityTypeCode1);
		Assert.assertNotNull(plantInsurabilityToDelete);
		plantInsurabilityToDelete.setDeletedByUserInd(true);

		
		//Save should not throw an error
		insurabilityList = service.saveCropVarietyInsurabilities(
				insurabilityList, 
				insurancePlanId.toString(),
				"Y"
		);		
		
		//Get insurability for new variety
		CropVarietyInsurability updatedCropVarietyInsurability = getCropVarietyInsurability(insurabilityList, cropVarietyId1);
		Assert.assertNotNull(updatedCropVarietyInsurability);
		Assert.assertNotNull(updatedCropVarietyInsurability.getCropVarietyPlantInsurabilities());
		Assert.assertEquals(0, updatedCropVarietyInsurability.getCropVarietyPlantInsurabilities().size());

		assertCropVarietyInsurability(cropVarietyInsurability1, updatedCropVarietyInsurability, true);

		getCropVarietyInsurabilityEditable(insurabilityList);

		//These tests rely on existing data. It is unlikely that there is none that is not editable  
		//Make sure there is a crop variety insurabilities that are not editable
		Assert.assertNotNull(notQuantityEditable);
		Assert.assertNotNull(notUnseededEditable);
		Assert.assertNotNull(notPlantEditable);
		Assert.assertNotNull(notAwpEditable);
		Assert.assertNotNull(notUnderseedingEditable);
		Assert.assertNotNull(notPlantXrefEditable);
		
		try {
			//Try to update Quantity Insurability. Expect error being thrown 
			notQuantityEditable.setIsQuantityInsurableInd(false);
			
			insurabilityList = service.saveCropVarietyInsurabilities(
					insurabilityList, 
					insurancePlanId.toString(),
					"Y"
			);	
			Assert.fail("Attempt to edit Quantity Insurability record where it's not allowed but didn't throw an error");
		} catch (CirrasUnderwritingServiceException e) {
			//Reset it
			notQuantityEditable.setIsQuantityInsurableInd(true);
			Assert.assertTrue(e.getMessage().contains("Quantity Insurability can't be removed for: " + notQuantityEditable.getCropVarietyId()));
		}
		
		try {
			//Try to update Unseeded Insurable. Expect error being thrown 
			notUnseededEditable.setIsUnseededInsurableInd(false);
			
			insurabilityList = service.saveCropVarietyInsurabilities(
					insurabilityList, 
					insurancePlanId.toString(),
					"Y"
			);	
			Assert.fail("Attempt to edit Unseeded Insurability record where it's not allowed but didn't throw an error");
		} catch (CirrasUnderwritingServiceException e) {
			//Reset it
			notUnseededEditable.setIsUnseededInsurableInd(true);
			Assert.assertTrue(e.getMessage().contains("Unseeded Insurability can't be removed for: " + notUnseededEditable.getCropVarietyId()));
		}
		
		try {
			//Try to update Plant Insurable. Expect error being thrown 
			notPlantEditable.setIsPlantInsurableInd(false);
			
			insurabilityList = service.saveCropVarietyInsurabilities(
					insurabilityList, 
					insurancePlanId.toString(),
					"Y"
			);	
			Assert.fail("Attempt to edit Plant Insurability record where it's not allowed but didn't throw an error");
		} catch (CirrasUnderwritingServiceException e) {
			//Reset it
			notPlantEditable.setIsPlantInsurableInd(true);
			Assert.assertTrue(e.getMessage().contains("Plant Insurability can't be removed for: " + notPlantEditable.getCropVarietyId()));
		}

		try {
			//Try to update AWP Eligible. Expect error being thrown 
			notAwpEditable.setIsAwpEligibleInd(false);
			
			insurabilityList = service.saveCropVarietyInsurabilities(
					insurabilityList, 
					insurancePlanId.toString(),
					"Y"
			);	
			Assert.fail("Attempt to edit AWP Eligible record where it's not allowed but didn't throw an error");
		} catch (CirrasUnderwritingServiceException e) {
			//Reset it
			notAwpEditable.setIsAwpEligibleInd(true);
			Assert.assertTrue(e.getMessage().contains("AWP Eligibility can't be removed for: " + notAwpEditable.getCropVarietyId()));
		}

		try {
			//Try to update Underseeding Eligible. Expect error being thrown 
			notUnderseedingEditable.setIsUnderseedingEligibleInd(false);
			
			insurabilityList = service.saveCropVarietyInsurabilities(
					insurabilityList, 
					insurancePlanId.toString(),
					"Y"
			);	
			Assert.fail("Attempt to edit Underseeding Eligible record where it's not allowed but didn't throw an error");
		} catch (CirrasUnderwritingServiceException e) {
			//Reset it
			notUnderseedingEditable.setIsUnderseedingEligibleInd(true);
			Assert.assertTrue(e.getMessage().contains("Underseeding Eligibility can't be removed for: " + notUnderseedingEditable.getCropVarietyId()));
		}
		
		//CropVarietyInsurability
		try {
			//Try to update Plant Insurability code from variety. Expect error being thrown 
			notPlantXrefEditable.setDeletedByUserInd(true);
			
			insurabilityList = service.saveCropVarietyInsurabilities(
					insurabilityList, 
					insurancePlanId.toString(),
					"Y"
			);	
			Assert.fail("Attempt to edit Underseeding Eligible record where it's not allowed but didn't throw an error");
		} catch (CirrasUnderwritingServiceException e) {
			//Reset it
			notPlantXrefEditable.setDeletedByUserInd(false);
			Assert.assertTrue(e.getMessage().contains("Plant Insurability " + notPlantXrefEditable.getPlantInsurabilityTypeCode() + " can't be removed from"));
		}
		
		
		delete();

		logger.debug(">testCropVarietyInsurabilitySaveValidation");
	}	
	
	private CropVarietyInsurability getCropVarietyInsurability(CropVarietyInsurabilityListRsrc results, Integer cropVarietyId) {
		
		if(results != null && results.getCollection().size() > 0) {
			List<CropVarietyInsurability> resources = results.getCollection().stream()
					.filter(x -> x.getCropVarietyId().equals(cropVarietyId))
					.collect(Collectors.toList());
			
			if(resources != null && resources.size() > 0) {
				return resources.get(0);
			}
		}
		
		return null;
	}	
	
	private CropVarietyInsurability notQuantityEditable = null;
	private CropVarietyInsurability notUnseededEditable = null;
	private CropVarietyInsurability notPlantEditable = null;
	private CropVarietyInsurability notAwpEditable = null;
	private CropVarietyInsurability notUnderseedingEditable = null;
	private CropVarietyPlantInsurability notPlantXrefEditable = null;

	private void getCropVarietyInsurabilityEditable(CropVarietyInsurabilityListRsrc results) {
		
		for (CropVarietyInsurability resource : results.getCollection()) {

			//Quantity Insurable
			if(notQuantityEditable == null && 
					resource.getIsQuantityInsurableEditableInd() == false &&
					resource.getIsQuantityInsurableInd() == true) {
				notQuantityEditable = resource;
			}

			//Unseeded Insurable
			if(notUnseededEditable == null && 
					resource.getIsUnseededInsurableEditableInd() == false &&
					resource.getIsUnseededInsurableInd() == true) {
				notUnseededEditable = resource;
			}

			//Plant Insurable
			if(notPlantEditable == null && 
					resource.getIsPlantInsurableEditableInd() == false &&
					resource.getIsPlantInsurableInd() == true) {
				notPlantEditable = resource;
			}

			//AWP Eligible
			if(notAwpEditable == null && 
					resource.getIsAwpEligibleEditableInd() == false &&
					resource.getIsAwpEligibleInd() == true) {
				notAwpEditable = resource;
			}

			//Underseeding Eligible
			if(notUnderseedingEditable == null &&
					resource.getIsUnderseedingEligibleEditableInd() == false && 
					resource.getIsUnderseedingEligibleInd() == true) {
				notUnderseedingEditable = resource;
			}
			
			//Find used Crop Variety Plant Insurability
			if(notPlantXrefEditable == null &&
					resource.getCropVarietyPlantInsurabilities() != null && 
					resource.getCropVarietyPlantInsurabilities().size() > 0) {
				for (CropVarietyPlantInsurability plantInsurability : resource.getCropVarietyPlantInsurabilities()) {
					if(plantInsurability.getIsUsedInd() == true) {
						notPlantXrefEditable = plantInsurability;
						break;
					}
				}
			}
		}
		
		
//		if(results != null && results.getCollection().size() > 0) {
//			List<CropVarietyInsurability> resources = results.getCollection().stream()
//					.filter(x -> x.getIsQuantityInsurableEditableInd().equals(true))
//					.collect(Collectors.toList());
//			
//			if(resources != null && resources.size() > 0) {
//				notQuantityEditable = resources.get(0);
//			}
//		}
//		
//		return null;
	}
	
	private CropVarietyPlantInsurability getCropVarietyPlantInsurability(List<CropVarietyPlantInsurability> results, String plantInsurabilityCode) {
		
		if(results != null && results.size() > 0) {
			List<CropVarietyPlantInsurability> resources = results.stream()
					.filter(x -> x.getPlantInsurabilityTypeCode().equals(plantInsurabilityCode))
					.collect(Collectors.toList());
			
			if(resources != null && resources.size() > 0) {
				return resources.get(0);
			}
		}
		
		return null;
	}

	private void createCommodity()
			throws CirrasUnderwritingServiceException, ValidationException {

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();
		
		//CREATE PARENT COMMODITY
		SyncCommodityVarietyRsrc resource = new SyncCommodityVarietyRsrc();

		resource.setCropId(cropCommodityId);
		resource.setCropName(cropCommodityName);
		resource.setInsurancePlanId(insurancePlanId);
		resource.setShortLabel("SHRT");
		resource.setPlantDurationTypeCode("PERENNIAL");
		resource.setIsInventoryCrop(true);
		resource.setIsYieldCrop(true);
		resource.setIsUnderwritingCrop(true);
		resource.setIsProductInsurableInd(true);
		resource.setIsCropInsuranceEligibleInd(true);
		resource.setIsPlantInsuranceEligibleInd(true);
		resource.setIsOtherInsuranceEligibleInd(true);
		resource.setYieldMeasUnitTypeCode("TONNE");
		resource.setYieldDecimalPrecision(2);
		resource.setDataSyncTransDate(transactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.CommodityVarietyCreated);

		service.synchronizeCommodityVariety(resource);
	}
	
	private void createVariety(Integer cropVarietyId, String cropVarietyName) throws CirrasUnderwritingServiceException, ValidationException {
		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		//CREATE VARIETY
		SyncCommodityVarietyRsrc resource = new SyncCommodityVarietyRsrc();

		resource.setCropId(cropVarietyId);
		resource.setParentCropId(cropCommodityId);
		resource.setCropName(cropVarietyName);
		resource.setDataSyncTransDate(transactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.CommodityVarietyCreated);

		service.synchronizeCommodityVariety(resource);

	}
	
	private void setCropVarietyInsurability(
			CropVarietyInsurability model,
			Boolean isQuantityInsurableInd,
			Boolean isUnseededInsurableInd,
			Boolean isPlantInsurableInd,
			Boolean isAwpEligibleInd,
			Boolean isUnderseedingEligibleInd
		) {
		
		model.setIsQuantityInsurableInd(isQuantityInsurableInd);
		model.setIsUnseededInsurableInd(isUnseededInsurableInd);
		model.setIsPlantInsurableInd(isPlantInsurableInd);
		model.setIsAwpEligibleInd(isAwpEligibleInd);
		model.setIsUnderseedingEligibleInd(isUnderseedingEligibleInd);
	}
	
	private CropVarietyPlantInsurability getNewCropVarietyPlantInsurability(
			String plantInsurabilityCode,
			String plantInsurabilityDesc, 
			String cropVarietyInsurabilityGuid,
			Integer cropVarietyId) {
		CropVarietyPlantInsurability model = new CropVarietyPlantInsurability();
		
		model.setCropVarietyInsurabilityGuid(cropVarietyInsurabilityGuid);
		model.setPlantInsurabilityTypeCode(plantInsurabilityCode);
		model.setCropVarietyId(cropVarietyId);
		model.setDescription(plantInsurabilityDesc);

		return model;
	}

	
	
	private void assertPlantInsurabilities(
			String plantInsurabilityCode,
			List<CropVarietyPlantInsurability> expectedList,
			List<CropVarietyPlantInsurability> actualList 
		) {
		
		Assert.assertNotNull(expectedList);
		Assert.assertNotNull(actualList);
		
		CropVarietyPlantInsurability expected = getCropVarietyPlantInsurability(expectedList, plantInsurabilityCode);
		Assert.assertNotNull(expected);
		
		CropVarietyPlantInsurability actual = getCropVarietyPlantInsurability(actualList, plantInsurabilityCode);
		Assert.assertNotNull(actual);

		assertCropVarietyPlantInsurability(expected, actual);
	}

	private void assertCropVarietyPlantInsurability(CropVarietyPlantInsurability expected, CropVarietyPlantInsurability actual) {
		if(expected.getCropVarietyInsurabilityGuid() == null) {
			Assert.assertNotNull("cropVarietyInsurabilityGuid", actual.getCropVarietyInsurabilityGuid());
		} else {
			Assert.assertEquals("cropVarietyInsurabilityGuid", expected.getCropVarietyInsurabilityGuid(), actual.getCropVarietyInsurabilityGuid());
		}
		Assert.assertEquals("PlantInsurabilityTypeCode", expected.getPlantInsurabilityTypeCode(), actual.getPlantInsurabilityTypeCode());
		Assert.assertEquals("CropVarietyId", expected.getCropVarietyId(), actual.getCropVarietyId());
		Assert.assertEquals("Description", expected.getDescription(), actual.getDescription());
		Assert.assertNotNull("IsUsedInd", actual.getIsUsedInd());

	}

	private void assertValidationCropVarietyInsurabilities(
			CropVarietyInsurabilityListRsrc actualList
		) {

		for (CropVarietyInsurability actual : actualList.getCollection()) {
			assertValidationFlags(actual, true);
		}
	}
	
	private void assertCropVarietyInsurability(CropVarietyInsurability expected, CropVarietyInsurability actual, Boolean expectValues) {
		
		Assert.assertNotNull("CropVarietyInsurabilityGuid", actual.getCropVarietyInsurabilityGuid());
		Assert.assertEquals("CropVarietyId", expected.getCropVarietyId(), actual.getCropVarietyId());
		Assert.assertEquals("IsQuantityInsurableInd", expected.getIsQuantityInsurableInd(), actual.getIsQuantityInsurableInd());
		Assert.assertEquals("IsUnseededInsurableInd", expected.getIsUnseededInsurableInd(), actual.getIsUnseededInsurableInd());
		Assert.assertEquals("IsPlantInsurableInd", expected.getIsPlantInsurableInd(), actual.getIsPlantInsurableInd());
		Assert.assertEquals("IsAwpEligibleInd", expected.getIsAwpEligibleInd(), actual.getIsAwpEligibleInd());
		Assert.assertEquals("IsUnderseedingEligibleInd", expected.getIsUnderseedingEligibleInd(), actual.getIsUnderseedingEligibleInd());
		
		assertValidationFlags(actual, expectValues);
	}
	
	private void assertValidationFlags(CropVarietyInsurability actual, Boolean expectValues) {
		
		if(expectValues) {
			Assert.assertNotNull("IsQuantityInsurableEditableInd", actual.getIsQuantityInsurableEditableInd());
			Assert.assertNotNull("IsUnseededInsurableEditableInd", actual.getIsUnseededInsurableEditableInd());
			Assert.assertNotNull("IsPlantInsurableEditableInd", actual.getIsPlantInsurableEditableInd());
			Assert.assertNotNull("IsAwpEligibleEditableInd", actual.getIsAwpEligibleEditableInd());
			Assert.assertNotNull("IsUnderseedingEligibleEditableInd", actual.getIsUnderseedingEligibleEditableInd());
		} else {
			Assert.assertNull("IsQuantityInsurableEditableInd", actual.getIsQuantityInsurableEditableInd());
			Assert.assertNull("IsUnseededInsurableEditableInd", actual.getIsUnseededInsurableEditableInd());
			Assert.assertNull("IsPlantInsurableEditableInd", actual.getIsPlantInsurableEditableInd());
			Assert.assertNull("IsAwpEligibleEditableInd", actual.getIsAwpEligibleEditableInd());
			Assert.assertNull("IsUnderseedingEligibleEditableInd", actual.getIsUnderseedingEligibleEditableInd());
		}
	}

}
