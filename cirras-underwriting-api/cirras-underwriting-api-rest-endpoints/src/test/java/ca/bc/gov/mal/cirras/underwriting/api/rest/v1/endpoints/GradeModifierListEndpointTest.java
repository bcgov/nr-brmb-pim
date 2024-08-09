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

public class GradeModifierListEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(GradeModifierListEndpointTest.class);


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

	private Integer cropYear = 2009;
	private Integer insurancePlanId = 4;
	
	private String commodityName1 = "Commodity 1";
	private String commodityName2 = "Commodity 2";
	private Integer cropCommodityId1 = 88888888;
	private Integer cropCommodityId2 = 88889898;

	private String gradeModifierTypeCode1 = "TST01";
	private String gradeModifierTypeCode2 = "TST02";

	private String gradeModifierDescription1 = "DESC1";
	private String gradeModifierDescription2 = "DESC2";


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

		GradeModifierListRsrc searchResults = service.getGradeModifierList(
				topLevelEndpoints, 
				cropYear.toString(),
				insurancePlanId.toString(),
				null
		);		
		
		if(searchResults != null && searchResults.getCollection().size() > 0) {
			for (GradeModifierRsrc resource : searchResults.getCollection()) {
				resource.setDeletedByUserInd(true);
			}
			service.saveGradeModifiers(searchResults, cropYear.toString(), insurancePlanId.toString(), null);
		}
		
		// delete commodity
		service.deleteSyncCommodityVariety(topLevelEndpoints, cropCommodityId1.toString());
		service.deleteSyncCommodityVariety(topLevelEndpoints, cropCommodityId2.toString());

		deleteGradeModifierTypes();
		
		//delete underwriting year
		deleteUwYear(cropYear);
	}
	
	protected void deleteGradeModifierTypes() throws CirrasUnderwritingServiceException, ValidationException {
		//Delete grade modifer types
		GradeModifierTypeListRsrc searchResults = service.getGradeModifierTypeList(
				topLevelEndpoints, 
				cropYear.toString()
		);		
		
		if(searchResults != null && searchResults.getCollection().size() > 0) {
			for (GradeModifierType resource : searchResults.getCollection()) {
				resource.setDeletedByUserInd(true);
			}
			service.saveGradeModifierTypes(searchResults, cropYear.toString());
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
	public void testCreateUpdateDeleteSelectGradeModifier() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testCreateUpdateDeleteSelectGradeModifier");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		int expectedCount = 2;

		//Create Underwriting Year
		createUnderwritingYear(cropYear);

		//Create commodities
		createCommodity(cropCommodityId1, commodityName1);
		createCommodity(cropCommodityId2, commodityName2);
		
		//Create grade modifier types
		createGradeModifierType(gradeModifierTypeCode1, gradeModifierDescription1);
		createGradeModifierType(gradeModifierTypeCode2, gradeModifierDescription2);

		//Create Grain Modifiers
	
		//Add 2 seeding deadlines
		GradeModifierListRsrc gradeModifierList = service.getGradeModifierList(
				topLevelEndpoints, 
				cropYear.toString(),
				insurancePlanId.toString(),
				null
		);
		Assert.assertNotNull(gradeModifierList);
		Assert.assertEquals(0, gradeModifierList.getCollection().size());


		GradeModifierRsrc gradeModifierRsrc1 = createGradeModifier(cropCommodityId1, gradeModifierTypeCode1, gradeModifierDescription1);
		GradeModifierRsrc gradeModifierRsrc2 = createGradeModifier(cropCommodityId2, gradeModifierTypeCode2, gradeModifierDescription2);


		gradeModifierList.getCollection().add(gradeModifierRsrc1);
		gradeModifierList.getCollection().add(gradeModifierRsrc2);
		
		//Save and Fetch
		 GradeModifierListRsrc insertedGradeModifiers = service.saveGradeModifiers(
				gradeModifierList, 
				cropYear.toString(),
				insurancePlanId.toString(),
				null
		);
		 
		Assert.assertNotNull(insertedGradeModifiers);
		Assert.assertEquals(expectedCount, insertedGradeModifiers.getCollection().size());
		
		assertGradeModifiers(insertedGradeModifiers, gradeModifierRsrc1, gradeModifierRsrc2);
		
		//Update 1 and add a third, and delete one
		//Update
		gradeModifierRsrc2 = getGradeModifier(insertedGradeModifiers, cropYear, cropCommodityId2, gradeModifierTypeCode2);
		Assert.assertNotNull(gradeModifierRsrc2);
		gradeModifierRsrc2.setGradeModifierValue(65.4321);

		//Delete
		gradeModifierRsrc1 = getGradeModifier(insertedGradeModifiers, cropYear, cropCommodityId1, gradeModifierTypeCode1);
		Assert.assertNotNull(gradeModifierRsrc1);
		gradeModifierRsrc1.setDeletedByUserInd(true);
		
		//Add
		GradeModifierRsrc gradeModifierRsrc3 = createGradeModifier(cropCommodityId1, gradeModifierTypeCode2, gradeModifierDescription2);
		insertedGradeModifiers.getCollection().add(gradeModifierRsrc3);

		//Save and Fetch
		 GradeModifierListRsrc updatedGradeModifiers = service.saveGradeModifiers(
				insertedGradeModifiers, 
				cropYear.toString(),
				insurancePlanId.toString(),
				null
		);


		Assert.assertNotNull(updatedGradeModifiers);
		Assert.assertEquals(expectedCount, updatedGradeModifiers.getCollection().size());
		
		assertGradeModifiers(updatedGradeModifiers, gradeModifierRsrc2, gradeModifierRsrc3);
		
		//TEST SELECT
		// TEST 1: Search with results.
		GradeModifierListRsrc searchResults = service.getGradeModifierList(
				topLevelEndpoints, 
				cropYear.toString(),
				insurancePlanId.toString(),
				null
		);

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(expectedCount, searchResults.getCollection().size());
		Assert.assertEquals(insurancePlanId, searchResults.getCollection().get(0).getInsurancePlanId());
		Assert.assertEquals(insurancePlanId, searchResults.getCollection().get(1).getInsurancePlanId());
		
		// TEST 2: Search with results, only 1 commodity
		searchResults = service.getGradeModifierList(
				topLevelEndpoints, 
				cropYear.toString(),
				insurancePlanId.toString(),
				cropCommodityId1.toString()
		);

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(1, searchResults.getCollection().size());
		Assert.assertEquals(cropCommodityId1, searchResults.getCollection().get(0).getCropCommodityId());
		
		// Set to year or plan without any existing data.
		Integer emptyCropYear = 2002;
		Integer emptyInsurancePlanId = 5;

		// TEST 3: Crop Year with no results.
		searchResults = service.getGradeModifierList(
				topLevelEndpoints, 
				emptyCropYear.toString(),
				insurancePlanId.toString(),
				null
		);

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(0, searchResults.getCollection().size());

		// TEST 4: Plan with no results.
		searchResults = service.getGradeModifierList(
				topLevelEndpoints, 
				cropYear.toString(),
				emptyInsurancePlanId.toString(),
				null
		);

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(0, searchResults.getCollection().size());

		// TEST 5: Commodity with no results.
		searchResults = service.getGradeModifierList(
				topLevelEndpoints, 
				cropYear.toString(),
				insurancePlanId.toString(),
				"56565656"
		);

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(0, searchResults.getCollection().size());

		//delete all
		delete();
		
		//Check if all created grade modifiers are deleted
		searchResults = service.getGradeModifierList(
				topLevelEndpoints, 
				cropYear.toString(),
				insurancePlanId.toString(),
				null
		);
		Assert.assertNotNull(searchResults);
		Assert.assertEquals(0, searchResults.getCollection().size());


		logger.debug(">testCreateUpdateDeleteSelectGradeModifier");
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
			select distinct dycc.grade_modifier_type_code, dyc.crop_year, dycc.crop_commodity_id
			from declared_yield_contract_commodity dycc
			join declared_yield_contract dyc on dyc.declared_yield_contract_guid = dycc.declared_yield_contract_guid
			where dycc.grade_modifier_type_code is not null
		*/
		//2. Set the modifier type code, crop year and commodity with one of the codes returned in the query
		String gradeModifierTypeCode = "BLY1";
		Integer cropYear = 2023;
		Integer cropCommodityId = 16;
		
		//3. Run test
		
		GradeModifierListRsrc searchResults = service.getGradeModifierList(
				topLevelEndpoints, 
				cropYear.toString(),
				insurancePlanId.toString(),
				cropCommodityId.toString()
		);
		Assert.assertNotNull(searchResults);
		
		//Returns the first one found
		GradeModifierRsrc gradeModifierRsrc = getGradeModifier(searchResults, cropYear, cropCommodityId, gradeModifierTypeCode);
		
		Assert.assertNotNull(gradeModifierRsrc);
		Assert.assertEquals("DeleteAllowedInd", false, gradeModifierRsrc.getDeleteAllowedInd());
		
		logger.debug(">testDeleteAllowed");

	}
	
	private GradeModifierRsrc getGradeModifier(GradeModifierListRsrc resources, Integer cropYear, Integer cropCommodityId, String commodityName) {
		
		for (GradeModifierRsrc gradeModifier : resources.getCollection()) {
			if (gradeModifier.getCropCommodityId().equals(cropCommodityId) &&
					gradeModifier.getGradeModifierTypeCode().equals(commodityName) &&
					gradeModifier.getCropYear().equals(cropYear)) {
				return gradeModifier;
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

	
	private GradeModifierRsrc createGradeModifier(Integer cropCommodityId, String gradeModifierTypeCode, String gradeModifierDescription) throws DaoException {
		
		
		GradeModifierRsrc resource = new GradeModifierRsrc();
		resource.setCropCommodityId(cropCommodityId);
		resource.setCropYear(cropYear);
		resource.setGradeModifierGuid(null);
		resource.setGradeModifierTypeCode(gradeModifierTypeCode);
		resource.setGradeModifierValue(11.22);
		resource.setDeleteAllowedInd(true);

		resource.setDescription(gradeModifierDescription);
		resource.setInsurancePlanId(4);

		return resource;
	}
	
	private void assertGradeModifiers(
			GradeModifierListRsrc resources,
			GradeModifierRsrc gradeModifierA,
			GradeModifierRsrc gradeModifierB) {
		
		for (GradeModifierRsrc gradeModifier : resources.getCollection()) {
			if (gradeModifier.getCropCommodityId().equals(gradeModifierA.getCropCommodityId()) &&
				gradeModifier.getGradeModifierTypeCode().equals(gradeModifierA.getGradeModifierTypeCode())) {
				assertGradeModifier(gradeModifierA, gradeModifier);
			} else if (gradeModifier.getCropCommodityId().equals(gradeModifierB.getCropCommodityId()) &&
					gradeModifier.getGradeModifierTypeCode().equals(gradeModifierB.getGradeModifierTypeCode())) {
				assertGradeModifier(gradeModifierB, gradeModifier);
			} else {
				Assert.fail("Unexpected commodity and/or modifer: " + gradeModifier.getCropCommodityId() + ", " + gradeModifier.getGradeModifierTypeCode());
			}

		}
		
	}
	
	private void assertGradeModifier(GradeModifierRsrc expected, GradeModifierRsrc actual) {
		
		Assert.assertNotNull("GradeModifierGuid", actual.getGradeModifierGuid());
		Assert.assertEquals("CropCommodityId", expected.getCropCommodityId(), actual.getCropCommodityId());
		Assert.assertEquals("CropYear", expected.getCropYear(), actual.getCropYear());
		Assert.assertEquals("GradeModifierTypeCode", expected.getGradeModifierTypeCode(), actual.getGradeModifierTypeCode());
		Assert.assertEquals("GradeModifierValue", expected.getGradeModifierValue(), actual.getGradeModifierValue());
		Assert.assertEquals("DeleteAllowed", expected.getDeleteAllowedInd(), actual.getDeleteAllowedInd());
	}

	
	private void createUnderwritingYear(Integer cropYear) throws CirrasUnderwritingServiceException, ValidationException {
		//CREATE Underwriting Years
		UnderwritingYearRsrc newResource = new UnderwritingYearRsrc();
		newResource.setCropYear(cropYear);
		service.createUnderwritingYear(topLevelEndpoints, newResource);
	}
	
	
	private void createGradeModifierType(String gradeModifierTypeCode, String gradeModifierDescription) throws CirrasUnderwritingServiceException, ValidationException {

		GradeModifierTypeListRsrc gradeModifierTypeList = service.getGradeModifierTypeList(
				topLevelEndpoints, 
				cropYear.toString()
		);
		Assert.assertNotNull(gradeModifierTypeList);
		
		GradeModifierType model = new GradeModifierType();
		model.setGradeModifierTypeCode(gradeModifierTypeCode);
		model.setDescription(gradeModifierDescription);
		model.setEffectiveYear(cropYear);
		model.setExpiryYear(null);
		model.setDeleteAllowedInd(true);
		model.setMaxYearUsed(null);

		gradeModifierTypeList.getCollection().add(model);
		
		//Save
		service.saveGradeModifierTypes(
				gradeModifierTypeList, 
				cropYear.toString()
		);
	}
	
}
