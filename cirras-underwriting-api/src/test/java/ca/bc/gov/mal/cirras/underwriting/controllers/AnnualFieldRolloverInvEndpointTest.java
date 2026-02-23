package ca.bc.gov.mal.cirras.underwriting.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.clients.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.clients.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.clients.ValidationException;
import ca.bc.gov.mal.cirras.underwriting.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.AnnualFieldListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.AnnualFieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryField;
import ca.bc.gov.mal.cirras.underwriting.data.models.PolicySimple;
import ca.bc.gov.mal.cirras.underwriting.data.models.UnderwritingComment;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryFieldDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.PolicyDto;
import ca.bc.gov.mal.cirras.underwriting.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class AnnualFieldRolloverInvEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(AnnualFieldRolloverInvEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.SEARCH_ANNUAL_FIELDS,
		Scopes.CREATE_INVENTORY_CONTRACT
	};
	
	
	@Test
	public void testAnnualFieldsRolloverInventoryFromPreviousYear() throws CirrasUnderwritingServiceException, Oauth2ClientException {
		logger.debug("<testAnnualFieldsRolloverInventoryFromPreviousYear");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//*****************************************************************
		//BEFORE TESTS: CREATE LAND AND INVENTORY BY RUNNING THE INSERT SCRIPTS BELOW
		//*****************************************************************
		
		CirrasUnderwritingService service = getService(SCOPES);

		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();
		String legalLandId = "987654321";
		Integer fieldId = 123456999;
		Integer insurancePlanId = 4;
		String cropYear = "2022";

		//getAnnualFieldList
		AnnualFieldListRsrc searchResults = service.getAnnualFieldList(
				topLevelEndpoints, 
				legalLandId, 
				null, //fieldId
				null,
				cropYear);

		Assert.assertNotNull(searchResults);
		
		List<AnnualFieldRsrc> fields = searchResults.getCollection();
		Assert.assertNotNull(fields);
		Assert.assertEquals("More than one field returned", 1, fields.size());
		
		AnnualFieldRsrc field = fields.get(0);
		Assert.assertEquals("field doesn't match", fieldId, field.getFieldId());
		
		//Rollover plantings
		AnnualFieldRsrc rolledoverField = service.rolloverAnnualFieldInventory(field, cropYear, insurancePlanId.toString()); 

		Assert.assertNotNull(rolledoverField);
		Assert.assertEquals("More plantings than expected returned", 3, rolledoverField.getPlantings().size());
		
		for (InventoryField invField: rolledoverField.getPlantings()) {
			Assert.assertFalse("is hidden on printout ind is not false", invField.getIsHiddenOnPrintoutInd());

			// TODO: Update to include variety (PIM-1466).
			
			Integer lastYearCmdtyId = invField.getLastYearCropCommodityId();
			//Check if commodity is oat or canola
			Assert.assertTrue("commodity id ("+lastYearCmdtyId+") not 24, 18 or 16", lastYearCmdtyId.equals(24) || lastYearCmdtyId.equals(18) || lastYearCmdtyId.equals(16));
			//Check acres according to commodity
			if(lastYearCmdtyId.equals(24)) {
				//Oat
				Assert.assertEquals("Oat not correct acres", (Double)25.0, invField.getInventoryUnseeded().getAcresToBeSeeded());
			} else if (lastYearCmdtyId.equals(18)) {
				//Canola
				Assert.assertEquals("Canola not correct acres", (Double)50.0, invField.getInventoryUnseeded().getAcresToBeSeeded());
			}else if (lastYearCmdtyId.equals(16)) {
				//Barley
				Assert.assertEquals("Barley not correct acres", (Double)30.0, invField.getInventoryUnseeded().getAcresToBeSeeded());
			}
		}

		//*****************************************************************
		//AFTER TESTS: DELETE LAND AND INVENTORY BY RUNNING THE DELETE SCRIPTS BELOW
		//*****************************************************************
		
		logger.debug(">testAnnualFieldsRolloverInventoryFromPreviousYear");
	}
	
	/*
	*****************
	INSERT STATEMENTS FOR PREVIOUS YEAR TESTS
	*****************
	INSERT INTO cuws.legal_land(legal_land_id, primary_reference_type_code, legal_description, legal_short_description, other_description, active_from_crop_year, active_to_crop_year, create_user, create_date, update_user, update_date, primary_property_identifier)
	VALUES (987654321, 'OTHER', 'Test Legal', 'Test Legal Short', 'Test Other', 1980, null, 'admin', now(), 'admin', now(), '123456723');
	 
	INSERT INTO cuws.field(field_id, field_label, active_from_crop_year, active_to_crop_year, create_user, create_date, update_user, update_date)
	VALUES (123456999, 'Test Field', 1980, null, 'admin', now(), 'admin', now());
	
	INSERT INTO cuws.annual_field_detail(annual_field_detail_id, legal_land_id, field_id, crop_year, create_user, create_date, update_user, update_date)
	VALUES (987652365, 987654321, 123456999, 2022, 'admin', now(), 'admin', now());
	
	--Inventory Field
	INSERT INTO cuws.inventory_field(inventory_field_guid, insurance_plan_id, field_id, last_year_crop_commodity_id, crop_year, planting_number, create_user, create_date, update_user, update_date, is_hidden_on_printout_ind)
	VALUES ('AbcDeFg12394039485-1', 4, 123456999, null, 2021, 1, 'admin', now(), 'admin', now(), 'N');
	
	INSERT INTO cuws.inventory_field(inventory_field_guid, insurance_plan_id, field_id, last_year_crop_commodity_id, crop_year, planting_number, create_user, create_date, update_user, update_date, is_hidden_on_printout_ind)
	VALUES ('AbcDeFg12394039485-2', 4, 123456999, null, 2021, 2, 'admin', now(), 'admin', now(), 'N');

	INSERT INTO cuws.inventory_field(inventory_field_guid, insurance_plan_id, field_id, last_year_crop_commodity_id, crop_year, planting_number, create_user, create_date, update_user, update_date, is_hidden_on_printout_ind)
	VALUES ('AbcDeFg12394039485-3', 4, 123456999, null, 2021, 3, 'admin', now(), 'admin', now(), 'Y');
	
	--Plantings
	INSERT INTO cuws.inventory_seeded_grain (inventory_seeded_grain_guid, inventory_field_guid, crop_commodity_id, crop_variety_id, commodity_type_code, is_quantity_insurable_ind, is_replaced_ind, is_pedigree_ind, seeding_date, seeded_acres, is_spot_loss_insurable_ind, create_user, create_date, update_user, update_date)
	VALUES (replace(cast(gen_random_uuid() as text), '-', ''), 'AbcDeFg12394039485-1', 24,  1010570,  'Forage Oat',  'N',  'N',  'N',  null,  25, 'N', 'admin', now(), 'admin', now());
	INSERT INTO cuws.inventory_seeded_grain (inventory_seeded_grain_guid, inventory_field_guid, crop_commodity_id, crop_variety_id, commodity_type_code, is_quantity_insurable_ind, is_replaced_ind, is_pedigree_ind, seeding_date, seeded_acres, is_spot_loss_insurable_ind, create_user, create_date, update_user, update_date)
	VALUES (replace(cast(gen_random_uuid() as text), '-', ''), 'AbcDeFg12394039485-1', 26,  1010603,  'CPSW',  'N',  'Y',  'N',  null,  0, 'N', 'admin', now(), 'admin', now());
	INSERT INTO cuws.inventory_seeded_grain (inventory_seeded_grain_guid, inventory_field_guid, crop_commodity_id, crop_variety_id, commodity_type_code, is_quantity_insurable_ind, is_replaced_ind, is_pedigree_ind, seeding_date, seeded_acres, is_spot_loss_insurable_ind, create_user, create_date, update_user, update_date)
	VALUES (replace(cast(gen_random_uuid() as text), '-', ''), 'AbcDeFg12394039485-2', 18,  1010471,  'Argentine Canola',  'N',  'N',  'N',  null,  40, 'N', 'admin', now(), 'admin', now());
	INSERT INTO cuws.inventory_seeded_grain (inventory_seeded_grain_guid, inventory_field_guid, crop_commodity_id, crop_variety_id, commodity_type_code, is_quantity_insurable_ind, is_replaced_ind, is_pedigree_ind, seeding_date, seeded_acres, is_spot_loss_insurable_ind, create_user, create_date, update_user, update_date)
	VALUES (replace(cast(gen_random_uuid() as text), '-', ''), 'AbcDeFg12394039485-3', 16,  1010424,  'Two Row',  'N',  'N',  'N',  null,  30, 'N', 'admin', now(), 'admin', now());
	INSERT INTO cuws.inventory_seeded_grain (inventory_seeded_grain_guid, inventory_field_guid, crop_commodity_id, crop_variety_id, commodity_type_code, is_quantity_insurable_ind, is_replaced_ind, is_pedigree_ind, seeding_date, seeded_acres, is_spot_loss_insurable_ind, create_user, create_date, update_user, update_date)
	VALUES (replace(cast(gen_random_uuid() as text), '-', ''), 'AbcDeFg12394039485-3', 18,  1010471,  'Argentine Canola',  'N',  'Y',  'N',  null,  10, 'N', 'admin', now(), 'admin', now());
	
	*****************
	DELETE STATEMENTS
	*****************

	DELETE FROM inventory_seeded_grain WHERE inventory_field_guid like 'AbcDeFg12394039485%';
	DELETE FROM inventory_field t WHERE t.inventory_field_guid like 'AbcDeFg12394039485%';
	DELETE FROM annual_field_detail where annual_field_detail_id = 987652365;	
	DELETE FROM field WHERE field_id = 123456999;
	DELETE FROM legal_land t WHERE t.legal_land_id = 987654321;	
	 
	*****************
	SELECT STATEMENTS
	*****************
	SELECT * FROM inventory_seeded_grain WHERE inventory_field_guid like 'AbcDeFg12394039485%';
	SELECT * FROM inventory_field t WHERE t.inventory_field_guid like 'AbcDeFg12394039485%';
	SELECT * FROM annual_field_detail where annual_field_detail_id = 987652365;
	SELECT * FROM field WHERE field_id = 123456999;
	SELECT * FROM legal_land t WHERE t.legal_land_id = 987654321;	
 
	 */

	
	@Test
	public void testAnnualFieldsRolloverInventoryFromSameYear() throws CirrasUnderwritingServiceException, Oauth2ClientException {
		logger.debug("<testAnnualFieldsRolloverInventoryFromSameYear");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//*****************************************************************
		//BEFORE TESTS: CREATE LAND AND INVENTORY BY RUNNING THE INSERT SCRIPTS BELOW
		//*****************************************************************
		
		CirrasUnderwritingService service = getService(SCOPES);

		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();
		String legalLandId = "987654321";
		Integer fieldId = 123456999;
		Integer insurancePlanId = 4;
		String cropYear = "2022";

		//getAnnualFieldList
		AnnualFieldListRsrc searchResults = service.getAnnualFieldList(
				topLevelEndpoints, 
				legalLandId, 
				null, //fieldId
				null,
				cropYear);

		Assert.assertNotNull(searchResults);
		
		List<AnnualFieldRsrc> fields = searchResults.getCollection();
		Assert.assertNotNull(fields);
		Assert.assertEquals("More than one field returned", 1, fields.size());
		
		AnnualFieldRsrc field = fields.get(0);
		Assert.assertEquals("field doesn't match", fieldId, field.getFieldId());
		
		//Rollover plantings
		AnnualFieldRsrc rolledoverField = service.rolloverAnnualFieldInventory(field, cropYear, insurancePlanId.toString()); 

		Assert.assertNotNull(rolledoverField);
		Assert.assertEquals("Different number of plantings than expected returned", 3, rolledoverField.getPlantings().size());
		Assert.assertEquals("Different number of comments than expected returned", 2, rolledoverField.getUwComments().size());
		
		
		
		for (UnderwritingComment comment: rolledoverField.getUwComments()) {
			
			String commentText = comment.getUnderwritingComment();
			
			//Check if comment text are correct
			Assert.assertTrue("comment ("+commentText+") not correct", commentText.equals("Comment 1") || commentText.equals("Comment 2"));
		}

		//*****************************************************************
		//AFTER TESTS: DELETE LAND AND INVENTORY BY RUNNING THE DELETE SCRIPTS BELOW
		//*****************************************************************
		
		logger.debug(">testAnnualFieldsRolloverInventorySameYear");
	}
	
	/*
	*****************
	INSERT STATEMENTS FOR SAME YEAR TESTS
	*****************
	INSERT INTO cuws.legal_land(legal_land_id, primary_reference_type_code, legal_description, legal_short_description, other_description, active_from_crop_year, active_to_crop_year, data_sync_trans_date, create_user, create_date, update_user, update_date, primary_property_identifier)
	VALUES (987654321, 'OTHER', 'Test Legal', 'Test Legal Short', 'Test Other', 1980, null, now(), 'admin', now(), 'admin', now(), '123456723');
	 
	INSERT INTO cuws.field(field_id, field_label, active_from_crop_year, active_to_crop_year, data_sync_trans_date, create_user, create_date, update_user, update_date)
	VALUES (123456999, 'Test Field', 1980, null, now(), 'admin', now(), 'admin', now());
	
	INSERT INTO cuws.annual_field_detail(annual_field_detail_id, legal_land_id, field_id, crop_year, data_sync_trans_date, create_user, create_date, update_user, update_date)
	VALUES (987652365, 987654321, 123456999, 2022, now(), 'admin', now(), 'admin', now());
	
	--Inventory Field
	INSERT INTO cuws.inventory_field(inventory_field_guid, insurance_plan_id, field_id, last_year_crop_commodity_id, crop_year, planting_number, create_user, create_date, update_user, update_date, is_hidden_on_printout_ind)
	VALUES ('AbcDeFg12394039485-1', 4, 123456999, null, 2022, 1, 'admin', now(), 'admin', now(), 'N');
	
	INSERT INTO cuws.inventory_field(inventory_field_guid, insurance_plan_id, field_id, last_year_crop_commodity_id, crop_year, planting_number, create_user, create_date, update_user, update_date, is_hidden_on_printout_ind)
	VALUES ('AbcDeFg12394039485-2', 4, 123456999, null, 2022, 2, 'admin', now(), 'admin', now(), 'N');

	INSERT INTO cuws.inventory_field(inventory_field_guid, insurance_plan_id, field_id, last_year_crop_commodity_id, crop_year, planting_number, create_user, create_date, update_user, update_date, is_hidden_on_printout_ind)
	VALUES ('AbcDeFg12394039485-3', 4, 123456999, null, 2022, 3, 'admin', now(), 'admin', now(), 'N');
	
	--Comments
	INSERT INTO cuws.underwriting_comment(underwriting_comment_guid, underwriting_comment_type_code, underwriting_comment, create_user, create_date, update_user, update_date, annual_field_detail_id)
	VALUES ('cmnt-1', 'INV', 'Comment 1', 'admin', now(), 'admin', now(), 987652365);
		
	INSERT INTO cuws.underwriting_comment(underwriting_comment_guid, underwriting_comment_type_code, underwriting_comment, create_user, create_date, update_user, update_date, annual_field_detail_id)
	VALUES ('cmnt-2', 'INV', 'Comment 2', 'admin', now(), 'admin', now(), 987652365);
	
	--Plantings

	
	*****************
	DELETE STATEMENTS
	*****************

	DELETE FROM inventory_field t WHERE t.inventory_field_guid like 'AbcDeFg12394039485%';
	DELETE FROM underwriting_comment where annual_field_detail_id = 987652365;
	DELETE FROM annual_field_detail where annual_field_detail_id = 987652365;	
	DELETE FROM field WHERE field_id = 123456999;
	DELETE FROM legal_land t WHERE t.legal_land_id = 987654321;	
	 
	*****************
	SELECT STATEMENTS
	*****************
	SELECT * FROM inventory_field t WHERE t.inventory_field_guid like 'AbcDeFg12394039485%';
	SELECT * FROM underwriting_comment where annual_field_detail_id = 987652365;
	SELECT * FROM annual_field_detail where annual_field_detail_id = 987652365;
	SELECT * FROM field WHERE field_id = 123456999;
	SELECT * FROM legal_land t WHERE t.legal_land_id = 987654321;	
 
	 */
	
	
}
