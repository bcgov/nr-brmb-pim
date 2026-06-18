package ca.bc.gov.mal.cirras.underwriting.controllers;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.clients.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.clients.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.AnnualFieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.DopYieldContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.UwContractListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.UwContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.test.EndpointsTest;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldFieldGrain;

import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class UwContractRolloverDopYieldEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(UwContractRolloverDopYieldEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.SEARCH_UWCONTRACTS,
		Scopes.CREATE_INVENTORY_CONTRACT,
		Scopes.CREATE_DOP_YIELD_CONTRACT
	};
	

	@Test
	public void testRolloverDopYieldContract() throws CirrasUnderwritingServiceException, Oauth2ClientException {
		logger.debug("<testRolloverDopYieldContract");
		
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
		String defaultMeasurementUnitCode = "TONNE";
		
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
		Assert.assertNull(referrer.getDeclaredYieldContractGuid());

		DopYieldContractRsrc dopYldContract = service.rolloverDopYieldContract(referrer);
		Assert.assertNotNull(dopYldContract);
		
		Assert.assertEquals(referrer.getContractId(), dopYldContract.getContractId());
		Assert.assertEquals(referrer.getCropYear(), dopYldContract.getCropYear());
		Assert.assertNull("DeclaredYieldContractGuid", dopYldContract.getDeclaredYieldContractGuid());
		Assert.assertNull("DeclarationOfProductionDate",dopYldContract.getDeclarationOfProductionDate());
		Assert.assertNull("DopUpdateTimestamp", dopYldContract.getDopUpdateTimestamp());
		Assert.assertNull("DopUpdateUser", dopYldContract.getDopUpdateUser());
		Assert.assertNull("EnteredYieldMeasUnitTypeCode", dopYldContract.getEnteredYieldMeasUnitTypeCode());
		Assert.assertEquals("DefaultYieldMeasUnitTypeCode", defaultMeasurementUnitCode, dopYldContract.getDefaultYieldMeasUnitTypeCode());
		Assert.assertNull("GrainFromOtherSourceInd", dopYldContract.getGrainFromOtherSourceInd());
		Assert.assertEquals("InsurancePlanId", 4, dopYldContract.getInsurancePlanId().intValue());
		
		List<AnnualFieldRsrc> fields = dopYldContract.getFields();
		Assert.assertNotNull(fields);
		Assert.assertEquals("More than one field returned", 1, fields.size());
		
		AnnualFieldRsrc field = fields.get(0);

		
		Assert.assertEquals(987652022, field.getAnnualFieldDetailId().intValue());
		Assert.assertEquals(888882022, field.getContractedFieldDetailId().intValue());
		Assert.assertEquals(2022, field.getCropYear().intValue());
		Assert.assertEquals(1, field.getDisplayOrder().intValue());
		Assert.assertEquals(123456999, field.getFieldId().intValue());
		Assert.assertEquals("Test Field", field.getFieldLabel());
		Assert.assertEquals(987654321, field.getLegalLandId().intValue());
		Assert.assertEquals("Test Other", field.getOtherLegalDescription());


		Assert.assertNotNull(field.getDopYieldFieldGrainList());
		Assert.assertEquals(2, field.getDopYieldFieldGrainList().size());  // Only two, because the first planting has 0 seeded acres, so is excluded.

		DopYieldFieldGrain dyf1 = field.getDopYieldFieldGrainList().get(0);
		Assert.assertEquals(18, dyf1.getCropCommodityId().intValue());
		Assert.assertEquals("CANOLA", dyf1.getCropCommodityName());
		Assert.assertEquals(1010471, dyf1.getCropVarietyId().intValue());
		Assert.assertEquals("6074 RR", dyf1.getCropVarietyName());
		Assert.assertEquals(2022, dyf1.getCropYear().intValue());
		Assert.assertNull(dyf1.getDeclaredYieldFieldGuid());
		Assert.assertNull(dyf1.getEstimatedYieldPerAcre());
		Assert.assertNull(dyf1.getEstimatedYieldPerAcreDefaultUnit());
		Assert.assertEquals(123456999, dyf1.getFieldId().intValue());
		Assert.assertEquals(4, dyf1.getInsurancePlanId().intValue());
		Assert.assertEquals("AbcDeFg12394039485-2", dyf1.getInventoryFieldGuid());
		Assert.assertEquals("isg123456789-2", dyf1.getInventorySeededGrainGuid());
		Assert.assertEquals(Boolean.FALSE, dyf1.getIsPedigreeInd());
		Assert.assertEquals(Double.valueOf(40), dyf1.getSeededAcres());
		Assert.assertNull(dyf1.getSeededDate());
		Assert.assertEquals(Boolean.FALSE, dyf1.getUnharvestedAcresInd());
		
		
		DopYieldFieldGrain dyf2 = field.getDopYieldFieldGrainList().get(1);
		Assert.assertEquals(18, dyf2.getCropCommodityId().intValue());
		Assert.assertEquals("CANOLA", dyf2.getCropCommodityName());
		Assert.assertEquals(1010471, dyf2.getCropVarietyId().intValue());
		Assert.assertEquals("6074 RR", dyf2.getCropVarietyName());
		Assert.assertEquals(2022, dyf2.getCropYear().intValue());
		Assert.assertNull(dyf2.getDeclaredYieldFieldGuid());
		Assert.assertNull(dyf2.getEstimatedYieldPerAcre());
		Assert.assertNull(dyf2.getEstimatedYieldPerAcreDefaultUnit());
		Assert.assertEquals(123456999, dyf2.getFieldId().intValue());
		Assert.assertEquals(4, dyf2.getInsurancePlanId().intValue());
		Assert.assertEquals("AbcDeFg12394039485-3", dyf2.getInventoryFieldGuid());
		Assert.assertEquals("isg123456789-3", dyf2.getInventorySeededGrainGuid());
		Assert.assertEquals(Boolean.FALSE, dyf2.getIsPedigreeInd());
		Assert.assertEquals(Double.valueOf(10), dyf2.getSeededAcres());
		Assert.assertEquals(null, dyf2.getSeededDate());
		Assert.assertEquals(Boolean.FALSE, dyf2.getUnharvestedAcresInd());
		

		//*****************************************************************
		//AFTER TESTS: DELETE LAND AND INVENTORY BY RUNNING THE DELETE SCRIPTS BELOW
		//*****************************************************************


		logger.debug(">testRolloverDopYieldContract");
	
	/* 
	 * 
	*****************
	INSERT STATEMENTS
	*****************
	INSERT INTO cuws.legal_land(legal_land_id, primary_reference_type_code, legal_description, legal_short_description, other_description, active_from_crop_year, active_to_crop_year, data_sync_trans_date, create_user, create_date, update_user, update_date, primary_property_identifier)
	VALUES (987654321, 'OTHER', 'Test Legal', 'Test Legal Short', 'Test Other', 1980, null, now(), 'admin', now(), 'admin', now(), '123456723');
	 
	INSERT INTO cuws.field(field_id, field_label, active_from_crop_year, active_to_crop_year, data_sync_trans_date, create_user, create_date, update_user, update_date)
	VALUES (123456999, 'Test Field', 1980, null, now(), 'admin', now(), 'admin', now());
	
	--policy and land ************************************
--	
	INSERT INTO cuws.policy(policy_id, grower_id, insurance_plan_id, policy_status_code, office_id, policy_number, contract_number, contract_id, crop_year, data_sync_trans_date, create_user, create_date, update_user, update_date)
	VALUES (999888777, 4, 4, 'ACTIVE', 1, '999888-22', '999888', 999888777, 2022, now(), 'admin', now(), 'admin', now());

	INSERT INTO cuws.grower_contract_year(grower_contract_year_id, grower_id, insurance_plan_id, contract_id, crop_year, data_sync_trans_date, create_user, create_date, update_user, update_date)
	VALUES (999992022, 4, 4, 999888777, 2022, now(), 'admin', now(), 'admin', now());

	INSERT INTO cuws.inventory_contract(inventory_contract_guid, contract_id, crop_year, unseeded_intentions_submitted_ind, seeded_crop_report_submitted_ind, fertilizer_ind, herbicide_ind, 
		tilliage_ind, other_changes_ind, other_changes_comment, grain_from_prev_year_ind, create_user, create_date, update_user, update_date)
	VALUES ('ic123456789-2022', 999888777, 2022, 'N', 'N', 'N', 'N', 'N', 'N', null, 'N', 'admin', now(), 'admin', now());
--
	INSERT INTO cuws.annual_field_detail(annual_field_detail_id, legal_land_id, field_id, crop_year, data_sync_trans_date, create_user, create_date, update_user, update_date)
	VALUES (987652022, 987654321, 123456999, 2022, now(), 'admin', now(), 'admin', now());
	
	INSERT INTO cuws.contracted_field_detail(contracted_field_detail_id, annual_field_detail_id, grower_contract_year_id, display_order, data_sync_trans_date, create_user, create_date, update_user, update_date)
	VALUES (888882022, 987652022, 999992022, 1, now(), 'admin', now(), 'admin', now());
	
	--Inventory Field
	INSERT INTO cuws.inventory_field(inventory_field_guid, insurance_plan_id, field_id, last_year_crop_commodity_id, crop_year, planting_number, create_user, create_date, update_user, update_date, is_hidden_on_printout_ind)
	VALUES ('AbcDeFg12394039485-1', 4, 123456999, null, 2022, 1, 'admin', now(), 'admin', now(), 'N');
	
	INSERT INTO cuws.inventory_field(inventory_field_guid, insurance_plan_id, field_id, last_year_crop_commodity_id, crop_year, planting_number, create_user, create_date, update_user, update_date, is_hidden_on_printout_ind)
	VALUES ('AbcDeFg12394039485-2', 4, 123456999, null, 2022, 2, 'admin', now(), 'admin', now(), 'N');

	INSERT INTO cuws.inventory_field(inventory_field_guid, insurance_plan_id, field_id, last_year_crop_commodity_id, crop_year, planting_number, create_user, create_date, update_user, update_date, is_hidden_on_printout_ind)
	VALUES ('AbcDeFg12394039485-3', 4, 123456999, null, 2022, 3, 'admin', now(), 'admin', now(), 'Y');
	
	--Plantings
	INSERT INTO cuws.inventory_seeded_grain (inventory_seeded_grain_guid, inventory_field_guid, crop_commodity_id, crop_variety_id, commodity_type_code, is_quantity_insurable_ind, is_replaced_ind, is_pedigree_ind, seeding_date, seeded_acres, is_spot_loss_insurable_ind, create_user, create_date, update_user, update_date)
	VALUES ('isg123456789-1', 'AbcDeFg12394039485-1', 26,  1010603,  'CPSW',  'N',  'Y',  'N',  null,  0, 'N', 'admin', now(), 'admin', now());
	INSERT INTO cuws.inventory_seeded_grain (inventory_seeded_grain_guid, inventory_field_guid, crop_commodity_id, crop_variety_id, commodity_type_code, is_quantity_insurable_ind, is_replaced_ind, is_pedigree_ind, seeding_date, seeded_acres, is_spot_loss_insurable_ind, create_user, create_date, update_user, update_date)
	VALUES ('isg123456789-2', 'AbcDeFg12394039485-2', 18,  1010471,  'Argentine Canola',  'N',  'N',  'N',  null,  40, 'N', 'admin', now(), 'admin', now());
	INSERT INTO cuws.inventory_seeded_grain (inventory_seeded_grain_guid, inventory_field_guid, crop_commodity_id, crop_variety_id, commodity_type_code, is_quantity_insurable_ind, is_replaced_ind, is_pedigree_ind, seeding_date, seeded_acres, is_spot_loss_insurable_ind, create_user, create_date, update_user, update_date)
	VALUES ('isg123456789-3', 'AbcDeFg12394039485-3', 18,  1010471,  'Argentine Canola',  'N',  'Y',  'N',  null,  10, 'N', 'admin', now(), 'admin', now());

	*****************
	DELETE STATEMENTS
	*****************

	DELETE FROM inventory_seeded_grain WHERE inventory_field_guid like 'AbcDeFg12394039485%';
	DELETE FROM inventory_field t WHERE t.inventory_field_guid like 'AbcDeFg12394039485%';
	DELETE FROM contracted_field_detail WHERE contracted_field_detail_id IN (888882022);
	DELETE FROM inventory_contract WHERE inventory_contract_guid = 'ic123456789-2022';
	DELETE FROM grower_contract_year WHERE grower_contract_year_id IN (999992022);
	DELETE FROM policy WHERE policy_id IN (999888777);
	DELETE FROM annual_field_detail where annual_field_detail_id IN (987652022);	
	DELETE FROM field WHERE field_id = 123456999;
	DELETE FROM legal_land t WHERE t.legal_land_id = 987654321;	
	 
	*****************
	SELECT STATEMENTS
	*****************
	SELECT * FROM inventory_seeded_grain WHERE inventory_field_guid like 'AbcDeFg12394039485%';
	SELECT * FROM inventory_field t WHERE t.inventory_field_guid like 'AbcDeFg12394039485%';
	SELECT * FROM contracted_field_detail WHERE contracted_field_detail_id IN (888882022);
	SELECT * FROM grower_contract_year WHERE grower_contract_year_id IN (999992022);
	SELECT * FROM inventory_contract WHERE inventory_contract_guid = 'ic123456789-2022';
	SELECT * FROM policy WHERE policy_id IN (999888777);
	SELECT * FROM annual_field_detail where annual_field_detail_id IN (987652022);	
	SELECT * FROM field WHERE field_id = 123456999;
	SELECT * FROM legal_land t WHERE t.legal_land_id = 987654321;	
 
	 */
	}
	
	@Test
	public void testRolloverDopYieldContractCommodities() throws CirrasUnderwritingServiceException, Oauth2ClientException {
		logger.debug("<testRolloverDopYieldContractCommodities");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasUnderwritingService service = getService(SCOPES);

		//*****************************************************************
		//BEFORE TESTS: CREATE INVENTORY AND CONTRACTS BY RUNNING THE INSERT SCRIPTS BELOW
		//*****************************************************************
		
		String policyNumber = "999888-22";
		
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
		Assert.assertNull(referrer.getDeclaredYieldContractGuid());

		DopYieldContractRsrc dopYldContract = service.rolloverDopYieldContract(referrer);
		Assert.assertNotNull(dopYldContract);

		//Test Declared Yield Contract Commodity
		List<DopYieldContractCommodity> commodities = dopYldContract.getDopYieldContractCommodities();

		Assert.assertNotNull(commodities);
		Assert.assertEquals("Wrong Number of Commodity Records", 2, commodities.size());
		
		//Object for comparison
		DopYieldContractCommodity dycc1 = new DopYieldContractCommodity();
		dycc1.setDeclaredYieldContractCommodityGuid(null);
		dycc1.setDeclaredYieldContractGuid(null);
		dycc1.setCropCommodityId(16);
		dycc1.setCropCommodityName("BARLEY");
		dycc1.setIsPedigreeInd(false);
		dycc1.setHarvestedAcres(null);
		dycc1.setStoredYield(null);
		dycc1.setStoredYieldDefaultUnit(null);
		dycc1.setSoldYield(null);
		dycc1.setSoldYieldDefaultUnit(null);
		dycc1.setGradeModifierTypeCode(null);
		dycc1.setTotalInsuredAcres((double)100);

		DopYieldContractCommodity dycc2 = new DopYieldContractCommodity();
		dycc2.setDeclaredYieldContractCommodityGuid(null);
		dycc2.setDeclaredYieldContractGuid(null);
		dycc2.setCropCommodityId(18);
		dycc2.setCropCommodityName("CANOLA");
		dycc2.setIsPedigreeInd(true);
		dycc2.setHarvestedAcres(null);
		dycc2.setStoredYield(null);
		dycc2.setStoredYieldDefaultUnit(null);
		dycc2.setSoldYield(null);
		dycc2.setSoldYieldDefaultUnit(null);
		dycc2.setGradeModifierTypeCode(null);
		dycc2.setTotalInsuredAcres((double)120);

		for (DopYieldContractCommodity commodity : commodities) {
			
			if(commodity.getCropCommodityId() == 16) {
				checkDopYieldContractCommodity(dycc1, commodity);
			} else if(commodity.getCropCommodityId() == 18) {
				checkDopYieldContractCommodity(dycc2, commodity);
			} else {
				Assert.fail("Wrong commodity: " + commodity.getCropCommodityId());
			}
		}

		//*****************************************************************
		//AFTER TESTS: INVENTORY AND CONTRACTS BY RUNNING THE DELETE SCRIPTS BELOW
		//*****************************************************************


		logger.debug(">testRolloverDopYieldContractCommodities");
	
	/* 
	 * 
	*****************
	INSERT STATEMENTS
	*****************
	--policy ************************************
	INSERT INTO cuws.policy(policy_id, grower_id, insurance_plan_id, policy_status_code, office_id, policy_number, contract_number, contract_id, crop_year, data_sync_trans_date, create_user, create_date, update_user, update_date)
	VALUES (999888777, 4, 4, 'ACTIVE', 1, '999888-22', '999888', 999888777, 2022, now(), 'admin', now(), 'admin', now());

	INSERT INTO cuws.grower_contract_year(grower_contract_year_id, grower_id, insurance_plan_id, contract_id, crop_year, data_sync_trans_date, create_user, create_date, update_user, update_date)
	VALUES (999992022, 4, 4, 999888777, 2022, now(), 'admin', now(), 'admin', now());

	-- Inventory Contract
	INSERT INTO cuws.inventory_contract(inventory_contract_guid, contract_id, crop_year, unseeded_intentions_submitted_ind, seeded_crop_report_submitted_ind, fertilizer_ind, herbicide_ind, 
		tilliage_ind, other_changes_ind, other_changes_comment, grain_from_prev_year_ind, create_user, create_date, update_user, update_date)
	VALUES ('ic123456789-2022', 999888777, 2022, 'N', 'N', 'N', 'N', 'N', 'N', null, 'N', 'admin', now(), 'admin', now());

	-- Inventory Contract Commodity
	INSERT INTO cuws.inventory_contract_commodity(inventory_contract_commodity_guid, inventory_contract_guid, crop_commodity_id, is_pedigree_ind, total_seeded_acres, create_user, create_date, update_user, update_date)
	VALUES ('icc123456789-2022-01', 'ic123456789-2022', 16, 'N', 100, 'admin', now(), 'admin', now());
	INSERT INTO cuws.inventory_contract_commodity(inventory_contract_commodity_guid, inventory_contract_guid, crop_commodity_id, is_pedigree_ind, total_seeded_acres, create_user, create_date, update_user, update_date)
	VALUES ('icc123456789-2022-02', 'ic123456789-2022', 18, 'Y', 120, 'admin', now(), 'admin', now());

	*****************
	DELETE STATEMENTS
	*****************

	delete from inventory_contract_commodity where inventory_contract_guid = 'ic123456789-2022';
	DELETE FROM inventory_contract WHERE inventory_contract_guid = 'ic123456789-2022';
	DELETE FROM grower_contract_year WHERE grower_contract_year_id IN (999992022);
	DELETE FROM policy WHERE policy_id IN (999888777);
	 
	*****************
	SELECT STATEMENTS
	*****************
	select * from inventory_contract_commodity where inventory_contract_guid = 'ic123456789-2022';
	SELECT * FROM grower_contract_year WHERE grower_contract_year_id IN (999992022);
	SELECT * FROM inventory_contract WHERE inventory_contract_guid = 'ic123456789-2022';
	SELECT * FROM policy WHERE policy_id IN (999888777);
 
	 */
	}	
	
	private void checkDopYieldContractCommodity(
			DopYieldContractCommodity expectedCommodity,
			DopYieldContractCommodity actualCommodity) {
		Assert.assertEquals("DeclaredYieldContractCommodityGuid", expectedCommodity.getDeclaredYieldContractCommodityGuid(), actualCommodity.getDeclaredYieldContractCommodityGuid());
		Assert.assertEquals("DeclaredYieldContractGuid", expectedCommodity.getDeclaredYieldContractGuid(), actualCommodity.getDeclaredYieldContractGuid());
		Assert.assertEquals("CropCommodityId", expectedCommodity.getCropCommodityId(), actualCommodity.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", expectedCommodity.getCropCommodityName(), actualCommodity.getCropCommodityName());
		Assert.assertEquals("IsPedigreeInd", expectedCommodity.getIsPedigreeInd(), actualCommodity.getIsPedigreeInd());
		Assert.assertEquals("HarvestedAcres", expectedCommodity.getHarvestedAcres(), actualCommodity.getHarvestedAcres());
		Assert.assertEquals("StoredYield", expectedCommodity.getStoredYield(), actualCommodity.getStoredYield());
		Assert.assertEquals("StoredYieldDefaultUnit", expectedCommodity.getStoredYieldDefaultUnit(), actualCommodity.getStoredYieldDefaultUnit());
		Assert.assertEquals("SoldYield", expectedCommodity.getSoldYield(), actualCommodity.getSoldYield());
		Assert.assertEquals("SoldYieldDefaultUnit", expectedCommodity.getSoldYieldDefaultUnit(), actualCommodity.getSoldYieldDefaultUnit());
		Assert.assertEquals("GradeModifierTypeCode", expectedCommodity.getGradeModifierTypeCode(), actualCommodity.getGradeModifierTypeCode());
		Assert.assertEquals("TotalInsuredAcres", expectedCommodity.getTotalInsuredAcres(), actualCommodity.getTotalInsuredAcres());

	}

	
}
