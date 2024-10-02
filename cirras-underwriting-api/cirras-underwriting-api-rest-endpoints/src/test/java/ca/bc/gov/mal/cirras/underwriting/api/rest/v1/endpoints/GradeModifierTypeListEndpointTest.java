package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints;


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
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GradeModifierListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GradeModifierRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GradeModifierTypeListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SyncCommodityVarietyRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UnderwritingYearListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UnderwritingYearRsrc;
import ca.bc.gov.mal.cirras.underwriting.model.v1.GradeModifierType;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class GradeModifierTypeListEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(GradeModifierTypeListEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.GET_GRADE_MODIFIERS,
		Scopes.SAVE_GRADE_MODIFIERS,
		Scopes.GET_UNDERWRITING_YEAR,
		Scopes.CREATE_UNDERWRITING_YEAR,
		Scopes.DELETE_UNDERWRITING_YEAR,
		Scopes.UPDATE_SYNC_UNDERWRITING,
		Scopes.DELETE_SYNC_UNDERWRITING
	};
	
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;

	private Integer effectiveCropYear = 2009;
	private Integer expiryCropYear = 2011;
	private Integer insurancePlanId = 4;
	
	private String commodityName1 = "Commodity 1";
	private Integer cropCommodityId1 = 88888888;
	
	private String gradeModifierTypeCode1 = "TST01";
	private String gradeModifierTypeCode2 = "TST02";
	private String gradeModifierTypeCode3 = "TST03";

	private String gradeModifierDescription1 = "DESC1";
	private String gradeModifierDescription2 = "DESC2";
	private String gradeModifierDescription3 = "DESC3";


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

		//Delete grade modifiers
		deleteGradeModifiers();
		
		// delete commodity
		service.deleteSyncCommodityVariety(topLevelEndpoints, cropCommodityId1.toString());

		//Delete grade modifer types
		GradeModifierTypeListRsrc searchResults = service.getGradeModifierTypeList(
				topLevelEndpoints, 
				effectiveCropYear.toString()
		);		
		
		if(searchResults != null && searchResults.getCollection().size() > 0) {
			for (GradeModifierType resource : searchResults.getCollection()) {
				resource.setDeletedByUserInd(true);
			}
			service.saveGradeModifierTypes(searchResults, effectiveCropYear.toString());
		}
		
		//delete underwriting year
		deleteUwYear(effectiveCropYear);
	}
	
	protected void deleteGradeModifiers() throws CirrasUnderwritingServiceException, ValidationException {
		GradeModifierListRsrc searchResults = service.getGradeModifierList(
				topLevelEndpoints, 
				effectiveCropYear.toString(),
				insurancePlanId.toString(),
				null
		);		
		
		if(searchResults != null && searchResults.getCollection().size() > 0) {
			for (GradeModifierRsrc resource : searchResults.getCollection()) {
				resource.setDeletedByUserInd(true);
			}
			service.saveGradeModifiers(searchResults, effectiveCropYear.toString(), insurancePlanId.toString(), null);
		}

	}
	
	protected void deleteUwYear(Integer cropYear) throws CirrasUnderwritingServiceException {
		UnderwritingYearListRsrc results = service.getUnderwritingYearList(topLevelEndpoints);
		
		if(results != null && results.getCollection().size() > 0) {
			List<UnderwritingYearRsrc> resources = results.getCollection().stream()
					.filter(x -> x.getCropYear().equals(cropYear))
					.collect(Collectors.toList());
			
			if(resources != null && resources.size() > 0) {
				//FETCH then DELETE.
				UnderwritingYearRsrc uwYearRsrc = service.getUnderwritingYear(resources.get(0));				
				service.deleteUnderwritingYear(uwYearRsrc);
			}
		}
	}
	
	@Test
	public void testCreateUpdateDeleteSelectGradeModifierType() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testCreateUpdateDeleteSelectGradeModifierType");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//Establish current record count
		GradeModifierTypeListRsrc searchResults = service.getGradeModifierTypeList(
				topLevelEndpoints, 
				null
		);

		Assert.assertNotNull(searchResults);
		int totalExistingRecords = searchResults.getCollection().size();
		
		int expectedCount = 2;

		//Create Underwriting Year
		createUnderwritingYear(effectiveCropYear);

		//Create commodities
		createCommodity(cropCommodityId1, commodityName1);
		
		GradeModifierTypeListRsrc gradeModifierTypeList = service.getGradeModifierTypeList(
				topLevelEndpoints, 
				effectiveCropYear.toString()
		);
		Assert.assertNotNull(gradeModifierTypeList);
		Assert.assertEquals(0, gradeModifierTypeList.getCollection().size());

		//Create Grade Modifier Types
		GradeModifierType gradeModifierType1 = createGradeModifierType(effectiveCropYear, null, gradeModifierTypeCode1, gradeModifierDescription1);
		GradeModifierType gradeModifierType2 = createGradeModifierType(effectiveCropYear, expiryCropYear, gradeModifierTypeCode2, gradeModifierDescription2);
		
		gradeModifierTypeList.getCollection().add(gradeModifierType1);
		gradeModifierTypeList.getCollection().add(gradeModifierType2);
		
		//Save and Fetch
		GradeModifierTypeListRsrc insertedGradeModifierTypes = service.saveGradeModifierTypes(
				gradeModifierTypeList, 
				effectiveCropYear.toString()
		);
		 
		Assert.assertNotNull(insertedGradeModifierTypes);
		Assert.assertEquals(expectedCount, insertedGradeModifierTypes.getCollection().size());
		
		
		assertGradeModifierTypes(insertedGradeModifierTypes, gradeModifierType1, gradeModifierType2);
		
		
		//Update 1 and add a third, and delete one
		//Update
		gradeModifierType1 = getGradeModifierType(insertedGradeModifierTypes, gradeModifierTypeCode1);
		Assert.assertNotNull(gradeModifierType1);
		gradeModifierType1.setDescription("Updated DESC");
		gradeModifierType1.setExpiryYear(2011);

		//Delete
		gradeModifierType2 = getGradeModifierType(insertedGradeModifierTypes, gradeModifierTypeCode2);
		Assert.assertNotNull(gradeModifierType2);
		gradeModifierType2.setDeletedByUserInd(true);
		
		//Add
		GradeModifierType gradeModifierType3 = createGradeModifierType(effectiveCropYear, expiryCropYear, gradeModifierTypeCode3, gradeModifierDescription3);
		insertedGradeModifierTypes.getCollection().add(gradeModifierType3);

		//Save and Fetch
		 GradeModifierTypeListRsrc updatedGradeModifierTypes = service.saveGradeModifierTypes(
				insertedGradeModifierTypes, 
				effectiveCropYear.toString()
		);


		Assert.assertNotNull(updatedGradeModifierTypes);
		Assert.assertEquals(expectedCount, updatedGradeModifierTypes.getCollection().size());
		
		assertGradeModifierTypes(updatedGradeModifierTypes, gradeModifierType1, gradeModifierType3);
		
		//TEST SELECT
		// TEST 1: Search with results. All types
		searchResults = service.getGradeModifierTypeList(
				topLevelEndpoints, 
				null
		);

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(expectedCount + totalExistingRecords, searchResults.getCollection().size());

		// TEST 2: Search with results, only 1 commodity
		searchResults = service.getGradeModifierTypeList(
				topLevelEndpoints, 
				effectiveCropYear.toString()
		);

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(expectedCount, searchResults.getCollection().size());
		
		for (GradeModifierType gradeModifierType : searchResults.getCollection()) {
			//Check if all the records are active for the search.
			Assert.assertTrue(gradeModifierType.getEffectiveYear() <= effectiveCropYear);
			if(gradeModifierType.getExpiryYear() != null) {
				Assert.assertTrue(gradeModifierType.getExpiryYear() >= effectiveCropYear);
			}
		}
		
		// Set to year or plan without any existing data.
		Integer emptyCropYear = 2002;

		// TEST 3: Crop Year with no results.
		searchResults = service.getGradeModifierTypeList(
				topLevelEndpoints, 
				emptyCropYear.toString()
		);

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(0, searchResults.getCollection().size());

		//Add 2 Grade Modifier
		createGradeModifier(cropCommodityId1, gradeModifierTypeCode1, gradeModifierDescription1);
		
		searchResults = service.getGradeModifierTypeList(
				topLevelEndpoints, 
				effectiveCropYear.toString()
		);

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(expectedCount, searchResults.getCollection().size());

		gradeModifierType1 = getGradeModifierType(searchResults, gradeModifierTypeCode1);
		Assert.assertNotNull(gradeModifierType1);
		Assert.assertEquals(false, gradeModifierType1.getDeleteAllowedInd());
		Assert.assertEquals("MaxYearUsed", effectiveCropYear, gradeModifierType1.getMaxYearUsed());


		gradeModifierType3 = getGradeModifierType(searchResults, gradeModifierTypeCode3);
		Assert.assertNotNull(gradeModifierType3);
		Assert.assertEquals(true, gradeModifierType3.getDeleteAllowedInd());
		Assert.assertNull(gradeModifierType3.getMaxYearUsed());
		
		//delete all
		delete();
		
		//Check if all created grade modifiers are deleted
		searchResults = service.getGradeModifierTypeList(
				topLevelEndpoints, 
				effectiveCropYear.toString()
		);
		Assert.assertNotNull(searchResults);
		Assert.assertEquals(0, searchResults.getCollection().size());


		logger.debug(">testCreateUpdateDeleteSelectGradeModifierType");
	}	
	
	@Test
	public void testDeleteAllowed() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testDeleteAllowed");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//Test if delete allowed is set correctly if it's used in a dop contract.
		//1. Run this query
		/*
			select distinct dycc.grade_modifier_type_code
			from declared_yield_contract_commodity dycc
			join declared_yield_contract dyc on dyc.declared_yield_contract_guid = dycc.declared_yield_contract_guid
			where dycc.grade_modifier_type_code is not null
		*/
		//2. Set the modifier type code with one of the codes returned in the query
		String gradeModifierTypeCode = "BLY1";
		
		//3. Run test
		
		GradeModifierTypeListRsrc searchResults = service.getGradeModifierTypeList(
				topLevelEndpoints, 
				null
		);

		Assert.assertNotNull(searchResults);
		
		//Returns the first one found
		GradeModifierType gradeModifierType = getGradeModifierType(searchResults, gradeModifierTypeCode);
		
		Assert.assertNotNull(gradeModifierType);
		Assert.assertEquals("DeleteAllowedInd", false, gradeModifierType.getDeleteAllowedInd());
		
		logger.debug(">testDeleteAllowed");

	}
	
	private GradeModifierType getGradeModifierType(GradeModifierTypeListRsrc resources, String gradeModifierTypeCode) {
		
		for (GradeModifierType gradeModifierType : resources.getCollection()) {
			if (gradeModifierType.getGradeModifierTypeCode().equals(gradeModifierTypeCode)) {
				return gradeModifierType;
			}
		}
		return null;
	}

	private void createCommodity(Integer cropCommodityId, String commodityName)
			throws CirrasUnderwritingServiceException, ValidationException {

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();
		
		//CREATE PARENT COMMODITY
		SyncCommodityVarietyRsrc resource = new SyncCommodityVarietyRsrc();

		resource.setCropId(cropCommodityId);
		resource.setCropName(commodityName);
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
		resource.setDataSyncTransDate(transactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.CommodityVarietyCreated);

		service.synchronizeCommodityVariety(resource);
	}
	
	private GradeModifierType createGradeModifierType(Integer effCropYear, Integer expCropYear, String gradeModifierTypeCode, String gradeModifierDescription) {
		GradeModifierType model = new GradeModifierType();
		model.setGradeModifierTypeCode(gradeModifierTypeCode);
		model.setDescription(gradeModifierDescription);
		model.setEffectiveYear(effCropYear);
		model.setExpiryYear(expCropYear);
		model.setDeleteAllowedInd(true);
		model.setMaxYearUsed(null);

		return model;
	}
	
	private void createGradeModifier(Integer cropCommodityId, String gradeModifierTypeCode, String gradeModifierDescription) throws DaoException, CirrasUnderwritingServiceException, ValidationException {
		
		GradeModifierListRsrc gradeModifierList = service.getGradeModifierList(
				topLevelEndpoints, 
				effectiveCropYear.toString(),
				insurancePlanId.toString(),
				null
		);
		Assert.assertNotNull(gradeModifierList);
		Assert.assertEquals(0, gradeModifierList.getCollection().size());

		GradeModifierRsrc resource = new GradeModifierRsrc();
		resource.setCropCommodityId(cropCommodityId);
		resource.setCropYear(effectiveCropYear);
		resource.setGradeModifierGuid(null);
		resource.setGradeModifierTypeCode(gradeModifierTypeCode);
		resource.setGradeModifierValue(11.22);
		resource.setDeleteAllowedInd(true);

		resource.setDescription(gradeModifierDescription);
		resource.setInsurancePlanId(4);

		gradeModifierList.getCollection().add(resource);
		
		//Save and Fetch
		GradeModifierListRsrc insertedGradeModifiers = service.saveGradeModifiers(
				gradeModifierList, 
				effectiveCropYear.toString(),
				insurancePlanId.toString(),
				null
		);

		Assert.assertNotNull(insertedGradeModifiers);
		Assert.assertEquals(1, insertedGradeModifiers.getCollection().size());

	}
	
	private void assertGradeModifierTypes(
			GradeModifierTypeListRsrc resources,
			GradeModifierType gradeModifierA,
			GradeModifierType gradeModifierB) {
		
		for (GradeModifierType gradeModifierType : resources.getCollection()) {
			if (gradeModifierType.getGradeModifierTypeCode().equals(gradeModifierA.getGradeModifierTypeCode())) {
				assertGradeModifierType(gradeModifierA, gradeModifierType);
			} else if (gradeModifierType.getGradeModifierTypeCode().equals(gradeModifierB.getGradeModifierTypeCode())) {
				assertGradeModifierType(gradeModifierB, gradeModifierType);
			} else {
				Assert.fail("Unexpected commodity and/or modifer: " + gradeModifierType.getGradeModifierTypeCode());
			}

		}
		
	}
	
	private void assertGradeModifierType(GradeModifierType expected, GradeModifierType actual) {
		
		Assert.assertEquals("GradeModifierTypeCode", expected.getGradeModifierTypeCode(), actual.getGradeModifierTypeCode());
		Assert.assertEquals("Description", expected.getDescription(), actual.getDescription());
		Assert.assertEquals("EffectiveYear", expected.getEffectiveYear(), actual.getEffectiveYear());
		Assert.assertEquals("ExpiryYear", expected.getExpiryYear(), actual.getExpiryYear());
		Assert.assertEquals("DeleteAllowedInd", expected.getDeleteAllowedInd(), actual.getDeleteAllowedInd());
		Assert.assertEquals("MaxYearUsed", expected.getMaxYearUsed(), actual.getMaxYearUsed());
	}

	
	private void createUnderwritingYear(Integer cropYear) throws CirrasUnderwritingServiceException, ValidationException {
		//CREATE Underwriting Years
		UnderwritingYearRsrc newResource = new UnderwritingYearRsrc();
		newResource.setCropYear(cropYear);
		service.createUnderwritingYear(topLevelEndpoints, newResource);
	}
}
