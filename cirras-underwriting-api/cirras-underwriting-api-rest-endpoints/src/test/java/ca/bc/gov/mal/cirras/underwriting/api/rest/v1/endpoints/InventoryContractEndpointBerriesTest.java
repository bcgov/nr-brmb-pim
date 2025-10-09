package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.InventoryServiceEnums.InventoryReportType;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.LandManagementEventTypes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.PoliciesSyncEventTypes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.ValidationException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldDetailRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ContractedFieldDetailRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.FieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GrowerContractYearSyncRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GrowerRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.InventoryContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.PolicyRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UwContractListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UwContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryBerries;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryContractCommodityBerries;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryCoverageTotalForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventorySeededForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryUnseeded;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.common.wfone.rest.resource.RelLink;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class InventoryContractEndpointBerriesTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(InventoryContractEndpointBerriesTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.SEARCH_UWCONTRACTS,
		Scopes.SEARCH_ANNUAL_FIELDS,
		Scopes.CREATE_INVENTORY_CONTRACT,
		Scopes.DELETE_INVENTORY_CONTRACT,
		Scopes.GET_INVENTORY_CONTRACT,
		Scopes.UPDATE_INVENTORY_CONTRACT,
		Scopes.PRINT_INVENTORY_CONTRACT,
		Scopes.CREATE_SYNC_UNDERWRITING,
		Scopes.UPDATE_SYNC_UNDERWRITING,
		Scopes.DELETE_SYNC_UNDERWRITING,
		Scopes.GET_GROWER,
		Scopes.GET_POLICY,
		Scopes.GET_LAND,
		Scopes.GET_LEGAL_LAND,
		Scopes.CREATE_DOP_YIELD_CONTRACT
	};
	
	private Integer growerId1 = 90000011;
	private Integer policyId1 = 90000012;
	private Integer gcyId1 = 90000013;
	private Integer contractId1 = 90000014;
	private String policyNumber1 = "998891-21";
	private String contractNumber1 = "998891";
	private Integer cropYear1 = 2021;

	private Integer legalLandId1 = 90000015;
	private Integer fieldId1 = 90000016;
	private Integer annualFieldDetailId1 = 90000017;
	private Integer contractedFieldDetailId1 = 90000018;
	
	private String fieldLocation = "Field Location";
		
	private String inventoryFieldGuid1 = null;
	
	private Integer insurancePlanId = 3; //Berries
	
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;

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

	
	private void delete() throws CirrasUnderwritingServiceException {

		Integer pageNumber = 1;
		Integer pageRowCount = 20;
		
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
		
		if ( searchResults.getCollection() != null && searchResults.getCollection().size() == 1 ) {
			UwContractRsrc referrer = searchResults.getCollection().get(0);
			
			if ( referrer.getInventoryContractGuid() != null ) { 
				InventoryContractRsrc invContract = service.getInventoryContract(referrer);
				service.deleteInventoryContract(invContract);
			}
		}

		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId1.toString());
		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId1.toString());
		service.deleteField(topLevelEndpoints, fieldId1.toString());
		service.deleteLegalLandSync(topLevelEndpoints, legalLandId1.toString());
		
		service.deleteGrowerContractYear(topLevelEndpoints, gcyId1.toString());
		service.deletePolicy(topLevelEndpoints, policyId1.toString());
		service.deleteGrower(topLevelEndpoints, growerId1.toString());
	}

	
	@Test
	public void testInsertUpdateDeleteInventoryBerries() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testInsertUpdateDeleteInventoryBerries");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		createGrower();
		createPolicy();
		createGrowerContractYear();

		createLegalLand();
		createField();
		createAnnualFieldDetail();
		createContractedFieldDetail(false);
		
		UwContractRsrc referrer = getUwContract(policyNumber1, service, topLevelEndpoints);
		Assert.assertNull(referrer.getInventoryContractGuid());
		
		InventoryContractRsrc invContract = service.rolloverInventoryContract(referrer);
		Assert.assertNotNull(invContract);
		Assert.assertNotNull(invContract.getFields());
		Assert.assertNotNull(invContract.getFields().get(0).getPlantings());
		Assert.assertNotNull(invContract.getFields().get(0).getPlantings().get(0).getInventoryBerries());
		
		InventoryBerries newBerries = invContract.getFields().get(0).getPlantings().get(0).getInventoryBerries();
		Assert.assertNull("InventoryBerriesGuid", newBerries.getInventoryBerriesGuid());
		Assert.assertNull("InventoryFieldGuid", newBerries.getInventoryFieldGuid());
		Assert.assertNull("CropCommodityId", newBerries.getCropCommodityId());
		Assert.assertNull("CropVarietyId", newBerries.getCropVarietyId());
		Assert.assertNull("PlantedYear", newBerries.getPlantedYear());
		Assert.assertNull("PlantedAcres", newBerries.getPlantedAcres());
		Assert.assertNull("RowSpacing", newBerries.getRowSpacing());
		Assert.assertNull("PlantSpacing", newBerries.getPlantSpacing());
		Assert.assertNull("TotalPlants", newBerries.getTotalPlants());
		Assert.assertNull("IsQuantityInsurableInd", newBerries.getIsQuantityInsurableInd());
		Assert.assertNull("IsPlantInsurableInd", newBerries.getIsPlantInsurableInd());
		//TODO Check new field data
		AnnualFieldRsrc field = invContract.getFields().get(0);
		//Assert.assertEquals("FieldLocation", fieldLocation, field.getFieldLocation());
		//Assert.assertEquals("IsLeased", false, field.getIsLeasedInd());

		newBerries.setCropCommodityId(10);
		newBerries.setCropCommodityName("BLUEBERRY");
		newBerries.setCropVarietyId(1010689);
		newBerries.setCropVarietyName("BLUEJAY");
		newBerries.setPlantedYear(2020);
		newBerries.setPlantedAcres((double)100);
		newBerries.setRowSpacing(10);
		newBerries.setPlantSpacing(5.3);
		newBerries.setTotalPlants(5000);
		newBerries.setIsQuantityInsurableInd(true);
		newBerries.setIsPlantInsurableInd(false);

		//Create inventory contract
		InventoryContractRsrc fetchedInvContract = service.createInventoryContract(topLevelEndpoints, invContract);
		
		field = fetchedInvContract.getFields().get(0);
		inventoryFieldGuid1 = field.getPlantings().get(0).getInventoryFieldGuid();
		newBerries.setInventoryFieldGuid(inventoryFieldGuid1);
		
		//PIM-2189: Temporary Insert inventoryBerries record using inventoryFieldGuid1 as InventoryFieldGuid. Replace &&&
		/*
		    SELECT * FROM inventory_field WHERE field_id = 90000016 AND crop_year = 2021;
		    INSERT INTO cuws.inventory_berries(
			inventory_berries_guid, inventory_field_guid, crop_commodity_id, crop_variety_id, planted_year, planted_acres, row_spacing, plant_spacing, total_plants, is_quantity_insurable_ind, is_plant_insurable_ind, create_user, create_date, update_user, update_date)
			VALUES ('testInvBerries1234', '&&&', 10, 1010689, 2020, 100, 10, 5.3, 5000, 'Y', 'N', 'admin', now(), 'admin', now());
		 */
