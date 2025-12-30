package ca.bc.gov.mal.cirras.underwriting.controllers;

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

import ca.bc.gov.mal.cirras.underwriting.services.utils.InventoryServiceEnums.InventoryReportType;
import ca.bc.gov.mal.cirras.underwriting.services.utils.LandManagementEventTypes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.PoliciesSyncEventTypes;
import ca.bc.gov.mal.cirras.underwriting.clients.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.clients.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.clients.ValidationException;
import ca.bc.gov.mal.cirras.underwriting.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.AnnualFieldDetailRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.AnnualFieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.ContractedFieldDetailRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.FieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.GrowerContractYearSyncRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.GrowerRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.InventoryContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.LegalLandRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.PolicyRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.UwContractListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.UwContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryCoverageTotalForage;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryField;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventorySeededForage;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryUnseeded;
import ca.bc.gov.mal.cirras.underwriting.test.EndpointsTest;
import ca.bc.gov.nrs.common.wfone.rest.resource.RelLink;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class InventoryContractEndpointForageTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(InventoryContractEndpointForageTest.class);


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
	
	private Integer growerId1 = 90000001;
	private Integer policyId1 = 90000002;
	private Integer gcyId1 = 90000003;
	private Integer contractId1 = 90000004;
	private String policyNumber1 = "998877-21";
	private String contractNumber1 = "998877";
	private Integer cropYear1 = 2021;

	private Integer legalLandId1 = 90000005;
	private Integer fieldId1 = 90000006;
	private Integer annualFieldDetailId1 = 90000007;
	private Integer contractedFieldDetailId1 = 90000008;
		
	private String inventoryFieldGuid1 = null;
	
	private Integer insurancePlanId = 5; //Forage
	
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
	public void testInsertUpdateDeleteInventorySeededForage() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testInsertUpdateDeleteInventorySeededForage");
		
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
		createContractedFieldDetail();
		
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
		Assert.assertEquals(1, searchResults.getCollection().size());

		UwContractRsrc referrer = searchResults.getCollection().get(0);
		Assert.assertNull(referrer.getInventoryContractGuid());
		
		InventoryContractRsrc invContract = service.rolloverInventoryContract(referrer);
		Assert.assertNotNull(invContract);
		Assert.assertNotNull(invContract.getFields());
		Assert.assertNotNull(invContract.getFields().get(0).getPlantings());
		Assert.assertNotNull(invContract.getFields().get(0).getPlantings().get(0).getInventorySeededForages());

		InventorySeededForage newForage = invContract.getFields().get(0).getPlantings().get(0).getInventorySeededForages().get(0);
		Assert.assertNull("InventorySeededForageGuid", newForage.getInventorySeededForageGuid());
		Assert.assertNull("InventoryFieldGuid", newForage.getInventoryFieldGuid());
		Assert.assertNull("CropCommodityId", newForage.getCropCommodityId());
		Assert.assertNull("CropVarietyId", newForage.getCropVarietyId());
		Assert.assertNull("CropVarietyName", newForage.getCropVarietyName());
		Assert.assertNull("CommodityTypeCode", newForage.getCommodityTypeCode());
		Assert.assertNull("FieldAcres", newForage.getFieldAcres());
		Assert.assertNull("SeedingYear", newForage.getSeedingYear());
		Assert.assertNull("SeedingDate", newForage.getSeedingDate());
		Assert.assertNull("IsIrrigatedInd", newForage.getIsIrrigatedInd());
		Assert.assertNull("IsQuantityInsurableInd", newForage.getIsQuantityInsurableInd());
		Assert.assertNull("PlantInsurabilityTypeCode", newForage.getPlantInsurabilityTypeCode());
		Assert.assertTrue("IsAwpEligibleInd", newForage.getIsAwpEligibleInd());
		
		inventoryFieldGuid1 = invContract.getFields().get(0).getPlantings().get(0).getInventoryFieldGuid();
		
		//Set seeded forage
		
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2020, Calendar.FEBRUARY, 15);
		Date seedingDate = cal.getTime();
		
		newForage.setInventoryFieldGuid(inventoryFieldGuid1);
		newForage.setCommodityTypeCode("CPSW");
		newForage.setCropCommodityId(26);
		newForage.setCropVarietyId(1010602);
		newForage.setCropVarietyName("AAC ENTICE");
		newForage.setFieldAcres(10.4);
		newForage.setSeedingYear(2020);
		newForage.setSeedingDate(seedingDate);
		newForage.setIsIrrigatedInd(true);
		newForage.setIsQuantityInsurableInd(false);
		newForage.setPlantInsurabilityTypeCode("E1");
		newForage.setIsAwpEligibleInd(true);
		
		InventoryContractRsrc fetchedInvContract = service.createInventoryContract(topLevelEndpoints, invContract);
		
		InventorySeededForage fetchedForage = fetchedInvContract.getFields().get(0).getPlantings().get(0).getInventorySeededForages().get(0);

		checkInventorySeededForage(newForage, fetchedForage);

		//Update
		cal.clear();
		cal.set(2021, Calendar.MARCH, 10);
		seedingDate = cal.getTime();
		
		fetchedForage.setCommodityTypeCode("Six Row");
		fetchedForage.setCropCommodityId(16);
		fetchedForage.setCropVarietyId(1010640);
		fetchedForage.setCropVarietyName("CDC TITANIUM");
		fetchedForage.setFieldAcres(22.5);
		fetchedForage.setSeedingYear(2021);
		fetchedForage.setSeedingDate(seedingDate);
		fetchedForage.setIsIrrigatedInd(false);
		fetchedForage.setIsQuantityInsurableInd(true);
		fetchedForage.setPlantInsurabilityTypeCode("W1");
		fetchedForage.setIsAwpEligibleInd(false);
		
		InventoryContractRsrc updatedInvContract = service.updateInventoryContract(fetchedInvContract.getInventoryContractGuid(), fetchedInvContract);

		InventorySeededForage updatedForage = updatedInvContract.getFields().get(0).getPlantings().get(0).getInventorySeededForages().get(0);
		
		checkInventorySeededForage(fetchedForage, updatedForage);

		//Check DOP link
		searchResults = service.getUwContractList(
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
		Assert.assertEquals(1, searchResults.getCollection().size());

		UwContractRsrc uwContractRsrc = searchResults.getCollection().get(0);
		
		Assert.assertNotNull(uwContractRsrc);
		Assert.assertNotNull(uwContractRsrc.getLinks());
		boolean dopLinkPresent = false;
		//Check if the rollover dop yield link is in the list
		for (RelLink link : uwContractRsrc.getLinks()) {
			if(link.getHref().contains("rolloverDopYieldContract")) {
				dopLinkPresent = true;
				break;
			}
		}
		Assert.assertTrue(dopLinkPresent);
		
		delete();
		
		logger.debug(">testInsertUpdateDeleteInventorySeededForage");
	
	/* 
	 * 
		*****************
		DELETE STATEMENTS
		*****************
		
		DELETE FROM inventory_seeded_forage WHERE inventory_field_guid IN (SELECT inventory_field_guid FROM inventory_field WHERE field_id = 90000006 AND crop_year = 2021);
		DELETE FROM inventory_field WHERE field_id = 90000006 AND crop_year = 2021;
		DELETE FROM inventory_contract_commodity WHERE inventory_contract_guid IN (SELECT ic.inventory_contract_guid FROM inventory_contract ic WHERE contract_id = 90000004 and crop_year = 2021);
		DELETE FROM inventory_contract WHERE contract_id = 90000004 and crop_year = 2021;
		
		DELETE FROM contracted_field_detail WHERE contracted_field_detail_id = 90000008;
		DELETE FROM annual_field_detail WHERE annual_field_detail_id = 90000007;
		DELETE FROM field WHERE field_id = 90000006;
		DELETE FROM legal_land WHERE legal_land_id = 90000005;
	
		DELETE FROM grower_contract_year WHERE grower_contract_year_id = 90000003;
		DELETE FROM policy WHERE policy_id = 90000002;
		DELETE FROM grower WHERE grower_id = 90000001;
		 
		*****************
		SELECT STATEMENTS
		*****************
		SELECT * FROM inventory_seeded_forage WHERE inventory_field_guid IN (SELECT inventory_field_guid FROM inventory_field WHERE field_id = 90000006 AND crop_year = 2021);
		SELECT * FROM inventory_field WHERE field_id = 90000006 AND crop_year = 2021;
		SELECT * FROM inventory_contract_commodity WHERE inventory_contract_guid IN (SELECT ic.inventory_contract_guid FROM inventory_contract ic WHERE contract_id = 90000004 and crop_year = 2021);
		SELECT * FROM inventory_contract WHERE contract_id = 90000004 and crop_year = 2021;
	
		SELECT * FROM contracted_field_detail WHERE contracted_field_detail_id = 90000008;
		SELECT * FROM annual_field_detail WHERE annual_field_detail_id = 90000007;
		SELECT * FROM field WHERE field_id = 90000006;
		SELECT * FROM legal_land WHERE legal_land_id = 90000005;
	
		SELECT * FROM grower_contract_year WHERE grower_contract_year_id = 90000003;
		SELECT * FROM policy WHERE policy_id = 90000002;
		SELECT * FROM grower WHERE grower_id = 90000001; 
	 */
	}
	
	@Test
	public void testInsertUpdateForageAnnualInventory() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testInsertUpdateForageAnnualInventory");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		/*
		 1. Add a silage corn planting
		 2. Expect an unseeded record
		 3. update to an perennial crop
		 4. Expect no unseeded record
		 5. Update to silage corn
		 6. Expect an unseeded record
		 7. Update to no seeded crop
		 8. Expect no seeded record
		*/

		createGrower();
		createPolicy();
		createGrowerContractYear();

		createLegalLand();
		createField();
		createAnnualFieldDetail();
		createContractedFieldDetail();
		
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
		Assert.assertEquals(1, searchResults.getCollection().size());

		UwContractRsrc referrer = searchResults.getCollection().get(0);
		Assert.assertNull(referrer.getInventoryContractGuid());
		
		InventoryContractRsrc invContract = service.rolloverInventoryContract(referrer);
		Assert.assertNotNull(invContract);
		Assert.assertNotNull(invContract.getFields());
		Assert.assertNotNull(invContract.getFields().get(0).getPlantings());
		Assert.assertNotNull(invContract.getFields().get(0).getPlantings().get(0).getInventorySeededForages());
		Assert.assertNull(invContract.getFields().get(0).getPlantings().get(0).getInventoryUnseeded());
		
		AnnualFieldRsrc field = invContract.getFields().get(0);
		InventoryField planting = field.getPlantings().get(0);
		InventorySeededForage invForage = planting.getInventorySeededForages().get(0);
		
		//1. Add a silage corn planting		
		updateInventorySeededForage(71, null, true, 101.00, invForage); //SILAGE CORN
		createInventoryUnseeded(planting, 71, true, 102.0); //SILAGE CORN
		
		InventoryContractRsrc fetchedInvContract = service.createInventoryContract(topLevelEndpoints, invContract);
		
		//2. Expect an unseeded record
		Assert.assertNotNull(fetchedInvContract.getFields().get(0).getPlantings().get(0).getInventorySeededForages());
		Assert.assertNotNull(fetchedInvContract.getFields().get(0).getPlantings().get(0).getInventoryUnseeded());


		//3. update to an perennial crop
		InventorySeededForage fetchedForage = fetchedInvContract.getFields().get(0).getPlantings().get(0).getInventorySeededForages().get(0);
		updateInventorySeededForage(65, null, true, 101.00, fetchedForage); //PERENNIAL

		fetchedInvContract = service.updateInventoryContract(fetchedInvContract.getInventoryContractGuid(), fetchedInvContract);

		//4. Expect no unseeded record
		Assert.assertNotNull(fetchedInvContract.getFields().get(0).getPlantings().get(0).getInventorySeededForages());
		Assert.assertNull(fetchedInvContract.getFields().get(0).getPlantings().get(0).getInventoryUnseeded());

		//5. Update to silage corn
		fetchedForage = fetchedInvContract.getFields().get(0).getPlantings().get(0).getInventorySeededForages().get(0);
		planting = fetchedInvContract.getFields().get(0).getPlantings().get(0);
		updateInventorySeededForage(71, null, true, 101.00, fetchedForage); //SILAGE CORN
		createInventoryUnseeded(planting, 71, true, 102.0); //SILAGE CORN

		fetchedInvContract = service.updateInventoryContract(fetchedInvContract.getInventoryContractGuid(), fetchedInvContract);

		//6. Expect an unseeded record
		Assert.assertNotNull(fetchedInvContract.getFields().get(0).getPlantings().get(0).getInventorySeededForages());
		Assert.assertNotNull(fetchedInvContract.getFields().get(0).getPlantings().get(0).getInventoryUnseeded());

		//7. Update to no seeded crop
		fetchedForage = fetchedInvContract.getFields().get(0).getPlantings().get(0).getInventorySeededForages().get(0);
		updateInventorySeededForage(null, null, true, 101.00, fetchedForage); //PERENNIAL

		fetchedInvContract = service.updateInventoryContract(fetchedInvContract.getInventoryContractGuid(), fetchedInvContract);

		//8. Expect no seeded record
		Assert.assertNotNull(fetchedInvContract.getFields().get(0).getPlantings().get(0).getInventorySeededForages());
		Assert.assertNull(fetchedInvContract.getFields().get(0).getPlantings().get(0).getInventoryUnseeded());

		delete();
		
		logger.debug(">testInsertUpdateForageAnnualInventory");
	
	}
	

	@Test
	public void testInventoryCoverageTotalForage() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testInventoryCoverageTotalForage");
		
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
		createContractedFieldDetail();
		
		
		CirrasUnderwritingService service = getService(SCOPES);

		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();
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
		Assert.assertTrue(searchResults.getCollection().size() == 1);

		UwContractRsrc uwContract = searchResults.getCollection().get(0);
		Assert.assertNull(uwContract.getInventoryContractGuid());
		
		InventoryContractRsrc invContract = service.rolloverInventoryContract(uwContract);
		
		Assert.assertEquals(0, invContract.getInventoryCoverageTotalForages().size());

		AnnualFieldRsrc field = invContract.getFields().get(0);

		// Remove default planting.
		field.getPlantings().remove(0);
		
		// Planting 1
		InventoryField planting = createPlanting(field, 1, cropYear1);
		createInventorySeededForage(planting, 65, "E2", true, 12.3456); //FORAGE Establishment 2

		// Planting 2
		planting = createPlanting(field, 2, cropYear1);
		createInventorySeededForage(planting, 65, "W3", true, 11.2233); //FORAGE Winter Survival 3
				
		// Planting 3
		planting = createPlanting(field, 3, cropYear1);
		createInventorySeededForage(planting, 65, "E2", true, 33.4455); //FORAGE Establishment 2

		// Planting 4
		planting = createPlanting(field, 4, cropYear1);
		createInventorySeededForage(planting, 65, null, false, 66.7788); //FORAGE Not insured		
		
		// Planting 5
		planting = createPlanting(field, 5, cropYear1);
		createInventoryUnseeded(planting, 71, true, 98.0);
		createInventorySeededForage(planting, 71, null, true, 98.7654); //SILAGE CORN
		createInventorySeededForage(planting, 71, null, true, 99.8877); //SILAGE CORN
		createInventorySeededForage(planting, 71, null, true, null); //SILAGE CORN - Null field acres
		createInventorySeededForage(planting, 71, null, true, 0.0); //SILAGE CORN - 0 field acres
		
		// Planting 6 - SILAGE CORN + IsUnseedeUnsurable = true
		planting = createPlanting(field, 6, cropYear1);  
		createInventoryUnseeded(planting, 71, true, 102.0);
		createInventorySeededForage(planting, 71, null, true, 101.00); //SILAGE CORN
		createInventorySeededForage(planting, 71, null, true, 102.00); //SILAGE CORN

		// Planting 7 - SILAGE CORN + IsUnseedeUnsurable = false
		planting = createPlanting(field, 7, cropYear1);  
		createInventoryUnseeded(planting, 71, false, 999.0);

		// Forage Totals
		List<InventoryCoverageTotalForage> expectedTotals = new ArrayList<InventoryCoverageTotalForage>();
		InventoryCoverageTotalForage ictf = createInventoryCovarageTotalForage(invContract.getInventoryContractGuid(), 65, "FORAGE", null, null, 57.0144, false);
		expectedTotals.add(ictf);

		ictf = createInventoryCovarageTotalForage(invContract.getInventoryContractGuid(), 71, "SILAGE CORN", null, null, 401.6531, false);
		expectedTotals.add(ictf);
		
		ictf = createInventoryCovarageTotalForage(invContract.getInventoryContractGuid(), 71, "SILAGE CORN", null, null, 200.00, true); // silage corn + unseeded insurable
		expectedTotals.add(ictf);

		ictf = createInventoryCovarageTotalForage(invContract.getInventoryContractGuid(), null, null, "E2", "Establishment 2", 45.7911, false);
		expectedTotals.add(ictf);
		
		ictf = createInventoryCovarageTotalForage(invContract.getInventoryContractGuid(), null, null, "W3", "Winter Survival 3", 11.2233, false);
		expectedTotals.add(ictf);

		InventoryContractRsrc fetchedInvContract = service.createInventoryContract(topLevelEndpoints, invContract);

		Assert.assertEquals(expectedTotals.size(), fetchedInvContract.getInventoryCoverageTotalForages().size());

		for (int i = 0; i < expectedTotals.size(); i++) {
			checkInventoryCoverageTotalForage(expectedTotals.get(i), fetchedInvContract.getInventoryCoverageTotalForages().get(i));
		}

		// Update FORAGE and Establishment 2.
		fetchedInvContract.getFields().get(0).getPlantings().get(0).getInventorySeededForages().get(0).setFieldAcres(13.579);
		
		// Remove W3, Add W1
		fetchedInvContract.getFields().get(0).getPlantings().get(1).getInventorySeededForages().get(0).setDeletedByUserInd(true);
		createInventorySeededForage(fetchedInvContract.getFields().get(0).getPlantings().get(1), 65, "W1", true, 97.531);

		// Update expected totals.
		// FORAGE
		expectedTotals.get(0).setTotalFieldAcres(144.5555);

		// Establishment 2
		expectedTotals.get(3).setTotalFieldAcres(47.0245);

		// Winter Survival 1 (replaces Winter Survival 3)
		ictf = createInventoryCovarageTotalForage(fetchedInvContract.getInventoryContractGuid(), null, null, "W1", "Winter Survival 1", 97.531, false);
		expectedTotals.set(4, ictf);
		
		InventoryContractRsrc updatedInvContract = service.updateInventoryContract(fetchedInvContract.getInventoryContractGuid(), fetchedInvContract);
		
		Assert.assertEquals(expectedTotals.size(), updatedInvContract.getInventoryCoverageTotalForages().size());

		for (int i = 0; i < expectedTotals.size(); i++) {
			checkInventoryCoverageTotalForage(expectedTotals.get(i), updatedInvContract.getInventoryCoverageTotalForages().get(i));
		}
		
		delete();

		/* 
		 * 
			*****************
			DELETE STATEMENTS
			*****************
		DELETE FROM inventory_seeded_forage WHERE inventory_field_guid IN (SELECT inventory_field_guid FROM inventory_field WHERE field_id = 90000006 AND crop_year = 2021);
		DELETE FROM inventory_field WHERE field_id = 90000006 AND crop_year = 2021;
		DELETE FROM inventory_coverage_total_forage WHERE inventory_contract_guid IN (SELECT ic.inventory_contract_guid FROM inventory_contract ic WHERE contract_id = 90000004 and crop_year = 2021);
		DELETE FROM inventory_contract_commodity WHERE inventory_contract_guid IN (SELECT ic.inventory_contract_guid FROM inventory_contract ic WHERE contract_id = 90000004 and crop_year = 2021);
		DELETE FROM inventory_contract WHERE contract_id = 90000004 and crop_year = 2021;
		
		DELETE FROM contracted_field_detail WHERE contracted_field_detail_id = 90000008;
		DELETE FROM annual_field_detail WHERE annual_field_detail_id = 90000007;
		DELETE FROM field WHERE field_id = 90000006;
		DELETE FROM legal_land WHERE legal_land_id = 90000005;
	
		DELETE FROM grower_contract_year WHERE grower_contract_year_id = 90000003;
		DELETE FROM policy WHERE policy_id = 90000002;
		DELETE FROM grower WHERE grower_id = 90000001;
			
			 
			*****************
			SELECT STATEMENTS
			*****************

		SELECT * FROM inventory_seeded_forage WHERE inventory_field_guid IN (SELECT inventory_field_guid FROM inventory_field WHERE field_id = 90000006 AND crop_year = 2021);
		SELECT * FROM inventory_field WHERE field_id = 90000006 AND crop_year = 2021;
		SELECT * FROM inventory_coverage_total_forage WHERE inventory_contract_guid IN (SELECT ic.inventory_contract_guid FROM inventory_contract ic WHERE contract_id = 90000004 and crop_year = 2021);
		SELECT * FROM inventory_contract_commodity WHERE inventory_contract_guid IN (SELECT ic.inventory_contract_guid FROM inventory_contract ic WHERE contract_id = 90000004 and crop_year = 2021);
		SELECT * FROM inventory_contract WHERE contract_id = 90000004 and crop_year = 2021;
	
		SELECT * FROM contracted_field_detail WHERE contracted_field_detail_id = 90000008;
		SELECT * FROM annual_field_detail WHERE annual_field_detail_id = 90000007;
		SELECT * FROM field WHERE field_id = 90000006;
		SELECT * FROM legal_land WHERE legal_land_id = 90000005;
	
		SELECT * FROM grower_contract_year WHERE grower_contract_year_id = 90000003;
		SELECT * FROM policy WHERE policy_id = 90000002;
		SELECT * FROM grower WHERE grower_id = 90000001; 

		 */
		
		logger.debug(">testInventoryCoverageTotalForage");
	}

	@Test
	public void testGenerateInventoryReport() throws CirrasUnderwritingServiceException, Oauth2ClientException {
		logger.debug("<testGenerateInventoryReport");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		// Test 1: Generate Forage report.
		byte[] reportContent = service.generateInventoryReport(topLevelEndpoints, "2023", "5", null, null, null, null, null, null, null);
		
		Assert.assertNotNull(reportContent);
		
		logger.debug(">testGenerateInventoryReport - Returned " + reportContent.length + " bytes");	

		// Test 2: Generate Grain Unseeded report
		reportContent = service.generateInventoryReport(topLevelEndpoints, "2023", "4", null, null, null, null, null, null, InventoryReportType.unseeded.name());
		
		Assert.assertNotNull(reportContent);
		
		logger.debug(">testGenerateInventoryReport - Returned " + reportContent.length + " bytes");	

		// Test 3: Generate Grain Seeded report
		reportContent = service.generateInventoryReport(topLevelEndpoints, "2023", "4", null, null, null, null, null, null, InventoryReportType.seeded.name());
		
		Assert.assertNotNull(reportContent);
		
		logger.debug(">testGenerateInventoryReport - Returned " + reportContent.length + " bytes");
		
		// Test 4: Omit Insurance Plan: Report generation should fail.
		try {
			reportContent = service.generateInventoryReport(topLevelEndpoints, "2023", null, null, null, null, null, null, null, null);
			Assert.fail("Report generated for missing insurance plan id ");
		} catch ( CirrasUnderwritingServiceException e ) {
			// Ok.
		}

		// Test 5: Invalid Insurance Plan: Report generation should fail.
		try {
			reportContent = service.generateInventoryReport(topLevelEndpoints, "2023", "2", null, null, null, null, null, null, null);
			Assert.fail("Report generated for invalid insurance plan id ");
		} catch ( CirrasUnderwritingServiceException e ) {
			// Ok.
		}

		// Test 6: Omit Report Type for GRAIN: Report generation should fail.
		try {
			reportContent = service.generateInventoryReport(topLevelEndpoints, "2023", "4", null, null, null, null, null, null, null);
			Assert.fail("Report generated for omitted report type");
		} catch ( CirrasUnderwritingServiceException e ) {
			// Ok.
		}
	
		// Test 7: Invalid Report Type for GRAIN: Report generation should fail.
		try {
			reportContent = service.generateInventoryReport(topLevelEndpoints, "2023", "4", null, null, null, null, null, null, "nosuchtype");
			Assert.fail("Report generated for invalid report type");
		} catch ( CirrasUnderwritingServiceException e ) {
			// Ok.
		}
		
		// Test 8: Generate Berries report.
		reportContent = service.generateInventoryReport(topLevelEndpoints, "2023", "3", null, null, null, null, null, null, null);
		
		Assert.assertNotNull(reportContent);
		
		logger.debug(">testGenerateInventoryReport - Returned " + reportContent.length + " bytes");	

	}	
	
	public void checkInventoryCoverageTotalForage(InventoryCoverageTotalForage expected, InventoryCoverageTotalForage actual) {
		Assert.assertEquals("CropCommodityId", expected.getCropCommodityId(), actual.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", expected.getCropCommodityName(), actual.getCropCommodityName());
		Assert.assertEquals("PlantInsurabilityTypeCode", expected.getPlantInsurabilityTypeCode(), actual.getPlantInsurabilityTypeCode());
		Assert.assertEquals("PlantInsurabilityTypeDesc", expected.getPlantInsurabilityTypeDesc(), actual.getPlantInsurabilityTypeDesc());
		Assert.assertEquals("TotalFieldAcres", expected.getTotalFieldAcres(), actual.getTotalFieldAcres());		
		Assert.assertEquals("IsUnseededInsurableInd", expected.getIsUnseededInsurableInd(), actual.getIsUnseededInsurableInd());
	}
	
	public void checkInventorySeededForage(InventorySeededForage expectedForage, InventorySeededForage actualForage) {
		Assert.assertEquals("CropCommodityId", expectedForage.getCropCommodityId(), actualForage.getCropCommodityId());
		Assert.assertEquals("CropVarietyId", expectedForage.getCropVarietyId(), actualForage.getCropVarietyId());
		Assert.assertEquals("CommodityTypeCode", expectedForage.getCommodityTypeCode(), actualForage.getCommodityTypeCode());
		Assert.assertEquals("FieldAcres", expectedForage.getFieldAcres(), actualForage.getFieldAcres());
		Assert.assertEquals("SeedingYear", expectedForage.getSeedingYear(), actualForage.getSeedingYear());
		Assert.assertEquals("SeedingDate", expectedForage.getSeedingDate(), actualForage.getSeedingDate());
		Assert.assertEquals("IsIrrigatedInd", expectedForage.getIsIrrigatedInd(), actualForage.getIsIrrigatedInd());
		Assert.assertEquals("IsQuantityInsurableInd", expectedForage.getIsQuantityInsurableInd(), actualForage.getIsQuantityInsurableInd());
		Assert.assertEquals("PlantInsurabilityTypeCode", expectedForage.getPlantInsurabilityTypeCode(), actualForage.getPlantInsurabilityTypeCode());
		Assert.assertEquals("IsAwpEligibleInd", expectedForage.getIsAwpEligibleInd(), actualForage.getIsAwpEligibleInd());
	}

	private InventoryField createPlanting(AnnualFieldRsrc field, Integer plantingNumber, Integer cropYear) {
		InventoryField planting = new InventoryField();

		planting.setCropYear(cropYear);
		planting.setFieldId(field.getFieldId());
		planting.setInsurancePlanId(5);
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

	private InventoryCoverageTotalForage createInventoryCovarageTotalForage(
			String invContractGuid,
            Integer cropCommodityId, 
            String cropCommodityName,
			String plantInsurabilityTypeCode,
			String plantInsurabilityTypeDesc,
			Double totalFieldAcres,
			Boolean isUnseededInsurableInd) {
	
		InventoryCoverageTotalForage ictf = new InventoryCoverageTotalForage();

		ictf.setCropCommodityId(cropCommodityId);
		ictf.setCropCommodityName(cropCommodityName);
		ictf.setInventoryContractGuid(invContractGuid);
		ictf.setPlantInsurabilityTypeCode(plantInsurabilityTypeCode);
		ictf.setPlantInsurabilityTypeDesc(plantInsurabilityTypeDesc);
		ictf.setTotalFieldAcres(totalFieldAcres);
		ictf.setIsUnseededInsurableInd(isUnseededInsurableInd);

		return ictf;
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
	
	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}
}
