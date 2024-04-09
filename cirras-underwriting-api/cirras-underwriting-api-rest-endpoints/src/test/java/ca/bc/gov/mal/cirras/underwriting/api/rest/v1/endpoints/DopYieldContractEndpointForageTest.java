package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.LandManagementEventTypes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.PoliciesSyncEventTypes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.ValidationException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldDetailRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ContractedFieldDetailRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.DopYieldContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.FieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GrowerContractYearSyncRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GrowerRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.InventoryContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.PolicyRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UwContractListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UwContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldContractCommodityForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldForageCut;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryCoverageTotalForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventorySeededForage;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.common.wfone.rest.resource.RelLink;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class DopYieldContractEndpointForageTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(DopYieldContractEndpointForageTest.class);


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
		Scopes.CREATE_DOP_YIELD_CONTRACT,
		Scopes.GET_DOP_YIELD_CONTRACT
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

	String ctcAlfalfa = "Alfalfa";
	String ctcSilageCorn = "Silage Corn";
	Integer cropIdAlfalfa = 65;
	Integer cropIdSilageCorn = 71;
	Integer varietyIdAlfalafaGrass = 118;
	Integer varietyIdGrass = 223;
	Integer varietyIdSilageCorn = 1010863;
	String varietyNameAlfalafaGrass = "ALFALFA/GRASS";
	String varietyNameGrass = "GRASS";
	String varietyNameSilageCorn = "SILAGE CORN - UNSPECIFIED";

	
	@Test
	public void testDopYieldForageRollover() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testDopYieldForageRollover");
		
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

		AnnualFieldRsrc field = invContract.getFields().get(0);

		// Remove default planting.
		field.getPlantings().remove(0);
		
		// Planting 1
		InventoryField planting = createPlanting(field, 1, cropYear1);
		createInventorySeededForage(planting, cropIdAlfalfa, varietyIdAlfalafaGrass, ctcAlfalfa, true, 100.0); //Alfalfa Grass

		// Planting 2
		planting = createPlanting(field, 2, cropYear1);
		createInventorySeededForage(planting, cropIdAlfalfa, varietyIdGrass, ctcAlfalfa, false, 150.0); //Grass - Not insured
				
		// Planting 3
		planting = createPlanting(field, 3, cropYear1);
		createInventorySeededForage(planting, cropIdSilageCorn, varietyIdSilageCorn, ctcSilageCorn, true, 130.0); //Silage Corn

		// Planting 4
		planting = createPlanting(field, 4, cropYear1);
		createInventorySeededForage(planting, cropIdSilageCorn, varietyIdSilageCorn, ctcSilageCorn, true, 70.0); //Silage Corn		

		// Planting 5
		planting = createPlanting(field, 5, cropYear1);
		createInventorySeededForage(planting, null, null, null, true, 500.0); //No crop selected		

		InventoryContractRsrc fetchedInvContract = service.createInventoryContract(topLevelEndpoints, invContract);
		
		Assert.assertNotNull(fetchedInvContract);
		Assert.assertNotNull(fetchedInvContract.getFields());
		Assert.assertNotNull(fetchedInvContract.getFields().get(0).getPlantings());
		Assert.assertEquals(5, fetchedInvContract.getFields().get(0).getPlantings().size());

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
		
		//Rollover DOP
		Assert.assertNull(uwContractRsrc.getDeclaredYieldContractGuid());
		DopYieldContractRsrc newDyc = service.rolloverDopYieldContract(uwContractRsrc);
		
		Assert.assertNotNull(newDyc);
		Assert.assertNull(newDyc.getDeclaredYieldContractGuid());
		Assert.assertNotNull(newDyc.getDopYieldContractCommodityForageList());
		Assert.assertEquals(2, newDyc.getDopYieldContractCommodityForageList().size());
		Assert.assertNotNull(newDyc.getFields());
		Assert.assertEquals(1, newDyc.getFields().size());
		Assert.assertNotNull(newDyc.getFields().get(0).getDopYieldFieldForageList());
		Assert.assertEquals(5, newDyc.getFields().get(0).getDopYieldFieldForageList().size());
		
		//Check commodity totals
		Boolean alfaalfaChecked = false;
		Boolean silageCornChecked = false;
		for (DopYieldContractCommodityForage dyccf : newDyc.getDopYieldContractCommodityForageList()) {
			Assert.assertNotNull(dyccf.getCommodityTypeDescription());
			Assert.assertEquals(dyccf.getCommodityTypeDescription(), dyccf.getCommodityTypeCode());
			Assert.assertTrue("Unexpected Commodity Type" + dyccf.getCommodityTypeCode(), dyccf.getCommodityTypeCode().equals(ctcAlfalfa) || dyccf.getCommodityTypeCode().equals(ctcSilageCorn));
			if(dyccf.getCommodityTypeCode().equals(ctcAlfalfa)) {
				Assert.assertEquals((double)100, dyccf.getTotalFieldAcres().doubleValue(), 0.1);
				alfaalfaChecked = true;
			} else if(dyccf.getCommodityTypeCode().equals(ctcSilageCorn)) {
				Assert.assertEquals((double)200, dyccf.getTotalFieldAcres().doubleValue(), 0.1);
				silageCornChecked = true;
			} 
		}
		Assert.assertTrue(alfaalfaChecked);
		Assert.assertTrue(silageCornChecked);

		Boolean checkedPlanting1 = false;
		Boolean checkedPlanting2 = false;
		Boolean checkedPlanting3 = false;
		Boolean checkedPlanting4 = false;
		Boolean checkedPlanting5 = false;
		
		//Check Field level rollover
		for (DopYieldFieldForage dyff : newDyc.getFields().get(0).getDopYieldFieldForageList()) {
			Assert.assertEquals(fieldId1, dyff.getFieldId());
			
			if(dyff.getCropVarietyName() == null) {
				//Planting 5
				checkedPlanting5 = true;
				Assert.assertTrue(dyff.getIsQuantityInsurableInd());
				Assert.assertNull(dyff.getCommodityTypeCode());
				Assert.assertEquals(500.0, dyff.getFieldAcres().doubleValue(), 0.1);
			} else {
				if(dyff.getCropVarietyName().equals(varietyNameAlfalafaGrass)) {
					//Planting 1
					checkedPlanting1 = true;
					Assert.assertTrue(dyff.getIsQuantityInsurableInd());
					Assert.assertEquals(100.0, dyff.getFieldAcres().doubleValue(), 0.1);
					Assert.assertEquals(ctcAlfalfa, dyff.getCommodityTypeCode());
					Assert.assertEquals(ctcAlfalfa, dyff.getCommodityTypeDescription());
				} else if(dyff.getCropVarietyName().equals(varietyNameGrass)) {
					//Planting 2
					checkedPlanting2 = true;
					Assert.assertFalse(dyff.getIsQuantityInsurableInd());
					Assert.assertEquals(150.0, dyff.getFieldAcres().doubleValue(), 0.1);
					Assert.assertEquals(ctcAlfalfa, dyff.getCommodityTypeCode());
					Assert.assertEquals(ctcAlfalfa, dyff.getCommodityTypeDescription());
				} else if(dyff.getCropVarietyName().equals(varietyNameSilageCorn)) {
					Assert.assertTrue(dyff.getIsQuantityInsurableInd());
					Assert.assertEquals(ctcSilageCorn, dyff.getCommodityTypeCode());
					Assert.assertEquals(ctcSilageCorn, dyff.getCommodityTypeDescription());
					if(dyff.getFieldAcres().equals(130.0)) {
						//Planting 3
						checkedPlanting3 = true;
					} else if(dyff.getFieldAcres().equals(70.0)) {
						//Planting 4
						checkedPlanting4 = true;
					} else {
						Assert.fail("Unexpected field acres: " + dyff.getFieldAcres());
					}
				}
			}
		}
		
		Assert.assertTrue(checkedPlanting1);
		Assert.assertTrue(checkedPlanting2);
		Assert.assertTrue(checkedPlanting3);
		Assert.assertTrue(checkedPlanting4);
		Assert.assertTrue(checkedPlanting5);
		
		//Create DOP Contract
		
		delete();
		
		logger.debug(">testDopYieldForageRollover");

	}

	@Test
	public void testInsertUpdateDeleteDopYieldForageContract() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testInsertUpdateDeleteDopYieldForageContract");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		//TODO: Create inventory and contract data once insert, update and delete is implemented
		//createContractAndInventory();
		
		/* *********************************************************
	    CREATE DOP TEST DATA IN THE MEANWHILE
	    
	    --Insert dop contract
			insert into declared_yield_contract(declared_yield_contract_guid, contract_id, crop_year, declaration_of_production_date, dop_update_timestamp, dop_update_user, entered_yield_meas_unit_type_code, default_yield_meas_unit_type_code, grain_from_other_source_ind, baler_wagon_info, total_livestock, create_user, create_date, update_user, update_date)
				values ('dyc1aa456789-2024', 3347, 2024, '2024-01-15', '2024-02-16 17:45:55', 'JSMITH', 'TON', 'TON', 'N', 'Test Baler', 100, 'admin', now(), 'admin', now());
	  	--Insert dop contract commodity forage
			insert into declared_yield_contract_cmdty_forage(declared_yield_contract_cmdty_forage_guid, declared_yield_contract_guid, commodity_type_code, total_field_acres, harvested_acres, harvested_acres_override, quantity_harvested_tons, quantity_harvested_tons_override, yield_per_acre, create_user, create_date, update_user, update_date)
				values ('dyccf145-2024-1', 'dyc1aa456789-2024', 'Alfalfa', 100, 50, 55, 40, 45, 1.5, 'admin', now(), 'admin', now());
		  
		--Insert dop field forage
			insert into declared_yield_field_forage(
					declared_yield_field_forage_guid, 
					inventory_field_guid, 
					cut_number, 
					total_bales_loads, 
					weight, 
					weight_default_unit, 
					moisture_percent, 
					create_user, create_date, update_user, update_date)
				VALUES (
					'dyff-123443-1', (SELECT inv.inventory_field_guid FROM inventory_field inv WHERE inv.field_id = 1034218 AND inv.crop_year = 2024),
					1, 10, 15.0, 1.5, 20.0, 'admin', now(), 'admin', now());
		
		  
		  
		select * from declared_yield_contract where declared_yield_contract_guid = 'dyc1aa456789-2024'
		select * from declared_yield_contract_cmdty_forage where declared_yield_contract_guid = 'dyc1aa456789-2024';
		select * from declared_yield_field_forage where inventory_field_guid = (SELECT inv.inventory_field_guid FROM inventory_field inv WHERE inv.field_id = 1034218 AND inv.crop_year = 2024)
		
		*/
		
		//Use this until insert and delete is implemented
		String tempPolicyNumber = "100438-24"; //ContractId: 3347

		
		//Get uw contract
		UwContractListRsrc searchResults = service.getUwContractList(
				topLevelEndpoints, 
				null, 
				null, 
				null,
				null,
				tempPolicyNumber,//policyNumber1,
				null,
				null, 
				null, 
				null, 
				1, 20);

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(1, searchResults.getCollection().size());

		UwContractRsrc referrer = searchResults.getCollection().get(0);
		
		//Rollover DOP 
		//-> Used once everything is implemented
