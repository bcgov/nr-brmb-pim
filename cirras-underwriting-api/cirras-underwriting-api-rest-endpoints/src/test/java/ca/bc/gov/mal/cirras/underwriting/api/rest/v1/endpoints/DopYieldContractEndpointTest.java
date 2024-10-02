package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints;

import java.math.BigDecimal;
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
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventorySeededGrain;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryUnseeded;
import ca.bc.gov.mal.cirras.underwriting.model.v1.UnderwritingComment;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldGrain;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class DopYieldContractEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(DopYieldContractEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
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
		Scopes.GET_LEGAL_LAND,
		Scopes.DELETE_COMMENTS
	};
	
	private static final String[] SCOPESUSER2 = {
			Scopes.GET_TOP_LEVEL, 
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
			Scopes.GET_LEGAL_LAND,
			Scopes.GET_LAND
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
		
	private String inventoryContractGuid1 = null;	
	private String declaredYieldContractGuid1 = null;

	private String inventoryFieldGuid1 = null;
	private String inventoryFieldGuid2 = null;
	
	private String inventorySeededGrainGuid1 = null;
	private String inventorySeededGrainGuid2 = null;
	
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;
	private CirrasUnderwritingService serviceUser2;
	private EndpointsRsrc topLevelEndpointsUser2;

	@Before
	public void prepareTests() throws CirrasUnderwritingServiceException, Oauth2ClientException, NotFoundDaoException, DaoException{
		service = getService(SCOPES);
		topLevelEndpoints = service.getTopLevelEndpoints();

		serviceUser2 = getServiceSecondUser(SCOPESUSER2);
		topLevelEndpointsUser2 = serviceUser2.getTopLevelEndpoints();

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

		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId1.toString());
		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId1.toString());
		service.deleteField(topLevelEndpoints, fieldId1.toString());
		service.deleteLegalLandSync(topLevelEndpoints, legalLandId1.toString());
		
		service.deleteGrowerContractYear(topLevelEndpoints, gcyId1.toString());
		service.deletePolicy(topLevelEndpoints, policyId1.toString());
		service.deleteGrower(topLevelEndpoints, growerId1.toString());
	}
	
	@Test
	public void testGetDopYieldContract() throws CirrasUnderwritingServiceException, Oauth2ClientException {
		logger.debug("<testGetDopYieldContract");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//*****************************************************************
		//BEFORE TESTS: CREATE LAND, INVENTORY AND YIELD BY RUNNING THE INSERT SCRIPTS BELOW.
		//*****************************************************************
		
		String policyNumber = "999888-22";

		// Dop Date
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2022, Calendar.JANUARY, 15);
		Date dopDate = cal.getTime();
		
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
		Assert.assertNotNull(referrer.getInventoryContractGuid());
		Assert.assertNotNull(referrer.getDeclaredYieldContractGuid());

		DopYieldContractRsrc dopYldContract = service.getDopYieldContract(referrer);
		Assert.assertNotNull(dopYldContract);
		
		Assert.assertEquals("ContractId", referrer.getContractId(), dopYldContract.getContractId());
		Assert.assertEquals("CropYear", referrer.getCropYear(), dopYldContract.getCropYear());
		Assert.assertEquals("DeclarationOfProductionDate", dopDate, dopYldContract.getDeclarationOfProductionDate());
		Assert.assertEquals("DeclaredYieldContractGuid", "dyc123456789-2022", dopYldContract.getDeclaredYieldContractGuid());
		Assert.assertEquals("DefaultYieldMeasUnitTypeCode", "TONNE", dopYldContract.getDefaultYieldMeasUnitTypeCode());
		// Assert.assertEquals("DopUpdateTimestamp", dopUpdateTimestamp, dopYldContract.getDopUpdateTimestamp());
		// Assert.assertEquals("DopUpdateUser", "JSMITH", dopYldContract.getDopUpdateUser());
		Assert.assertEquals("EnteredYieldMeasUnitTypeCode", "BUSHEL", dopYldContract.getEnteredYieldMeasUnitTypeCode());
		Assert.assertEquals("GrainFromOtherSourceInd", true, dopYldContract.getGrainFromOtherSourceInd());
		Assert.assertEquals("InsurancePlanId", 4, dopYldContract.getInsurancePlanId().intValue());
		Assert.assertEquals("growerContractYearId", 999992022, dopYldContract.getGrowerContractYearId().intValue());
		
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
		Assert.assertEquals("dyf123456789-2022", dyf1.getDeclaredYieldFieldGuid());
		Assert.assertEquals(Double.valueOf(12.34), dyf1.getEstimatedYieldPerAcre());
		Assert.assertEquals(Double.valueOf(56.78), dyf1.getEstimatedYieldPerAcreDefaultUnit());
		Assert.assertEquals(123456999, dyf1.getFieldId().intValue());
		Assert.assertEquals(4, dyf1.getInsurancePlanId().intValue());
		Assert.assertEquals("AbcDeFg12394039485-2", dyf1.getInventoryFieldGuid());
		Assert.assertEquals("isg123456789-2", dyf1.getInventorySeededGrainGuid());
		Assert.assertEquals(Boolean.FALSE, dyf1.getIsPedigreeInd());
		Assert.assertEquals(Double.valueOf(40), dyf1.getSeededAcres());
		Assert.assertNull(dyf1.getSeededDate());
		Assert.assertEquals(Boolean.TRUE, dyf1.getUnharvestedAcresInd());
		
		
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
		//AFTER TESTS: DELETE LAND, INVENTORY AND YIELD BY RUNNING THE DELETE SCRIPTS BELOW
		//*****************************************************************


		logger.debug(">testGetDopYieldContract");
	
	/* 
	 * 
	*****************
	INSERT STATEMENTS
	*****************
	INSERT INTO cuws.legal_land(legal_land_id, primary_reference_type_code, legal_description, legal_short_description, other_description, active_from_crop_year, active_to_crop_year, data_sync_trans_date, create_user, create_date, update_user, update_date, primary_property_identifier)
	VALUES (987654321, 'OTHER', 'Test Legal', 'Test Legal Short', 'Test Other', 1980, null, now(), 'admin', now(), 'admin', now(), '123456723');
	 
	INSERT INTO cuws.field(field_id, field_label, active_from_crop_year, active_to_crop_year, data_sync_trans_date, create_user, create_date, update_user, update_date)
	VALUES (123456999, 'Test Field', 1980, null, now(), 'admin', now(), 'admin', now());
	
	INSERT INTO cuws.policy(policy_id, grower_id, insurance_plan_id, policy_status_code, office_id, policy_number, contract_number, contract_id, crop_year, data_sync_trans_date, create_user, create_date, update_user, update_date)
	VALUES (999888777, 4, 4, 'ACTIVE', 1, '999888-22', '999888', 999888777, 2022, now(), 'admin', now(), 'admin', now());

	INSERT INTO cuws.grower_contract_year(grower_contract_year_id, grower_id, insurance_plan_id, contract_id, crop_year, data_sync_trans_date, create_user, create_date, update_user, update_date)
	VALUES (999992022, 4, 4, 999888777, 2022, now(), 'admin', now(), 'admin', now());

	INSERT INTO cuws.inventory_contract(inventory_contract_guid, contract_id, crop_year, unseeded_intentions_submitted_ind, seeded_crop_report_submitted_ind, fertilizer_ind, herbicide_ind, 
		tilliage_ind, other_changes_ind, other_changes_comment, grain_from_prev_year_ind, create_user, create_date, update_user, update_date)
	VALUES ('ic123456789-2022', 999888777, 2022, 'N', 'N', 'N', 'N', 'N', 'N', null, 'N', 'admin', now(), 'admin', now());

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

	-- Declared Yield Contract
	insert into declared_yield_contract(declared_yield_contract_guid, contract_id, crop_year, declaration_of_production_date, dop_update_timestamp, dop_update_user, entered_yield_meas_unit_type_code, default_yield_meas_unit_type_code, grain_from_other_source_ind, create_user, create_date, update_user, update_date) 
	values ('dyc123456789-2022', 999888777, 2022, '2022-01-15', '2022-02-16 17:45:55', 'JSMITH', 'BUSHEL', 'TONNE', 'Y', 'admin', now(), 'admin', now());

	-- Yield Field
	INSERT INTO cuws.declared_yield_field(declared_yield_field_guid, inventory_field_guid, estimated_yield_per_acre, estimated_yield_per_acre_default_unit, unharvested_acres_ind, create_user, create_date, update_user, update_date)
	VALUES ('dyf123456789-2022', 'AbcDeFg12394039485-2', 12.34, 56.78, 'Y', 'admin', now(), 'admin', now());	

	*****************
	DELETE STATEMENTS
	*****************
	delete from declared_yield_field where declared_yield_field_guid = 'dyf123456789-2022';
	delete from declared_yield_contract where declared_yield_contract_guid = 'dyc123456789-2022';

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
	select * from declared_yield_field where declared_yield_field_guid = 'dyf123456789-2022';
	select * from declared_yield_contract dyc where dyc.declared_yield_contract_guid = 'dyc123456789-2022';
	
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
	public void testDeclaredYieldFieldRollup() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException {
		//Test insert/update and calculate yield field rollup
		logger.debug("<testDeclaredYieldFieldRollup");
		
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
		
		createInventoryContract();
		
		// Dop Date
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2022, Calendar.JANUARY, 15);
		Date dopDate = cal.getTime();
		
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
		Assert.assertEquals(inventoryContractGuid1, referrer.getInventoryContractGuid());
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

		
		DopYieldFieldGrain dyf1 = newDyc.getFields().get(0).getDopYieldFieldGrainList().get(0);
		DopYieldFieldGrain dyf2 = newDyc.getFields().get(0).getDopYieldFieldGrainList().get(1);
		
		dyf1.setEstimatedYieldPerAcre(11.22);
		dyf1.setEstimatedYieldPerAcreDefaultUnit(null); // Calculated by backend
		dyf1.setUnharvestedAcresInd(true);
		
		DopYieldContractRsrc fetchedDyc = service.createDopYieldContract(topLevelEndpoints, newDyc);
		newDyc.setDeclaredYieldContractGuid(fetchedDyc.getDeclaredYieldContractGuid());
		
		dyf1.setDeclaredYieldFieldGuid(fetchedDyc.getFields().get(0).getDopYieldFieldGrainList().get(0).getDeclaredYieldFieldGuid());
		dyf1.setEstimatedYieldPerAcreDefaultUnit(0.2443); // 11.22 / 45.93 (conversion factor for BARLEY from BUSHELS to TONNES).

		dyf2.setDeclaredYieldFieldGuid(fetchedDyc.getFields().get(0).getDopYieldFieldGrainList().get(1).getDeclaredYieldFieldGuid());
		dyf2.setEstimatedYieldPerAcreDefaultUnit(null);
		
		checkDopYieldContract(newDyc, fetchedDyc);
		checkFieldsAndPlantings(newDyc.getFields(), fetchedDyc.getFields());
		
		declaredYieldContractGuid1 = fetchedDyc.getDeclaredYieldContractGuid();

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
		Assert.assertEquals(inventoryContractGuid1, referrer.getInventoryContractGuid());
		Assert.assertEquals(declaredYieldContractGuid1, referrer.getDeclaredYieldContractGuid());
		
		fetchedDyc = service.getDopYieldContract(referrer);
		checkDopYieldContract(newDyc, fetchedDyc);
		checkFieldsAndPlantings(newDyc.getFields(), fetchedDyc.getFields());

		// Dop Date
		cal = Calendar.getInstance();
		cal.clear();
		cal.set(2022, Calendar.JANUARY, 16);
		dopDate = cal.getTime();


		// Dop Update Timestamp
		cal = Calendar.getInstance();
		cal.clear();
		cal.set(2022, Calendar.FEBRUARY, 17, 18, 45, 55);
		
		fetchedDyc.setDeclarationOfProductionDate(dopDate);
		fetchedDyc.setDefaultYieldMeasUnitTypeCode("TONNE");
		// fetchedDyc.setDopUpdateTimestamp(dopUpdateTimestamp);
		// fetchedDyc.setDopUpdateUser("JDOE");
		fetchedDyc.setEnteredYieldMeasUnitTypeCode("TONNE");
		fetchedDyc.setGrainFromOtherSourceInd(false);

		// Dop Yield Field
		dyf1 = fetchedDyc.getFields().get(0).getDopYieldFieldGrainList().get(0);
		dyf1.setEstimatedYieldPerAcre(null);
		dyf1.setEstimatedYieldPerAcreDefaultUnit(null);
		dyf1.setUnharvestedAcresInd(false);
		
		dyf2 = fetchedDyc.getFields().get(0).getDopYieldFieldGrainList().get(1);
		dyf2.setEstimatedYieldPerAcre(33.44);
		dyf2.setEstimatedYieldPerAcreDefaultUnit(null); // Calculated by backend
		dyf2.setUnharvestedAcresInd(true);
		
		DopYieldContractRsrc updatedDyc = service.updateDopYieldContract(fetchedDyc);

		// update only setDopUpdateTimestamp and DopUpdateUser
		DopYieldContractRsrc updatedDyc2 = service.updateDopYieldContract(updatedDyc);		
		Assert.assertTrue("DopUpdateTimestamp", updatedDyc.getDopUpdateTimestamp().compareTo(updatedDyc2.getDopUpdateTimestamp()) < 0 );
		
		dyf1.setEstimatedYieldPerAcreDefaultUnit(null);
		dyf2.setEstimatedYieldPerAcreDefaultUnit(33.44); // Same as estimatedYieldPerAcre because entered units and default units are both TONNE.
		
		checkDopYieldContract(fetchedDyc, updatedDyc);
		checkFieldsAndPlantings(fetchedDyc.getFields(), updatedDyc.getFields());
		
		service.deleteDopYieldContract(updatedDyc2);
		
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
		Assert.assertEquals(inventoryContractGuid1, referrer.getInventoryContractGuid());
		Assert.assertNull(referrer.getDeclaredYieldContractGuid());

		declaredYieldContractGuid1 = null;
		
		delete();

		//*****************************************************************
		//AFTER TESTS: DELETE Inventory, Grower Contract Year, Policy and Grower using scripts below.
		//             If the growerId, policyId, contractId, cropYear or growerContractYearId fields are changed,
		//             these scripts must also be updated.
		//*****************************************************************
		
		logger.debug(">testDeclaredYieldFieldRollup");

	
	/* 
	 * 
	*****************
	DELETE STATEMENTS
	*****************
	DELETE FROM declared_yield_field WHERE inventory_field_guid IN (SELECT inventory_field_guid FROM inventory_field WHERE field_id = 90000006 AND crop_year = 2021);
	DELETE FROM declared_yield_contract WHERE contract_id = 90000004 and crop_year = 2021;

	DELETE FROM inventory_seeded_grain WHERE inventory_field_guid IN (SELECT inventory_field_guid FROM inventory_field WHERE field_id = 90000006 AND crop_year = 2021);
	DELETE FROM inventory_unseeded WHERE inventory_field_guid IN (SELECT inventory_field_guid FROM inventory_field WHERE field_id = 90000006 AND crop_year = 2021);
	DELETE FROM inventory_field WHERE field_id = 90000006 AND crop_year = 2021;
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
	SELECT * FROM declared_yield_field WHERE inventory_field_guid IN (SELECT inventory_field_guid FROM inventory_field WHERE field_id = 90000006 AND crop_year = 2021);
	SELECT * FROM declared_yield_contract WHERE contract_id = 90000004 and crop_year = 2021;

	SELECT * FROM inventory_seeded_grain WHERE inventory_field_guid IN (SELECT inventory_field_guid FROM inventory_field WHERE field_id = 90000006 AND crop_year = 2021);
	SELECT * FROM inventory_unseeded WHERE inventory_field_guid IN (SELECT inventory_field_guid FROM inventory_field WHERE field_id = 90000006 AND crop_year = 2021);
	SELECT * FROM inventory_field WHERE field_id = 90000006 AND crop_year = 2021;
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
	public void testGetDeclaredYieldContractCommodity() throws CirrasUnderwritingServiceException, Oauth2ClientException {
		logger.debug("<testGetDeclaredYieldContractCommodity");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//*****************************************************************
		//BEFORE TESTS: CREATE LAND, INVENTORY AND YIELD BY RUNNING THE INSERT SCRIPTS BELOW.
		//*****************************************************************
		
		String policyNumber = "999888-22";
		
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

		DopYieldContractRsrc dopYldContract = service.getDopYieldContract(referrer);
		Assert.assertNotNull(dopYldContract);
		
		Assert.assertEquals("ContractId", referrer.getContractId(), dopYldContract.getContractId());
		Assert.assertEquals("CropYear", referrer.getCropYear(), dopYldContract.getCropYear());

		List<DopYieldContractCommodity> commodities = dopYldContract.getDopYieldContractCommodities();

		Assert.assertNotNull(commodities);
		Assert.assertEquals("Wrong Number of Commodity Records", 2, commodities.size());
		
		//Object for comparison
		DopYieldContractCommodity dycc1 = new DopYieldContractCommodity();
		dycc1.setDeclaredYieldContractCommodityGuid("dycc123456789_01");
		dycc1.setDeclaredYieldContractGuid("dyc123456789-2022");
		dycc1.setCropCommodityId(16);
		dycc1.setCropCommodityName("BARLEY");
		dycc1.setIsPedigreeInd(false);
		dycc1.setHarvestedAcres((double)1000);
		dycc1.setStoredYield((double)555);
		dycc1.setStoredYieldDefaultUnit((double)500);
		dycc1.setSoldYield((double)444);
		dycc1.setSoldYieldDefaultUnit((double)400);
		dycc1.setGradeModifierTypeCode("BLY1");
		dycc1.setTotalInsuredAcres((double)100);

		DopYieldContractCommodity dycc2 = new DopYieldContractCommodity();
		dycc2.setDeclaredYieldContractCommodityGuid("dycc123456789_02");
		dycc2.setDeclaredYieldContractGuid("dyc123456789-2022");
		dycc2.setCropCommodityId(18);
		dycc2.setCropCommodityName("CANOLA");
		dycc2.setIsPedigreeInd(true);
		dycc2.setHarvestedAcres((double)2000);
		dycc2.setStoredYield((double)1555);
		dycc2.setStoredYieldDefaultUnit((double)1500);
		dycc2.setSoldYield((double)1444);
		dycc2.setSoldYieldDefaultUnit((double)1400);
		dycc2.setGradeModifierTypeCode("CAN2025");
		dycc2.setTotalInsuredAcres((double)120);
		
		for (DopYieldContractCommodity commodity : commodities) {
			
			if(commodity.getCropCommodityId() == 16) {
				checkDopYieldContractCommodity(dycc1, commodity, true);
			} else if(commodity.getCropCommodityId() == 18) {
				checkDopYieldContractCommodity(dycc2, commodity, true);
			} else {
				Assert.fail("Wrong commodity: " + commodity.getCropCommodityId());
			}
		}

		//*****************************************************************
		//AFTER TESTS: DELETE LAND, INVENTORY AND YIELD BY RUNNING THE DELETE SCRIPTS BELOW
		//*****************************************************************


		logger.debug(">testGetDeclaredYieldContractCommodity");
	
	/* 
	 * 
	*****************
	INSERT STATEMENTS
	*****************
	
	INSERT INTO cuws.policy(policy_id, grower_id, insurance_plan_id, policy_status_code, office_id, policy_number, contract_number, contract_id, crop_year, data_sync_trans_date, create_user, create_date, update_user, update_date)
	VALUES (999888777, 4, 4, 'ACTIVE', 1, '999888-22', '999888', 999888777, 2022, now(), 'admin', now(), 'admin', now());

	INSERT INTO cuws.grower_contract_year(grower_contract_year_id, grower_id, insurance_plan_id, contract_id, crop_year, data_sync_trans_date, create_user, create_date, update_user, update_date)
	VALUES (999992022, 4, 4, 999888777, 2022, now(), 'admin', now(), 'admin', now());

	-- Declared Yield Contract
	insert into declared_yield_contract(declared_yield_contract_guid, contract_id, crop_year, declaration_of_production_date, dop_update_timestamp, dop_update_user, entered_yield_meas_unit_type_code, default_yield_meas_unit_type_code, grain_from_other_source_ind, create_user, create_date, update_user, update_date) 
	values ('dyc123456789-2022', 999888777, 2022, '2022-01-15', '2022-02-16 17:45:55', 'JSMITH', 'BUSHEL', 'TONNE', 'Y', 'admin', now(), 'admin', now());
	
	-- Declared Yield Contract Commodity
	INSERT INTO cuws.declared_yield_contract_commodity(declared_yield_contract_commodity_guid, declared_yield_contract_guid, crop_commodity_id, is_pedigree_ind, harvested_acres, stored_yield, stored_yield_default_unit, sold_yield, sold_yield_default_unit, grade_modifier_type_code, create_user, create_date, update_user, update_date)
	VALUES ('dycc123456789_01', 'dyc123456789-2022', 16, 'N', 1000, 555, 500, 444, 400, 'BLY1', 'admin', now(), 'admin', now());
	INSERT INTO cuws.declared_yield_contract_commodity(declared_yield_contract_commodity_guid, declared_yield_contract_guid, crop_commodity_id, is_pedigree_ind, harvested_acres, stored_yield, stored_yield_default_unit, sold_yield, sold_yield_default_unit, grade_modifier_type_code, create_user, create_date, update_user, update_date)
	VALUES ('dycc123456789_02', 'dyc123456789-2022', 18, 'Y', 2000, 1555, 1500, 1444, 1400, 'CAN2025', 'admin', now(), 'admin', now());

	-- Inventory contract
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
	delete from declared_yield_contract_commodity where declared_yield_contract_guid = 'dyc123456789-2022';
	delete from declared_yield_contract where declared_yield_contract_guid = 'dyc123456789-2022';

	delete from inventory_contract_commodity where inventory_contract_guid = 'ic123456789-2022';
	DELETE FROM inventory_contract WHERE inventory_contract_guid = 'ic123456789-2022';
	DELETE FROM grower_contract_year WHERE grower_contract_year_id IN (999992022);
	DELETE FROM policy WHERE policy_id IN (999888777);
	 
	*****************
	SELECT STATEMENTS
	*****************
	select * from declared_yield_contract_commodity where declared_yield_contract_guid = 'dyc123456789-2022';
	select * from declared_yield_contract dyc where dyc.declared_yield_contract_guid = 'dyc123456789-2022';
	
	select * from inventory_contract_commodity where inventory_contract_guid = 'ic123456789-2022';
	SELECT * FROM inventory_contract WHERE inventory_contract_guid = 'ic123456789-2022';
	SELECT * FROM grower_contract_year WHERE grower_contract_year_id IN (999992022);
	SELECT * FROM policy WHERE policy_id IN (999888777);

 
	 */
	}		

	
	
	@Test
	public void testInsertUpdateDeleteDopYieldContract() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testInsertUpdateDeleteDopYieldContract");
		
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
		
		createInventoryContract();
		
		// Dop Date
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2022, Calendar.JANUARY, 15);
		Date dopDate = cal.getTime();
		
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
		Assert.assertEquals(inventoryContractGuid1, referrer.getInventoryContractGuid());
		Assert.assertNull(referrer.getDeclaredYieldContractGuid());

		DopYieldContractRsrc newDyc = service.rolloverDopYieldContract(referrer);

		Assert.assertEquals(contractId1, newDyc.getContractId());
		Assert.assertEquals(cropYear1, newDyc.getCropYear());
		Assert.assertNull(newDyc.getDeclaredYieldContractGuid());
		Assert.assertTrue(newDyc.getFields().get(0).getUwComments().size() == 1);
		
		checkRolledOverDopYieldContractCommodities(newDyc.getDopYieldContractCommodities());
		
		newDyc.setDeclarationOfProductionDate(dopDate);
		newDyc.setDefaultYieldMeasUnitTypeCode("TONNE");
		// newDyc.setDopUpdateTimestamp(dopUpdateTimestamp);
		// newDyc.setDopUpdateUser("JSMITH");
		newDyc.setEnteredYieldMeasUnitTypeCode("BUSHEL");
		newDyc.setGrainFromOtherSourceInd(true);
		newDyc.setBalerWagonInfo("Test Insert");
		newDyc.setTotalLivestock(100);
		newDyc.setInsurancePlanId(4);
		
		// update the existing field level comment and add another field comment before create DOP
		UnderwritingComment underwritingComment = new UnderwritingComment();
		List<UnderwritingComment> uwComments = newDyc.getFields().get(0).getUwComments();
	
		uwComments.get(0).setUnderwritingComment("Updated comment 1");
		
		underwritingComment = new UnderwritingComment();
		underwritingComment.setAnnualFieldDetailId(null);
		underwritingComment.setUnderwritingComment("Comment2 for field " + newDyc.getFields().get(0).getFieldId());
		underwritingComment.setUnderwritingCommentGuid(null);
		underwritingComment.setUnderwritingCommentTypeCode("INV");
		underwritingComment.setUnderwritingCommentTypeDesc("Inventory");		
	
		uwComments.add(underwritingComment);
		newDyc.getFields().get(0).setUwComments(uwComments);
		
		// add a DOP contract level 
		UnderwritingComment contractUnderwritingComment = new UnderwritingComment();
		List<UnderwritingComment> contractUwComments = newDyc.getUwComments();
	
		contractUnderwritingComment = new UnderwritingComment();
		contractUnderwritingComment.setAnnualFieldDetailId(null);
		contractUnderwritingComment.setUnderwritingComment("Comment for dopContractYieldGuid " + newDyc.getDeclaredYieldContractGuid());
		contractUnderwritingComment.setUnderwritingCommentGuid(null);
		contractUnderwritingComment.setUnderwritingCommentTypeCode("DOP");
		contractUnderwritingComment.setUnderwritingCommentTypeDesc("Declaration of Production");		
		contractUnderwritingComment.setDeclaredYieldContractGuid(newDyc.getDeclaredYieldContractGuid());
		contractUnderwritingComment.setGrowerContractYearId(gcyId1);
	
		contractUwComments.add(contractUnderwritingComment);
		newDyc.setUwComments(contractUwComments);
						
		List<AnnualFieldRsrc> expectedRolloverFields = new ArrayList<AnnualFieldRsrc>();
		AnnualFieldRsrc expectedField = new AnnualFieldRsrc();
		expectedField.setContractedFieldDetailId(contractedFieldDetailId1);
		expectedRolloverFields.add(expectedField);
		
		List<DopYieldFieldGrain> expectedRolloverDyfList = new ArrayList<DopYieldFieldGrain>();
		DopYieldFieldGrain expDyf = new DopYieldFieldGrain();
		expDyf.setCropCommodityId(16);
		expDyf.setCropCommodityName("BARLEY");
		expDyf.setCropVarietyId(null);
		expDyf.setCropVarietyName(null);
		expDyf.setCropYear(cropYear1);
		expDyf.setDeclaredYieldFieldGuid(null);
		expDyf.setEstimatedYieldPerAcre(null);
		expDyf.setEstimatedYieldPerAcreDefaultUnit(null);
		expDyf.setFieldId(fieldId1);
		expDyf.setInsurancePlanId(4);
		expDyf.setInventoryFieldGuid(inventoryFieldGuid1);
		expDyf.setInventorySeededGrainGuid(inventorySeededGrainGuid1);
		expDyf.setIsPedigreeInd(false);
		expDyf.setSeededAcres((double)25);
		expDyf.setSeededDate(null);
		expDyf.setUnharvestedAcresInd(false);
		
		expectedRolloverDyfList.add(expDyf);
		
		expDyf = new DopYieldFieldGrain();
		expDyf.setCropCommodityId(20);
		expDyf.setCropCommodityName("FALL RYE");
		expDyf.setCropVarietyId(null);
		expDyf.setCropVarietyName(null);
		expDyf.setCropYear(cropYear1);
		expDyf.setDeclaredYieldFieldGuid(null);
		expDyf.setEstimatedYieldPerAcre(null);
		expDyf.setEstimatedYieldPerAcreDefaultUnit(null);
		expDyf.setFieldId(fieldId1);
		expDyf.setInsurancePlanId(4);
		expDyf.setInventoryFieldGuid(inventoryFieldGuid2);
		expDyf.setInventorySeededGrainGuid(inventorySeededGrainGuid2);
		expDyf.setIsPedigreeInd(false);
		expDyf.setSeededAcres(56.78);
		expDyf.setSeededDate(null);
		expDyf.setUnharvestedAcresInd(false);
		
		expectedRolloverDyfList.add(expDyf);
		expectedRolloverFields.get(0).setDopYieldFieldGrainList(expectedRolloverDyfList);

		checkFieldsAndPlantings(expectedRolloverFields, newDyc.getFields());
		
		DopYieldFieldGrain dyf1 = newDyc.getFields().get(0).getDopYieldFieldGrainList().get(0);
		DopYieldFieldGrain dyf2 = newDyc.getFields().get(0).getDopYieldFieldGrainList().get(1);
		
		dyf1.setEstimatedYieldPerAcre(11.22);
		dyf1.setEstimatedYieldPerAcreDefaultUnit(null); // Calculated by backend
		dyf1.setUnharvestedAcresInd(true);
		
		createDopYieldCommodities(newDyc);
		//expectedDopCommodities are set in the method above 
		newDyc.setDopYieldContractCommodities(expectedDopCommodities);
		
		DopYieldContractRsrc fetchedDyc = service.createDopYieldContract(topLevelEndpoints, newDyc);
		newDyc.setDeclaredYieldContractGuid(fetchedDyc.getDeclaredYieldContractGuid());
		
		dyf1.setDeclaredYieldFieldGuid(fetchedDyc.getFields().get(0).getDopYieldFieldGrainList().get(0).getDeclaredYieldFieldGuid());
		dyf1.setEstimatedYieldPerAcreDefaultUnit(0.2443); // 11.22 / 45.93 (conversion factor for BARLEY from BUSHELS to TONNES).

		dyf2.setDeclaredYieldFieldGuid(fetchedDyc.getFields().get(0).getDopYieldFieldGrainList().get(1).getDeclaredYieldFieldGuid());
		dyf2.setEstimatedYieldPerAcreDefaultUnit(null);
		
		checkDopYieldContract(newDyc, fetchedDyc);
		checkFieldsAndPlantings(newDyc.getFields(), fetchedDyc.getFields());
		checkUpdatedDopYieldContractCommodities(fetchedDyc.getDopYieldContractCommodities(), false);
		
		// check the number of field level comments for the first field
		Assert.assertEquals("CountUwFieldComments", newDyc.getFields().get(0).getUwComments().size(), fetchedDyc.getFields().get(0).getUwComments().size());
		Assert.assertNotNull("NotNullUwFieldComments", fetchedDyc.getFields().get(0).getUwComments().get(0).getAnnualFieldDetailId());
		Assert.assertNull("NullDeclaredYieldContractGuid", fetchedDyc.getFields().get(0).getUwComments().get(0).getDeclaredYieldContractGuid());
		Assert.assertNull("NullGrowerContractYearId", fetchedDyc.getFields().get(0).getUwComments().get(0).getGrowerContractYearId());
		Assert.assertEquals("UnderwritingCommentTypeCode", newDyc.getFields().get(0).getUwComments().get(0).getUnderwritingCommentTypeCode(), fetchedDyc.getFields().get(0).getUwComments().get(0).getUnderwritingCommentTypeCode());
		
		for (int i = 0; i < newDyc.getFields().get(0).getUwComments().size(); i++ ) {
			for (int k = 0; k < fetchedDyc.getFields().get(0).getUwComments().size(); k++ ) {
				
				String newDycCommentGuid = newDyc.getFields().get(0).getUwComments().get(i).getUnderwritingCommentGuid();
				String fetchedDycCommentGuid = fetchedDyc.getFields().get(0).getUwComments().get(k).getUnderwritingCommentGuid();
				
				if ( newDycCommentGuid != null && newDycCommentGuid.equals(fetchedDycCommentGuid)) {
					
					Assert.assertEquals("UnderwritingComment", 
							fetchedDyc.getFields().get(0).getUwComments().get(k).getUnderwritingComment(), 
							newDyc.getFields().get(0).getUwComments().get(i).getUnderwritingComment());
					
				}
			}			
		}		
		
		declaredYieldContractGuid1 = fetchedDyc.getDeclaredYieldContractGuid();
		
		// check that there is one DOP contract level comment
		Assert.assertEquals("CountContractUwComments", newDyc.getUwComments().size(), fetchedDyc.getUwComments().size());
		Assert.assertEquals("UnderwritingCommentTypeCode", newDyc.getUwComments().get(0).getUnderwritingCommentTypeCode(), fetchedDyc.getUwComments().get(0).getUnderwritingCommentTypeCode());
		Assert.assertEquals("UnderwritingComment", newDyc.getUwComments().get(0).getUnderwritingComment() , fetchedDyc.getUwComments().get(0).getUnderwritingComment());
		Assert.assertEquals("DeclaredYieldContractGuid", declaredYieldContractGuid1, fetchedDyc.getUwComments().get(0).getDeclaredYieldContractGuid());
		Assert.assertEquals("GrowerContractYearId", gcyId1, fetchedDyc.getUwComments().get(0).getGrowerContractYearId());
		Assert.assertNull("NullAnnualFieldDetailId", fetchedDyc.getUwComments().get(0).getAnnualFieldDetailId());

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
		Assert.assertEquals(inventoryContractGuid1, referrer.getInventoryContractGuid());
		Assert.assertEquals(declaredYieldContractGuid1, referrer.getDeclaredYieldContractGuid());
		
		fetchedDyc = service.getDopYieldContract(referrer);
		checkDopYieldContract(newDyc, fetchedDyc);
		checkFieldsAndPlantings(newDyc.getFields(), fetchedDyc.getFields());
		
		//Check field rollup
		Assert.assertEquals(1, fetchedDyc.getDopYieldFieldRollupList().size());
		
		//BUSHEL to TONNE
		Assert.assertEquals(dyf1.getCropCommodityId(), fetchedDyc.getDopYieldFieldRollupList().get(0).getCropCommodityId());
		Assert.assertEquals(dyf1.getIsPedigreeInd(), fetchedDyc.getDopYieldFieldRollupList().get(0).getIsPedigreeInd());
		Assert.assertEquals(dyf1.getEstimatedYieldPerAcre(), fetchedDyc.getDopYieldFieldRollupList().get(0).getEstimatedYieldPerAcreBushels(), 0);
		Assert.assertEquals(dyf1.getEstimatedYieldPerAcreDefaultUnit(), fetchedDyc.getDopYieldFieldRollupList().get(0).getEstimatedYieldPerAcreTonnes(), 0);

		// Dop Date
		cal = Calendar.getInstance();
		cal.clear();
		cal.set(2022, Calendar.JANUARY, 16);
		dopDate = cal.getTime();
		
		fetchedDyc.setDeclarationOfProductionDate(dopDate);
		fetchedDyc.setDefaultYieldMeasUnitTypeCode("TONNE");
		// fetchedDyc.setDopUpdateTimestamp(dopUpdateTimestamp);
		// fetchedDyc.setDopUpdateUser("JDOE");
		fetchedDyc.setEnteredYieldMeasUnitTypeCode("TONNE");
		fetchedDyc.setGrainFromOtherSourceInd(false);
		fetchedDyc.setBalerWagonInfo("Test Update");
		fetchedDyc.setTotalLivestock(200);


		// Dop Yield Field
		dyf1 = fetchedDyc.getFields().get(0).getDopYieldFieldGrainList().get(0);
		dyf1.setEstimatedYieldPerAcre(null);
		dyf1.setEstimatedYieldPerAcreDefaultUnit(null);
		dyf1.setUnharvestedAcresInd(false);
		
		dyf2 = fetchedDyc.getFields().get(0).getDopYieldFieldGrainList().get(1);
		dyf2.setEstimatedYieldPerAcre(33.44);
		dyf2.setEstimatedYieldPerAcreDefaultUnit(null); // Calculated by backend
		dyf2.setUnharvestedAcresInd(true);
		
		updateDopYieldCommodities(fetchedDyc);
		//expectedDopCommodities are set in the method above 
		fetchedDyc.setDopYieldContractCommodities(expectedDopCommodities);
		
		
		// add one more field level comment before update
		UnderwritingComment underwritingComment2 = new UnderwritingComment();
		uwComments = fetchedDyc.getFields().get(0).getUwComments();
	
		underwritingComment2 = new UnderwritingComment();
		underwritingComment2.setAnnualFieldDetailId(null);
		underwritingComment2.setUnderwritingComment("Comment3 for field " + fetchedDyc.getFields().get(0).getFieldId());
		underwritingComment2.setUnderwritingCommentGuid(null);
		underwritingComment2.setUnderwritingCommentTypeCode("INV");
		underwritingComment2.setUnderwritingCommentTypeDesc("Inventory");		
	
		uwComments.add(underwritingComment2);
		fetchedDyc.getFields().get(0).setUwComments(uwComments);
		
		// add one more DOP underwriting comment before update
		UnderwritingComment contractUnderwritingComment2 = new UnderwritingComment();
		List<UnderwritingComment> contractUwComments2 = fetchedDyc.getUwComments();
		
		// update a previous comment
		contractUwComments2.get(0).setUnderwritingComment("Updated Comment 1");
		
		// add a new comment
		contractUnderwritingComment2 = new UnderwritingComment();
		contractUnderwritingComment2.setAnnualFieldDetailId(null);
		contractUnderwritingComment2.setUnderwritingComment("Second Comment for dopContractYieldGuid " + newDyc.getDeclaredYieldContractGuid());
		contractUnderwritingComment2.setUnderwritingCommentGuid(fetchedDyc.getDeclaredYieldContractGuid());
		contractUnderwritingComment2.setUnderwritingCommentTypeCode("DOP");
		contractUnderwritingComment2.setUnderwritingCommentTypeDesc("Declaration of Production");		
		contractUnderwritingComment2.setDeclaredYieldContractGuid(newDyc.getDeclaredYieldContractGuid());
		contractUnderwritingComment2.setGrowerContractYearId(gcyId1);
	
		contractUwComments2.add(contractUnderwritingComment2);
		fetchedDyc.setUwComments(contractUwComments2);
		
		DopYieldContractRsrc updatedDyc = service.updateDopYieldContract(fetchedDyc);

		// update only setDopUpdateTimestamp and DopUpdateUser
		DopYieldContractRsrc updatedDyc2 = service.updateDopYieldContract(updatedDyc);		
		//Assert.assertTrue("DopUpdateTimestamp", updatedDyc.getDopUpdateTimestamp().compareTo(updatedDyc2.getDopUpdateTimestamp()) < 0 );
		
		dyf1.setEstimatedYieldPerAcreDefaultUnit(null);
		dyf2.setEstimatedYieldPerAcreDefaultUnit(33.44); // Same as estimatedYieldPerAcre because entered units and default units are both TONNE.
		
		checkDopYieldContract(fetchedDyc, updatedDyc);
		checkFieldsAndPlantings(fetchedDyc.getFields(), updatedDyc.getFields());
		checkUpdatedDopYieldContractCommodities(updatedDyc.getDopYieldContractCommodities(), true);
		
		// check the number of comments for the first field
		Assert.assertEquals("UwFieldComments", fetchedDyc.getFields().get(0).getUwComments().size(), updatedDyc.getFields().get(0).getUwComments().size());
		
		// check the DOP underwriting comments
		Assert.assertEquals("CountContractUwComments", fetchedDyc.getUwComments().size(), updatedDyc.getUwComments().size());
		
		for (int i = 0; i < updatedDyc.getUwComments().size(); i++ ) {
			for (int k = 0; k < fetchedDyc.getUwComments().size(); k++ ) {
				
				String updatedDycCommentGuid = updatedDyc.getUwComments().get(i).getUnderwritingCommentGuid();
				String fetchedDycCommentGuid = fetchedDyc.getUwComments().get(k).getUnderwritingCommentGuid();
				
				if ( updatedDycCommentGuid.equals(fetchedDycCommentGuid)) {
					
					Assert.assertEquals("UnderwritingComment", fetchedDyc.getUwComments().get(k).getUnderwritingComment(), updatedDyc.getUwComments().get(i).getUnderwritingComment());
					
				}
			}			
		}
	
		//Check field rollup
		Assert.assertEquals(1, fetchedDyc.getDopYieldFieldRollupList().size());
		
		//TONNE to BUSHEL
		double expectedValue = dyf2.getEstimatedYieldPerAcre()*39.368; //conversion factor for Fall Rye
		expectedValue = BigDecimal.valueOf(expectedValue)
                .setScale(4, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
		Assert.assertEquals(dyf2.getCropCommodityId(), updatedDyc.getDopYieldFieldRollupList().get(0).getCropCommodityId());
		Assert.assertEquals(dyf2.getIsPedigreeInd(), updatedDyc.getDopYieldFieldRollupList().get(0).getIsPedigreeInd());
		Assert.assertEquals(expectedValue, updatedDyc.getDopYieldFieldRollupList().get(0).getEstimatedYieldPerAcreBushels(), 0);
		Assert.assertEquals(dyf2.getEstimatedYieldPerAcre(), updatedDyc.getDopYieldFieldRollupList().get(0).getEstimatedYieldPerAcreTonnes(), 0);
		
		//Try to delete inventory contract with DOP
		try {
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

			if ( referrer.getInventoryContractGuid() != null ) { 
				InventoryContractRsrc invContract = service.getInventoryContract(referrer);
				service.deleteInventoryContract(invContract);
			}			
			
			Assert.fail("Attempt to delete inventory with yield data did not trigger ServiceException");
		} catch (CirrasUnderwritingServiceException e) {
			Assert.assertTrue(e.getMessage().contains("Can't delete inventory of contract with yield data"));
		}
		
		//Delete DOP
		service.deleteDopYieldContract(updatedDyc2);
		
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
		Assert.assertEquals(inventoryContractGuid1, referrer.getInventoryContractGuid());
		Assert.assertNull(referrer.getDeclaredYieldContractGuid());

		declaredYieldContractGuid1 = null;
		
		delete();

		logger.debug(">testInsertUpdateDeleteDopYieldContract");
	
	/* 
	 * 
	*****************
	DELETE STATEMENTS
	*****************
	
	DELETE FROM underwriting_comment where grower_contract_year_id=90000003 or annual_field_detail_id = 90000007;
	
	DELETE FROM declared_yield_field_rollup t WHERE t.declared_yield_contract_guid IN (SELECT declared_yield_contract_guid FROM declared_yield_contract WHERE contract_id = 90000004 and crop_year = 2021);
	DELETE FROM declared_yield_field WHERE inventory_field_guid IN (SELECT inventory_field_guid FROM inventory_field WHERE field_id = 90000006 AND crop_year = 2021);
	DELETE FROM declared_yield_contract_commodity t WHERE t.declared_yield_contract_guid IN (SELECT declared_yield_contract_guid FROM declared_yield_contract WHERE contract_id = 90000004 and crop_year = 2021);
	DELETE FROM declared_yield_contract WHERE contract_id = 90000004 and crop_year = 2021;

	DELETE FROM inventory_seeded_grain WHERE inventory_field_guid IN (SELECT inventory_field_guid FROM inventory_field WHERE field_id = 90000006 AND crop_year = 2021);
	DELETE FROM inventory_unseeded WHERE inventory_field_guid IN (SELECT inventory_field_guid FROM inventory_field WHERE field_id = 90000006 AND crop_year = 2021);
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
	SELECT * FROM declared_yield_field_rollup t WHERE t.declared_yield_contract_guid IN (SELECT declared_yield_contract_guid FROM declared_yield_contract WHERE contract_id = 90000004 and crop_year = 2021)
	SELECT * FROM declared_yield_field WHERE inventory_field_guid IN (SELECT inventory_field_guid FROM inventory_field WHERE field_id = 90000006 AND crop_year = 2021);
	SELECT * FROM declared_yield_contract_commodity t WHERE t.declared_yield_contract_guid IN (SELECT declared_yield_contract_guid FROM declared_yield_contract WHERE contract_id = 90000004 and crop_year = 2021);
	SELECT * FROM declared_yield_contract WHERE contract_id = 90000004 and crop_year = 2021;

	SELECT * FROM inventory_seeded_grain WHERE inventory_field_guid IN (SELECT inventory_field_guid FROM inventory_field WHERE field_id = 90000006 AND crop_year = 2021);
	SELECT * FROM inventory_unseeded WHERE inventory_field_guid IN (SELECT inventory_field_guid FROM inventory_field WHERE field_id = 90000006 AND crop_year = 2021);
	SELECT * FROM inventory_field WHERE field_id = 90000006 AND crop_year = 2021;
	SELECT * FROM inventory_contract_commodity WHERE inventory_contract_guid IN (SELECT ic.inventory_contract_guid FROM inventory_contract ic WHERE contract_id = 90000004 and crop_year = 2021);
	SELECT * FROM inventory_contract WHERE contract_id = 90000004 and crop_year = 2021;

	SELECT * FROM underwriting_comment where annual_field_detail_id = 90000007;

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
	public void testDeleteUwComments() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testDeleteUwComments");
		
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
		
		createInventoryContract();
		
		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(20);
		
		// Dop Date
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2022, Calendar.JANUARY, 15);
		Date dopDate = cal.getTime();

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
		Assert.assertEquals(inventoryContractGuid1, referrer.getInventoryContractGuid());
		Assert.assertNull(referrer.getDeclaredYieldContractGuid());

		DopYieldContractRsrc newDyc = service.rolloverDopYieldContract(referrer);

		Assert.assertEquals(contractId1, newDyc.getContractId());
		Assert.assertEquals(cropYear1, newDyc.getCropYear());
		Assert.assertNull(newDyc.getDeclaredYieldContractGuid());
		Assert.assertTrue(newDyc.getFields().get(0).getUwComments().size() == 1);
		
		newDyc.setDeclarationOfProductionDate(dopDate);
		newDyc.setDefaultYieldMeasUnitTypeCode("TONNE");
		newDyc.setEnteredYieldMeasUnitTypeCode("BUSHEL");
		newDyc.setGrainFromOtherSourceInd(true);
		newDyc.setInsurancePlanId(4);

		DopYieldContractRsrc fetchedDyc = service.createDopYieldContract(topLevelEndpoints, newDyc);
		
		Assert.assertTrue(fetchedDyc.getFields().get(0).getUwComments().size() == 1);


		//Try to delete with second user without permissions to delete all comments
		//Expect an error message
		try {
			DopYieldContractRsrc dopContractUser2 = getDopContract(serviceUser2, topLevelEndpointsUser2);
			// Delete the second comment.
			dopContractUser2.getFields().get(0).getUwComments().get(0).setDeletedByUserInd(true);
			serviceUser2.updateDopYieldContract(dopContractUser2);
			Assert.fail("Attempt to delete another user's comment did not trigger ServiceException");
		} catch (CirrasUnderwritingServiceException e) {
			Assert.assertTrue(e.getMessage().contains("The current user is not authorized to delete this comment."));
		}
		
		//Add a comment with second user
		DopYieldContractRsrc dopContractUser2 = getDopContract(serviceUser2, topLevelEndpointsUser2);
		createComment(dopContractUser2.getFields().get(0));
		dopContractUser2 = serviceUser2.updateDopYieldContract(dopContractUser2);
		
		Assert.assertTrue(dopContractUser2.getFields().get(0).getUwComments().size() == 2);
		
		//Delete own comment
		AnnualFieldRsrc field = dopContractUser2.getFields().get(0);
		Boolean hasCommentToDelete = false;
	
		String userToCompare = "SCL\\" + WebadeOauth2ClientId2;
		for ( int i = 0; i < field.getUwComments().size(); i++) {
			UnderwritingComment comment = field.getUwComments().get(i);
			if(comment.getCreateUser().equals(userToCompare)) {
				comment.setDeletedByUserInd(true);
				hasCommentToDelete = true;
			}
		}
		
		Assert.assertTrue("No comment to delete", hasCommentToDelete);
		dopContractUser2 = serviceUser2.updateDopYieldContract(dopContractUser2);

		Assert.assertTrue(dopContractUser2.getFields().get(0).getUwComments().size() == 1);

		//Create another comment
		createComment(dopContractUser2.getFields().get(0));
		serviceUser2.updateDopYieldContract(dopContractUser2);
		

		//Get inventory contract with first user again
		DopYieldContractRsrc fetchedDopContract = getDopContract(service, topLevelEndpoints);

		Assert.assertTrue(fetchedDopContract.getFields().get(0).getUwComments().size() == 2);

		//Delete comment of the second user with the main user. 
		//No exception expected as this user has the permission to delete all comments
		field = fetchedDopContract.getFields().get(0);
	
		for ( int i = 0; i < field.getUwComments().size(); i++) {
			UnderwritingComment comment = field.getUwComments().get(i);
			if(comment.getCreateUser().equals(userToCompare)) {
				comment.setDeletedByUserInd(true);
			}
		}
		fetchedDopContract = service.updateDopYieldContract(fetchedDopContract);
		
		Assert.assertTrue(fetchedDopContract.getFields().get(0).getUwComments().size() == 1);
			
		logger.debug(">testDeleteUwComments");

	}
	
	private void createComment(AnnualFieldRsrc field) {
		//UnderwritingComment
		UnderwritingComment underwritingComment = new UnderwritingComment();

		underwritingComment.setAnnualFieldDetailId(field.getAnnualFieldDetailId());
		underwritingComment.setUnderwritingComment("Comment 2nd user");
		underwritingComment.setUnderwritingCommentGuid(null);
		underwritingComment.setUnderwritingCommentTypeCode("INV");
		underwritingComment.setUnderwritingCommentTypeDesc("Inventory");
		
		List<UnderwritingComment> uwComments = field.getUwComments();
		uwComments.add(underwritingComment);
	
	}
	
	private DopYieldContractRsrc getDopContract(CirrasUnderwritingService service, EndpointsRsrc topLevelEndpoints
			) throws CirrasUnderwritingServiceException {
		
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

		DopYieldContractRsrc dopContract = service.getDopYieldContract(referrer);
		Assert.assertNotNull(dopContract);

		return dopContract;
	}


	@Test
	public void testGenerateDopReport() throws CirrasUnderwritingServiceException, Oauth2ClientException {
		logger.debug("<testGenerateDopReport");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		// Test 1: Generate Forage report.
		byte[] reportContent = service.generateDopReport(topLevelEndpoints, "2024", "5", "4", null, null, null, null, null);
		
		Assert.assertNotNull(reportContent);
		
		logger.debug(">testGenerateDopReport - Returned " + reportContent.length + " bytes");	

		// Test 2: Generate Grain report
		reportContent = service.generateDopReport(topLevelEndpoints, "2024", "4", null, null, null, null, null, null);
		
		Assert.assertNotNull(reportContent);
		
		logger.debug(">testGenerateDopReport - Returned " + reportContent.length + " bytes");	
		
		// Test 3: Omit Insurance Plan: Report generation should fail.
		try {
			reportContent = service.generateDopReport(topLevelEndpoints, "2024", null, null, null, null, null, null, null);
			Assert.fail("Report generated for missing insurance plan id ");
		} catch ( CirrasUnderwritingServiceException e ) {
			// Ok.
		}

		// Test 4: Invalid Insurance Plan: Report generation should fail.
		try {
			reportContent = service.generateDopReport(topLevelEndpoints, "2024", "2", null, null, null, null, null, null);
			Assert.fail("Report generated for invalid insurance plan id ");
		} catch ( CirrasUnderwritingServiceException e ) {
			// Ok.
		}
		
	}
	
	
	@Test
	public void testGetDopYieldContractComment() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testGetDopYieldContractComment");


		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(20);

		String myPolicyNumber = "712863-23";
		
		UwContractListRsrc searchResults = service.getUwContractList(
				topLevelEndpoints, 
				null, 
				null, 
				null,
				null,
				myPolicyNumber,
				null,
				null, 
				null, 
				null, 
				pageNumber, pageRowCount);

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(1, searchResults.getCollection().size());

		UwContractRsrc referrer = searchResults.getCollection().get(0);
		
		DopYieldContractRsrc fetchedDyc = service.getDopYieldContract(referrer);
		
		// check that there is one DOP contract level comment
		Assert.assertEquals("ContractUwFieldComments", fetchedDyc.getUwComments().size(), 1);
		Assert.assertEquals("ContractUwFieldComments", fetchedDyc.getUwComments().get(0).getUnderwritingCommentTypeCode(), "DOP");
		Assert.assertEquals("ContractUwFieldComments", fetchedDyc.getUwComments().get(0).getUnderwritingComment(), "Contract level Comment 1");

		logger.debug(">testGetDopYieldContractComment");	
	}

	
	
	private void checkDopYieldContract(DopYieldContractRsrc expected, DopYieldContractRsrc actual) {

		Assert.assertEquals("ContractId", expected.getContractId(), actual.getContractId());
		Assert.assertEquals("CropYear", expected.getCropYear(), actual.getCropYear());
		Assert.assertEquals("DeclarationOfProductionDate", expected.getDeclarationOfProductionDate(), actual.getDeclarationOfProductionDate());
		Assert.assertEquals("DeclaredYieldContractGuid", expected.getDeclaredYieldContractGuid(), actual.getDeclaredYieldContractGuid());
		Assert.assertEquals("DefaultYieldMeasUnitTypeCode", expected.getDefaultYieldMeasUnitTypeCode(), actual.getDefaultYieldMeasUnitTypeCode());
		// Assert.assertEquals("DopUpdateTimestamp", expected.getDopUpdateTimestamp(), actual.getDopUpdateTimestamp());
		// Assert.assertEquals("DopUpdateUser", expected.getDopUpdateUser(), actual.getDopUpdateUser());
		Assert.assertEquals("EnteredYieldMeasUnitTypeCode", expected.getEnteredYieldMeasUnitTypeCode(), actual.getEnteredYieldMeasUnitTypeCode());
		Assert.assertEquals("GrainFromOtherSourceInd", expected.getGrainFromOtherSourceInd(), actual.getGrainFromOtherSourceInd());
		Assert.assertEquals("BalerWagonInfo", expected.getBalerWagonInfo(), actual.getBalerWagonInfo());
		Assert.assertEquals("TotalLivestock", expected.getTotalLivestock(), actual.getTotalLivestock());
		Assert.assertEquals("InsurancePlanId", expected.getInsurancePlanId(), actual.getInsurancePlanId());
		
	}
	
	private void checkDopYieldContractCommodity(
			DopYieldContractCommodity expectedCommodity,
			DopYieldContractCommodity actualCommodity,
			Boolean checkKeys) {
		if(checkKeys) {
			Assert.assertEquals("DeclaredYieldContractCommodityGuid", expectedCommodity.getDeclaredYieldContractCommodityGuid(), actualCommodity.getDeclaredYieldContractCommodityGuid());
			Assert.assertEquals("DeclaredYieldContractGuid", expectedCommodity.getDeclaredYieldContractGuid(), actualCommodity.getDeclaredYieldContractGuid());
		}
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
	
	private double assertDelta = 0;

	private void checkRolledOverDopYieldContractCommodities(List<DopYieldContractCommodity> contractCommodities) {
		
		for(DopYieldContractCommodity commodity : contractCommodities) {
				
			if(commodity.getCropCommodityId() == 16) {
				Assert.assertEquals("TotalInsuredAcres Barley", (double)25, commodity.getTotalInsuredAcres(), assertDelta);
			}  else if(commodity.getCropCommodityId() == 20) {
				Assert.assertEquals("TotalInsuredAcres FALL RYE", (double)56.78, commodity.getTotalInsuredAcres(), assertDelta);
			} else {
				Assert.fail("Wrong commodity: " + commodity.getCropCommodityId());
			}

		}
		
	}
	
	private void checkUpdatedDopYieldContractCommodities(List<DopYieldContractCommodity> updatedDopCommodities, Boolean checkKeys) {
		
		//Barley
		DopYieldContractCommodity expectedBarley = getDopYieldContractCommodity(16, expectedDopCommodities);
		DopYieldContractCommodity updatedBarley = getDopYieldContractCommodity(16, updatedDopCommodities);
		checkDopYieldContractCommodity(expectedBarley, updatedBarley, checkKeys);
		
		//Fall Rye
		DopYieldContractCommodity expectedFallRye = getDopYieldContractCommodity(20, expectedDopCommodities);
		DopYieldContractCommodity updatedFallRye = getDopYieldContractCommodity(20, updatedDopCommodities);
		checkDopYieldContractCommodity(expectedFallRye, updatedFallRye, checkKeys);
		
	}
	
	private DopYieldContractCommodity getDopYieldContractCommodity(Integer cropCommodityId, List<DopYieldContractCommodity> dyccList) {
		
		DopYieldContractCommodity dycc = null;
		
		List<DopYieldContractCommodity> dyccFiltered = dyccList.stream()
				.filter(x -> x.getCropCommodityId() == cropCommodityId)
				.collect(Collectors.toList());
		
		if (dyccFiltered != null) {
			dycc = dyccFiltered.get(0);
		}
		return dycc;
	}

	private List<DopYieldContractCommodity> expectedDopCommodities = null;

	private void createDopYieldCommodities(DopYieldContractRsrc newDyc) {
		
		expectedDopCommodities = new ArrayList<DopYieldContractCommodity>();
		
		//		Entered Yield Meas Unit Type = BUSHEL
		//		Default  Yield Meas Unit Type = TONNE
		
		//Barley
		DopYieldContractCommodity dycc = getDopYieldContractCommodity(16, newDyc.getDopYieldContractCommodities());
		if(dycc != null) {
			dycc.setHarvestedAcres((double)100);
			dycc.setStoredYield((double)50);
			dycc.setStoredYieldDefaultUnit(1.0886); //Conversion factor: 45.93
			dycc.setSoldYield((double)30);
			dycc.setSoldYieldDefaultUnit(0.6532); //Conversion factor: 45.93
			dycc.setGradeModifierTypeCode("BLYFOOD");
			
			expectedDopCommodities.add(dycc);
		}
		
		//Fall Rye
		dycc = getDopYieldContractCommodity(20, newDyc.getDopYieldContractCommodities());
		if(dycc != null) {
			dycc.setHarvestedAcres(null);
			dycc.setStoredYield(null);
			dycc.setStoredYieldDefaultUnit(null);
			dycc.setSoldYield(null);
			dycc.setSoldYieldDefaultUnit(null);
			dycc.setGradeModifierTypeCode(null);
			
			expectedDopCommodities.add(dycc);
		}
		
	}
	
	private void updateDopYieldCommodities(DopYieldContractRsrc newDyc) {
		
		expectedDopCommodities = new ArrayList<DopYieldContractCommodity>();
		
		//		Entered Yield Meas Unit Type = TONNE
		//		Default  Yield Meas Unit Type = TONNE

		//Barley
		DopYieldContractCommodity dycc = getDopYieldContractCommodity(16, newDyc.getDopYieldContractCommodities());
		if(dycc != null) {
			dycc.setHarvestedAcres(null);
			dycc.setStoredYield(null);
			dycc.setStoredYieldDefaultUnit(null);
			dycc.setSoldYield(null);
			dycc.setSoldYieldDefaultUnit(null);
			dycc.setGradeModifierTypeCode(null);
			
			expectedDopCommodities.add(dycc);
		}
		
		//Fall Rye
		dycc = getDopYieldContractCommodity(20, newDyc.getDopYieldContractCommodities());
		if(dycc != null) {
			dycc.setHarvestedAcres((double)100.5);
			dycc.setStoredYield((double)50.5);
			dycc.setStoredYieldDefaultUnit((double)50.5);
			dycc.setSoldYield((double)30.5);
			dycc.setSoldYieldDefaultUnit((double)30.5);
			dycc.setGradeModifierTypeCode("BLYFOOD");
			
			expectedDopCommodities.add(dycc);
		}
		
	}

	private void checkFieldsAndPlantings(List<AnnualFieldRsrc> expectedFields, List<AnnualFieldRsrc> actualFields) {
		
		Assert.assertEquals(expectedFields.size(), actualFields.size());
		
		for ( int i = 0; i < expectedFields.size(); i++ ) {

			checkAnnualField(expectedFields.get(i), actualFields.get(i));
			
			List<DopYieldFieldGrain> expectedDyfList = expectedFields.get(i).getDopYieldFieldGrainList();
			List<DopYieldFieldGrain> actualDyfList = actualFields.get(i).getDopYieldFieldGrainList();
			
			Assert.assertEquals(expectedDyfList.size(), actualDyfList.size());
			
			for ( int j = 0; j < expectedDyfList.size(); j++ ) {
				checkDopYieldField(expectedDyfList.get(j), actualDyfList.get(j));
			}
		}		
	}

	private void checkAnnualField(AnnualFieldRsrc expected, AnnualFieldRsrc actual) {
		Assert.assertEquals("ContractedFieldDetailId", expected.getContractedFieldDetailId(), actual.getContractedFieldDetailId());		
	}
	
	private void checkDopYieldField(DopYieldFieldGrain expected, DopYieldFieldGrain actual) {

		Assert.assertEquals("CropCommodityId", expected.getCropCommodityId(), actual.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", expected.getCropCommodityName(), actual.getCropCommodityName());
		Assert.assertEquals("CropVarietyId", expected.getCropVarietyId(), actual.getCropVarietyId());
		Assert.assertEquals("CropVarietyName", expected.getCropVarietyName(), actual.getCropVarietyName());
		Assert.assertEquals("CropYear", expected.getCropYear(), actual.getCropYear());
		Assert.assertEquals("DeclaredYieldFieldGuid", expected.getDeclaredYieldFieldGuid(), actual.getDeclaredYieldFieldGuid());
		Assert.assertEquals("EstimatedYieldPerAcre", expected.getEstimatedYieldPerAcre(), actual.getEstimatedYieldPerAcre());
		Assert.assertEquals("EstimatedYieldPerAcreDefaultUnit", expected.getEstimatedYieldPerAcreDefaultUnit(), actual.getEstimatedYieldPerAcreDefaultUnit());
		Assert.assertEquals("FieldId", expected.getFieldId(), actual.getFieldId());
		Assert.assertEquals("InsurancePlanId", expected.getInsurancePlanId(), actual.getInsurancePlanId());
		Assert.assertEquals("InventoryFieldGuid", expected.getInventoryFieldGuid(), actual.getInventoryFieldGuid());
		Assert.assertEquals("InventorySeededGrainGuid", expected.getInventorySeededGrainGuid(), actual.getInventorySeededGrainGuid());
		Assert.assertEquals("IsPedigreeInd", expected.getIsPedigreeInd(), actual.getIsPedigreeInd());
		Assert.assertEquals("SeededAcres", expected.getSeededAcres(), actual.getSeededAcres());
		Assert.assertEquals("SeededDate", expected.getSeededDate(), actual.getSeededDate());
		Assert.assertEquals("UnharvestedAcresInd", expected.getUnharvestedAcresInd(), actual.getUnharvestedAcresInd());

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
		resource.setInsurancePlanId(4);
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
		resource.setInsurancePlanId(4);
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
	
	
	private void createInventoryContract() throws ValidationException, CirrasUnderwritingServiceException {

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

		List<InventorySeededGrain> isgList = new ArrayList<InventorySeededGrain>();
		
		InventorySeededGrain isg = new InventorySeededGrain();
		isg.setCommodityTypeCode(null);
		isg.setCropCommodityId(16);
		isg.setCropVarietyId(null);
		isg.setIsPedigreeInd(false);
		isg.setIsQuantityInsurableInd(true);
		isg.setIsReplacedInd(false);
		isg.setIsSpotLossInsurableInd(false);
		isg.setSeededAcres((double)25);
		isg.setSeededDate(null);
		
		isgList.add(isg);
		
		resource.getFields().get(0).getPlantings().get(0).setInventorySeededGrains(isgList);
		
		InventoryUnseeded iu = new InventoryUnseeded();
		iu.setAcresToBeSeeded(null);
		iu.setCropCommodityId(null);
		iu.setIsUnseededInsurableInd(false);
		
		isgList = new ArrayList<InventorySeededGrain>();
		
		isg = new InventorySeededGrain();
		isg.setCommodityTypeCode(null);
		isg.setCropCommodityId(20);
		isg.setCropVarietyId(null);
		isg.setIsPedigreeInd(false);
		isg.setIsQuantityInsurableInd(true);
		isg.setIsReplacedInd(false);
		isg.setIsSpotLossInsurableInd(false);
		isg.setSeededAcres(56.78);
		isg.setSeededDate(null);
		
		isgList.add(isg);
		
		InventoryField invf = new InventoryField();
		invf.setCropYear(cropYear1);
		invf.setFieldId(fieldId1);
		invf.setInsurancePlanId(4);
		invf.setInventorySeededGrains(isgList);
		invf.setInventoryUnseeded(iu);
		invf.setIsHiddenOnPrintoutInd(false);
		invf.setLastYearCropCommodityId(null);
		invf.setLastYearCropVarietyId(null);
		invf.setPlantingNumber(2);
		invf.setUnderseededAcres(null);
		invf.setUnderseededCropVarietyId(null);

		resource.getFields().get(0).getPlantings().add(invf);

		// add comments
		UnderwritingComment underwritingComment = new UnderwritingComment();
		List<UnderwritingComment> uwComments = new ArrayList<UnderwritingComment>();

		underwritingComment = new UnderwritingComment();
		underwritingComment.setAnnualFieldDetailId(null);
		underwritingComment.setUnderwritingComment("Comment1 for field " + resource.getFields().get(0).getFieldId());
		underwritingComment.setUnderwritingCommentGuid(null);
		underwritingComment.setUnderwritingCommentTypeCode("INV");
		underwritingComment.setUnderwritingCommentTypeDesc("Inventory");		

		uwComments.add(underwritingComment);
		
		resource.getFields().get(0).setUwComments(uwComments);
		
		
		//Create inventory contract commodities
		List<InventoryContractCommodity> iccList = new ArrayList<InventoryContractCommodity>();
		iccList.add(createInventoryContractCommodity(16, "BARLEY", (double)25, false));
		iccList.add(createInventoryContractCommodity(20, "FALL RYE", (double)56.78, false));
		
		resource.setCommodities(iccList);
		
		resource = service.createInventoryContract(topLevelEndpoints, resource);
		inventoryContractGuid1 = resource.getInventoryContractGuid();
		
		inventoryFieldGuid1 = resource.getFields().get(0).getPlantings().get(0).getInventoryFieldGuid();
		inventoryFieldGuid2 = resource.getFields().get(0).getPlantings().get(1).getInventoryFieldGuid();

		inventorySeededGrainGuid1 = resource.getFields().get(0).getPlantings().get(0).getInventorySeededGrains().get(0).getInventorySeededGrainGuid();
		inventorySeededGrainGuid2 = resource.getFields().get(0).getPlantings().get(1).getInventorySeededGrains().get(0).getInventorySeededGrainGuid();
		
		
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

	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}
}
