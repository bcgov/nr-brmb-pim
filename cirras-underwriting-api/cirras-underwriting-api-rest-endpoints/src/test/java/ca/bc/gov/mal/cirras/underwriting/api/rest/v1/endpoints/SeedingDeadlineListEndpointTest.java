package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints;


import static org.junit.Assert.fail;

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
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SeedingDeadlineListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SyncCommodityTypeCodeRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SyncCommodityVarietyRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UnderwritingYearListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UnderwritingYearRsrc;
import ca.bc.gov.mal.cirras.underwriting.model.v1.SeedingDeadline;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class SeedingDeadlineListEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(SeedingDeadlineListEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL,
		Scopes.GET_SEEDING_DEADLINES,
		Scopes.SAVE_SEEDING_DEADLINES, 
		Scopes.UPDATE_SYNC_UNDERWRITING,
		Scopes.DELETE_SYNC_UNDERWRITING,
		Scopes.GET_UNDERWRITING_YEAR,
		Scopes.CREATE_UNDERWRITING_YEAR,
		Scopes.DELETE_UNDERWRITING_YEAR
	};
	
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;
	private String commodityTypeCode1 = "TESTCODE 1";
	private String commodityTypeCode2 = "TESTCODE 2";
	private String commodityTypeCode3 = "TESTCODE 3";
	private Integer cropCommodityId = 88888888;
	private Integer cropYear = 2009;
	

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

		// delete seedingDeadlines
		SeedingDeadlineListRsrc searchResults = service.getSeedingDeadlines(
				topLevelEndpoints, 
				cropYear.toString()
		);
		
		if(searchResults != null && searchResults.getCollection().size() > 0) {
			for (SeedingDeadline resource : searchResults.getCollection()) {
				resource.setDeletedByUserInd(true);
			}
			service.saveSeedingDeadlines(topLevelEndpoints, searchResults, cropYear.toString());
		}

		// delete commodity type code
		service.deleteCommodityTypeCode(topLevelEndpoints, commodityTypeCode1);
		service.deleteCommodityTypeCode(topLevelEndpoints, commodityTypeCode2);
		service.deleteCommodityTypeCode(topLevelEndpoints, commodityTypeCode3);

		// delete commodity
		service.deleteSyncCommodityVariety(topLevelEndpoints, cropCommodityId.toString());

		//delete underwriting year
		deleteUwYear(cropYear);
	}
	
	protected void deleteUwYear(Integer cropYear) throws CirrasUnderwritingServiceException {
		UnderwritingYearListRsrc results = service.getUnderwritingYearList(topLevelEndpoints);
		
		if(results != null && results.getCollection().size() > 0) {
			List<UnderwritingYearRsrc> resources = results.getCollection().stream()
					.filter(x -> x.getCropYear().equals(cropYear))
					.collect(Collectors.toList());
			
			if(resources != null && resources.size() > 0) {
				//DELETE
				service.deleteUnderwritingYear(resources.get(0));
			}
		}
	}

	@Test
	public void testGetSeedingDeadlines() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testGetSeedingDeadlines");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		// Set to year and plan with existing data.
		Integer cropYear = 2023;
		int expectedCount = 16;

		// Set to year or plan without any existing data.
		Integer emptyCropYear = 2022;

		// TEST 1: Search with results.
		SeedingDeadlineListRsrc searchResults = service.getSeedingDeadlines(
				topLevelEndpoints, 
				cropYear.toString()
		);

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(expectedCount, searchResults.getCollection().size());
		
		for (SeedingDeadline resource : searchResults.getCollection()) {
			Assert.assertEquals(cropYear, resource.getCropYear());
		}

		// TEST 2: Crop Year with no results.
		searchResults = service.getSeedingDeadlines(
				topLevelEndpoints, 
				emptyCropYear.toString()
		);

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(0, searchResults.getCollection().size());
		
		logger.debug(">testGetSeedingDeadlines");
	}
	
	@Test
	public void testCreateUpdateDeleteSeedingDeadline() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testCreateUpdateDeleteSeedingDeadline");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		int expectedCount = 2;
		
		//Create commodity and commodity types
		createCommodity();
		
		createCommodityTypeCode(commodityTypeCode1);
		createCommodityTypeCode(commodityTypeCode2);
		createCommodityTypeCode(commodityTypeCode3);
		
		//Create Underwriting Year
		createUnderwritingYear(cropYear);
		
		//Add 2 seeding deadlines
		SeedingDeadlineListRsrc seedingDeadlineList = new SeedingDeadlineListRsrc();
		
		SeedingDeadline seedingDeadline1 = createSeedingDeadline(commodityTypeCode1);
		SeedingDeadline seedingDeadline2 = createSeedingDeadline(commodityTypeCode2);

		seedingDeadlineList.getCollection().add(seedingDeadline1);
		seedingDeadlineList.getCollection().add(seedingDeadline2);
		
		//Save and Fetch
		 SeedingDeadlineListRsrc insertedSeedingDeadlines = service.saveSeedingDeadlines(
				topLevelEndpoints,
				seedingDeadlineList, 
				cropYear.toString()
		);
		Assert.assertNotNull(insertedSeedingDeadlines);
		Assert.assertEquals(expectedCount, insertedSeedingDeadlines.getCollection().size());
		
		assertSeedingDeadlines(insertedSeedingDeadlines, seedingDeadline1, seedingDeadline2);
		
		//Update 1 and add a third, and delete one
		//Update
		seedingDeadline1 = getSeedingDeadline(insertedSeedingDeadlines, seedingDeadline1.getCommodityTypeCode());
		Assert.assertNotNull(seedingDeadline1);
		updateCoverageDeadlines(seedingDeadline1);
		//Delete
		seedingDeadline2 = getSeedingDeadline(insertedSeedingDeadlines, seedingDeadline2.getCommodityTypeCode());
		Assert.assertNotNull(seedingDeadline2);
		seedingDeadline2.setDeletedByUserInd(true);
		
		//Add
		SeedingDeadline seedingDeadline3 = createSeedingDeadline(commodityTypeCode3);
		insertedSeedingDeadlines.getCollection().add(seedingDeadline3);
		
		//Save and Fetch
		 SeedingDeadlineListRsrc updatedSeedingDeadlines = service.saveSeedingDeadlines(
				topLevelEndpoints,
				insertedSeedingDeadlines, 
				cropYear.toString()
		);
		Assert.assertNotNull(updatedSeedingDeadlines);
		Assert.assertEquals(expectedCount, updatedSeedingDeadlines.getCollection().size());

		assertSeedingDeadlines(updatedSeedingDeadlines, seedingDeadline1, seedingDeadline3);

		//delete all
		
		delete();

		logger.debug(">testCreateUpdateDeleteSeedingDeadline");
	}

	private void assertSeedingDeadlines(
			SeedingDeadlineListRsrc resources,
			SeedingDeadline seedingDeadlineA,
			SeedingDeadline seedingDeadlineB) {
		
		for (SeedingDeadline seedingDeadline : resources.getCollection()) {
			if (seedingDeadline.getCommodityTypeCode().equals(seedingDeadlineA.getCommodityTypeCode())) {
				assertSeedingDeadline(seedingDeadlineA, seedingDeadline);
			} else if (seedingDeadline.getCommodityTypeCode().equals(seedingDeadlineB.getCommodityTypeCode())) {
				assertSeedingDeadline(seedingDeadlineB, seedingDeadline);
			} else {
				Assert.fail("Unexpected commodity type: " + seedingDeadline.getCommodityTypeCode());
			}

		}
		
	}
	
	private void assertSeedingDeadline(SeedingDeadline expected, SeedingDeadline actual) {
		Assert.assertNotNull("SeedingDeadlineGuid", actual.getSeedingDeadlineGuid());
		Assert.assertEquals("CommodityTypeCode", expected.getCommodityTypeCode(), actual.getCommodityTypeCode());
		Assert.assertEquals("CropYear", expected.getCropYear(), actual.getCropYear());
		Assert.assertEquals("FullCoverageDeadlineDate", expected.getFullCoverageDeadlineDate(), actual.getFullCoverageDeadlineDate());
		Assert.assertEquals("FullCoverageDeadlineDateDefault", expected.getFullCoverageDeadlineDateDefault(), actual.getFullCoverageDeadlineDateDefault());
		Assert.assertEquals("FinalCoverageDeadlineDate", expected.getFinalCoverageDeadlineDate(), actual.getFinalCoverageDeadlineDate());
		Assert.assertEquals("FinalCoverageDeadlineDateDefault", expected.getFinalCoverageDeadlineDateDefault(), actual.getFinalCoverageDeadlineDateDefault());
	}
	
	private SeedingDeadline getSeedingDeadline(SeedingDeadlineListRsrc resources, String commodityTypeCode) {
		
		for (SeedingDeadline seedingDeadline : resources.getCollection()) {
			if (seedingDeadline.getCommodityTypeCode().equals(commodityTypeCode)) {
				return seedingDeadline;
			}
		}
		return null;
	}
	
	private SeedingDeadline createSeedingDeadline(String commodityType) throws DaoException {
		
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(cropYear, Calendar.JANUARY, 15);
		Date coverageDeadline = cal.getTime();

		Date fullCoverageDeadline = coverageDeadline;
		Date fullCoverageDeadlineDefault = addDays(coverageDeadline, 1);

		Date finalCoverageDeadline = addDays(coverageDeadline, 5);
		Date finalCoverageDeadlineDefault = addDays(coverageDeadline, 6);
		
		SeedingDeadline resource = new SeedingDeadline();
		resource.setCommodityTypeCode(commodityType);
		resource.setCropYear(cropYear);
		resource.setFullCoverageDeadlineDate(fullCoverageDeadline);
		resource.setFinalCoverageDeadlineDate(finalCoverageDeadline);
		resource.setFullCoverageDeadlineDateDefault(fullCoverageDeadlineDefault);
		resource.setFinalCoverageDeadlineDateDefault(finalCoverageDeadlineDefault);

		return resource;
	}

	
	private SeedingDeadline updateCoverageDeadlines(SeedingDeadline resource) throws DaoException {

		resource.setFullCoverageDeadlineDate(addDays(resource.getFullCoverageDeadlineDate(), 10));
		resource.setFinalCoverageDeadlineDate(addDays(resource.getFinalCoverageDeadlineDate(), 10));
		resource.setFullCoverageDeadlineDateDefault(addDays(resource.getFullCoverageDeadlineDateDefault(), 10));
		resource.setFinalCoverageDeadlineDateDefault(addDays(resource.getFinalCoverageDeadlineDateDefault(), 10));

		return resource;
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
		resource.setDataSyncTransDate(transactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.CommodityVarietyCreated);

		service.synchronizeCommodityVariety(resource);
	}
	
	private void createCommodityTypeCode(String commodityTypeCode)
			throws CirrasUnderwritingServiceException, ValidationException {
		SyncCommodityTypeCodeRsrc resource = new SyncCommodityTypeCodeRsrc();

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		resource.setCommodityTypeCode(commodityTypeCode);
		resource.setCropCommodityId(cropCommodityId);
		resource.setDescription("Test Code");
		resource.setIsActive(true);
		resource.setDataSyncTransDate(transactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.CommodityTypeCodeCreated);

		service.synchronizeCommodityTypeCode(resource);

	}
	
	private void createUnderwritingYear(Integer cropYear) throws CirrasUnderwritingServiceException, ValidationException {
		//CREATE Underwriting Years
		UnderwritingYearRsrc newResource = new UnderwritingYearRsrc();
		newResource.setCropYear(cropYear);
		service.createUnderwritingYear(topLevelEndpoints, newResource);
	}
	
	private static Date addDays(Date date, Integer days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, days);
		return cal.getTime();
	}}