//		Assert.assertNull(uwContractRsrc.getDeclaredYieldContractGuid());
//		DopYieldContractRsrc newDyc = service.rolloverDopYieldContract(uwContractRsrc);
//		
//		Assert.assertNotNull(newDyc);
//		Assert.assertNull(newDyc.getDeclaredYieldContractGuid());
		
		//****************************************************************
		//THE FOLLOWING ASSERTS ARE TEMPORARY AND RELY ON EXISTING DATA
		//THEY WILL BE REPLACED ONCE INSERT AND DELETE IS IMPLEMENTED
		//****************************************************************

		//Get DOP contract
		DopYieldContractRsrc dopYldContract = service.getDopYieldContract(referrer);
		Assert.assertNotNull(dopYldContract);
		Assert.assertNotNull(dopYldContract.getDopYieldContractCommodityForageList());
		Assert.assertEquals(1, dopYldContract.getDopYieldContractCommodityForageList().size());
		Assert.assertNotNull(dopYldContract.getDopYieldContractCommodityForageList().get(0));
		
		Assert.assertEquals("ContractId", referrer.getContractId(), dopYldContract.getContractId());
		Assert.assertEquals("CropYear", referrer.getCropYear(), dopYldContract.getCropYear());

		//Check new contract level fields
		Assert.assertEquals("BalerWagonInfo", "Test Baler", dopYldContract.getBalerWagonInfo());
		Assert.assertEquals("TotalLivestock", 100.0, dopYldContract.getTotalLivestock().doubleValue(), 0.1);
		
		DopYieldContractCommodityForage dopCmdty = dopYldContract.getDopYieldContractCommodityForageList().get(0);

		Assert.assertEquals("DeclaredYieldContractCmdtyForageGuid", "dyccf145-2024-1", dopCmdty.getDeclaredYieldContractCmdtyForageGuid());
		Assert.assertEquals("DeclaredYieldContractGuid", "dyc1aa456789-2024", dopCmdty.getDeclaredYieldContractGuid());
		Assert.assertEquals("CommodityTypeCode", "Alfalfa", dopCmdty.getCommodityTypeCode());
		Assert.assertEquals("CommodityTypeDescription" , "Alfalfa", dopCmdty.getCommodityTypeDescription());
		Assert.assertEquals("TotalFieldAcres", 100.0, dopCmdty.getTotalFieldAcres(), 0.1);
		Assert.assertEquals("HarvestedAcres", 50.0, dopCmdty.getHarvestedAcres(), 0.1);
		Assert.assertEquals("HarvestedAcresOverride", 55.0, dopCmdty.getHarvestedAcresOverride(), 0.1);
		Assert.assertEquals("QuantityHarvestedTons", 40.0, dopCmdty.getQuantityHarvestedTons(), 0.1);
		Assert.assertEquals("QuantityHarvestedTonsOverride", 45.0, dopCmdty.getQuantityHarvestedTonsOverride(), 0.1);
		Assert.assertEquals("YieldPerAcre", 1.5, dopCmdty.getYieldPerAcre(), 0.1);
		
		//Check field forage data
		for (AnnualFieldRsrc field : dopYldContract.getFields()) {
			if(field.getFieldId().equals(1034218)) {
				Assert.assertEquals(1, field.getDopYieldFieldForageList().size());
				
				DopYieldFieldForage dopYieldFieldForage = field.getDopYieldFieldForageList().get(0);
				
				Assert.assertNotNull("InventoryFieldGuid", dopYieldFieldForage.getInventoryFieldGuid());
				Assert.assertEquals("CommodityTypeCode", "Alfalfa", dopYieldFieldForage.getCommodityTypeCode());
				Assert.assertEquals("CommodityTypeDescription", "Alfalfa", dopYieldFieldForage.getCommodityTypeDescription());
				Assert.assertEquals("IsQuantityInsurableInd", true, dopYieldFieldForage.getIsQuantityInsurableInd());
				Assert.assertEquals("FieldAcres", 125.0, dopYieldFieldForage.getFieldAcres().doubleValue(), 0.1);
				Assert.assertEquals("CropVarietyName", "ALFALFA", dopYieldFieldForage.getCropVarietyName());
				
				Assert.assertEquals(1, dopYieldFieldForage.getDopYieldFieldForageCuts().size());
				DopYieldFieldForageCut cut = dopYieldFieldForage.getDopYieldFieldForageCuts().get(0);

				Assert.assertNotNull("DeclaredYieldFieldForageGuid", cut.getDeclaredYieldFieldForageGuid());
				Assert.assertEquals("InventoryFieldGuid", dopYieldFieldForage.getInventoryFieldGuid(), cut.getInventoryFieldGuid());
				Assert.assertEquals("CutNumber", 1, cut.getCutNumber().intValue());
				Assert.assertEquals("TotalBalesLoads", 10, cut.getTotalBalesLoads().intValue());
				Assert.assertEquals("Weight", 15.0, cut.getWeight().doubleValue(), 0.1);
				Assert.assertEquals("WeightDefaultUnit", 1.5, cut.getWeightDefaultUnit().doubleValue(), 0.1);
				Assert.assertEquals("MoisturePercent", 20.0, cut.getMoisturePercent().doubleValue(), 0.1);

			}
		}
		
		
		
		/* *********************************************************
		  delete data

	    delete from declared_yield_contract_cmdty_forage where declared_yield_contract_guid = 'dyc1aa456789-2024';
		delete from declared_yield_contract where declared_yield_contract_guid = 'dyc1aa456789-2024';
		delete from declared_yield_field_forage where inventory_field_guid = (SELECT inv.inventory_field_guid FROM inventory_field inv WHERE inv.field_id = 1034218 AND inv.crop_year = 2024);
		
		*/
		
		//delete();
		
		logger.debug(">testInsertUpdateDeleteDopYieldForageContract");

	}

	protected void createContractAndInventory() throws ValidationException, CirrasUnderwritingServiceException {
		createGrower();
		createPolicy();
		createGrowerContractYear();

		createLegalLand();
		createField();
		createAnnualFieldDetail();
		createContractedFieldDetail();
		
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
				1, 20);

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(1, searchResults.getCollection().size());

		UwContractRsrc referrer = searchResults.getCollection().get(0);
		Assert.assertNull(referrer.getInventoryContractGuid());
		
		InventoryContractRsrc invContract = service.rolloverInventoryContract(referrer);
		Assert.assertNotNull(invContract);
		Assert.assertNotNull(invContract.getFields());
		Assert.assertNotNull(invContract.getFields().get(0).getPlantings());
		Assert.assertNotNull(invContract.getFields().get(0).getPlantings().get(0).getInventorySeededForages());

		AnnualFieldRsrc field = invContract.getFields().get(0);

		// Remove default planting.
		field.getPlantings().remove(0);
		
		// Planting 1
		InventoryField planting = createPlanting(field, 1, cropYear1);
		createInventorySeededForage(planting, cropIdAlfalfa, varietyIdAlfalafaGrass, ctcAlfalfa, true, 100.0); //Alfalfa Grass

		// Planting 2
		planting = createPlanting(field, 2, cropYear1);
		createInventorySeededForage(planting, cropIdAlfalfa, varietyIdGrass, ctcAlfalfa, false, 150.0); //Grass - Not insured
				
		// Planting 3
		planting = createPlanting(field, 3, cropYear1);
		createInventorySeededForage(planting, cropIdSilageCorn, varietyIdSilageCorn, ctcSilageCorn, true, 130.0); //Silage Corn

		// Planting 4
		planting = createPlanting(field, 4, cropYear1);
		createInventorySeededForage(planting, cropIdSilageCorn, varietyIdSilageCorn, ctcSilageCorn, true, 70.0); //Silage Corn		

		// Planting 5
		planting = createPlanting(field, 5, cropYear1);
		createInventorySeededForage(planting, null, null, null, true, 500.0); //No crop selected		

		InventoryContractRsrc fetchedInvContract = service.createInventoryContract(topLevelEndpoints, invContract);
		
		Assert.assertNotNull(fetchedInvContract);
		Assert.assertNotNull(fetchedInvContract.getFields());
		Assert.assertNotNull(fetchedInvContract.getFields().get(0).getPlantings());
		Assert.assertEquals(5, fetchedInvContract.getFields().get(0).getPlantings().size());
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
            Integer cropVarietyId,
            String commodityTypeCode,
			Boolean isQuantityInsurableInd,
			Double fieldAcres) {
		
		InventorySeededForage isf = new InventorySeededForage();


		isf.setCommodityTypeCode(commodityTypeCode);
		isf.setCropCommodityId(cropCommodityId);
		isf.setCropVarietyId(cropVarietyId);
		isf.setCropVarietyName(null);
		isf.setFieldAcres(fieldAcres);
		isf.setInventoryFieldGuid(planting.getInventoryFieldGuid());
		isf.setIsAwpEligibleInd(false);
		isf.setIsIrrigatedInd(false);
		isf.setIsQuantityInsurableInd(isQuantityInsurableInd);
		isf.setPlantInsurabilityTypeCode(null);
		isf.setSeedingYear(planting.getCropYear() - 3);
		
		planting.getInventorySeededForages().add(isf);

		return isf;
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
