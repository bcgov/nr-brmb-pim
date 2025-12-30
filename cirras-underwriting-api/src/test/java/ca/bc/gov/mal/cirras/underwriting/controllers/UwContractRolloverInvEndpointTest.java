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
import ca.bc.gov.mal.cirras.underwriting.data.resources.PoliciesSyncEventTypes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.PolicyRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.UwContractListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.UwContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.test.EndpointsTest;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryField;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventorySeededForage;
import ca.bc.gov.mal.cirras.underwriting.data.models.UnderwritingComment;
import ca.bc.gov.mal.cirras.underwriting.services.utils.LandManagementEventTypes;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class UwContractRolloverInvEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(UwContractRolloverInvEndpointTest.class);


	private static final String[] SCOPES = {
			Scopes.GET_TOP_LEVEL, 
			Scopes.SEARCH_UWCONTRACTS,
			Scopes.SEARCH_ANNUAL_FIELDS,
			Scopes.CREATE_INVENTORY_CONTRACT,
			Scopes.DELETE_INVENTORY_CONTRACT,
			Scopes.GET_INVENTORY_CONTRACT,
			Scopes.UPDATE_INVENTORY_CONTRACT,
			Scopes.CREATE_SYNC_UNDERWRITING,
			Scopes.UPDATE_SYNC_UNDERWRITING,
			Scopes.DELETE_SYNC_UNDERWRITING,
			Scopes.GET_GROWER,
			Scopes.GET_POLICY,
			Scopes.GET_LAND,
			Scopes.GET_LEGAL_LAND,
		};
	
	
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;

	private Integer contractId = 90000001;
	private Integer policyId1 = 90000002;
	private String policyNumber1 = "998877-22";
	private Integer cropYear1 = 2022;
	private String contractNumber = "998877";
	private Integer growerId = 90000003;
	private Integer growerContractYearId1 = 999999999;

	
	private Integer policyId2 = 90002502;
	private String policyNumber2 = "998877-21";
	private Integer cropYear2 = 2021;
	private Integer growerContractYearId2 = 90000004;
	
	private Integer legalLandId = 90000015;
	private Integer fieldId = 90000016;
	private Integer annualFieldDetailId1 = 90000017;
	private Integer contractedFieldDetailId1 = 90000018;
	private Integer annualFieldDetailId2 = 90000019;
	private Integer contractedFieldDetailId2 = 90000020;	


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
	
	private void delete() throws NotFoundDaoException, DaoException, CirrasUnderwritingServiceException {

		deleteInventoryContract(policyNumber2);
				
		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId1.toString());
		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId2.toString());
		
		service.deleteGrowerContractYear(topLevelEndpoints, growerContractYearId1.toString());
		service.deleteGrowerContractYear(topLevelEndpoints, growerContractYearId2.toString());

		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId1.toString());
		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId2.toString());

		service.deleteField(topLevelEndpoints, fieldId.toString());
		service.deleteLegalLandSync(topLevelEndpoints, legalLandId.toString());

		service.deletePolicy(topLevelEndpoints, policyId1.toString());
		service.deletePolicy(topLevelEndpoints, policyId2.toString());
		service.deleteGrower(topLevelEndpoints, growerId.toString());	
	}
	
	
	public void deleteInventoryContract(String policyNumber)
			throws CirrasUnderwritingServiceException {
		
		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(20);
		
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
				pageNumber, 
				pageRowCount);
		
		if ( searchResults.getCollection() != null && searchResults.getCollection().size() == 1 ) {
			UwContractRsrc referrer = searchResults.getCollection().get(0);
						
			if ( referrer.getInventoryContractGuid() != null ) { 
				InventoryContractRsrc invContract = service.getInventoryContract(referrer);
				service.deleteInventoryContract(invContract);
			}
		}
	}
	
	
	@Test
	public void testRolloverGrainInventoryContract() throws CirrasUnderwritingServiceException, Oauth2ClientException {
		logger.debug("<testRolloverGrainInventoryContract");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasUnderwritingService service = getService(SCOPES);

		//*****************************************************************
		//BEFORE TESTS: CREATE LAND AND INVENTORY BY RUNNING THE INSERT SCRIPTS BELOW
		//*****************************************************************
		
		String policyNumber = "999888-22";
		Integer fieldId = 123456999;
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();
		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(20);

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
				pageNumber, pageRowCount);

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(1, searchResults.getCollection().size());

		UwContractRsrc referrer = searchResults.getCollection().get(0);
		Assert.assertNull(referrer.getInventoryContractGuid());

		InventoryContractRsrc invContract = service.rolloverInventoryContract(referrer);
		Assert.assertNotNull(invContract);
		
		Assert.assertEquals(referrer.getContractId(), invContract.getContractId());
		Assert.assertEquals(referrer.getCropYear(), invContract.getCropYear());
		Assert.assertEquals(false, invContract.getFertilizerInd());
		Assert.assertNull(invContract.getGrainFromPrevYearInd()); //No default is set anymore
		Assert.assertEquals(false, invContract.getHerbicideInd());
		Assert.assertEquals(null, invContract.getInventoryContractGuid());
		Assert.assertEquals(null, invContract.getOtherChangesComment());
		Assert.assertEquals(false, invContract.getOtherChangesInd());
		Assert.assertEquals(false, invContract.getSeededCropReportSubmittedInd());
		Assert.assertEquals(false, invContract.getTilliageInd());
		Assert.assertEquals(false, invContract.getUnseededIntentionsSubmittedInd());
		
		List<AnnualFieldRsrc> fields = invContract.getFields();
		Assert.assertNotNull(fields);
		Assert.assertEquals("More than one field returned", 1, fields.size());
		
		AnnualFieldRsrc field = fields.get(0);
		Assert.assertEquals("field doesn't match", fieldId, field.getFieldId());

		
		Assert.assertEquals(987652022, field.getAnnualFieldDetailId().intValue());
		Assert.assertEquals(888882022, field.getContractedFieldDetailId().intValue());
		Assert.assertEquals(invContract.getCropYear(), field.getCropYear());
		Assert.assertEquals(1, field.getDisplayOrder().intValue());
		
		Assert.assertNotNull(field.getPlantings());
		Assert.assertEquals("More plantings than expected returned", 7, field.getPlantings().size());
		
		for (InventoryField invField: field.getPlantings()) {

			Assert.assertFalse("is hidden on printout ind is not false", invField.getIsHiddenOnPrintoutInd());

			Integer lastYearCmdtyId = invField.getLastYearCropCommodityId();
			Integer lastYearVrtyId = invField.getLastYearCropVarietyId();

			//Check if commodity is oat or canola
			Assert.assertTrue("commodity id ("+lastYearCmdtyId+") not 24, 18, 65, 71, 1010893 or none", lastYearCmdtyId == null 
					|| lastYearCmdtyId.equals(24) || lastYearCmdtyId.equals(18)
					|| lastYearCmdtyId.equals(65)|| lastYearCmdtyId.equals(71)
					|| lastYearCmdtyId.equals(1010893));
			
			Assert.assertNull("Unseeded crop commodity not null", invField.getInventoryUnseeded().getCropCommodityId());
			
			//Check acres according to commodity
			if(lastYearCmdtyId == null) {
				//None
				Assert.assertNull("None", invField.getLastYearCropCommodityName());
				Assert.assertNull(invField.getLastYearCropVarietyName());
				Assert.assertNull(invField.getLastYearCropVarietyId());
				Assert.assertEquals("None not correct acres", (Double)30.0, invField.getInventoryUnseeded().getAcresToBeSeeded());
				Assert.assertTrue("Unseeded Insurable not true", invField.getInventoryUnseeded().getIsUnseededInsurableInd());
			} else if(lastYearCmdtyId.equals(24)) {
				//Oat
				Assert.assertEquals("OAT", invField.getLastYearCropCommodityName());
				Assert.assertNull(invField.getLastYearCropVarietyName());
				Assert.assertNull(invField.getLastYearCropVarietyId());
				Assert.assertEquals("Oat not correct acres", (Double)25.0, invField.getInventoryUnseeded().getAcresToBeSeeded());
				Assert.assertTrue("Unseeded Insurable not true", invField.getInventoryUnseeded().getIsUnseededInsurableInd());
			} else if (lastYearCmdtyId.equals(18)) {
				//Canola
				Assert.assertEquals("CANOLA", invField.getLastYearCropCommodityName());
				Assert.assertNull(invField.getLastYearCropVarietyName());
				Assert.assertNull(invField.getLastYearCropVarietyId());
				Assert.assertEquals("Canola not correct acres", (Double)50.0, invField.getInventoryUnseeded().getAcresToBeSeeded());
				Assert.assertTrue("Unseeded Insurable not true", invField.getInventoryUnseeded().getIsUnseededInsurableInd());
			} else if (lastYearCmdtyId.equals(65) && lastYearVrtyId != null && lastYearVrtyId.equals(119)) {
				//Forage - From underseeded Alfalfa
				Assert.assertEquals("ALFALFA", invField.getLastYearCropVarietyName());
				Assert.assertEquals(119, invField.getLastYearCropVarietyId().intValue());
				Assert.assertEquals("FORAGE", invField.getLastYearCropCommodityName());
				Assert.assertEquals("FORAGE not correct acres", (Double)58.0, invField.getInventoryUnseeded().getAcresToBeSeeded());
				Assert.assertFalse("Unseeded Insurable not false", invField.getInventoryUnseeded().getIsUnseededInsurableInd());
			} else if (lastYearCmdtyId.equals(71)) {
				//Silage Corn - From underseeded SILAGE CORN - UNSPECIFIED
				Assert.assertEquals("SILAGE CORN - UNSPECIFIED", invField.getLastYearCropVarietyName());
				Assert.assertEquals(1010863, invField.getLastYearCropVarietyId().intValue());
				Assert.assertEquals("SILAGE CORN", invField.getLastYearCropCommodityName());
				Assert.assertEquals("SILAGE CORN not correct acres", (Double)34.0, invField.getInventoryUnseeded().getAcresToBeSeeded());
				Assert.assertTrue("Unseeded Insurable not true", invField.getInventoryUnseeded().getIsUnseededInsurableInd());
			} else if (lastYearCmdtyId.equals(65) && lastYearVrtyId != null && lastYearVrtyId.equals(220)) {
				//Underseeded forage seeded with CLOVER/GRASS
				Assert.assertEquals("CLOVER/GRASS", invField.getLastYearCropVarietyName());
				Assert.assertEquals(220, invField.getLastYearCropVarietyId().intValue());
				Assert.assertEquals("FORAGE", invField.getLastYearCropCommodityName());
				Assert.assertEquals("FORAGE CLOVER/GRASS not correct acres", (Double)90.0, invField.getInventoryUnseeded().getAcresToBeSeeded());
				Assert.assertFalse("IsGrainUnseededDefaultInd", invField.getInventoryUnseeded().getIsUnseededInsurableInd());
			} else if (lastYearCmdtyId.equals(1010893)) {
				//FORAGE SEED - Brome
				Assert.assertEquals("FORAGE SEED", invField.getLastYearCropCommodityName());
				Assert.assertEquals("BROME", invField.getLastYearCropVarietyName());
				Assert.assertEquals(1010999, invField.getLastYearCropVarietyId().intValue());
				Assert.assertEquals("FORAGE SEED not correct acres", (Double)28.0, invField.getInventoryUnseeded().getAcresToBeSeeded());
				Assert.assertFalse("IsGrainUnseededDefaultInd", invField.getInventoryUnseeded().getIsUnseededInsurableInd()); //Expected false because it's a forage commodity
			}
		}

		//*****************************************************************
		//AFTER TESTS: DELETE LAND AND INVENTORY BY RUNNING THE DELETE SCRIPTS BELOW
		//*****************************************************************


		logger.debug(">testRolloverGrainInventoryContract");
	
	/* Creates: 
	 * - A field and a legal land
	 * - Two policies (one for 2021 and 2022) for the same contract (policy and grower contract year records)
	 * - annual field tables (contracted field details and annual field details records)
	 * - Inventory plantings for 2021
	 * 
	*****************
	INSERT STATEMENTS
	*****************

	INSERT INTO cuws.legal_land(legal_land_id, primary_reference_type_code, legal_description, legal_short_description, other_description, active_from_crop_year, active_to_crop_year, create_user, create_date, update_user, update_date, primary_property_identifier)
	VALUES (987654321, 'OTHER', 'Test Legal', 'Test Legal Short', 'Test Other', 1980, null, 'admin', now(), 'admin', now(), '123456723');
	 
	INSERT INTO cuws.field(field_id, field_label, active_from_crop_year, active_to_crop_year, create_user, create_date, update_user, update_date)
	VALUES (123456999, 'Test Field', 1980, null, 'admin', now(), 'admin', now());
	
	--target year policy and land ************************************
	INSERT INTO cuws.policy(policy_id, grower_id, insurance_plan_id, policy_status_code, office_id, policy_number, contract_number, contract_id, crop_year, data_sync_trans_date, create_user, create_date, update_user, update_date)
	VALUES (999888777, 4, 4, 'ACTIVE', 1, '999888-22', '999888', 999888777, 2022, now(), 'admin', now(), 'admin', now());

	INSERT INTO cuws.grower_contract_year(grower_contract_year_id, grower_id, insurance_plan_id, contract_id, crop_year, data_sync_trans_date, create_user, create_date, update_user, update_date)
	VALUES (999992022, 4, 4, 999888777, 2022, now(), 'admin', now(), 'admin', now());

	INSERT INTO cuws.annual_field_detail(annual_field_detail_id, legal_land_id, field_id, crop_year, create_user, create_date, update_user, update_date)
	VALUES (987652022, 987654321, 123456999, 2022, 'admin', now(), 'admin', now());
	
	INSERT INTO cuws.contracted_field_detail(contracted_field_detail_id, annual_field_detail_id, grower_contract_year_id, display_order, create_user, create_date, update_user, update_date)
	VALUES (888882022, 987652022, 999992022, 1, 'admin', now(), 'admin', now());
	
	--source year policy and land ************************************ 
	INSERT INTO cuws.policy(policy_id, grower_id, insurance_plan_id, policy_status_code, office_id, policy_number, contract_number, contract_id, crop_year, data_sync_trans_date, create_user, create_date, update_user, update_date)
	VALUES (999888111, 4, 4, 'ACTIVE', 1, '999888-21', '999888', 999888777, 2021, now(), 'admin', now(), 'admin', now());

	INSERT INTO cuws.grower_contract_year(grower_contract_year_id, grower_id, insurance_plan_id, contract_id, crop_year, data_sync_trans_date, create_user, create_date, update_user, update_date)
	VALUES (999992021, 4, 4, 999888777, 2021, now(), 'admin', now(), 'admin', now());

	INSERT INTO cuws.annual_field_detail(annual_field_detail_id, legal_land_id, field_id, crop_year, create_user, create_date, update_user, update_date)
	VALUES (987652021, 987654321, 123456999, 2021, 'admin', now(), 'admin', now());
	
	INSERT INTO cuws.contracted_field_detail(contracted_field_detail_id, annual_field_detail_id, grower_contract_year_id, display_order, create_user, create_date, update_user, update_date)
	VALUES (888882021, 987652021, 999992021, 1, 'admin', now(), 'admin', now());
	
	--Inventory Field
	INSERT INTO cuws.inventory_field(inventory_field_guid, insurance_plan_id, field_id, last_year_crop_commodity_id, crop_year, planting_number, create_user, create_date, update_user, update_date, is_hidden_on_printout_ind)
	VALUES ('AbcDeFg12394039485-1', 4, 123456999, null, 2021, 1, 'admin', now(), 'admin', now(), 'N');
	
	INSERT INTO cuws.inventory_field(inventory_field_guid, insurance_plan_id, field_id, last_year_crop_commodity_id, crop_year, planting_number, create_user, create_date, update_user, update_date, is_hidden_on_printout_ind)
	VALUES ('AbcDeFg12394039485-2', 4, 123456999, null, 2021, 2, 'admin', now(), 'admin', now(), 'N');

	INSERT INTO cuws.inventory_field(inventory_field_guid, insurance_plan_id, field_id, last_year_crop_commodity_id, crop_year, planting_number, create_user, create_date, update_user, update_date, is_hidden_on_printout_ind)
	VALUES ('AbcDeFg12394039485-3', 4, 123456999, null, 2021, 3, 'admin', now(), 'admin', now(), 'Y');

	INSERT INTO cuws.inventory_field(inventory_field_guid, insurance_plan_id, field_id, last_year_crop_commodity_id, crop_year, planting_number, underseeded_crop_variety_id, underseeded_acres, create_user, create_date, update_user, update_date, is_hidden_on_printout_ind)
	VALUES ('AbcDeFg12394039485-4', 4, 123456999, null, 2021, 4, 119, 58, 'admin', now(), 'admin', now(), 'Y');

	INSERT INTO cuws.inventory_field(inventory_field_guid, insurance_plan_id, field_id, last_year_crop_commodity_id, crop_year, planting_number, underseeded_crop_variety_id, underseeded_acres, create_user, create_date, update_user, update_date, is_hidden_on_printout_ind)
	VALUES ('AbcDeFg12394039485-5', 4, 123456999, null, 2021, 5, 1010863, 34, 'admin', now(), 'admin', now(), 'Y');

	INSERT INTO cuws.inventory_field(inventory_field_guid, insurance_plan_id, field_id, last_year_crop_commodity_id, crop_year, planting_number, create_user, create_date, update_user, update_date, is_hidden_on_printout_ind)
	VALUES ('AbcDeFg12394039485-6', 4, 123456999, null, 2021, 6, 'admin', now(), 'admin', now(), 'Y');

	INSERT INTO cuws.inventory_field(inventory_field_guid, insurance_plan_id, field_id, last_year_crop_commodity_id, crop_year, planting_number, underseeded_crop_variety_id, underseeded_acres, create_user, create_date, update_user, update_date, is_hidden_on_printout_ind)
	VALUES ('AbcDeFg12394039485-7', 4, 123456999, null, 2021, 7, 220, 50, 'admin', now(), 'admin', now(), 'Y');

	INSERT INTO cuws.inventory_field(inventory_field_guid, insurance_plan_id, field_id, last_year_crop_commodity_id, crop_year, planting_number, underseeded_crop_variety_id, underseeded_acres, create_user, create_date, update_user, update_date, is_hidden_on_printout_ind)
	VALUES ('AbcDeFg12394039485-8', 4, 123456999, null, 2021, 8, 220, 40, 'admin', now(), 'admin', now(), 'Y');


	--Plantings
	INSERT INTO cuws.inventory_seeded_grain (inventory_seeded_grain_guid, inventory_field_guid, crop_commodity_id, crop_variety_id, commodity_type_code, is_quantity_insurable_ind, is_replaced_ind, is_pedigree_ind, seeding_date, seeded_acres, is_spot_loss_insurable_ind, create_user, create_date, update_user, update_date)
	VALUES (replace(cast(gen_random_uuid() as text), '-', ''), 'AbcDeFg12394039485-1', 24,  1010570,  'Forage Oat',  'N',  'N',  'N',  null,  25, 'N', 'admin', now(), 'admin', now());
	INSERT INTO cuws.inventory_seeded_grain (inventory_seeded_grain_guid, inventory_field_guid, crop_commodity_id, crop_variety_id, commodity_type_code, is_quantity_insurable_ind, is_replaced_ind, is_pedigree_ind, seeding_date, seeded_acres, is_spot_loss_insurable_ind, create_user, create_date, update_user, update_date)
	VALUES (replace(cast(gen_random_uuid() as text), '-', ''), 'AbcDeFg12394039485-1', 26,  1010603,  'CPSW',  'N',  'Y',  'N',  null,  0, 'N', 'admin', now(), 'admin', now());
	INSERT INTO cuws.inventory_seeded_grain (inventory_seeded_grain_guid, inventory_field_guid, crop_commodity_id, crop_variety_id, commodity_type_code, is_quantity_insurable_ind, is_replaced_ind, is_pedigree_ind, seeding_date, seeded_acres, is_spot_loss_insurable_ind, create_user, create_date, update_user, update_date)
	VALUES (replace(cast(gen_random_uuid() as text), '-', ''), 'AbcDeFg12394039485-2', 18,  1010940,  'Polish Canola',  'N',  'N',  'N',  null,  40, 'N', 'admin', now(), 'admin', now());
	INSERT INTO cuws.inventory_seeded_grain (inventory_seeded_grain_guid, inventory_field_guid, crop_commodity_id, crop_variety_id, commodity_type_code, is_quantity_insurable_ind, is_replaced_ind, is_pedigree_ind, seeding_date, seeded_acres, is_spot_loss_insurable_ind, create_user, create_date, update_user, update_date)
	VALUES (replace(cast(gen_random_uuid() as text), '-', ''), 'AbcDeFg12394039485-3', null,  null,  null,  'N',  'N',  'N',  null,  30, 'N', 'admin', now(), 'admin', now());
	INSERT INTO cuws.inventory_seeded_grain (inventory_seeded_grain_guid, inventory_field_guid, crop_commodity_id, crop_variety_id, commodity_type_code, is_quantity_insurable_ind, is_replaced_ind, is_pedigree_ind, seeding_date, seeded_acres, is_spot_loss_insurable_ind, create_user, create_date, update_user, update_date)
	VALUES (replace(cast(gen_random_uuid() as text), '-', ''), 'AbcDeFg12394039485-3', 18,  1010471,  'Argentine Canola',  'N',  'Y',  'N',  null,  10, 'N', 'admin', now(), 'admin', now());
	INSERT INTO cuws.inventory_seeded_grain (inventory_seeded_grain_guid, inventory_field_guid, crop_commodity_id, crop_variety_id, commodity_type_code, is_quantity_insurable_ind, is_replaced_ind, is_pedigree_ind, seeding_date, seeded_acres, is_spot_loss_insurable_ind, create_user, create_date, update_user, update_date)
	VALUES (replace(cast(gen_random_uuid() as text), '-', ''), 'AbcDeFg12394039485-4', 18,  1010471,  'Argentine Canola',  'N',  'Y',  'N',  null,  100, 'N', 'admin', now(), 'admin', now());
	INSERT INTO cuws.inventory_seeded_grain (inventory_seeded_grain_guid, inventory_field_guid, crop_commodity_id, crop_variety_id, commodity_type_code, is_quantity_insurable_ind, is_replaced_ind, is_pedigree_ind, seeding_date, seeded_acres, is_spot_loss_insurable_ind, create_user, create_date, update_user, update_date)
	VALUES (replace(cast(gen_random_uuid() as text), '-', ''), 'AbcDeFg12394039485-5', 18,  null, null,  'N',  'Y',  'N',  null,  null, 'N', 'admin', now(), 'admin', now());
	--Variety: BROME Commodity: FORAGE SEED
	INSERT INTO cuws.inventory_seeded_grain (inventory_seeded_grain_guid, inventory_field_guid, crop_commodity_id, crop_variety_id, commodity_type_code, is_quantity_insurable_ind, is_replaced_ind, is_pedigree_ind, seeding_date, seeded_acres, is_spot_loss_insurable_ind, create_user, create_date, update_user, update_date)
	VALUES (replace(cast(gen_random_uuid() as text), '-', ''), 'AbcDeFg12394039485-6', 1010893,  1010999, 'Forage Seed',  'N',  'Y',  'N',  null,  28, 'N', 'admin', now(), 'admin', now());
	--Variety: TIMOTHY Commodity: FORAGE SEED -> Underseeded with Clover/Grass
	INSERT INTO cuws.inventory_seeded_grain (inventory_seeded_grain_guid, inventory_field_guid, crop_commodity_id, crop_variety_id, commodity_type_code, is_quantity_insurable_ind, is_replaced_ind, is_pedigree_ind, seeding_date, seeded_acres, is_spot_loss_insurable_ind, create_user, create_date, update_user, update_date)
	VALUES (replace(cast(gen_random_uuid() as text), '-', ''), 'AbcDeFg12394039485-7', 1010893,  1010998, 'Forage Seed',  'N',  'Y',  'N',  null,  17.5, 'N', 'admin', now(), 'admin', now());
	--Grain commodity with underseeded Clover/Grass -> Should merge with AbcDeFg12394039485-7
	INSERT INTO cuws.inventory_seeded_grain (inventory_seeded_grain_guid, inventory_field_guid, crop_commodity_id, crop_variety_id, commodity_type_code, is_quantity_insurable_ind, is_replaced_ind, is_pedigree_ind, seeding_date, seeded_acres, is_spot_loss_insurable_ind, create_user, create_date, update_user, update_date)
	VALUES (replace(cast(gen_random_uuid() as text), '-', ''), 'AbcDeFg12394039485-8', 18,  1010471,  'Argentine Canola',  'N',  'N',  'N',  null,  40, 'N', 'admin', now(), 'admin', now());

	*****************
	DELETE STATEMENTS
	*****************

	DELETE FROM inventory_seeded_grain WHERE inventory_field_guid like 'AbcDeFg12394039485%';
	DELETE FROM inventory_field t WHERE t.inventory_field_guid like 'AbcDeFg12394039485%';
	DELETE FROM contracted_field_detail WHERE contracted_field_detail_id IN (888882022, 888882021);
	DELETE FROM grower_contract_year WHERE grower_contract_year_id IN (999992022, 999992021);
	DELETE FROM policy WHERE policy_id IN (999888777, 999888111);
	DELETE FROM annual_field_detail where annual_field_detail_id IN (987652022, 987652021);	
	DELETE FROM field WHERE field_id = 123456999;
	DELETE FROM legal_land t WHERE t.legal_land_id = 987654321;	
	 
	*****************
	SELECT STATEMENTS
	*****************
	SELECT * FROM inventory_seeded_grain WHERE inventory_field_guid like 'AbcDeFg12394039485%';
	SELECT * FROM inventory_field t WHERE t.inventory_field_guid like 'AbcDeFg12394039485%';
	SELECT * FROM contracted_field_detail WHERE contracted_field_detail_id IN (888882022, 888882021);
	SELECT * FROM grower_contract_year WHERE grower_contract_year_id IN (999992022, 999992021);
	SELECT * FROM policy WHERE policy_id IN (999888777, 999888111);
	SELECT * FROM annual_field_detail where annual_field_detail_id IN (987652022, 987652021);	
	SELECT * FROM field WHERE field_id = 123456999;
	SELECT * FROM legal_land t WHERE t.legal_land_id = 987654321;	
 
	 */
	}

	@Test
	public void testRolloverInventoryContractNoSeededData() throws CirrasUnderwritingServiceException, Oauth2ClientException {
		logger.debug("<testRolloverInventoryContractNoSeededData");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasUnderwritingService service = getService(SCOPES);

		//*****************************************************************
		//BEFORE TESTS: CREATE LAND AND INVENTORY BY RUNNING THE INSERT SCRIPTS BELOW
		//*****************************************************************
		
		String policyNumber = "999888-22";
		Integer fieldId = 123456999;
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();
		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(20);

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
				pageNumber, pageRowCount);

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(1, searchResults.getCollection().size());

		UwContractRsrc referrer = searchResults.getCollection().get(0);
		Assert.assertNull(referrer.getInventoryContractGuid());

		InventoryContractRsrc invContract = service.rolloverInventoryContract(referrer);
		Assert.assertNotNull(invContract);
		
		Assert.assertEquals(referrer.getContractId(), invContract.getContractId());
		Assert.assertEquals(referrer.getCropYear(), invContract.getCropYear());
		Assert.assertEquals(false, invContract.getFertilizerInd());
		Assert.assertEquals(false, invContract.getGrainFromPrevYearInd());
		Assert.assertEquals(false, invContract.getHerbicideInd());
		Assert.assertEquals(null, invContract.getInventoryContractGuid());
		Assert.assertEquals(null, invContract.getOtherChangesComment());
		Assert.assertEquals(false, invContract.getOtherChangesInd());
		Assert.assertEquals(false, invContract.getSeededCropReportSubmittedInd());
		Assert.assertEquals(false, invContract.getTilliageInd());
		Assert.assertEquals(false, invContract.getUnseededIntentionsSubmittedInd());
		
		List<AnnualFieldRsrc> fields = invContract.getFields();
		Assert.assertNotNull(fields);
		Assert.assertEquals("More than one field returned", 1, fields.size());
		
		AnnualFieldRsrc field = fields.get(0);
		Assert.assertEquals("field doesn't match", fieldId, field.getFieldId());

		
		Assert.assertEquals(987652022, field.getAnnualFieldDetailId().intValue());
		Assert.assertEquals(888882022, field.getContractedFieldDetailId().intValue());
		Assert.assertEquals(invContract.getCropYear(), field.getCropYear());
		Assert.assertEquals(1, field.getDisplayOrder().intValue());
		
		Assert.assertNotNull(field.getPlantings());
		Assert.assertEquals("More plantings than expected returned", 1, field.getPlantings().size());
		
		InventoryField invField = field.getPlantings().get(0);

		Assert.assertTrue("is hidden on printout ind is not true", invField.getIsHiddenOnPrintoutInd());

		// TODO: Add variety (PIM-1466).
		Integer lastYearCmdtyId = invField.getLastYearCropCommodityId();
		Assert.assertNull("commodity id not none", lastYearCmdtyId);
		
		Assert.assertTrue("Unseeded Insurable not true", invField.getInventoryUnseeded().getIsUnseededInsurableInd());
		Assert.assertNull("Unseeded crop commodity not null", invField.getInventoryUnseeded().getCropCommodityId());
		
		Assert.assertNull("not correct acres", invField.getInventoryUnseeded().getAcresToBeSeeded());

		//*****************************************************************
		//AFTER TESTS: DELETE LAND AND INVENTORY BY RUNNING THE DELETE SCRIPTS BELOW
		//*****************************************************************


		logger.debug(">testRolloverInventoryContractNoSeededData");
	
	/* Creates: 
	 * - A field and a legal land
	 * - Two policies (one for 2021 and 2022) for the same contract (policy and grower contract year records)
	 * - annual field tables (contracted field details and annual field details records)
	 * - Inventory plantings for 2021
	 * 
	*****************
	INSERT STATEMENTS
	*****************
	INSERT INTO cuws.legal_land(legal_land_id, primary_reference_type_code, legal_description, legal_short_description, other_description, active_from_crop_year, active_to_crop_year, data_sync_trans_date, create_user, create_date, update_user, update_date, primary_property_identifier)
	VALUES (987654321, 'OTHER', 'Test Legal', 'Test Legal Short', 'Test Other', 1980, null, now(), 'admin', now(), 'admin', now(), '123456723');
	 
	INSERT INTO cuws.field(field_id, field_label, active_from_crop_year, active_to_crop_year, data_sync_trans_date, create_user, create_date, update_user, update_date)
	VALUES (123456999, 'Test Field', 1980, null, now(), 'admin', now(), 'admin', now());
	
	--target year policy and land ************************************
	INSERT INTO cuws.policy(policy_id, grower_id, insurance_plan_id, policy_status_code, office_id, policy_number, contract_number, contract_id, crop_year, data_sync_trans_date, create_user, create_date, update_user, update_date)
	VALUES (999888777, 4, 4, 'ACTIVE', 1, '999888-22', '999888', 999888777, 2022, now(), 'admin', now(), 'admin', now());

	INSERT INTO cuws.grower_contract_year(grower_contract_year_id, grower_id, insurance_plan_id, contract_id, crop_year, data_sync_trans_date, create_user, create_date, update_user, update_date)
	VALUES (999992022, 4, 4, 999888777, 2022, now(), 'admin', now(), 'admin', now());

	INSERT INTO cuws.annual_field_detail(annual_field_detail_id, legal_land_id, field_id, crop_year, data_sync_trans_date, create_user, create_date, update_user, update_date)
	VALUES (987652022, 987654321, 123456999, 2022, now(), 'admin', now(), 'admin', now());
	
	INSERT INTO cuws.contracted_field_detail(contracted_field_detail_id, annual_field_detail_id, grower_contract_year_id, display_order, data_sync_trans_date, create_user, create_date, update_user, update_date)
	VALUES (888882022, 987652022, 999992022, 1, now(), 'admin', now(), 'admin', now());
	
	--source year policy and land ************************************ 
	INSERT INTO cuws.policy(policy_id, grower_id, insurance_plan_id, policy_status_code, office_id, policy_number, contract_number, contract_id, crop_year, data_sync_trans_date, create_user, create_date, update_user, update_date)
	VALUES (999888111, 4, 4, 'ACTIVE', 1, '999888-21', '999888', 999888777, 2021, now(), 'admin', now(), 'admin', now());

	INSERT INTO cuws.grower_contract_year(grower_contract_year_id, grower_id, insurance_plan_id, contract_id, crop_year, data_sync_trans_date, create_user, create_date, update_user, update_date)
	VALUES (999992021, 4, 4, 999888777, 2021, now(), 'admin', now(), 'admin', now());

	INSERT INTO cuws.annual_field_detail(annual_field_detail_id, legal_land_id, field_id, crop_year, data_sync_trans_date, create_user, create_date, update_user, update_date)
	VALUES (987652021, 987654321, 123456999, 2021, now(), 'admin', now(), 'admin', now());
	
	INSERT INTO cuws.contracted_field_detail(contracted_field_detail_id, annual_field_detail_id, grower_contract_year_id, display_order, data_sync_trans_date, create_user, create_date, update_user, update_date)
	VALUES (888882021, 987652021, 999992021, 1, now(), 'admin', now(), 'admin', now());
	
	--Inventory Field
	INSERT INTO cuws.inventory_field(inventory_field_guid, insurance_plan_id, field_id, last_year_crop_commodity_id, crop_year, planting_number, create_user, create_date, update_user, update_date, is_hidden_on_printout_ind)
	VALUES ('AbcDeFg12394039485-1', 4, 123456999, null, 2021, 1, 'admin', now(), 'admin', now(), 'Y');
	
	INSERT INTO cuws.inventory_field(inventory_field_guid, insurance_plan_id, field_id, last_year_crop_commodity_id, crop_year, planting_number, create_user, create_date, update_user, update_date, is_hidden_on_printout_ind)
	VALUES ('AbcDeFg12394039485-2', 4, 123456999, null, 2021, 2, 'admin', now(), 'admin', now(), 'Y');

	INSERT INTO cuws.inventory_field(inventory_field_guid, insurance_plan_id, field_id, last_year_crop_commodity_id, crop_year, planting_number, create_user, create_date, update_user, update_date, is_hidden_on_printout_ind)
	VALUES ('AbcDeFg12394039485-3', 4, 123456999, null, 2021, 3, 'admin', now(), 'admin', now(), 'Y');

	*****************
	DELETE STATEMENTS
	*****************

	DELETE FROM inventory_seeded_grain WHERE inventory_field_guid like 'AbcDeFg12394039485%';
	DELETE FROM inventory_field t WHERE t.inventory_field_guid like 'AbcDeFg12394039485%';
	DELETE FROM contracted_field_detail WHERE contracted_field_detail_id IN (888882022, 888882021);
	DELETE FROM grower_contract_year WHERE grower_contract_year_id IN (999992022, 999992021);
	DELETE FROM policy WHERE policy_id IN (999888777, 999888111);
	DELETE FROM annual_field_detail where annual_field_detail_id IN (987652022, 987652021);	
	DELETE FROM field WHERE field_id = 123456999;
	DELETE FROM legal_land t WHERE t.legal_land_id = 987654321;	
	 
	*****************
	SELECT STATEMENTS
	*****************
	SELECT * FROM inventory_seeded_grain WHERE inventory_field_guid like 'AbcDeFg12394039485%';
	SELECT * FROM inventory_field t WHERE t.inventory_field_guid like 'AbcDeFg12394039485%';
	SELECT * FROM contracted_field_detail WHERE contracted_field_detail_id IN (888882022, 888882021);
	SELECT * FROM grower_contract_year WHERE grower_contract_year_id IN (999992022, 999992021);
	SELECT * FROM policy WHERE policy_id IN (999888777, 999888111);
	SELECT * FROM annual_field_detail where annual_field_detail_id IN (987652022, 987652021);	
	SELECT * FROM field WHERE field_id = 123456999;
	SELECT * FROM legal_land t WHERE t.legal_land_id = 987654321;	
 
	 */
	}

	@Test
	public void testRolloverForageInventoryContract() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, DaoException {
		logger.debug("<testRolloverForageInventoryContract");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();
		Date createTransactionDate = addSeconds(transactionDate, -1);

		createGrower(growerId, 999888, "grower name", createTransactionDate);
		
		createLegalLand("test legal 9999", null, legalLandId, "999-888-000", 1980, null);
		createField(fieldId, "LOT 3", 1980, null);

		//2022
		createGrowerContractYear(growerContractYearId1, contractId, growerId, cropYear1, 5, createTransactionDate);
		createPolicy(policyId1, growerId, 5, policyNumber1, contractNumber, contractId, cropYear1, createTransactionDate);
		createAnnualFieldDetail(annualFieldDetailId1, legalLandId, fieldId, cropYear1);
		createContractedFieldDetail(contractedFieldDetailId1, annualFieldDetailId1, growerContractYearId1, 1);

		//2021
		createGrowerContractYear(growerContractYearId2, contractId, growerId, cropYear2, 5, createTransactionDate);
		createPolicy(policyId2, growerId, 5, policyNumber2, contractNumber, contractId, cropYear2, createTransactionDate);
		createAnnualFieldDetail(annualFieldDetailId2, legalLandId, fieldId, cropYear2);
		createContractedFieldDetail(contractedFieldDetailId2, annualFieldDetailId2, growerContractYearId2, 1);
				
		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(20);

		// Rollover and create InventoryContract for 2021 policy.
		UwContractListRsrc searchResults = service.getUwContractList(
				topLevelEndpoints, 
				null, 
				null, 
				null,
				null,
				policyNumber2,
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

		// Check InventoryContract
		Assert.assertEquals(contractId, invContract.getContractId());
		Assert.assertEquals(cropYear2, invContract.getCropYear());
		Assert.assertEquals(false, invContract.getFertilizerInd());
		Assert.assertNull(invContract.getGrainFromPrevYearInd());
		Assert.assertEquals(false, invContract.getHerbicideInd());
		Assert.assertEquals(null, invContract.getInventoryContractGuid());
		Assert.assertEquals(null, invContract.getOtherChangesComment());
		Assert.assertEquals(false, invContract.getOtherChangesInd());
		Assert.assertEquals(false, invContract.getSeededCropReportSubmittedInd());
		Assert.assertEquals(false, invContract.getTilliageInd());
		Assert.assertEquals(false, invContract.getUnseededIntentionsSubmittedInd());

		// Check Fields
		List<AnnualFieldRsrc> fields = invContract.getFields();
		Assert.assertNotNull(fields);
		Assert.assertEquals(1, fields.size());
		
		AnnualFieldRsrc field = fields.get(0);
		Assert.assertEquals(fieldId, field.getFieldId());
		
		Assert.assertEquals(annualFieldDetailId2, field.getAnnualFieldDetailId());
		Assert.assertEquals(contractedFieldDetailId2, field.getContractedFieldDetailId());
		Assert.assertEquals(cropYear2, field.getCropYear());
		Assert.assertEquals(1, field.getDisplayOrder().intValue());
		
		Assert.assertNotNull(field.getPlantings());
		Assert.assertEquals(1, field.getPlantings().size());

		Assert.assertNotNull(field.getUwComments());
		Assert.assertEquals(0, field.getUwComments().size());

		// Check Planting
		InventoryField invField = field.getPlantings().get(0);
			
		Assert.assertNull(invField.getInventoryUnseeded());
		Assert.assertNotNull(invField.getInventorySeededForages());
		Assert.assertEquals(1, invField.getInventorySeededForages().size());
		Assert.assertEquals(cropYear2, invField.getCropYear());
		Assert.assertEquals(fieldId, invField.getFieldId());
		Assert.assertEquals(5, invField.getInsurancePlanId().intValue());
		Assert.assertFalse(invField.getIsHiddenOnPrintoutInd());
		Assert.assertNull(invField.getLastYearCropCommodityId());
		Assert.assertNull(invField.getLastYearCropCommodityName());
		Assert.assertNull(invField.getLastYearCropVarietyId());
		Assert.assertNull(invField.getLastYearCropVarietyName());
		Assert.assertEquals(1, invField.getPlantingNumber().intValue());
		Assert.assertNull(invField.getUnderseededAcres());
		Assert.assertNull(invField.getUnderseededCropVarietyId());
		Assert.assertNull(invField.getUnderseededCropVarietyName());
		Assert.assertNull(invField.getUnderseededInventorySeededForageGuid());

		// Check InventorySeededForage
		InventorySeededForage forage = invField.getInventorySeededForages().get(0);
		Assert.assertNull("InventorySeededForageGuid", forage.getInventorySeededForageGuid());
		Assert.assertNull("InventoryFieldGuid", forage.getInventoryFieldGuid());
		Assert.assertNull("CropCommodityId", forage.getCropCommodityId());
		Assert.assertNull("CropVarietyId", forage.getCropVarietyId());
		Assert.assertNull("CropVarietyName", forage.getCropVarietyName());
		Assert.assertNull("CommodityTypeCode", forage.getCommodityTypeCode());
		Assert.assertNull("FieldAcres", forage.getFieldAcres());
		Assert.assertNull("SeedingDate", forage.getSeedingDate());
		Assert.assertNull("SeedingYear", forage.getSeedingYear());
		Assert.assertNull("IsIrrigatedInd", forage.getIsIrrigatedInd());
		Assert.assertNull("IsQuantityInsurableInd", forage.getIsQuantityInsurableInd());
		Assert.assertNull("PlantInsurabilityTypeCode", forage.getPlantInsurabilityTypeCode());
		Assert.assertTrue("IsAwpEligibleInd", forage.getIsAwpEligibleInd());

		// Add InventorySeededForage to Planting 1
		invField.getInventorySeededForages().remove(0);
		createInventorySeededForage(invField, "PERENNIAL", 65, 118, "ALFALFA/GRASS", "E1", true, true, true, 11.2, 2015);  // IsIrrigated=true, so E1 rolls over to W1.

		// Add Planting 2 and InventorySeededForage
		invField = createPlanting(field, 2, cropYear2, 5, true);
		createInventorySeededForage(invField, "Dbl Crop Barley", 65, 221, "DBL CROP BARLEY", "W1", false, false, true, 33.4, 2020);

		// Add Planting 3 and InventorySeededForage
		invField = createPlanting(field, 3, cropYear2, 5, false);
		createInventorySeededForage(invField, "PERENNIAL", 65, 118, "ALFALFA/GRASS", "E1", true, false, true, 11.2, 2015);  // IsIrrigated=false, so E1 rolls over to E2.
		
		
		// add comment
		UnderwritingComment uwComment = new UnderwritingComment();
		List<UnderwritingComment> uwComments = new ArrayList<UnderwritingComment>();

		uwComment.setAnnualFieldDetailId(null);
		uwComment.setUnderwritingComment("Comment1 for field " + field.getFieldId());
		uwComment.setUnderwritingCommentGuid(null);
		uwComment.setUnderwritingCommentTypeCode("INV");
		uwComment.setUnderwritingCommentTypeDesc("Inventory");

		uwComments.add(uwComment);
		
		field.setUwComments(uwComments);

		// Create for 2021.
		InventoryContractRsrc lastYearInvContract = service.createInventoryContract(topLevelEndpoints, invContract);
		Assert.assertNotNull(lastYearInvContract);

		// Rollover InventoryContract from 2021 to 2022.
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

		referrer = searchResults.getCollection().get(0);
		Assert.assertNull(referrer.getInventoryContractGuid());

		InventoryContractRsrc rolledOverInvContract = service.rolloverInventoryContract(referrer);
		Assert.assertNotNull(rolledOverInvContract);

		// Check InventoryContract
		Assert.assertEquals(contractId, rolledOverInvContract.getContractId());
		Assert.assertEquals(cropYear1, rolledOverInvContract.getCropYear());
		Assert.assertEquals(false, rolledOverInvContract.getFertilizerInd());
		Assert.assertNull(rolledOverInvContract.getGrainFromPrevYearInd());
		Assert.assertEquals(false, rolledOverInvContract.getHerbicideInd());
		Assert.assertEquals(null, rolledOverInvContract.getInventoryContractGuid());
		Assert.assertEquals(null, rolledOverInvContract.getOtherChangesComment());
		Assert.assertEquals(false, rolledOverInvContract.getOtherChangesInd());
		Assert.assertEquals(false, rolledOverInvContract.getSeededCropReportSubmittedInd());
		Assert.assertEquals(false, rolledOverInvContract.getTilliageInd());
		Assert.assertEquals(false, rolledOverInvContract.getUnseededIntentionsSubmittedInd());

		// Check Fields
		List<AnnualFieldRsrc> rolledOverFields = rolledOverInvContract.getFields();
		Assert.assertNotNull(rolledOverFields);
		Assert.assertEquals(1, rolledOverFields.size());
		
		AnnualFieldRsrc rolledOverField = rolledOverFields.get(0);
		Assert.assertEquals(fieldId, rolledOverField.getFieldId());
		
		Assert.assertEquals(annualFieldDetailId1, rolledOverField.getAnnualFieldDetailId());
		Assert.assertEquals(contractedFieldDetailId1, rolledOverField.getContractedFieldDetailId());
		Assert.assertEquals(cropYear1, rolledOverField.getCropYear());
		Assert.assertEquals(1, rolledOverField.getDisplayOrder().intValue());
		
		Assert.assertNotNull(rolledOverField.getPlantings());
		Assert.assertEquals(3, rolledOverField.getPlantings().size());

		// Check Comment
		Assert.assertNotNull(rolledOverField.getUwComments());
		Assert.assertEquals(1, rolledOverField.getUwComments().size());

		uwComment = rolledOverField.getUwComments().get(0);
		Assert.assertEquals(annualFieldDetailId2, uwComment.getAnnualFieldDetailId());
		Assert.assertEquals("Comment1 for field " + fieldId, uwComment.getUnderwritingComment());
		Assert.assertEquals("INV", uwComment.getUnderwritingCommentTypeCode());
		Assert.assertEquals("Inventory", uwComment.getUnderwritingCommentTypeDesc());

		// Check Planting 1
		invField = rolledOverField.getPlantings().get(0);
			
		Assert.assertNull(invField.getInventoryUnseeded());
		Assert.assertNotNull(invField.getInventorySeededForages());
		Assert.assertEquals(1, invField.getInventorySeededForages().size());
		Assert.assertEquals(cropYear1, invField.getCropYear());
		Assert.assertEquals(fieldId, invField.getFieldId());
		Assert.assertEquals(5, invField.getInsurancePlanId().intValue());
		Assert.assertFalse(invField.getIsHiddenOnPrintoutInd());
		Assert.assertNull(invField.getLastYearCropCommodityId());
		Assert.assertNull(invField.getLastYearCropCommodityName());
		Assert.assertNull(invField.getLastYearCropVarietyId());
		Assert.assertNull(invField.getLastYearCropVarietyName());
		Assert.assertEquals(1, invField.getPlantingNumber().intValue());
		Assert.assertNull(invField.getUnderseededAcres());
		Assert.assertNull(invField.getUnderseededCropVarietyId());
		Assert.assertNull(invField.getUnderseededCropVarietyName());
		Assert.assertNull(invField.getUnderseededInventorySeededForageGuid());
			

		forage = invField.getInventorySeededForages().get(0);
		Assert.assertNull("InventorySeededForageGuid", forage.getInventorySeededForageGuid());
		Assert.assertNull("InventoryFieldGuid", forage.getInventoryFieldGuid());
		Assert.assertEquals("CropCommodityId", 65, forage.getCropCommodityId().intValue());
		Assert.assertEquals("CropVarietyId", 118, forage.getCropVarietyId().intValue());
		Assert.assertEquals("CropVarietyName", "ALFALFA/GRASS", forage.getCropVarietyName());
		Assert.assertEquals("CommodityTypeCode", "PERENNIAL", forage.getCommodityTypeCode());
		Assert.assertEquals("FieldAcres", Double.valueOf(11.2), forage.getFieldAcres());
		Assert.assertNull("SeedingDate", forage.getSeedingDate());
		Assert.assertEquals("SeedingYear", 2015, forage.getSeedingYear().intValue());
		Assert.assertTrue("IsIrrigatedInd", forage.getIsIrrigatedInd());
		Assert.assertTrue("IsQuantityInsurableInd", forage.getIsQuantityInsurableInd());
		Assert.assertEquals("PlantInsurabilityTypeCode", "W1", forage.getPlantInsurabilityTypeCode()); // E1 rolls over as W1 because isIrrigatedInd is true.
		Assert.assertTrue("IsAwpEligibleInd", forage.getIsAwpEligibleInd());

		// Check Planting 2
		invField = rolledOverField.getPlantings().get(1);
		
		Assert.assertNull(invField.getInventoryUnseeded());
		Assert.assertNotNull(invField.getInventorySeededForages());
		Assert.assertEquals(1, invField.getInventorySeededForages().size());
		Assert.assertEquals(cropYear1, invField.getCropYear());
		Assert.assertEquals(fieldId, invField.getFieldId());
		Assert.assertEquals(5, invField.getInsurancePlanId().intValue());
		Assert.assertTrue(invField.getIsHiddenOnPrintoutInd());
		Assert.assertNull(invField.getLastYearCropCommodityId());
		Assert.assertNull(invField.getLastYearCropCommodityName());
		Assert.assertNull(invField.getLastYearCropVarietyId());
		Assert.assertNull(invField.getLastYearCropVarietyName());
		Assert.assertEquals(2, invField.getPlantingNumber().intValue());
		Assert.assertNull(invField.getUnderseededAcres());
		Assert.assertNull(invField.getUnderseededCropVarietyId());
		Assert.assertNull(invField.getUnderseededCropVarietyName());
		Assert.assertNull(invField.getUnderseededInventorySeededForageGuid());

		
		forage = invField.getInventorySeededForages().get(0);
		Assert.assertNull("InventorySeededForageGuid", forage.getInventorySeededForageGuid());
		Assert.assertNull("InventoryFieldGuid", forage.getInventoryFieldGuid());
		Assert.assertNull("CropCommodityId", forage.getCropCommodityId());
		Assert.assertNull("CropVarietyId", forage.getCropVarietyId());
		Assert.assertNull("CropVarietyName", forage.getCropVarietyName());
		Assert.assertNull("CommodityTypeCode", forage.getCommodityTypeCode());
		Assert.assertEquals("FieldAcres", Double.valueOf(33.4), forage.getFieldAcres());
		Assert.assertNull("SeedingDate", forage.getSeedingDate());
		Assert.assertNull("SeedingYear", forage.getSeedingYear());
		Assert.assertFalse("IsIrrigatedInd", forage.getIsIrrigatedInd());
		Assert.assertFalse("IsQuantityInsurableInd", forage.getIsQuantityInsurableInd());
		Assert.assertNull("PlantInsurabilityTypeCode", forage.getPlantInsurabilityTypeCode());
		Assert.assertFalse("IsAwpEligibleInd", forage.getIsAwpEligibleInd());

		// Check Planting 3
		invField = rolledOverField.getPlantings().get(2);
		
		Assert.assertNull(invField.getInventoryUnseeded());
		Assert.assertNotNull(invField.getInventorySeededForages());
		Assert.assertEquals(1, invField.getInventorySeededForages().size());
		Assert.assertEquals(cropYear1, invField.getCropYear());
		Assert.assertEquals(fieldId, invField.getFieldId());
		Assert.assertEquals(5, invField.getInsurancePlanId().intValue());
		Assert.assertFalse(invField.getIsHiddenOnPrintoutInd());
		Assert.assertNull(invField.getLastYearCropCommodityId());
		Assert.assertNull(invField.getLastYearCropCommodityName());
		Assert.assertNull(invField.getLastYearCropVarietyId());
		Assert.assertNull(invField.getLastYearCropVarietyName());
		Assert.assertEquals(3, invField.getPlantingNumber().intValue());
		Assert.assertNull(invField.getUnderseededAcres());
		Assert.assertNull(invField.getUnderseededCropVarietyId());
		Assert.assertNull(invField.getUnderseededCropVarietyName());
		Assert.assertNull(invField.getUnderseededInventorySeededForageGuid());
			

		forage = invField.getInventorySeededForages().get(0);
		Assert.assertNull("InventorySeededForageGuid", forage.getInventorySeededForageGuid());
		Assert.assertNull("InventoryFieldGuid", forage.getInventoryFieldGuid());
		Assert.assertEquals("CropCommodityId", 65, forage.getCropCommodityId().intValue());
		Assert.assertEquals("CropVarietyId", 118, forage.getCropVarietyId().intValue());
		Assert.assertEquals("CropVarietyName", "ALFALFA/GRASS", forage.getCropVarietyName());
		Assert.assertEquals("CommodityTypeCode", "PERENNIAL", forage.getCommodityTypeCode());
		Assert.assertEquals("FieldAcres", Double.valueOf(11.2), forage.getFieldAcres());
		Assert.assertNull("SeedingDate", forage.getSeedingDate());
		Assert.assertEquals("SeedingYear", 2015, forage.getSeedingYear().intValue());
		Assert.assertFalse("IsIrrigatedInd", forage.getIsIrrigatedInd());
		Assert.assertTrue("IsQuantityInsurableInd", forage.getIsQuantityInsurableInd());
		Assert.assertEquals("PlantInsurabilityTypeCode", "E2", forage.getPlantInsurabilityTypeCode()); // E1 rolls over as E2.
		Assert.assertTrue("IsAwpEligibleInd", forage.getIsAwpEligibleInd());
		
		
		logger.debug(">testRolloverForageInventoryContract");
	}
	
	
	private void createLegalLand(
			String legalLocation, 
			String legalDescription, 
			Integer llId, 
			String primaryPropertyIdentifier, 
			Integer activeFromCropYear, 
			Integer activeToCropYear
	) throws DaoException, CirrasUnderwritingServiceException, ValidationException {
		
		String primaryReferenceTypeCode = "OTHER";
		String legalShortDescription = "Short Legal";
		String otherDescription = legalLocation;
		
		//CREATE LegalLand
		LegalLandRsrc resource = new LegalLandRsrc();
		
		resource.setLegalLandId(llId);
		resource.setPrimaryPropertyIdentifier(primaryPropertyIdentifier);
		resource.setPrimaryLandIdentifierTypeCode("PID");
		resource.setPrimaryReferenceTypeCode(primaryReferenceTypeCode);
		resource.setLegalDescription(legalDescription);
		resource.setLegalShortDescription(legalShortDescription);
		resource.setOtherDescription(otherDescription);
		resource.setActiveFromCropYear(activeFromCropYear);
		resource.setActiveToCropYear(activeToCropYear);
		resource.setTransactionType(LandManagementEventTypes.LegalLandCreated);
		service.synchronizeLegalLand(resource);
		
	}

	
	private void createField(Integer fieldId, String fieldLabel, Integer activeFromCropYear, Integer activeToCropYear) throws CirrasUnderwritingServiceException, ValidationException {

		FieldRsrc resource = new FieldRsrc();
		
		resource.setFieldId(fieldId);
		resource.setFieldLabel(fieldLabel);
		resource.setActiveFromCropYear(activeFromCropYear);
		resource.setActiveToCropYear(activeToCropYear);
		resource.setTransactionType(LandManagementEventTypes.FieldCreated);
		
		service.synchronizeField(resource);
	}
		
	private void createAnnualFieldDetail( Integer annualFieldDetailId, Integer legalLandId, Integer fieldId, Integer cropYear) throws CirrasUnderwritingServiceException, ValidationException {

		AnnualFieldDetailRsrc resource = new AnnualFieldDetailRsrc();
		
		resource.setAnnualFieldDetailId(annualFieldDetailId);
		resource.setLegalLandId(legalLandId);
		resource.setFieldId(fieldId);
		resource.setCropYear(cropYear);
		resource.setTransactionType(LandManagementEventTypes.AnnualFieldDetailCreated);
		
		service.synchronizeAnnualFieldDetail(resource);
	}

	private void createContractedFieldDetail( 
			Integer contractedFieldDetailId, 
			Integer annualFieldDetailId, 
			Integer growerContractYearId, 
			Integer displayOrder) throws CirrasUnderwritingServiceException, ValidationException {

		ContractedFieldDetailRsrc resource = new ContractedFieldDetailRsrc();
		
		resource.setContractedFieldDetailId(contractedFieldDetailId);
		resource.setAnnualFieldDetailId(annualFieldDetailId);
		resource.setGrowerContractYearId(growerContractYearId);
		resource.setDisplayOrder(displayOrder);
		resource.setTransactionType(LandManagementEventTypes.ContractedFieldDetailCreated);
		
		service.synchronizeContractedFieldDetail(resource);
	}

	private void createGrowerContractYear(Integer gcyId, Integer contractId, Integer growerId, Integer cropYear, Integer insurancePlanId, Date createTransactionDate) throws DaoException, CirrasUnderwritingServiceException, ValidationException { 

		//CREATE GrowerContractYear
		GrowerContractYearSyncRsrc resource = new GrowerContractYearSyncRsrc();
		
		resource.setGrowerContractYearId(gcyId);
		resource.setContractId(contractId);
		resource.setGrowerId(growerId);
		resource.setInsurancePlanId(insurancePlanId);
		resource.setCropYear(cropYear);
		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.GrowerContractYearCreated);
		
		service.synchronizeGrowerContractYear(resource);
	}

	private void createPolicy(
			Integer policyId, 
			Integer growerId, 
			Integer insurancePlanId, 
			String policyNumber, 
			String contractNumber, 
			Integer contractId, 
			Integer cropYear, 
			Date createTransactionDate
	) throws ValidationException, CirrasUnderwritingServiceException {

		PolicyRsrc resource = new PolicyRsrc();
		
		resource.setPolicyId(policyId);
		resource.setGrowerId(growerId);
		resource.setInsurancePlanId(insurancePlanId);
		resource.setPolicyStatusCode("ACTIVE");
		resource.setOfficeId(1);
		resource.setPolicyNumber(policyNumber);
		resource.setContractNumber(contractNumber);
		resource.setContractId(contractId);
		resource.setCropYear(cropYear);
		
		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.PolicyCreated);

		service.synchronizePolicy(resource);
	}
	
	
	private void createGrower(Integer growerId, Integer growerNumber, String growerName, Date createTransactionDate) throws DaoException, CirrasUnderwritingServiceException, ValidationException {
		GrowerRsrc resource = new GrowerRsrc();
		
		String growerAddressLine1 = "address line 1";
		String growerAddressLine2 = "address line 2";
		String growerPostalCode = "V8P 4N8";
		String growerCity = "Victoria";
		Integer cityId = 1;
		String growerProvince = "BC";
		
		//INSERT
		resource.setGrowerId(growerId);
		resource.setGrowerNumber(growerNumber);
		resource.setGrowerName(growerName);
		resource.setGrowerAddressLine1(growerAddressLine1);
		resource.setGrowerAddressLine2(growerAddressLine2);
		resource.setGrowerPostalCode(growerPostalCode);
		resource.setGrowerCity(growerCity);
		resource.setCityId(cityId);
		resource.setGrowerProvince(growerProvince);

		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.GrowerCreated);
		
		service.synchronizeGrower(resource);
	}
	

	private InventoryField createPlanting(AnnualFieldRsrc field, 
			                              Integer plantingNumber, 
			                              Integer cropYear, 
			                              Integer insurancePlanId,
			                              Boolean isHiddenOnPrintoutInd) {
		InventoryField planting = new InventoryField();

		planting.setCropYear(cropYear);
		planting.setFieldId(field.getFieldId());
		planting.setInsurancePlanId(insurancePlanId);
		planting.setInventoryFieldGuid(null);
		planting.setLastYearCropCommodityId(null);
		planting.setLastYearCropCommodityName(null);
		planting.setLastYearCropVarietyId(null);
		planting.setLastYearCropVarietyName(null);
		planting.setIsHiddenOnPrintoutInd(isHiddenOnPrintoutInd);
		planting.setPlantingNumber(plantingNumber);
		planting.setUnderseededAcres(null);
		planting.setUnderseededCropVarietyId(null);
		planting.setUnderseededCropVarietyName(null);
		planting.setUnderseededInventorySeededForageGuid(null);
		
		field.getPlantings().add(planting);

		return planting;
	}

	private InventorySeededForage createInventorySeededForage(
			InventoryField planting, 
			String commodityTypeCode,
            Integer cropCommodityId, 
            Integer cropVarietyId,
            String cropVarietyName,
			String plantInsurabilityTypeCode,
			Boolean isAwpEligibleInd,
			Boolean isIrrigatedInd,
			Boolean isQuantityInsurableInd,
			Double fieldAcres,
			Integer seedingYear) {
		
		InventorySeededForage isf = new InventorySeededForage();


		isf.setCommodityTypeCode(commodityTypeCode);
		isf.setCropCommodityId(cropCommodityId);
		isf.setCropVarietyId(cropVarietyId);
		isf.setCropVarietyName(cropVarietyName);
		isf.setFieldAcres(fieldAcres);
		isf.setInventoryFieldGuid(planting.getInventoryFieldGuid());
		isf.setIsAwpEligibleInd(isAwpEligibleInd);
		isf.setIsIrrigatedInd(isIrrigatedInd);
		isf.setIsQuantityInsurableInd(isQuantityInsurableInd);
		isf.setPlantInsurabilityTypeCode(plantInsurabilityTypeCode);
		isf.setSeedingYear(seedingYear);
		
		planting.getInventorySeededForages().add(isf);

		return isf;
	}	
	
	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}

}