//		UwContractRsrc uwContract = getUwContract(policyNumber1, service, topLevelEndpoints);
//		Assert.assertNotNull(uwContract.getInventoryContractGuid());
//		fetchedInvContract = service.getInventoryContract(uwContract);

		InventoryBerries fetchedBerries = fetchedInvContract.getFields().get(0).getPlantings().get(0).getInventoryBerries();

		//checkInventoryBerries(newBerries, fetchedBerries);
//
//		//Update
//		cal.clear();
//		cal.set(2021, Calendar.MARCH, 10);
//		seedingDate = cal.getTime();
//		
//		fetchedForage.setCommodityTypeCode("Six Row");
//		fetchedForage.setCropCommodityId(16);
//		fetchedForage.setCropVarietyId(1010640);
//		fetchedForage.setCropVarietyName("CDC TITANIUM");
//		fetchedForage.setFieldAcres(22.5);
//		fetchedForage.setSeedingYear(2021);
//		fetchedForage.setSeedingDate(seedingDate);
//		fetchedForage.setIsIrrigatedInd(false);
//		fetchedForage.setIsQuantityInsurableInd(true);
//		fetchedForage.setPlantInsurabilityTypeCode("W1");
//		fetchedForage.setIsAwpEligibleInd(false);
//		
//		InventoryContractRsrc updatedInvContract = service.updateInventoryContract(fetchedInvContract.getInventoryContractGuid(), fetchedInvContract);
//
//		InventorySeededForage updatedForage = updatedInvContract.getFields().get(0).getPlantings().get(0).getInventorySeededForages().get(0);
//		
//		checkInventorySeededForage(fetchedForage, updatedForage);
//
//		//Check DOP link
//		searchResults = service.getUwContractList(
//				topLevelEndpoints, 
//				null, 
//				null, 
//				null,
//				null,
//				policyNumber1,
//				null,
//				null, 
//				null, 
//				null, 
//				pageNumber, pageRowCount);
//
//		Assert.assertNotNull(searchResults);
//		Assert.assertEquals(1, searchResults.getCollection().size());
//
//		UwContractRsrc uwContractRsrc = searchResults.getCollection().get(0);
//		
//		Assert.assertNotNull(uwContractRsrc);
//		Assert.assertNotNull(uwContractRsrc.getLinks());
//		boolean dopLinkPresent = false;
//		//Check if the rollover dop yield link is in the list
//		for (RelLink link : uwContractRsrc.getLinks()) {
//			if(link.getHref().contains("rolloverDopYieldContract")) {
//				dopLinkPresent = true;
//				break;
//			}
//		}
//		Assert.assertTrue(dopLinkPresent);
		
		
		//PIM-2189: Temporary Delete inventoryBerries record
		//DELETE FROM inventory_berries WHERE inventory_berries_guid = 'testInvBerries1234';
		//SELECT * FROM inventory_berries WHERE inventory_berries_guid = 'testInvBerries1234';
		delete();
		
		logger.debug(">testInsertUpdateDeleteInventoryBerries");
	

	}
	
	@Test
	public void testInventoryContractCommodityBerries() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testInventoryContractCommodityBerries");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		createGrower();
		createPolicy();
		createGrowerContractYear();

		createLegalLand();
		createField();
		createAnnualFieldDetail();
		createContractedFieldDetail(false);
		
		
		CirrasUnderwritingService service = getService(SCOPES);

		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		UwContractRsrc uwContract = getUwContract(policyNumber1, service, topLevelEndpoints);
		Assert.assertNull(uwContract.getInventoryContractGuid());
		
		InventoryContractRsrc invContract = service.rolloverInventoryContract(uwContract);
		
		Assert.assertNotNull(invContract.getInventoryContractCommodityBerries());
		Assert.assertEquals(0, invContract.getInventoryContractCommodityBerries().size());

