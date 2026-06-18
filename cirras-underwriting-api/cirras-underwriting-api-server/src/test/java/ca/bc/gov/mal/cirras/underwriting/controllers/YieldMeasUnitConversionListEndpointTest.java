package ca.bc.gov.mal.cirras.underwriting.controllers;

import java.util.ArrayList;
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

import ca.bc.gov.mal.cirras.underwriting.clients.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.clients.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.clients.ValidationException;
import ca.bc.gov.mal.cirras.underwriting.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.AnnualFieldDetailRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.ContractedFieldDetailRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.DopYieldContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.FieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.GrowerContractYearSyncRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.GrowerRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.InventoryContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.LegalLandRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.PoliciesSyncEventTypes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.PolicyRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.SyncCommodityVarietyRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.UwContractListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.UwContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.YieldMeasUnitConversionListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldFieldGrain;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldFieldRollup;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryField;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventorySeededGrain;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryUnseeded;
import ca.bc.gov.mal.cirras.underwriting.data.models.YieldMeasUnitConversion;
import ca.bc.gov.mal.cirras.underwriting.services.utils.LandManagementEventTypes;
import ca.bc.gov.mal.cirras.underwriting.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class YieldMeasUnitConversionListEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(YieldMeasUnitConversionListEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL,
		Scopes.GET_YIELD_MEAS_UNIT_CONVERSIONS,
		Scopes.SAVE_YIELD_MEAS_UNIT_CONVERSIONS,
		Scopes.UPDATE_SYNC_UNDERWRITING,
		Scopes.DELETE_SYNC_UNDERWRITING,
		Scopes.SEARCH_UWCONTRACTS,
		Scopes.SEARCH_ANNUAL_FIELDS,
		Scopes.CREATE_INVENTORY_CONTRACT,
		Scopes.DELETE_INVENTORY_CONTRACT,
		Scopes.GET_INVENTORY_CONTRACT,
		Scopes.UPDATE_INVENTORY_CONTRACT,
		Scopes.CREATE_DOP_YIELD_CONTRACT,
		Scopes.GET_DOP_YIELD_CONTRACT,
		Scopes.UPDATE_DOP_YIELD_CONTRACT,
		Scopes.DELETE_DOP_YIELD_CONTRACT,
		Scopes.PRINT_DOP_YIELD_CONTRACT,
		Scopes.CREATE_SYNC_UNDERWRITING,
		Scopes.UPDATE_SYNC_UNDERWRITING,
		Scopes.DELETE_SYNC_UNDERWRITING,
		Scopes.GET_GROWER,
		Scopes.GET_POLICY,
		Scopes.GET_LAND,
		Scopes.GET_LEGAL_LAND
	};
	
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;

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

		deleteInventoryContract(policyNumber1);
		
		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId1.toString());
		
		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId1.toString());

		service.deleteField(topLevelEndpoints, fieldId1.toString());

		service.deleteLegalLandSync(topLevelEndpoints, legalLandId1.toString());
		
		service.deleteGrowerContractYear(topLevelEndpoints, gcyId1.toString());
		service.deletePolicy(topLevelEndpoints, policyId1.toString());

		service.deleteGrower(topLevelEndpoints, growerId1.toString());
		
		YieldMeasUnitConversionListRsrc searchResults = service.getYieldMeasUnitConversions(
				topLevelEndpoints,
				insurancePlanId.toString(), 
				srcYieldMeasUnitTypeCode, 
				targetYieldMeasUnitTypeCode
		);
		
		if(searchResults != null && searchResults.getCollection().size() > 0) {
			
			YieldMeasUnitConversion yieldMeasUnitConversion = getYieldMeasUnitConversion(searchResults, cropCommodityId1);
			
			if(yieldMeasUnitConversion != null && yieldMeasUnitConversion.getYieldMeasUnitConversionGuid() != null) {
				yieldMeasUnitConversion.setDeletedByUserInd(true);

				searchResults = service.saveYieldMeasUnitConversions(
						searchResults, 
						insurancePlanId.toString(), 
						srcYieldMeasUnitTypeCode, 
						targetYieldMeasUnitTypeCode
					);

				//Make sure both versions are deleted
				yieldMeasUnitConversion = getYieldMeasUnitConversion(searchResults, cropCommodityId1);
				if(yieldMeasUnitConversion != null && yieldMeasUnitConversion.getYieldMeasUnitConversionGuid() != null) {
					yieldMeasUnitConversion.setDeletedByUserInd(true);

					service.saveYieldMeasUnitConversions(
							searchResults, 
							insurancePlanId.toString(), 
							srcYieldMeasUnitTypeCode, 
							targetYieldMeasUnitTypeCode
						);
				}
			}
			
			yieldMeasUnitConversion = getYieldMeasUnitConversion(searchResults, cropCommodityId2);
			if(yieldMeasUnitConversion != null && yieldMeasUnitConversion.getYieldMeasUnitConversionGuid() != null) {
				yieldMeasUnitConversion.setDeletedByUserInd(true);

				searchResults = service.saveYieldMeasUnitConversions(
						searchResults, 
						insurancePlanId.toString(), 
						srcYieldMeasUnitTypeCode, 
						targetYieldMeasUnitTypeCode
					);
			}
		}
		
		// delete commodity
		service.deleteSyncCommodityVariety(topLevelEndpoints, cropCommodityId1.toString());
		service.deleteSyncCommodityVariety(topLevelEndpoints, cropCommodityId2.toString());

	}
	
	public void deleteInventoryContract(String policyNumber)
			throws CirrasUnderwritingServiceException {
		
		UwContractListRsrc searchResults = getUwContracts();
		
		if ( searchResults.getCollection() != null && searchResults.getCollection().size() == 1 ) {
			UwContractRsrc referrer = searchResults.getCollection().get(0);
			
			if ( referrer.getDeclaredYieldContractGuid() != null ) {
				DopYieldContractRsrc dyc = service.getDopYieldContract(referrer);
				if ( dyc != null ) {
					service.deleteDopYieldContract(dyc);
				}
			}			
			
			if ( referrer.getInventoryContractGuid() != null ) { 
				InventoryContractRsrc invContract = service.getInventoryContract(referrer);
				service.deleteInventoryContract(invContract);
			}
		}
	}

	@Test
	public void testGetYieldMeasUnitConversions() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testGetYieldMeasUnitConversions");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		Integer insurancePlanId = 4;
		String srcYieldMeasUnitTypeCode ="TONNE";
		String targetYieldMeasUnitTypeCode ="BUSHEL";


		YieldMeasUnitConversionListRsrc searchResults = service.getYieldMeasUnitConversions(
				topLevelEndpoints,
				insurancePlanId.toString(), 
				srcYieldMeasUnitTypeCode, 
				targetYieldMeasUnitTypeCode
		);

		Assert.assertNotNull(searchResults);
		Assert.assertTrue(searchResults.getCollection().size() > 0);

		//Check if plan and units are correct
		for (YieldMeasUnitConversion yldMeasConv : searchResults.getCollection()) {
			Assert.assertEquals("InsurancePlanId", insurancePlanId, yldMeasConv.getInsurancePlanId());
			Assert.assertNotNull("CommodityName", yldMeasConv.getCommodityName());
			Assert.assertNotNull("CropCommodityId", yldMeasConv.getCropCommodityId());
			if(yldMeasConv.getYieldMeasUnitConversionGuid() != null) {
				Assert.assertEquals("SrcYieldMeasUnitTypeCode", srcYieldMeasUnitTypeCode, yldMeasConv.getSrcYieldMeasUnitTypeCode());
				Assert.assertEquals("TargetYieldMeasUnitTypeCode", targetYieldMeasUnitTypeCode, yldMeasConv.getTargetYieldMeasUnitTypeCode());
			}
		}
		
		logger.debug(">testGetYieldMeasUnitConversions");
	}
	
	private Integer insurancePlanId = 4;
	
	private String commodityName1 = "Commodity 1";
	private Integer cropCommodityId1 = 88558888;

	private String commodityName2 = "Commodity 2";
	private Integer cropCommodityId2 = 88559999;

	private String srcYieldMeasUnitTypeCode ="TONNE";
	private String targetYieldMeasUnitTypeCode ="BUSHEL";


	@Test
	public void testCreateUpdateDeleteSelectYieldMeasUnitConversions() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testCreateUpdateDeleteSelectYieldMeasUnitConversions");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		int existingRecordCount = 0;

		//Get current list
		YieldMeasUnitConversionListRsrc list = service.getYieldMeasUnitConversions(
				topLevelEndpoints, 
				insurancePlanId.toString(), 
				srcYieldMeasUnitTypeCode, 
				targetYieldMeasUnitTypeCode
				);
				
		Assert.assertNotNull(list);
		Assert.assertNotNull(list.getCollection());
		Assert.assertTrue(list.getCollection().size() > 0);

		existingRecordCount = list.getCollection().size();
		int expectedCount = existingRecordCount +1;

		//Create commodity
		createCommodity(cropCommodityId1, commodityName1);
		//createCommodity(cropCommodityId2, commodityName2);

		//Get list: 1 more record expected
		list = service.getYieldMeasUnitConversions(
				topLevelEndpoints, 
				insurancePlanId.toString(), 
				srcYieldMeasUnitTypeCode, 
				targetYieldMeasUnitTypeCode
				);
				
		Assert.assertNotNull(list);
		Assert.assertNotNull(list.getCollection());
		Assert.assertEquals(expectedCount, list.getCollection().size());

		YieldMeasUnitConversion yieldMeasUnitConversion = getYieldMeasUnitConversion(list, cropCommodityId1);
		Assert.assertNotNull(yieldMeasUnitConversion);
		Assert.assertNull(yieldMeasUnitConversion.getYieldMeasUnitConversionGuid());

		
		//1. add unit conversion without conversion factor
		//Expected: No insert

		yieldMeasUnitConversion = setYieldMeasUnitConversion(yieldMeasUnitConversion, 1, null, 2020, 9999);

		list.getCollection().add(yieldMeasUnitConversion);
		
		//Save and Fetch
		list = service.saveYieldMeasUnitConversions(
				list, 
				insurancePlanId.toString(), 
				srcYieldMeasUnitTypeCode, 
				targetYieldMeasUnitTypeCode
			);
		 
		Assert.assertNotNull(list);
		Assert.assertEquals(expectedCount, list.getCollection().size());

		//Assert commodity: Expect null
		yieldMeasUnitConversion = getYieldMeasUnitConversion(list, cropCommodityId1);
		Assert.assertNotNull(yieldMeasUnitConversion);
		Assert.assertNull(yieldMeasUnitConversion.getYieldMeasUnitConversionGuid());
		
		//Add conversion factor to yieldMeasUnitConversion
		Double conversionFactor = 11.2;
		yieldMeasUnitConversion = setYieldMeasUnitConversion(yieldMeasUnitConversion, 1, conversionFactor, 2020, 9999);

		yieldMeasUnitConversion.setConversionFactor(conversionFactor);
		
		//Save again
		list = service.saveYieldMeasUnitConversions(
				list, 
				insurancePlanId.toString(), 
				srcYieldMeasUnitTypeCode, 
				targetYieldMeasUnitTypeCode
			);
		 
		Assert.assertNotNull(list);
		Assert.assertEquals(expectedCount, list.getCollection().size());	
		
		//Assert commodity: Expect conversionFactor
		yieldMeasUnitConversion = getYieldMeasUnitConversion(list, cropCommodityId1);
		Assert.assertNotNull(yieldMeasUnitConversion);
		Assert.assertNotNull(yieldMeasUnitConversion.getYieldMeasUnitConversionGuid());
		Assert.assertEquals(conversionFactor, yieldMeasUnitConversion.getConversionFactor());	
		Assert.assertEquals("CropCommodityId", cropCommodityId1, yieldMeasUnitConversion.getCropCommodityId());
		Assert.assertEquals("SrcYieldMeasUnitTypeCode", srcYieldMeasUnitTypeCode, yieldMeasUnitConversion.getSrcYieldMeasUnitTypeCode());
		Assert.assertEquals("TargetYieldMeasUnitTypeCode", targetYieldMeasUnitTypeCode, yieldMeasUnitConversion.getTargetYieldMeasUnitTypeCode());
		Assert.assertEquals("VersionNumber", 1, yieldMeasUnitConversion.getVersionNumber().intValue());
		Assert.assertEquals("ConversionFactor", conversionFactor, yieldMeasUnitConversion.getConversionFactor());
		Assert.assertEquals("EffectiveCropYear", 2020, yieldMeasUnitConversion.getEffectiveCropYear().intValue());
		Assert.assertEquals("ExpiryCropYear", 9999, yieldMeasUnitConversion.getExpiryCropYear().intValue());
		
		//Update yieldMeasUnitConversion with new conversion factor
		conversionFactor = 12.8;
		yieldMeasUnitConversion.setConversionFactor(conversionFactor);
		yieldMeasUnitConversion.setEffectiveCropYear(2011);
		yieldMeasUnitConversion.setExpiryCropYear(9998);
		
		//Save again
		list = service.saveYieldMeasUnitConversions(
				list, 
				insurancePlanId.toString(), 
				srcYieldMeasUnitTypeCode, 
				targetYieldMeasUnitTypeCode
			);
		 
		Assert.assertNotNull(list);
		Assert.assertEquals(expectedCount, list.getCollection().size());	
		
		//Assert new conversion factor and updated effective and expiry date
		yieldMeasUnitConversion = getYieldMeasUnitConversion(list, cropCommodityId1);
		Assert.assertNotNull(yieldMeasUnitConversion);
		Assert.assertEquals(conversionFactor, yieldMeasUnitConversion.getConversionFactor());
		Assert.assertEquals(1,  yieldMeasUnitConversion.getVersionNumber().intValue());
		Assert.assertEquals(2011,  yieldMeasUnitConversion.getEffectiveCropYear().intValue());
		Assert.assertEquals(9998,  yieldMeasUnitConversion.getExpiryCropYear().intValue());
		
		//Set conversion factor to NULL: Expect error
		try {
			conversionFactor = null;
			yieldMeasUnitConversion.setConversionFactor(conversionFactor);
			
			//Save again
			list = service.saveYieldMeasUnitConversions(
					list, 
					insurancePlanId.toString(), 
					srcYieldMeasUnitTypeCode, 
					targetYieldMeasUnitTypeCode
				);

			Assert.fail("Attempt to set conversion factor to null which is not allowed but didn't throw an error");
		} catch (CirrasUnderwritingServiceException e) {
			//Reset it
			conversionFactor = 12.8;
			yieldMeasUnitConversion.setConversionFactor(conversionFactor);

			Assert.assertTrue(e.getMessage().contains("Conversion Factor can't be null for an existing conversion. Commodity: " + yieldMeasUnitConversion.getCommodityName()));
		}
		
		//Add second version for same commodity
		YieldMeasUnitConversion newVersion = new YieldMeasUnitConversion();
		conversionFactor = 15.0;
		newVersion = setYieldMeasUnitConversion(newVersion, 2, conversionFactor, 2021, 9999);
		newVersion.setCropCommodityId(cropCommodityId1);
		list.getCollection().add(newVersion);

		//Save New Version
		list = service.saveYieldMeasUnitConversions(
				list, 
				insurancePlanId.toString(), 
				srcYieldMeasUnitTypeCode, 
				targetYieldMeasUnitTypeCode
			);

		Assert.assertNotNull(list);
		Assert.assertEquals(expectedCount, list.getCollection().size());	
		
		//Assert new conversion factor
		yieldMeasUnitConversion = getYieldMeasUnitConversion(list, cropCommodityId1);
		Assert.assertNotNull(yieldMeasUnitConversion);
		Assert.assertEquals(conversionFactor, yieldMeasUnitConversion.getConversionFactor());
		Assert.assertEquals(2,  yieldMeasUnitConversion.getVersionNumber().intValue());

		//Delete version 2
		yieldMeasUnitConversion.setDeletedByUserInd(true);
		
		//Save New Version
		list = service.saveYieldMeasUnitConversions(
				list, 
				insurancePlanId.toString(), 
				srcYieldMeasUnitTypeCode, 
				targetYieldMeasUnitTypeCode
			);

		Assert.assertNotNull(list);
		Assert.assertEquals(expectedCount, list.getCollection().size());	
		
		//Assert: Expect version 1 again
		yieldMeasUnitConversion = getYieldMeasUnitConversion(list, cropCommodityId1);
		Assert.assertNotNull(yieldMeasUnitConversion);
		Assert.assertEquals(1,  yieldMeasUnitConversion.getVersionNumber().intValue());

		//delete all
		delete();
		
		//Check if all created grade modifiers are deleted
		YieldMeasUnitConversionListRsrc searchResults = service.getYieldMeasUnitConversions(
				topLevelEndpoints, 
				insurancePlanId.toString(), 
				srcYieldMeasUnitTypeCode, 
				targetYieldMeasUnitTypeCode
			);
		Assert.assertNotNull(searchResults);
		Assert.assertEquals(existingRecordCount, searchResults.getCollection().size());


		logger.debug(">testCreateUpdateDeleteSelectYieldMeasUnitConversions");
	}
	
	private Integer growerId1 = 90000011;
	private Integer policyId1 = 90000012;
	private Integer gcyId1 = 90000013;
	private Integer contractId1 = 90000014;
	private String policyNumber1 = "998865-21";
	private String contractNumber1 = "998865";
	private Integer cropYear1 = 2014;

	private Integer legalLandId1 = 90000015;
	private Integer fieldId1 = 90000016;
	private Integer annualFieldDetailId1 = 90000017;
	private Integer contractedFieldDetailId1 = 90000018;
	
	private String inventoryFieldGuid1 = null;
	private String inventoryFieldGuid2 = null;
	private String inventoryFieldGuid3 = null;
	
	private double commodity1Yield1 = 1000;
	private double commodity1Yield2 = 10000;
	private double commodity2Yield1 = 5000;
	private double commodity1ConversionFactor1 = 1.5;
	private double commodity1ConversionFactor2 = 3.0;
	private double commodity2ConversionFactor1 = 2.0;
	private double commodity2ConversionFactor2 = 4.0;

	@Test
	public void testRecalculation() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		//Test insert/update and calculate yield field rollup
		logger.debug("<testRecalculation");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//Create commodity
		createCommodity(cropCommodityId1, commodityName1);
		createCommodity(cropCommodityId2, commodityName2);
		
		//Add new conversion factors for barley and canola conversion factors
		YieldMeasUnitConversionListRsrc list = service.getYieldMeasUnitConversions(
				topLevelEndpoints, 
				insurancePlanId.toString(), 
				srcYieldMeasUnitTypeCode, 
				targetYieldMeasUnitTypeCode
				);
				
		Assert.assertNotNull(list);
		Assert.assertNotNull(list.getCollection());

		YieldMeasUnitConversion yieldMeasUnitConversion1 = getYieldMeasUnitConversion(list, cropCommodityId1);
		Assert.assertNotNull(yieldMeasUnitConversion1);
		
		//Add unit conversion
		yieldMeasUnitConversion1 = setYieldMeasUnitConversion(yieldMeasUnitConversion1, 1, commodity1ConversionFactor1, 2000, 9999);

		YieldMeasUnitConversion yieldMeasUnitConversion2 = getYieldMeasUnitConversion(list, cropCommodityId2);
		Assert.assertNotNull(yieldMeasUnitConversion2);
		
		//Add unit conversion
		yieldMeasUnitConversion2 = setYieldMeasUnitConversion(yieldMeasUnitConversion2, 1, commodity2ConversionFactor1, 2000, 9999);
		
		//Save and Fetch
		list = service.saveYieldMeasUnitConversions(
				list, 
				insurancePlanId.toString(), 
				srcYieldMeasUnitTypeCode, 
				targetYieldMeasUnitTypeCode
			);
		 
		Assert.assertNotNull(list);
		
		createGrower();
		createPolicy();
		createGrowerContractYear();

		createLegalLand();
		createField();
		createAnnualFieldDetail();
		createContractedFieldDetail();
		
		createInventoryContract();
		
		createDopData();
		
		//Check conversions
		checkConversions(commodity1ConversionFactor1, commodity2ConversionFactor1);
		
		//Update conversion factors for commodity 1 and 2
		list = service.getYieldMeasUnitConversions(
				topLevelEndpoints, 
				insurancePlanId.toString(), 
				srcYieldMeasUnitTypeCode, 
				targetYieldMeasUnitTypeCode
				);
				
		Assert.assertNotNull(list);
		Assert.assertNotNull(list.getCollection());

		yieldMeasUnitConversion1 = getYieldMeasUnitConversion(list, cropCommodityId1);
		Assert.assertNotNull(yieldMeasUnitConversion1);
		yieldMeasUnitConversion1.setConversionFactor(commodity1ConversionFactor2);

		yieldMeasUnitConversion2 = getYieldMeasUnitConversion(list, cropCommodityId2);
		Assert.assertNotNull(yieldMeasUnitConversion2);
		yieldMeasUnitConversion2.setConversionFactor(commodity2ConversionFactor2);
		
		//Save and Fetch
		list = service.saveYieldMeasUnitConversions(
				list, 
				insurancePlanId.toString(), 
				srcYieldMeasUnitTypeCode, 
				targetYieldMeasUnitTypeCode
			);
		 
		Assert.assertNotNull(list);

		//fetch dop data and compare calculations
		checkConversions(commodity1ConversionFactor2, commodity2ConversionFactor2);

		
		//fetch dop data and set entered units to tonnes
		UwContractListRsrc searchResults = getUwContracts();
		
		UwContractRsrc referrer = searchResults.getCollection().get(0);
		DopYieldContractRsrc dopYieldContractRsrc = service.getDopYieldContract(referrer);

		dopYieldContractRsrc.setEnteredYieldMeasUnitTypeCode("TONNE");
		
		DopYieldContractRsrc fetchedDyc = service.updateDopYieldContract(dopYieldContractRsrc);
		Assert.assertEquals(dopYieldContractRsrc.getEnteredYieldMeasUnitTypeCode(), fetchedDyc.getEnteredYieldMeasUnitTypeCode());

		//Update conversion factors for commodity 1 and 2
		list = service.getYieldMeasUnitConversions(
				topLevelEndpoints, 
				insurancePlanId.toString(), 
				srcYieldMeasUnitTypeCode, 
				targetYieldMeasUnitTypeCode
				);
				
		Assert.assertNotNull(list);
		Assert.assertNotNull(list.getCollection());

		yieldMeasUnitConversion1 = getYieldMeasUnitConversion(list, cropCommodityId1);
		Assert.assertNotNull(yieldMeasUnitConversion1);
		yieldMeasUnitConversion1.setConversionFactor(commodity1ConversionFactor1);

		yieldMeasUnitConversion2 = getYieldMeasUnitConversion(list, cropCommodityId2);
		Assert.assertNotNull(yieldMeasUnitConversion2);
		yieldMeasUnitConversion2.setConversionFactor(commodity2ConversionFactor1);

		//Save and Fetch
		list = service.saveYieldMeasUnitConversions(
				list, 
				insurancePlanId.toString(), 
				srcYieldMeasUnitTypeCode, 
				targetYieldMeasUnitTypeCode
			);

		//Check field rollup estimated bushel conversion
		searchResults = getUwContracts();
		
		referrer = searchResults.getCollection().get(0);
		dopYieldContractRsrc = service.getDopYieldContract(referrer);
		
		// estimated_yield_per_acre_bushels based on estimated_yield_per_acre_tonnes
		if(dopYieldContractRsrc.getDopYieldFieldRollupList() != null && dopYieldContractRsrc.getDopYieldFieldRollupList().size() > 0) {
			for (DopYieldFieldRollup rollup : dopYieldContractRsrc.getDopYieldFieldRollupList()) {
				double conversionFactor = commodity1ConversionFactor1;
				if(rollup.getCropCommodityId().equals(cropCommodityId2)) {
					conversionFactor = commodity2ConversionFactor1;
				} 
				double dblCalculatedValue = rollup.getEstimatedYieldPerAcreTonnes() * conversionFactor;
				Assert.assertEquals(dblCalculatedValue, rollup.getEstimatedYieldPerAcreBushels().doubleValue(), delta);
			}
		} else {
			Assert.fail("No dop yield field rollups found 2");
		}
		
		
		delete();
		
		logger.debug(">testRecalculation");

	}
	
	double delta = 0.0001; //Allow rounding differences

	private void checkConversions(double conversionFactor1, double conversionFactor2) throws CirrasUnderwritingServiceException {
		//fetch dop data and compare calculations
		UwContractListRsrc searchResults = getUwContracts();
		
		UwContractRsrc referrer = searchResults.getCollection().get(0);
		DopYieldContractRsrc dopYieldContractRsrc = service.getDopYieldContract(referrer);
		
		//dop field
		//estimated_yield_per_acre_default_unit based on estimated_yield_per_acre
		if(dopYieldContractRsrc != null && dopYieldContractRsrc.getFields().get(0).getDopYieldFieldGrainList().size() > 0) {
			for (DopYieldFieldGrain dopYieldField : dopYieldContractRsrc.getFields().get(0).getDopYieldFieldGrainList()) {
				double conversionFactor = conversionFactor1;
				if(dopYieldField.getCropCommodityId().equals(cropCommodityId2)) {
					conversionFactor = conversionFactor2;
				} 
				double dblCalculatedValue = dopYieldField.getEstimatedYieldPerAcre() / conversionFactor;
				Assert.assertEquals(dblCalculatedValue, dopYieldField.getEstimatedYieldPerAcreDefaultUnit().doubleValue(), delta);
			}
		} else {
			Assert.fail("No dop fields found");
		}
		
		//dop contract
		//stored_yield_default_unit based on stored_yield
		//sold_yield_default_unit based on sold_yield
		if(dopYieldContractRsrc.getDopYieldContractCommodities() != null && dopYieldContractRsrc.getDopYieldContractCommodities().size() > 0) {
			for (DopYieldContractCommodity cmdty : dopYieldContractRsrc.getDopYieldContractCommodities()) {
				double conversionFactor = conversionFactor1;
				if(cmdty.getCropCommodityId().equals(cropCommodityId2)) {
					conversionFactor = conversionFactor2;
				} 
				double dblCalculatedValue = cmdty.getStoredYield() / conversionFactor;
				Assert.assertEquals(dblCalculatedValue, cmdty.getStoredYieldDefaultUnit().doubleValue(), delta);
				dblCalculatedValue = cmdty.getSoldYield() / conversionFactor;
				Assert.assertEquals(dblCalculatedValue, cmdty.getSoldYieldDefaultUnit().doubleValue(), delta);
			}
		} else {
			Assert.fail("No contract commodities found");
		}
		
		//dop field rollup
		// estimated_yield_per_acre_tonnes based on estimated_yield_per_acre_bushels
		if(dopYieldContractRsrc.getDopYieldFieldRollupList() != null && dopYieldContractRsrc.getDopYieldFieldRollupList().size() > 0) {
			for (DopYieldFieldRollup rollup : dopYieldContractRsrc.getDopYieldFieldRollupList()) {
				double conversionFactor = conversionFactor1;
				if(rollup.getCropCommodityId().equals(cropCommodityId2)) {
					conversionFactor = conversionFactor2;
				} 
				double dblCalculatedValue = rollup.getEstimatedYieldPerAcreBushels() / conversionFactor;
				Assert.assertEquals(dblCalculatedValue, rollup.getEstimatedYieldPerAcreTonnes().doubleValue(), delta);
			}
		} else {
			Assert.fail("No dop yield field rollups found");
		}

	}

	protected void createDopData() throws CirrasUnderwritingServiceException, ValidationException {
		// Dop Date
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2022, Calendar.JANUARY, 15);
		Date dopDate = cal.getTime();
		
		UwContractListRsrc searchResults = getUwContracts();
		
		UwContractRsrc referrer = searchResults.getCollection().get(0);
		Assert.assertNotNull(referrer.getInventoryContractGuid());
		Assert.assertNull(referrer.getDeclaredYieldContractGuid());

		DopYieldContractRsrc newDyc = service.rolloverDopYieldContract(referrer);

		Assert.assertEquals(contractId1, newDyc.getContractId());
		Assert.assertEquals(cropYear1, newDyc.getCropYear());
		Assert.assertNull(newDyc.getDeclaredYieldContractGuid());
		
		newDyc.setDeclarationOfProductionDate(dopDate);
		newDyc.setDefaultYieldMeasUnitTypeCode("TONNE");
		newDyc.setEnteredYieldMeasUnitTypeCode("BUSHEL");
		newDyc.setGrainFromOtherSourceInd(true);
		newDyc.setInsurancePlanId(4);

		DopYieldFieldGrain dyfCommodity1a = getDopYieldField(newDyc.getFields().get(0).getDopYieldFieldGrainList(), inventoryFieldGuid1);
		DopYieldFieldGrain dyfCommodity1b = getDopYieldField(newDyc.getFields().get(0).getDopYieldFieldGrainList(), inventoryFieldGuid2);
		DopYieldFieldGrain dyfCommodity2 = getDopYieldField(newDyc.getFields().get(0).getDopYieldFieldGrainList(), inventoryFieldGuid3);
		Assert.assertNotNull(dyfCommodity1a);
		Assert.assertNotNull(dyfCommodity1b);
		Assert.assertNotNull(dyfCommodity2);
		
		dyfCommodity1a.setEstimatedYieldPerAcre(commodity1Yield1);
		dyfCommodity1a.setEstimatedYieldPerAcreDefaultUnit(null); // Calculated by backend
		dyfCommodity1a.setUnharvestedAcresInd(true);
		
		dyfCommodity1b.setEstimatedYieldPerAcre(commodity1Yield2);
		dyfCommodity1b.setEstimatedYieldPerAcreDefaultUnit(null); // Calculated by backend
		dyfCommodity1b.setUnharvestedAcresInd(true);
		
		dyfCommodity2.setEstimatedYieldPerAcre(commodity2Yield1);
		dyfCommodity2.setEstimatedYieldPerAcreDefaultUnit(null); // Calculated by backend
		dyfCommodity2.setUnharvestedAcresInd(true);
		
		createDopYieldCommodities(newDyc);
		
		DopYieldContractRsrc fetchedDyc = service.createDopYieldContract(topLevelEndpoints, newDyc);
		
	}

	protected UwContractListRsrc getUwContracts() throws CirrasUnderwritingServiceException {
		
		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(20);

		UwContractListRsrc searchResults = service.getUwContractList(
				topLevelEndpoints, 
				null, 
				null, 
				null,
				null,
				policyNumber1,
				null,
				null, 
				null, 
				null, 
				pageNumber, pageRowCount);
		
		Assert.assertNotNull(searchResults);

		return searchResults;
	}
	
	private void createGrower() throws ValidationException, CirrasUnderwritingServiceException {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		//Date createTransactionDate = addSeconds(transactionDate, -1);

		GrowerRsrc resource = new GrowerRsrc();
		
		resource.setGrowerId(growerId1);
		resource.setGrowerNumber(999888);
		resource.setGrowerName("grower test name");
		resource.setGrowerAddressLine1("address line 1");
		resource.setGrowerAddressLine2("address line 2");
		resource.setGrowerPostalCode("V8P 4N8");
		resource.setGrowerCity("Victoria");
		resource.setCityId(1);
		resource.setGrowerProvince("BC");
		resource.setDataSyncTransDate(transactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.GrowerCreated);

		service.synchronizeGrower(resource);
		
	}
	
	private void createPolicy() throws ValidationException, CirrasUnderwritingServiceException {

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		PolicyRsrc resource = new PolicyRsrc();
		
		resource.setPolicyId(policyId1);
		resource.setGrowerId(growerId1);
		resource.setInsurancePlanId(4);
		resource.setPolicyStatusCode("ACTIVE");
		resource.setOfficeId(1);
		resource.setPolicyNumber(policyNumber1);
		resource.setContractNumber(contractNumber1);
		resource.setContractId(contractId1);
		resource.setCropYear(cropYear1);
		
		resource.setDataSyncTransDate(transactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.PolicyCreated);

		service.synchronizePolicy(resource);
	}
	
	private void createGrowerContractYear() throws ValidationException, CirrasUnderwritingServiceException {

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();
		
		GrowerContractYearSyncRsrc resource = new GrowerContractYearSyncRsrc();
		
		resource.setGrowerContractYearId(gcyId1);
		resource.setContractId(contractId1);
		resource.setGrowerId(growerId1);
		resource.setInsurancePlanId(4);
		resource.setCropYear(cropYear1);
		resource.setDataSyncTransDate(transactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.GrowerContractYearCreated);

		service.synchronizeGrowerContractYear(resource);
		
	}

	private void createLegalLand() throws CirrasUnderwritingServiceException, ValidationException {

		//CREATE LegalLand
		LegalLandRsrc resource = new LegalLandRsrc();
		
		resource.setLegalLandId(legalLandId1);
		resource.setPrimaryPropertyIdentifier("GF0099999");
		resource.setPrimaryReferenceTypeCode("OTHER");
		resource.setLegalDescription(null);
		resource.setLegalShortDescription(null);
		resource.setOtherDescription("TEST LEGAL LOC 123");
		resource.setActiveFromCropYear(2011);
		resource.setActiveToCropYear(2022);
		resource.setTransactionType(LandManagementEventTypes.LegalLandCreated);
		
		service.synchronizeLegalLand(resource);

	}
	
	private void createField() throws CirrasUnderwritingServiceException, ValidationException {

		FieldRsrc resource = new FieldRsrc();
		
		resource.setFieldId(fieldId1);
		resource.setFieldLabel("Field Label");
		resource.setActiveFromCropYear(2011);
		resource.setActiveToCropYear(2022);
		resource.setTransactionType(LandManagementEventTypes.FieldCreated);
		
		service.synchronizeField(resource);
	}
	
	private void createAnnualFieldDetail() throws CirrasUnderwritingServiceException, ValidationException {

		AnnualFieldDetailRsrc resource = new AnnualFieldDetailRsrc();
		
		resource.setAnnualFieldDetailId(annualFieldDetailId1);
		resource.setLegalLandId(legalLandId1);
		resource.setFieldId(fieldId1);
		resource.setCropYear(cropYear1);
		resource.setTransactionType(LandManagementEventTypes.AnnualFieldDetailCreated);
		
		service.synchronizeAnnualFieldDetail(resource);
	}

	private void createContractedFieldDetail() throws CirrasUnderwritingServiceException, ValidationException {

		ContractedFieldDetailRsrc resource = new ContractedFieldDetailRsrc();
		
		resource.setContractedFieldDetailId(contractedFieldDetailId1);
		resource.setAnnualFieldDetailId(annualFieldDetailId1);
		resource.setGrowerContractYearId(gcyId1);
		resource.setDisplayOrder(1);
		resource.setTransactionType(LandManagementEventTypes.ContractedFieldDetailCreated);
	
		service.synchronizeContractedFieldDetail(resource);

	}
	
	
	private void createInventoryContract() throws ValidationException, CirrasUnderwritingServiceException {

		UwContractListRsrc searchResults = getUwContracts();
		
		UwContractRsrc referrer = searchResults.getCollection().get(0);
		Assert.assertNull(referrer.getInventoryContractGuid());

		InventoryContractRsrc resource = service.rolloverInventoryContract(referrer);
		
		resource.setFertilizerInd(false);
		resource.setGrainFromPrevYearInd(true);
		resource.setHerbicideInd(false);
		resource.setInventoryContractGuid(null);
		resource.setOtherChangesComment("other changes comment");
		resource.setOtherChangesInd(true);
		resource.setSeededCropReportSubmittedInd(false);
		resource.setTilliageInd(false);
		resource.setUnseededIntentionsSubmittedInd(false);

		//Add barley 20 acres
		List<InventorySeededGrain> isgList = createSeededInventory(cropCommodityId1, (double)20);
		resource.getFields().get(0).getPlantings().get(0).setInventorySeededGrains(isgList);

		//Add barley 100 acres
		InventoryField invf = createInventoryField(cropCommodityId1, (double)100, 2);
		resource.getFields().get(0).getPlantings().add(invf);

		//Add canola 1000 acres
		invf = createInventoryField(cropCommodityId2, (double)1000, 3);
		resource.getFields().get(0).getPlantings().add(invf);

		//Create inventory contract commodities
		List<InventoryContractCommodity> iccList = new ArrayList<InventoryContractCommodity>();
		iccList.add(createInventoryContractCommodity(cropCommodityId1, "Commodity 1", (double)125, false));
		iccList.add(createInventoryContractCommodity(cropCommodityId2, "Commodity 2", (double)1000, false));
		
		resource.setCommodities(iccList);
		
		resource = service.createInventoryContract(topLevelEndpoints, resource);
		
		inventoryFieldGuid1 = resource.getFields().get(0).getPlantings().get(0).getInventoryFieldGuid();
		inventoryFieldGuid2 = resource.getFields().get(0).getPlantings().get(1).getInventoryFieldGuid();
		inventoryFieldGuid3 = resource.getFields().get(0).getPlantings().get(2).getInventoryFieldGuid();
	}

	protected InventoryField createInventoryField(
			Integer cropId,
			Double seededAcres,
			Integer plantingNumber) {

		InventoryUnseeded iu = new InventoryUnseeded();
		iu.setAcresToBeSeeded(null);
		iu.setCropCommodityId(null);
		iu.setIsUnseededInsurableInd(false);
		
		List<InventorySeededGrain> isgList = createSeededInventory(cropId, seededAcres);

		InventoryField invf = new InventoryField();
		invf.setCropYear(cropYear1);
		invf.setFieldId(fieldId1);
		invf.setInsurancePlanId(4);
		invf.setInventorySeededGrains(isgList);
		invf.setInventoryUnseeded(iu);
		invf.setIsHiddenOnPrintoutInd(false);
		invf.setLastYearCropCommodityId(null);
		invf.setLastYearCropVarietyId(null);
		invf.setPlantingNumber(plantingNumber);
		invf.setUnderseededAcres(null);
		invf.setUnderseededCropVarietyId(null);
		return invf;
	}

	protected List<InventorySeededGrain> createSeededInventory(
			Integer cropId,
			Double seededAcres
			) {
		List<InventorySeededGrain> isgList = new ArrayList<InventorySeededGrain>();
		
		InventorySeededGrain isg = new InventorySeededGrain();
		isg.setCommodityTypeCode(null);
		isg.setCropCommodityId(cropId);
		isg.setCropVarietyId(null);
		isg.setIsPedigreeInd(false);
		isg.setIsQuantityInsurableInd(true);
		isg.setIsReplacedInd(false);
		isg.setIsSpotLossInsurableInd(false);
		isg.setSeededAcres(seededAcres);
		isg.setSeededDate(null);
		
		isgList.add(isg);
		
		 return isgList;
	}
	
	private InventoryContractCommodity createInventoryContractCommodity(Integer cropId, String cropName,
			Double totalSeededAcres, Boolean isPedigreeInd) {
		//InventoryContractCommodity
		InventoryContractCommodity invContractCommodity = new InventoryContractCommodity();
		invContractCommodity.setCropCommodityId(cropId);
		invContractCommodity.setCropCommodityName(cropName);
		invContractCommodity.setInventoryContractGuid(null);
		invContractCommodity.setInventoryContractCommodityGuid(null);
		invContractCommodity.setTotalSeededAcres(totalSeededAcres);
		invContractCommodity.setTotalSpotLossAcres(0.0);
		invContractCommodity.setTotalUnseededAcres(10.0);
		invContractCommodity.setTotalUnseededAcresOverride(56.78);
		invContractCommodity.setIsPedigreeInd(isPedigreeInd);
		
		return invContractCommodity;
	}
	
	
	private YieldMeasUnitConversion setYieldMeasUnitConversion(
			YieldMeasUnitConversion model,
			Integer versionNumber, 
			Double conversionFactor,
			Integer effectiveCropYear,
			Integer expiryCropYear) throws DaoException {
		
		//model.setCropCommodityId(cropCommodityId);
		model.setSrcYieldMeasUnitTypeCode(srcYieldMeasUnitTypeCode);
		model.setTargetYieldMeasUnitTypeCode(targetYieldMeasUnitTypeCode);
		model.setVersionNumber(versionNumber);
		model.setConversionFactor(conversionFactor);
		model.setEffectiveCropYear(effectiveCropYear);
		model.setExpiryCropYear(expiryCropYear);

		return model;
	}
	
	private YieldMeasUnitConversion getYieldMeasUnitConversion(YieldMeasUnitConversionListRsrc results, Integer cropCommodityId) {
		
		if(results != null && results.getCollection().size() > 0) {
			List<YieldMeasUnitConversion> resources = results.getCollection().stream()
					.filter(x -> x.getCropCommodityId().equals(cropCommodityId))
					.collect(Collectors.toList());
			
			if(resources != null && resources.size() > 0) {
				return resources.get(0);
			}
		}
		
		return null;
	}	
	
	private DopYieldFieldGrain getDopYieldField(List<DopYieldFieldGrain> results, String inventoryFieldGuid) {
		
		if(results != null && results.size() > 0) {
			
			for (DopYieldFieldGrain dopYieldField : results) {
				if(dopYieldField.getInventoryFieldGuid().equals(inventoryFieldGuid)) {
					return dopYieldField;
				}
			}
		}
		
		return null;
	}
	
	private void createDopYieldCommodities(DopYieldContractRsrc newDyc) {

		//		Entered Yield Meas Unit Type = BUSHEL
		//		Default  Yield Meas Unit Type = TONNE
		
		//Barley
		DopYieldContractCommodity dycc = getDopYieldContractCommodity(cropCommodityId1, newDyc.getDopYieldContractCommodities());
		if(dycc != null) {
			dycc.setHarvestedAcres((double)100);
			dycc.setStoredYield((double)10000);
			dycc.setStoredYieldDefaultUnit(null); //calculated in the backend
			dycc.setSoldYield((double)1000);
			dycc.setSoldYieldDefaultUnit(null); //calculated in the backend
			dycc.setGradeModifierTypeCode(null);
		}
		
		//Canola
		dycc = getDopYieldContractCommodity(cropCommodityId2, newDyc.getDopYieldContractCommodities());
		if(dycc != null) {
			dycc.setHarvestedAcres((double)100);
			dycc.setStoredYield((double)10000);
			dycc.setStoredYieldDefaultUnit(null); //calculated in the backend
			dycc.setSoldYield((double)1000);
			dycc.setSoldYieldDefaultUnit(null);
			dycc.setGradeModifierTypeCode(null);
		}
		
	}
	
	private DopYieldContractCommodity getDopYieldContractCommodity(Integer cropCommodityId, List<DopYieldContractCommodity> dyccList) {
		
		DopYieldContractCommodity dycc = null;
		
		List<DopYieldContractCommodity> dyccFiltered = dyccList.stream()
				.filter(x -> x.getCropCommodityId().equals(cropCommodityId))
				.collect(Collectors.toList());
		
		if (dyccFiltered != null) {
			dycc = dyccFiltered.get(0);
		}
		return dycc;
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
}
