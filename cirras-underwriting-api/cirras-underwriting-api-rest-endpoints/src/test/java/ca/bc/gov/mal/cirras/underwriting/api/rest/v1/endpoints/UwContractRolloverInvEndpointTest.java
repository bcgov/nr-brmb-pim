package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.InventoryContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UwContractListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UwContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventorySeededForage;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class UwContractRolloverInvEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(UwContractRolloverInvEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.SEARCH_UWCONTRACTS,
		Scopes.CREATE_INVENTORY_CONTRACT
	};
	

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
		Assert.assertEquals("More plantings than expected returned", 5, field.getPlantings().size());
		
		for (InventoryField invField: field.getPlantings()) {

			Assert.assertFalse("is hidden on printout ind is not false", invField.getIsHiddenOnPrintoutInd());

			Integer lastYearCmdtyId = invField.getLastYearCropCommodityId();
			//Check if commodity is oat or canola
			Assert.assertTrue("commodity id ("+lastYearCmdtyId+") not 24, 18, 65, 71 or none", lastYearCmdtyId == null || lastYearCmdtyId.equals(24) || lastYearCmdtyId.equals(18)|| lastYearCmdtyId.equals(65)|| lastYearCmdtyId.equals(71));
			
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
			} else if (lastYearCmdtyId.equals(65)) {
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
	
	--Plantings
	INSERT INTO cuws.inventory_seeded_grain (inventory_seeded_grain_guid, inventory_field_guid, crop_commodity_id, crop_variety_id, commodity_type_code, is_quantity_insurable_ind, is_replaced_ind, is_pedigree_ind, seeding_date, seeded_acres, is_spot_loss_insurable_ind, create_user, create_date, update_user, update_date)
	VALUES (replace(cast(gen_random_uuid() as text), '-', ''), 'AbcDeFg12394039485-1', 24,  1010570,  'Forage Oat',  'N',  'N',  'N',  null,  25, 'N', 'admin', now(), 'admin', now());
	INSERT INTO cuws.inventory_seeded_grain (inventory_seeded_grain_guid, inventory_field_guid, crop_commodity_id, crop_variety_id, commodity_type_code, is_quantity_insurable_ind, is_replaced_ind, is_pedigree_ind, seeding_date, seeded_acres, is_spot_loss_insurable_ind, create_user, create_date, update_user, update_date)
	VALUES (replace(cast(gen_random_uuid() as text), '-', ''), 'AbcDeFg12394039485-1', 26,  1010603,  'CPSW',  'N',  'Y',  'N',  null,  0, 'N', 'admin', now(), 'admin', now());
	INSERT INTO cuws.inventory_seeded_grain (inventory_seeded_grain_guid, inventory_field_guid, crop_commodity_id, crop_variety_id, commodity_type_code, is_quantity_insurable_ind, is_replaced_ind, is_pedigree_ind, seeding_date, seeded_acres, is_spot_loss_insurable_ind, create_user, create_date, update_user, update_date)
	VALUES (replace(cast(gen_random_uuid() as text), '-', ''), 'AbcDeFg12394039485-2', 18,  1010471,  'Argentine Canola',  'N',  'N',  'N',  null,  40, 'N', 'admin', now(), 'admin', now());
	INSERT INTO cuws.inventory_seeded_grain (inventory_seeded_grain_guid, inventory_field_guid, crop_commodity_id, crop_variety_id, commodity_type_code, is_quantity_insurable_ind, is_replaced_ind, is_pedigree_ind, seeding_date, seeded_acres, is_spot_loss_insurable_ind, create_user, create_date, update_user, update_date)
	VALUES (replace(cast(gen_random_uuid() as text), '-', ''), 'AbcDeFg12394039485-3', null,  null,  null,  'N',  'N',  'N',  null,  30, 'N', 'admin', now(), 'admin', now());
	INSERT INTO cuws.inventory_seeded_grain (inventory_seeded_grain_guid, inventory_field_guid, crop_commodity_id, crop_variety_id, commodity_type_code, is_quantity_insurable_ind, is_replaced_ind, is_pedigree_ind, seeding_date, seeded_acres, is_spot_loss_insurable_ind, create_user, create_date, update_user, update_date)
	VALUES (replace(cast(gen_random_uuid() as text), '-', ''), 'AbcDeFg12394039485-3', 18,  1010471,  'Argentine Canola',  'N',  'Y',  'N',  null,  10, 'N', 'admin', now(), 'admin', now());
	INSERT INTO cuws.inventory_seeded_grain (inventory_seeded_grain_guid, inventory_field_guid, crop_commodity_id, crop_variety_id, commodity_type_code, is_quantity_insurable_ind, is_replaced_ind, is_pedigree_ind, seeding_date, seeded_acres, is_spot_loss_insurable_ind, create_user, create_date, update_user, update_date)
	VALUES (replace(cast(gen_random_uuid() as text), '-', ''), 'AbcDeFg12394039485-4', 18,  1010471,  'Argentine Canola',  'N',  'Y',  'N',  null,  100, 'N', 'admin', now(), 'admin', now());
	INSERT INTO cuws.inventory_seeded_grain (inventory_seeded_grain_guid, inventory_field_guid, crop_commodity_id, crop_variety_id, commodity_type_code, is_quantity_insurable_ind, is_replaced_ind, is_pedigree_ind, seeding_date, seeded_acres, is_spot_loss_insurable_ind, create_user, create_date, update_user, update_date)
	VALUES (replace(cast(gen_random_uuid() as text), '-', ''), 'AbcDeFg12394039485-5', 18,  null, null,  'N',  'Y',  'N',  null,  null, 'N', 'admin', now(), 'admin', now());

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
	public void testRolloverForageInventoryContract() throws CirrasUnderwritingServiceException, Oauth2ClientException {
		logger.debug("<testRolloverForageInventoryContract");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasUnderwritingService service = getService(SCOPES);

		//*****************************************************************
		//BEFORE TESTS: CREATE LAND AND INVENTORY BY RUNNING THE INSERT SCRIPTS BELOW
		//*****************************************************************
		
		String policyNumber = "999777-22";
		Integer fieldId = 223456777;
		
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

		
		Assert.assertEquals(223452022, field.getAnnualFieldDetailId().intValue());
		Assert.assertEquals(177772022, field.getContractedFieldDetailId().intValue());
		Assert.assertEquals(invContract.getCropYear(), field.getCropYear());
		Assert.assertEquals(1, field.getDisplayOrder().intValue());
		
		Assert.assertNotNull(field.getPlantings());
		Assert.assertEquals("More plantings than expected returned", 3, field.getPlantings().size());

		Assert.assertNotNull(field.getUwComments());
		Assert.assertEquals(1, field.getUwComments().size());
		
		for (InventoryField invField: field.getPlantings()) {
			
			Assert.assertNull(invField.getInventoryUnseeded());
			Assert.assertNotNull(invField.getInventorySeededForages());
			
			for(InventorySeededForage forage: invField.getInventorySeededForages()) {
				Assert.assertNull("InventorySeededForageGuid", forage.getInventorySeededForageGuid());
				Assert.assertNull("InventoryFieldGuid", forage.getInventoryFieldGuid());
				Assert.assertNull("CropCommodityId", forage.getCropCommodityId());
				Assert.assertNull("CropVarietyId", forage.getCropVarietyId());
				Assert.assertNull("CropVarietyName", forage.getCropVarietyName());
				Assert.assertNull("CommodityTypeCode", forage.getCommodityTypeCode());
				Assert.assertNull("FieldAcres", forage.getFieldAcres());
				Assert.assertNull("SeedingYear", forage.getSeedingYear());
				Assert.assertNull("IsIrrigatedInd", forage.getIsIrrigatedInd());
				Assert.assertNull("IsQuantityInsurableInd", forage.getIsQuantityInsurableInd());
				Assert.assertNull("PlantInsurabilityTypeCode", forage.getPlantInsurabilityTypeCode());
				Assert.assertTrue("IsAwpEligibleInd", forage.getIsAwpEligibleInd());
			}
		}

		//*****************************************************************
		//AFTER TESTS: DELETE LAND AND INVENTORY BY RUNNING THE DELETE SCRIPTS BELOW
		//*****************************************************************


		logger.debug(">testRolloverForageInventoryContract");
	
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
	VALUES (887654321, 'OTHER', 'Test Legal', 'Test Legal Short', 'Test Other', 1980, null, now(), 'admin', now(), 'admin', now(), '123456723');
	 
	INSERT INTO cuws.field(field_id, field_label, active_from_crop_year, active_to_crop_year, data_sync_trans_date, create_user, create_date, update_user, update_date)
	VALUES (223456777, 'Test Field', 1980, null, now(), 'admin', now(), 'admin', now());
	
	--target year policy and land ************************************
	INSERT INTO cuws.policy(policy_id, grower_id, insurance_plan_id, policy_status_code, office_id, policy_number, contract_number, contract_id, crop_year, data_sync_trans_date, create_user, create_date, update_user, update_date)
	VALUES (199888777, 4, 5, 'ACTIVE', 1, '999777-22', '999888', 199888777, 2022, now(), 'admin', now(), 'admin', now());

	INSERT INTO cuws.grower_contract_year(grower_contract_year_id, grower_id, insurance_plan_id, contract_id, crop_year, data_sync_trans_date, create_user, create_date, update_user, update_date)
	VALUES (199992022, 4, 5, 199888777, 2022, now(), 'admin', now(), 'admin', now());

	INSERT INTO cuws.annual_field_detail(annual_field_detail_id, legal_land_id, field_id, crop_year, data_sync_trans_date, create_user, create_date, update_user, update_date)
	VALUES (223452022, 887654321, 223456777, 2022, now(), 'admin', now(), 'admin', now());
	
	INSERT INTO cuws.contracted_field_detail(contracted_field_detail_id, annual_field_detail_id, grower_contract_year_id, display_order, data_sync_trans_date, create_user, create_date, update_user, update_date)
	VALUES (177772022, 223452022, 199992022, 1, now(), 'admin', now(), 'admin', now());
	
	--source year policy and land ************************************ 
	INSERT INTO cuws.policy(policy_id, grower_id, insurance_plan_id, policy_status_code, office_id, policy_number, contract_number, contract_id, crop_year, data_sync_trans_date, create_user, create_date, update_user, update_date)
	VALUES (199888111, 4, 5, 'ACTIVE', 1, '999888-21', '999888', 199888777, 2021, now(), 'admin', now(), 'admin', now());

	INSERT INTO cuws.grower_contract_year(grower_contract_year_id, grower_id, insurance_plan_id, contract_id, crop_year, data_sync_trans_date, create_user, create_date, update_user, update_date)
	VALUES (199992021, 4, 5, 199888777, 2021, now(), 'admin', now(), 'admin', now());

	INSERT INTO cuws.annual_field_detail(annual_field_detail_id, legal_land_id, field_id, crop_year, data_sync_trans_date, create_user, create_date, update_user, update_date)
	VALUES (187652021, 887654321, 223456777, 2021, now(), 'admin', now(), 'admin', now());
	
	INSERT INTO cuws.contracted_field_detail(contracted_field_detail_id, annual_field_detail_id, grower_contract_year_id, display_order, data_sync_trans_date, create_user, create_date, update_user, update_date)
	VALUES (188882021, 187652021, 199992021, 1, now(), 'admin', now(), 'admin', now());
	
	--Inventory Field
	INSERT INTO cuws.inventory_field(inventory_field_guid, insurance_plan_id, field_id, last_year_crop_commodity_id, crop_year, planting_number, create_user, create_date, update_user, update_date, is_hidden_on_printout_ind)
	VALUES ('BbcDeFg12394039485-1', 5, 223456777, null, 2021, 1, 'admin', now(), 'admin', now(), 'N');
	
	INSERT INTO cuws.inventory_field(inventory_field_guid, insurance_plan_id, field_id, last_year_crop_commodity_id, crop_year, planting_number, create_user, create_date, update_user, update_date, is_hidden_on_printout_ind)
	VALUES ('BbcDeFg12394039485-2', 5, 223456777, null, 2021, 2, 'admin', now(), 'admin', now(), 'N');

	INSERT INTO cuws.inventory_field(inventory_field_guid, insurance_plan_id, field_id, last_year_crop_commodity_id, crop_year, planting_number, create_user, create_date, update_user, update_date, is_hidden_on_printout_ind)
	VALUES ('BbcDeFg12394039485-3', 5, 223456777, null, 2021, 3, 'admin', now(), 'admin', now(), 'Y');
	
	--Plantings
	INSERT INTO cuws.inventory_seeded_grain (inventory_seeded_grain_guid, inventory_field_guid, crop_commodity_id, crop_variety_id, commodity_type_code, is_quantity_insurable_ind, is_replaced_ind, is_pedigree_ind, seeding_date, seeded_acres, is_spot_loss_insurable_ind, create_user, create_date, update_user, update_date)
	VALUES (replace(cast(gen_random_uuid() as text), '-', ''), 'BbcDeFg12394039485-1', 24,  1010570,  'Forage Oat',  'N',  'N',  'N',  null,  25, 'N', 'admin', now(), 'admin', now());
	INSERT INTO cuws.inventory_seeded_grain (inventory_seeded_grain_guid, inventory_field_guid, crop_commodity_id, crop_variety_id, commodity_type_code, is_quantity_insurable_ind, is_replaced_ind, is_pedigree_ind, seeding_date, seeded_acres, is_spot_loss_insurable_ind, create_user, create_date, update_user, update_date)
	VALUES (replace(cast(gen_random_uuid() as text), '-', ''), 'BbcDeFg12394039485-1', 26,  1010603,  'CPSW',  'N',  'Y',  'N',  null,  0, 'N', 'admin', now(), 'admin', now());
	INSERT INTO cuws.inventory_seeded_grain (inventory_seeded_grain_guid, inventory_field_guid, crop_commodity_id, crop_variety_id, commodity_type_code, is_quantity_insurable_ind, is_replaced_ind, is_pedigree_ind, seeding_date, seeded_acres, is_spot_loss_insurable_ind, create_user, create_date, update_user, update_date)
	VALUES (replace(cast(gen_random_uuid() as text), '-', ''), 'BbcDeFg12394039485-2', 18,  1010471,  'Argentine Canola',  'N',  'N',  'N',  null,  40, 'N', 'admin', now(), 'admin', now());
	INSERT INTO cuws.inventory_seeded_grain (inventory_seeded_grain_guid, inventory_field_guid, crop_commodity_id, crop_variety_id, commodity_type_code, is_quantity_insurable_ind, is_replaced_ind, is_pedigree_ind, seeding_date, seeded_acres, is_spot_loss_insurable_ind, create_user, create_date, update_user, update_date)
	VALUES (replace(cast(gen_random_uuid() as text), '-', ''), 'BbcDeFg12394039485-3', null,  null,  null,  'N',  'N',  'N',  null,  30, 'N', 'admin', now(), 'admin', now());
	INSERT INTO cuws.inventory_seeded_grain (inventory_seeded_grain_guid, inventory_field_guid, crop_commodity_id, crop_variety_id, commodity_type_code, is_quantity_insurable_ind, is_replaced_ind, is_pedigree_ind, seeding_date, seeded_acres, is_spot_loss_insurable_ind, create_user, create_date, update_user, update_date)
	VALUES (replace(cast(gen_random_uuid() as text), '-', ''), 'BbcDeFg12394039485-3', 18,  1010471,  'Argentine Canola',  'N',  'Y',  'N',  null,  10, 'N', 'admin', now(), 'admin', now());

	--Comment
	INSERT INTO cuws.underwriting_comment(underwriting_comment_guid, underwriting_comment_type_code, underwriting_comment, annual_field_detail_id, grower_contract_year_id, declared_yield_contract_guid, create_user, create_date, update_user, update_date)
	VALUES ('uwCommentAbcDefG-1', 'INV', 'Test Comment', 223452022, null, null, 'admin', now(), 'admin', now());

	*****************
	DELETE STATEMENTS
	*****************

	DELETE FROM inventory_seeded_grain WHERE inventory_field_guid like 'BbcDeFg12394039485%';
	DELETE FROM inventory_field t WHERE t.inventory_field_guid like 'BbcDeFg12394039485%';
	DELETE FROM contracted_field_detail WHERE contracted_field_detail_id IN (177772022, 188882021);
	DELETE FROM grower_contract_year WHERE grower_contract_year_id IN (199992022, 199992021);
	DELETE FROM policy WHERE policy_id IN (199888777, 199888111);
	DELETE FROM underwriting_comment WHERE annual_field_detail_id IN (223452022, 187652021);	
	DELETE FROM annual_field_detail where annual_field_detail_id IN (223452022, 187652021);	
	DELETE FROM field WHERE field_id = 223456777;
	DELETE FROM legal_land t WHERE t.legal_land_id = 887654321;	
	 
	*****************
	SELECT STATEMENTS
	*****************
	SELECT * FROM inventory_seeded_grain WHERE inventory_field_guid like 'BbcDeFg12394039485%';
	SELECT * FROM inventory_field t WHERE t.inventory_field_guid like 'BbcDeFg12394039485%';
	SELECT * FROM contracted_field_detail WHERE contracted_field_detail_id IN (177772022, 188882021);
	SELECT * FROM grower_contract_year WHERE grower_contract_year_id IN (199992022, 199992021);
	SELECT * FROM policy WHERE policy_id IN (199888777, 199888111);
	SELECT * FROM underwriting_comment WHERE annual_field_detail_id IN (223452022, 187652021);	
	SELECT * FROM annual_field_detail where annual_field_detail_id IN (223452022, 187652021);	
	SELECT * FROM field WHERE field_id = 223456777;
	SELECT * FROM legal_land t WHERE t.legal_land_id = 887654321;	
 
	 */
	}
}