//		AnnualFieldRsrc field = invContract.getFields().get(0);
//
//		// Remove default planting.
//		field.getPlantings().remove(0);
//		
//		// Planting 1
//		InventoryField planting = createPlanting(field, 1, cropYear1);
//		createInventorySeededForage(planting, 65, "E2", true, 12.3456); //FORAGE Establishment 2
//
//		// Planting 2
//		planting = createPlanting(field, 2, cropYear1);
//		createInventorySeededForage(planting, 65, "W3", true, 11.2233); //FORAGE Winter Survival 3
//				
//		// Planting 3
//		planting = createPlanting(field, 3, cropYear1);
//		createInventorySeededForage(planting, 65, "E2", true, 33.4455); //FORAGE Establishment 2
//
//		// Planting 4
//		planting = createPlanting(field, 4, cropYear1);
//		createInventorySeededForage(planting, 65, null, false, 66.7788); //FORAGE Not insured		
//		
//		// Planting 5
//		planting = createPlanting(field, 5, cropYear1);
//		createInventoryUnseeded(planting, 71, true, 98.0);
//		createInventorySeededForage(planting, 71, null, true, 98.7654); //SILAGE CORN
//		createInventorySeededForage(planting, 71, null, true, 99.8877); //SILAGE CORN
//		createInventorySeededForage(planting, 71, null, true, null); //SILAGE CORN - Null field acres
//		createInventorySeededForage(planting, 71, null, true, 0.0); //SILAGE CORN - 0 field acres
//		
//		// Planting 6 - SILAGE CORN + IsUnseedeUnsurable = true
//		planting = createPlanting(field, 6, cropYear1);  
//		createInventoryUnseeded(planting, 71, true, 102.0);
//		createInventorySeededForage(planting, 71, null, true, 101.00); //SILAGE CORN
//		createInventorySeededForage(planting, 71, null, true, 102.00); //SILAGE CORN
//
//		// Planting 7 - SILAGE CORN + IsUnseedeUnsurable = false
//		planting = createPlanting(field, 7, cropYear1);  
//		createInventoryUnseeded(planting, 71, false, 999.0);
//
		//Berries Totals
		List<InventoryContractCommodityBerries> expectedTotals = new ArrayList<InventoryContractCommodityBerries>();
		//Blueberries
		InventoryContractCommodityBerries iccb = createInventoryContractCommodityBerries(invContract.getInventoryContractGuid(), 10, "BLUEBERRY", 5000, 200, (double)1000, (double)100);
		expectedTotals.add(iccb);
		
		//Raspberries
		iccb = createInventoryContractCommodityBerries(invContract.getInventoryContractGuid(), 12, "RASPBERRY", 5001, 201, (double)1001, (double)101);
		expectedTotals.add(iccb);

		InventoryContractRsrc fetchedInvContract = service.createInventoryContract(topLevelEndpoints, invContract);

		//PIM-2189: Temporary Insert inventoryContractCommodityBerries record using invContract.getInventoryContractGuid() as InventoryFieldGuid. Replace &&&
		/*
		    SELECT * FROM inventory_contract WHERE contract_id = 90000014 and crop_year = 2021;
		    
			INSERT INTO cuws.inventory_contract_commodity_berries(
				inventory_contract_commodity_berries_guid, inventory_contract_guid, crop_commodity_id, total_insured_plants, total_uninsured_plants, total_insured_acres, total_uninsured_acres, create_user, create_date, update_user, update_date)
				VALUES ('testCcBerries1234', '&&&', 10, 5000, 200, 1000, 100, 'admin', now(), 'admin', now());		 
			
			INSERT INTO cuws.inventory_contract_commodity_berries(
				inventory_contract_commodity_berries_guid, inventory_contract_guid, crop_commodity_id, total_insured_plants, total_uninsured_plants, total_insured_acres, total_uninsured_acres, create_user, create_date, update_user, update_date)
				VALUES ('testCcBerries12345', '&&&', 12, 5001, 201, 1001, 101, 'admin', now(), 'admin', now());
		 */
		uwContract = getUwContract(policyNumber1, service, topLevelEndpoints);
		Assert.assertNotNull(uwContract.getInventoryContractGuid());
		fetchedInvContract = service.getInventoryContract(uwContract);

		Assert.assertEquals(expectedTotals.size(), fetchedInvContract.getInventoryContractCommodityBerries().size());

		for (int i = 0; i < expectedTotals.size(); i++) {
			checkInventoryContractCommodityBerries(expectedTotals.get(i), fetchedInvContract.getInventoryContractCommodityBerries().get(i), fetchedInvContract.getInventoryContractGuid());
		}

//		// Update FORAGE and Establishment 2.
//		fetchedInvContract.getFields().get(0).getPlantings().get(0).getInventorySeededForages().get(0).setFieldAcres(13.579);
//		
//		// Remove W3, Add W1
//		fetchedInvContract.getFields().get(0).getPlantings().get(1).getInventorySeededForages().get(0).setDeletedByUserInd(true);
//		createInventorySeededForage(fetchedInvContract.getFields().get(0).getPlantings().get(1), 65, "W1", true, 97.531);
//
//		// Update expected totals.
//		// FORAGE
//		expectedTotals.get(0).setTotalFieldAcres(144.5555);
//
//		// Establishment 2
//		expectedTotals.get(3).setTotalFieldAcres(47.0245);
//
//		// Winter Survival 1 (replaces Winter Survival 3)
//		ictf = createInventoryCovarageTotalForage(fetchedInvContract.getInventoryContractGuid(), null, null, "W1", "Winter Survival 1", 97.531, false);
//		expectedTotals.set(4, ictf);
//		
//		InventoryContractRsrc updatedInvContract = service.updateInventoryContract(fetchedInvContract.getInventoryContractGuid(), fetchedInvContract);
//		
//		Assert.assertEquals(expectedTotals.size(), updatedInvContract.getInventoryCoverageTotalForages().size());
//
//		for (int i = 0; i < expectedTotals.size(); i++) {
//			checkInventoryCoverageTotalForage(expectedTotals.get(i), updatedInvContract.getInventoryCoverageTotalForages().get(i));
//		}
		
		//PIM-2189: Temporary Delete inventoryBerries record
		//SELECT * FROM inventory_contract_commodity_berries WHERE inventory_contract_commodity_berries_guid in ('testCcBerries1234','testCcBerries12345')
		//DELETE FROM inventory_contract_commodity_berries WHERE inventory_contract_commodity_berries_guid in ('testCcBerries1234','testCcBerries12345');
		delete();


		logger.debug(">testInventoryContractCommodityBerries");
	}

//	@Test
//	public void testGenerateInventoryReport() throws CirrasUnderwritingServiceException, Oauth2ClientException {
//		logger.debug("<testGenerateInventoryReport");
//		
//		if(skipTests) {
//			logger.warn("Skipping tests");
//			return;
//		}
//
//		// Test 1: Generate Forage report.
//		byte[] reportContent = service.generateInventoryReport(topLevelEndpoints, "2023", "5", null, null, null, null, null, null, null);
//		
//		Assert.assertNotNull(reportContent);
//		
//		logger.debug(">testGenerateInventoryReport - Returned " + reportContent.length + " bytes");	
//
//		// Test 2: Generate Grain Unseeded report
//		reportContent = service.generateInventoryReport(topLevelEndpoints, "2023", "4", null, null, null, null, null, null, InventoryReportType.unseeded.name());
//		
//		Assert.assertNotNull(reportContent);
//		
//		logger.debug(">testGenerateInventoryReport - Returned " + reportContent.length + " bytes");	
//
//		// Test 3: Generate Grain Seeded report
//		reportContent = service.generateInventoryReport(topLevelEndpoints, "2023", "4", null, null, null, null, null, null, InventoryReportType.seeded.name());
//		
//		Assert.assertNotNull(reportContent);
//		
//		logger.debug(">testGenerateInventoryReport - Returned " + reportContent.length + " bytes");
//		
//		// Test 4: Omit Insurance Plan: Report generation should fail.
//		try {
//			reportContent = service.generateInventoryReport(topLevelEndpoints, "2023", null, null, null, null, null, null, null, null);
//			Assert.fail("Report generated for missing insurance plan id ");
//		} catch ( CirrasUnderwritingServiceException e ) {
//			// Ok.
//		}
//
//		// Test 5: Invalid Insurance Plan: Report generation should fail.
//		try {
//			reportContent = service.generateInventoryReport(topLevelEndpoints, "2023", "2", null, null, null, null, null, null, null);
//			Assert.fail("Report generated for invalid insurance plan id ");
//		} catch ( CirrasUnderwritingServiceException e ) {
//			// Ok.
//		}
//
//		// Test 6: Omit Report Type for GRAIN: Report generation should fail.
//		try {
//			reportContent = service.generateInventoryReport(topLevelEndpoints, "2023", "4", null, null, null, null, null, null, null);
//			Assert.fail("Report generated for omitted report type");
//		} catch ( CirrasUnderwritingServiceException e ) {
//			// Ok.
//		}
//	
//		// Test 7: Invalid Report Type for GRAIN: Report generation should fail.
//		try {
//			reportContent = service.generateInventoryReport(topLevelEndpoints, "2023", "4", null, null, null, null, null, null, "nosuchtype");
//			Assert.fail("Report generated for invalid report type");
//		} catch ( CirrasUnderwritingServiceException e ) {
//			// Ok.
//		}
//	}	
	
	private UwContractRsrc getUwContract(String policyNumber,
			CirrasUnderwritingService service, 
			EndpointsRsrc topLevelEndpoints) throws CirrasUnderwritingServiceException {

		UwContractListRsrc searchResults = service.getUwContractList(
		topLevelEndpoints, 
		null, 
		null, 
		null,
		null,
		policyNumber,
		null,
		null, 
		null, 
		null, 
		1, 
		20);
		
		Assert.assertNotNull(searchResults);
		Assert.assertTrue(searchResults.getCollection().size() == 1);
		
		UwContractRsrc uwContract = searchResults.getCollection().get(0);
		return uwContract;
	}
	
	public void checkInventoryContractCommodityBerries(InventoryContractCommodityBerries expected, InventoryContractCommodityBerries actual, String inventoryContractGuid) {
		Assert.assertNotNull("InventoryContractCommodityBerriesGuid", actual.getInventoryContractCommodityBerriesGuid());
		Assert.assertEquals("InventoryContractGuid", inventoryContractGuid, actual.getInventoryContractGuid());
		Assert.assertEquals("CropCommodityId", expected.getCropCommodityId(), actual.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", expected.getCropCommodityName(), actual.getCropCommodityName());
		Assert.assertEquals("TotalInsuredPlants", expected.getTotalInsuredPlants(), actual.getTotalInsuredPlants());
		Assert.assertEquals("TotalUninsuredPlants", expected.getTotalUninsuredPlants(), actual.getTotalUninsuredPlants());
		Assert.assertEquals("TotalInsuredAcres", expected.getTotalInsuredAcres(), actual.getTotalInsuredAcres());
		Assert.assertEquals("TotalUninsuredAcres", expected.getTotalUninsuredAcres(), actual.getTotalUninsuredAcres());
	}
	
	public void checkInventoryBerries(InventoryBerries expected, InventoryBerries actual) {
		
		Assert.assertNotNull("InventoryBerriesGuid", actual.getInventoryBerriesGuid());
		Assert.assertEquals("InventoryFieldGuid", expected.getInventoryFieldGuid(), actual.getInventoryFieldGuid());
		Assert.assertEquals("CropCommodityId", expected.getCropCommodityId(), actual.getCropCommodityId());
		Assert.assertEquals("CropVarietyId", expected.getCropVarietyId(), actual.getCropVarietyId());
		Assert.assertEquals("PlantedYear", expected.getPlantedYear(), actual.getPlantedYear());
		Assert.assertEquals("PlantedAcres", expected.getPlantedAcres(), actual.getPlantedAcres());
		Assert.assertEquals("RowSpacing", expected.getRowSpacing(), actual.getRowSpacing());
		Assert.assertEquals("PlantSpacing", expected.getPlantSpacing(), actual.getPlantSpacing());
		Assert.assertEquals("TotalPlants", expected.getTotalPlants(), actual.getTotalPlants());
		Assert.assertEquals("IsQuantityInsurableInd", expected.getIsQuantityInsurableInd(), actual.getIsQuantityInsurableInd());
		Assert.assertEquals("IsPlantInsurableInd", expected.getIsPlantInsurableInd(), actual.getIsPlantInsurableInd());
		Assert.assertEquals("CropCommodityName", expected.getCropCommodityName(), actual.getCropCommodityName());
		Assert.assertEquals("CropVarietyName", expected.getCropVarietyName(), actual.getCropVarietyName());

	}

	private InventoryField createPlanting(AnnualFieldRsrc field, Integer plantingNumber, Integer cropYear) {
		InventoryField planting = new InventoryField();

		planting.setCropYear(cropYear);
		planting.setFieldId(field.getFieldId());
		planting.setInsurancePlanId(insurancePlanId);
		planting.setInventoryFieldGuid(null);
		planting.setLastYearCropCommodityId(null);
		planting.setLastYearCropCommodityName(null);
		planting.setLastYearCropVarietyId(null);
		planting.setLastYearCropVarietyName(null);
		planting.setIsHiddenOnPrintoutInd(false);
		planting.setPlantingNumber(plantingNumber);
		planting.setUnderseededAcres(null);
		planting.setUnderseededCropVarietyId(null);
		planting.setUnderseededCropVarietyName(null);
		
		field.getPlantings().add(planting);

		return planting;
	}

	private InventorySeededForage createInventorySeededForage(
			InventoryField planting, 
            Integer cropCommodityId, 
			String plantInsurabilityTypeCode,
			Boolean isQuantityInsurableInd,
			Double fieldAcres) {
		
		InventorySeededForage isf = new InventorySeededForage();


		isf.setCommodityTypeCode(null);
		isf.setCropCommodityId(cropCommodityId);
		isf.setCropVarietyId(null);
		isf.setCropVarietyName(null);
		isf.setFieldAcres(fieldAcres);
		isf.setInventoryFieldGuid(planting.getInventoryFieldGuid());
		isf.setIsAwpEligibleInd(false);
		isf.setIsIrrigatedInd(false);
		isf.setIsQuantityInsurableInd(isQuantityInsurableInd);
		isf.setPlantInsurabilityTypeCode(plantInsurabilityTypeCode);
		isf.setSeedingYear(planting.getCropYear() - 3);
		
		planting.getInventorySeededForages().add(isf);

		return isf;
	}	
	
	private void updateInventorySeededForage(
            Integer cropCommodityId, 
			String plantInsurabilityTypeCode,
			Boolean isQuantityInsurableInd,
			Double fieldAcres,
			InventorySeededForage isf) {
		
		isf.setCommodityTypeCode(null);
		isf.setCropCommodityId(cropCommodityId);
		isf.setCropVarietyId(null);
		isf.setCropVarietyName(null);
		isf.setFieldAcres(fieldAcres);
		//isf.setInventoryFieldGuid(planting.getInventoryFieldGuid());
		isf.setIsAwpEligibleInd(false);
		isf.setIsIrrigatedInd(false);
		isf.setIsQuantityInsurableInd(isQuantityInsurableInd);
		isf.setPlantInsurabilityTypeCode(plantInsurabilityTypeCode);
		//isf.setSeedingYear(planting.getCropYear() - 3);
		
	}	
	private void createInventoryUnseeded(
			InventoryField planting, 
            Integer cropCommodityId, 
            Boolean isUnseededInsurableInd,
            Double acresToBeSeeded) {
		
		InventoryUnseeded inventoryUnseeded = new InventoryUnseeded();
		inventoryUnseeded.setCropCommodityId(cropCommodityId);
		inventoryUnseeded.setIsUnseededInsurableInd(isUnseededInsurableInd);
		inventoryUnseeded.setAcresToBeSeeded(acresToBeSeeded);
		inventoryUnseeded.setInventoryFieldGuid(planting.getInventoryFieldGuid());
		planting.setInventoryUnseeded(inventoryUnseeded);

	}

	private InventoryContractCommodityBerries createInventoryContractCommodityBerries(
			String invContractGuid,
            Integer cropCommodityId, 
            String cropCommodityName, 
            Integer totalInsuredPlants, 
            Integer totalUninsuredPlants, 
            Double totalInsuredAcres, 
            Double totalUninsuredAcres
			) {
	
		InventoryContractCommodityBerries model = new InventoryContractCommodityBerries();
		
		model.setInventoryContractGuid(invContractGuid);
		model.setCropCommodityId(cropCommodityId);
		model.setCropCommodityName(cropCommodityName);
		model.setTotalInsuredPlants(totalInsuredPlants);
		model.setTotalUninsuredPlants(totalUninsuredPlants);
		model.setTotalInsuredAcres(totalInsuredAcres);
		model.setTotalUninsuredAcres(totalUninsuredAcres);


		return model;
	}
	
	private void createGrower() throws ValidationException, CirrasUnderwritingServiceException {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);

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
		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.GrowerCreated);

		service.synchronizeGrower(resource);
		
	}
	
	private void createPolicy() throws ValidationException, CirrasUnderwritingServiceException {

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);

		PolicyRsrc resource = new PolicyRsrc();
		
		resource.setPolicyId(policyId1);
		resource.setGrowerId(growerId1);
		resource.setInsurancePlanId(insurancePlanId);
		resource.setPolicyStatusCode("ACTIVE");
		resource.setOfficeId(1);
		resource.setPolicyNumber(policyNumber1);
		resource.setContractNumber(contractNumber1);
		resource.setContractId(contractId1);
		resource.setCropYear(cropYear1);
		
		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.PolicyCreated);

		service.synchronizePolicy(resource);
	}
	
	private void createGrowerContractYear() throws ValidationException, CirrasUnderwritingServiceException {

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);
		
		GrowerContractYearSyncRsrc resource = new GrowerContractYearSyncRsrc();
		
		resource.setGrowerContractYearId(gcyId1);
		resource.setContractId(contractId1);
		resource.setGrowerId(growerId1);
		resource.setInsurancePlanId(insurancePlanId);
		resource.setCropYear(cropYear1);
		resource.setDataSyncTransDate(createTransactionDate);
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
		resource.setFieldLocation(fieldLocation );
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

	private void createContractedFieldDetail(Boolean isLeased) throws CirrasUnderwritingServiceException, ValidationException {
		
		ContractedFieldDetailRsrc resource = new ContractedFieldDetailRsrc();
		
		resource.setContractedFieldDetailId(contractedFieldDetailId1);
		resource.setAnnualFieldDetailId(annualFieldDetailId1);
		resource.setGrowerContractYearId(gcyId1);
		resource.setDisplayOrder(1);
		resource.setIsLeasedInd(isLeased);
		resource.setTransactionType(LandManagementEventTypes.ContractedFieldDetailCreated);
	
		service.synchronizeContractedFieldDetail(resource);

	}
	
	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}
}
