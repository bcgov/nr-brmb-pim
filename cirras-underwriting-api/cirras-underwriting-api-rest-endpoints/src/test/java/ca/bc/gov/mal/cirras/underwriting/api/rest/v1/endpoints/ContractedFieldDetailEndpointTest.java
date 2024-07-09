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
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryCoverageTotalForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventorySeededForage;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.LandManagementEventTypes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.PoliciesSyncEventTypes;


public class ContractedFieldDetailEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(ContractedFieldDetailEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.CREATE_SYNC_UNDERWRITING,
		Scopes.UPDATE_SYNC_UNDERWRITING,
		Scopes.DELETE_SYNC_UNDERWRITING,
		Scopes.GET_LAND,
		Scopes.SEARCH_UWCONTRACTS,
		Scopes.SEARCH_ANNUAL_FIELDS,
		Scopes.CREATE_INVENTORY_CONTRACT,
		Scopes.DELETE_INVENTORY_CONTRACT,
		Scopes.GET_INVENTORY_CONTRACT,
		Scopes.UPDATE_INVENTORY_CONTRACT,
		Scopes.GET_GROWER,
		Scopes.GET_POLICY,
		Scopes.GET_LEGAL_LAND
	};
	
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;
	
	private Integer contractedFieldDetailId = 999999999;
	private Integer annualFieldDetailId = 999999996;
	private Integer growerContractYearId = 999999995;
	private Integer legalLandId = 999999998;
	private Integer fieldId = 999999997;
	private Integer growerId1 = 90000321;
	private Integer contractId = 990000999;
	
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
	
	private void delete() throws NotFoundDaoException, DaoException, CirrasUnderwritingServiceException{

		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId.toString());
		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId.toString());
		service.deleteGrowerContractYear(topLevelEndpoints, growerContractYearId.toString());
		service.deleteGrower(topLevelEndpoints, growerId1.toString());
		service.deleteField(topLevelEndpoints, fieldId.toString());
		service.deleteLegalLandSync(topLevelEndpoints, legalLandId.toString());
		
	}
	
	@Test
	public void testCreateUpdateDeleteContractedFieldDetail() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testCreateUpdateDeleteContractedFieldDetail");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 because they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);
		
		//CREATE Legal Land
		LegalLandRsrc legalLandResource = createLegalLand( legalLandId, "OTHER", "Legal Description", "Short Legal",  "Other Description", 2011, 2022);
		service.synchronizeLegalLand(legalLandResource);
								
		//CREATE Field
		FieldRsrc fieldResource = createField( fieldId, "Field Label", 2011, 2022);
		service.synchronizeField(fieldResource);

		// Create Grower
		GrowerRsrc growerRsrc = createGrower(growerId1, createTransactionDate);
		service.synchronizeGrower(growerRsrc);

		// Create GROWER CONTRACT YEAR
		GrowerContractYearSyncRsrc growerContractYearSyncRsrc = createGrowerContractYear(growerContractYearId, contractId, growerId1, 2, 2024, createTransactionDate );
		service.synchronizeGrowerContractYear(growerContractYearSyncRsrc);
		
		//CREATE Annual Field Detail
		AnnualFieldDetailRsrc annualFieldDetailResource = createAnnualFieldDetail(annualFieldDetailId, legalLandId, fieldId, 2022);
		service.synchronizeAnnualFieldDetail(annualFieldDetailResource);

		//CREATE Contracted Field Detail
		ContractedFieldDetailRsrc contractedFieldDetailResource = createContractedFieldDetail( contractedFieldDetailId, annualFieldDetailId, growerContractYearId, 1);
		service.synchronizeContractedFieldDetail(contractedFieldDetailResource);
		
		//check if it matches
		ContractedFieldDetailRsrc fetchedResource = service.getContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId.toString()); 

		Assert.assertEquals("ContractedFieldDetailId 1", contractedFieldDetailResource.getContractedFieldDetailId(), fetchedResource.getContractedFieldDetailId());
		Assert.assertEquals("AnnualFieldDetailId 1", contractedFieldDetailResource.getAnnualFieldDetailId(), fetchedResource.getAnnualFieldDetailId());
		Assert.assertEquals("GrowerContractYearId 1", contractedFieldDetailResource.getGrowerContractYearId(), fetchedResource.getGrowerContractYearId());
		Assert.assertEquals("DisplayOrder 1", contractedFieldDetailResource.getDisplayOrder(), fetchedResource.getDisplayOrder());
		
				
		//UPDATE CODE -> just changing the crop year
		fetchedResource.setDisplayOrder(2);
		fetchedResource.setTransactionType(LandManagementEventTypes.ContractedFieldDetailUpdated);

		service.synchronizeContractedFieldDetail(fetchedResource);
		
		//check if it was updated
		ContractedFieldDetailRsrc updatedResource = service.getContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId.toString()); 

		Assert.assertEquals("DisplayOrder 2", fetchedResource.getDisplayOrder(), updatedResource.getDisplayOrder());
		
		//CLEAN UP: DELETE CODE
		delete();
		
		logger.debug(">testCreateUpdateDeleteContractedFieldDetail");
	}

	
	@Test
	public void testUpdateContractedFieldDetailWithoutRecordNoUpdate() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testUpdateContractedFieldDetailWithoutRecordNoUpdate");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);
		
		//CREATE Legal Land
		LegalLandRsrc legalLandResource = createLegalLand( legalLandId, "OTHER", "Legal Description", "Short Legal",  "Other Description", 2011, 2022);
		service.synchronizeLegalLand(legalLandResource);
								
		//CREATE Field
		FieldRsrc fieldResource = createField( fieldId, "Field Label", 2011, 2022);
		service.synchronizeField(fieldResource);

		// Create Grower
		GrowerRsrc growerRsrc = createGrower(growerId1, createTransactionDate);
		service.synchronizeGrower(growerRsrc);

		// Create GROWER CONTRACT YEAR
		GrowerContractYearSyncRsrc growerContractYearSyncRsrc = createGrowerContractYear(growerContractYearId, contractId, growerId1, 2, 2024, createTransactionDate );
		service.synchronizeGrowerContractYear(growerContractYearSyncRsrc);

		//CREATE Annual Field Detail
		AnnualFieldDetailRsrc annualFieldDetailResource = createAnnualFieldDetail(annualFieldDetailId, legalLandId, fieldId, 2022);
		service.synchronizeAnnualFieldDetail(annualFieldDetailResource);
		
		//CREATE Contracted Field Detail
		ContractedFieldDetailRsrc contractedFieldDetailResource = createContractedFieldDetail( contractedFieldDetailId, annualFieldDetailId, growerContractYearId, 1);
		
		//TRY TO DELETE A record THAT DOESN'T EXIST (NO ERROR EXPECTED)
		contractedFieldDetailResource.setTransactionType(LandManagementEventTypes.ContractedFieldDetailDeleted);
		service.synchronizeContractedFieldDetail(contractedFieldDetailResource);
		
		//SHOULD RESULT IN AN INSERT
		contractedFieldDetailResource.setTransactionType(LandManagementEventTypes.ContractedFieldDetailUpdated);
		service.synchronizeContractedFieldDetail(contractedFieldDetailResource);
		
		ContractedFieldDetailRsrc fetchedResource = service.getContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId.toString()); 

		Assert.assertEquals("ContractedFieldDetailId 1", contractedFieldDetailResource.getContractedFieldDetailId(), fetchedResource.getContractedFieldDetailId());
		Assert.assertEquals("AnnualFieldDetailId 1", contractedFieldDetailResource.getAnnualFieldDetailId(), fetchedResource.getAnnualFieldDetailId());
		Assert.assertEquals("GrowerContractYearId 1", contractedFieldDetailResource.getGrowerContractYearId(), fetchedResource.getGrowerContractYearId());
		Assert.assertEquals("DisplayOrder 1", contractedFieldDetailResource.getDisplayOrder(), fetchedResource.getDisplayOrder());
		
		//UPDATE Contracted Field Detail 
		fetchedResource.setDisplayOrder(3);
		
		//UPDATE EXPECTED BECAUSE RECORD EXISTS IT WILL UPDATE IT
		fetchedResource.setTransactionType(LandManagementEventTypes.ContractedFieldDetailCreated);
		service.synchronizeContractedFieldDetail(fetchedResource);
		
		ContractedFieldDetailRsrc updatedResource = service.getContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId.toString()); 

		Assert.assertEquals("DisplayOrder 2", fetchedResource.getDisplayOrder(), updatedResource.getDisplayOrder());
		
		//CLEAN UP: DELETE CODE
		delete();		
		
		logger.debug(">testUpdateContractedFieldDetailWithoutRecordNoUpdate");
	}
	
	@Test
	public void testCreateInventoryFieldAndUnseededRecords() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testCreateInventoryFieldAndUnseededRecords");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//CREATE Legal Land
		LegalLandRsrc legalLandResource = createLegalLand( legalLandId, "OTHER", "Legal Description", "Short Legal",  "Other Description", 2011, 2022);
		service.synchronizeLegalLand(legalLandResource);
								
		//CREATE Field
		FieldRsrc fieldResource = createField( fieldId, "Field Label", 2011, 2022);
		service.synchronizeField(fieldResource);
		
		//Needs to be a gcy that has a inventory contract
		Integer localGrowerContractYearId = 95128;
		
		//CREATE Annual Field Detail
		AnnualFieldDetailRsrc annualFieldDetailResource = createAnnualFieldDetail(annualFieldDetailId, legalLandId, fieldId, 2022);
		service.synchronizeAnnualFieldDetail(annualFieldDetailResource);

		//CREATE Contracted Field Detail
		ContractedFieldDetailRsrc contractedFieldDetailResource = createContractedFieldDetail( contractedFieldDetailId, annualFieldDetailId, localGrowerContractYearId, 1);
		service.synchronizeContractedFieldDetail(contractedFieldDetailResource);
		
		//check if it matches
		ContractedFieldDetailRsrc fetchedResource = service.getContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId.toString()); 

		Assert.assertEquals("ContractedFieldDetailId 1", contractedFieldDetailResource.getContractedFieldDetailId(), fetchedResource.getContractedFieldDetailId());
		Assert.assertEquals("AnnualFieldDetailId 1", contractedFieldDetailResource.getAnnualFieldDetailId(), fetchedResource.getAnnualFieldDetailId());
		Assert.assertEquals("GrowerContractYearId 1", contractedFieldDetailResource.getGrowerContractYearId(), fetchedResource.getGrowerContractYearId());
		Assert.assertEquals("DisplayOrder 1", contractedFieldDetailResource.getDisplayOrder(), fetchedResource.getDisplayOrder());
				
		//UPDATE CODE -> just changing the crop year
		fetchedResource.setDisplayOrder(2);
		fetchedResource.setTransactionType(LandManagementEventTypes.ContractedFieldDetailUpdated);

		service.synchronizeContractedFieldDetail(fetchedResource);
		
		//check if it was updated
		ContractedFieldDetailRsrc updatedResource = service.getContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId.toString()); 

		Assert.assertEquals("DisplayOrder 2", fetchedResource.getDisplayOrder(), updatedResource.getDisplayOrder());
		
		//CLEAN UP: DELETE CODE
		delete();
		
		logger.debug(">testCreateInventoryFieldAndUnseededRecords");
	}	
	
	@Test
	public void testRecalculateInventoryContractCommodities() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testRecalculateInventoryContractCommodities");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//GET A VALID CONTRACTED FIELD DETAIL ID
		//SELECT * FROM contracted_field_detail cfd
		//JOIN annual_field_detail afd ON afd.annual_field_detail_id = cfd.annual_field_detail_id
		//WHERE afd.field_id = 28648 AND afd.crop_year = 2023
		
		//GET THE TOTALS FOR THE POLICY/CONTRACT
		//SELECT icc.inventory_contract_guid, ct.crop_commodity_id, ct.commodity_name, icc.total_unseeded_acres, icc.total_unseeded_acres_override, p.policy_number, gcy.grower_contract_year_id
		//FROM inventory_contract_commodity icc
		//LEFT JOIN crop_commodity ct ON ct.crop_commodity_id = icc.crop_commodity_id
		//JOIN inventory_contract ic ON ic.inventory_contract_guid = icc.inventory_contract_guid
		//JOIN grower_contract_year gcy ON gcy.contract_id = ic.contract_id AND gcy.crop_year = ic.crop_year
		//JOIN policy p ON p.contract_id = gcy.contract_id AND p.crop_year = gcy.crop_year
		//WHERE p.policy_number IN ('140160-23')

		//Get contracted field
		//It's easiest to test with an existing field with inventory
		Integer cfdId = 149171; //contractedFieldId of field 28648 in 2023 (policy 140160-23)
		ContractedFieldDetailRsrc contractedField = service.getContractedFieldDetail(topLevelEndpoints, cfdId.toString()); 

		//DELETE Contracted Field Detail
		contractedField.setTransactionType(LandManagementEventTypes.ContractedFieldDetailDeleted);
		service.synchronizeContractedFieldDetail(contractedField);
		
		//Check data manually in database
		
		//CREATE Contracted Field Detail
		ContractedFieldDetailRsrc contractedFieldDetailResource = createContractedFieldDetail( 
				contractedField.getContractedFieldDetailId(), 
				contractedField.getAnnualFieldDetailId(),
				contractedField.getGrowerContractYearId(), 
				contractedField.getDisplayOrder());
				contractedField.setTransactionType(LandManagementEventTypes.ContractedFieldDetailCreated);
		service.synchronizeContractedFieldDetail(contractedFieldDetailResource);

		//Check data manually in database
	
		logger.debug(">testRecalculateInventoryContractCommodities");
	
	}

	@Test
	public void testRecalculateInventoryCoverageTotalForages() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testRecalculateInventoryCoverageTotalForages");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 because they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);

		String policyNumber1 = "998877-21";
		String contractNumber1 = "998877";
		Integer cropYear1 = 2021;
		Integer insurancePlanId1 = 5;
		Integer policyId1 = 90000002;
		Integer gcyId1 = 90000003;
		Integer contractId1 = 90000004;
		Integer legalLandId1 = 90000005;

		Integer fieldId1 = 90000006;
		Integer annualFieldDetailId1 = 90000007;
		Integer contractedFieldDetailId1 = 90000008;

		Integer fieldId2 = 90000009;
		Integer annualFieldDetailId2 = 90000010;
		Integer contractedFieldDetailId2 = 90000011;
		
		
		// Create Grower
		GrowerRsrc growerRsrc = createGrower(growerId1, createTransactionDate);
		service.synchronizeGrower(growerRsrc);
		
		// Create GROWER CONTRACT YEAR
		GrowerContractYearSyncRsrc growerContractYearSyncRsrc = createGrowerContractYear(gcyId1, contractId1, growerId1, insurancePlanId1, cropYear1, createTransactionDate );
		service.synchronizeGrowerContractYear(growerContractYearSyncRsrc);

		// Create Policy
		PolicyRsrc policyRsrc = createPolicy(policyId1, growerId1, insurancePlanId1, policyNumber1, contractNumber1, contractId1, cropYear1, createTransactionDate);
		service.synchronizePolicy(policyRsrc);
		
		//CREATE Legal Land
		LegalLandRsrc legalLandResource = createLegalLand( legalLandId1, "OTHER", "Legal Description", "Short Legal",  "Other Description", 2011, cropYear1);
		service.synchronizeLegalLand(legalLandResource);

		
		//CREATE Fields
		FieldRsrc fieldResource = createField( fieldId1, "Field Label", 2011, cropYear1);
		service.synchronizeField(fieldResource);
		
		fieldResource = createField( fieldId2, "Field Label", 2011, cropYear1);
		service.synchronizeField(fieldResource);
		

		//CREATE Annual Field Details
		AnnualFieldDetailRsrc annualFieldDetailResource = createAnnualFieldDetail(annualFieldDetailId1, legalLandId1, fieldId1, cropYear1);
		service.synchronizeAnnualFieldDetail(annualFieldDetailResource);

		annualFieldDetailResource = createAnnualFieldDetail(annualFieldDetailId2, legalLandId1, fieldId2, cropYear1);
		service.synchronizeAnnualFieldDetail(annualFieldDetailResource);


		//CREATE Contracted Field Details
		ContractedFieldDetailRsrc contractedFieldDetailResource = createContractedFieldDetail( contractedFieldDetailId1, annualFieldDetailId1, gcyId1, 1);
		service.synchronizeContractedFieldDetail(contractedFieldDetailResource);

		contractedFieldDetailResource = createContractedFieldDetail( contractedFieldDetailId2, annualFieldDetailId2, gcyId1, 2);
		service.synchronizeContractedFieldDetail(contractedFieldDetailResource);

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

		AnnualFieldRsrc field1 = invContract.getFields().get(0);
		AnnualFieldRsrc field2 = invContract.getFields().get(1);

		// Remove default planting.
		field1.getPlantings().remove(0);
		field2.getPlantings().remove(0);
		
		// Field 1
		InventoryField planting = createPlanting(field1, 1, cropYear1);
		createInventorySeededForage(planting, 65, null, true, 12.3456); //FORAGE
				
		// Field 2
		planting = createPlanting(field2, 1, cropYear1);
		createInventorySeededForage(planting, 65, null, true, 33.4455); //FORAGE
		
		// Forage Totals
		List<InventoryCoverageTotalForage> expectedTotals = new ArrayList<InventoryCoverageTotalForage>();
		InventoryCoverageTotalForage ictf = createInventoryCovarageTotalForage(invContract.getInventoryContractGuid(), 65, "FORAGE", null, null, 45.7911, false);
		expectedTotals.add(ictf);

		InventoryContractRsrc fetchedInvContract = service.createInventoryContract(topLevelEndpoints, invContract);

		Assert.assertEquals(expectedTotals.size(), fetchedInvContract.getInventoryCoverageTotalForages().size());

		for (int i = 0; i < expectedTotals.size(); i++) {
			checkInventoryCoverageTotalForage(expectedTotals.get(i), fetchedInvContract.getInventoryCoverageTotalForages().get(i));
		}

		
		// Remove Field 2 from policy
		ContractedFieldDetailRsrc fetchedResource = service.getContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId2.toString());
		fetchedResource.setTransactionType(LandManagementEventTypes.ContractedFieldDetailDeleted);
		service.synchronizeContractedFieldDetail(fetchedResource);
		
		// Update expected totals.
		// FORAGE
		expectedTotals.get(0).setTotalFieldAcres(12.3456);

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
		
		uwContract = searchResults.getCollection().get(0);
		
		fetchedInvContract = service.getInventoryContract(uwContract);
		
		Assert.assertEquals(expectedTotals.size(), fetchedInvContract.getInventoryCoverageTotalForages().size());

		for (int i = 0; i < expectedTotals.size(); i++) {
			checkInventoryCoverageTotalForage(expectedTotals.get(i), fetchedInvContract.getInventoryCoverageTotalForages().get(i));
		}

		// Re-add Field 2 to policy
		contractedFieldDetailResource = createContractedFieldDetail( contractedFieldDetailId2, annualFieldDetailId2, gcyId1, 2);
		service.synchronizeContractedFieldDetail(contractedFieldDetailResource);

		expectedTotals.get(0).setTotalFieldAcres(45.7911);

		//Re-fetch InventoryContract.
		fetchedInvContract = service.getInventoryContract(uwContract);
		
		Assert.assertEquals(expectedTotals.size(), fetchedInvContract.getInventoryCoverageTotalForages().size());

		for (int i = 0; i < expectedTotals.size(); i++) {
			checkInventoryCoverageTotalForage(expectedTotals.get(i), fetchedInvContract.getInventoryCoverageTotalForages().get(i));
		}
		
		
		//*****************************************************************
		//AFTER TESTS: DELETE Inventory, Grower Contract Year, Policy and Grower using scripts below.
		//             If the growerId1, policyId1, contractId1, cropYear1 or growerContractYearId1 fields are changed,
		//             these scripts must also be updated.
		//*****************************************************************
		
		/* 
		 * 
			*****************
			DELETE STATEMENTS
			*****************

		DELETE FROM inventory_seeded_forage WHERE inventory_field_guid IN (SELECT inventory_field_guid FROM inventory_field WHERE field_id in (90000006, 90000009) AND crop_year = 2021);
		DELETE FROM inventory_field WHERE field_id in (90000006, 90000009) AND crop_year = 2021;
		DELETE FROM inventory_coverage_total_forage WHERE inventory_contract_guid IN (SELECT ic.inventory_contract_guid FROM inventory_contract ic WHERE contract_id = 90000004 and crop_year = 2021);
		DELETE FROM inventory_contract_commodity WHERE inventory_contract_guid IN (SELECT ic.inventory_contract_guid FROM inventory_contract ic WHERE contract_id = 90000004 and crop_year = 2021);
		DELETE FROM inventory_contract WHERE contract_id = 90000004 and crop_year = 2021;
		
		DELETE FROM contracted_field_detail WHERE contracted_field_detail_id in (90000008, 90000011);
		DELETE FROM annual_field_detail WHERE annual_field_detail_id in (90000007, 90000010);
		DELETE FROM field WHERE field_id in (90000006, 90000009);
		DELETE FROM legal_land WHERE legal_land_id = 90000005;
	
		DELETE FROM grower_contract_year WHERE grower_contract_year_id = 90000003;
		DELETE FROM policy WHERE policy_id = 90000002;
		DELETE FROM grower WHERE grower_id = 90000321;
			 
			*****************
			SELECT STATEMENTS
			*****************

		SELECT * FROM inventory_seeded_forage WHERE inventory_field_guid IN (SELECT inventory_field_guid FROM inventory_field WHERE field_id in (90000006, 90000009) AND crop_year = 2021);
		SELECT * FROM inventory_field WHERE field_id in (90000006, 90000009) AND crop_year = 2021;
		SELECT * FROM inventory_coverage_total_forage WHERE inventory_contract_guid IN (SELECT ic.inventory_contract_guid FROM inventory_contract ic WHERE contract_id = 90000004 and crop_year = 2021);
		SELECT * FROM inventory_contract_commodity WHERE inventory_contract_guid IN (SELECT ic.inventory_contract_guid FROM inventory_contract ic WHERE contract_id = 90000004 and crop_year = 2021);
		SELECT * FROM inventory_contract WHERE contract_id = 90000004 and crop_year = 2021;
		
		SELECT * FROM contracted_field_detail WHERE contracted_field_detail_id in (90000008, 90000011);
		SELECT * FROM annual_field_detail WHERE annual_field_detail_id in (90000007, 90000010);
		SELECT * FROM field WHERE field_id in (90000006, 90000009);
		SELECT * FROM legal_land WHERE legal_land_id = 90000005;
	
		SELECT * FROM grower_contract_year WHERE grower_contract_year_id = 90000003;
		SELECT * FROM policy WHERE policy_id = 90000002;
		SELECT * FROM grower WHERE grower_id = 90000001;

		 */
		
		logger.debug(">testRecalculateInventoryCoverageTotalForages");
	}
	
	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}
	
	private static LegalLandRsrc createLegalLand(Integer legalLandId, 
			String primaryReferenceTypeCode, String legalDescription, String legalShortDescription, String otherDescription,
			Integer activeFromCropYear, Integer activeToCropYear) {

		//CREATE LegalLand
		LegalLandRsrc resource = new LegalLandRsrc();
		
		resource.setLegalLandId(legalLandId);
		resource.setPrimaryReferenceTypeCode(primaryReferenceTypeCode);
		resource.setLegalDescription(legalDescription);
		resource.setLegalShortDescription(legalShortDescription);
		resource.setOtherDescription(otherDescription);
		resource.setActiveFromCropYear(activeFromCropYear);
		resource.setActiveToCropYear(activeToCropYear);
		resource.setTransactionType(LandManagementEventTypes.LegalLandCreated);
		
		return resource;
	}
	
	private static FieldRsrc createField( Integer fieldId, String fieldLabel, Integer activeFromCropYear, Integer activeToCropYear) {

		FieldRsrc resource = new FieldRsrc();
		
		resource.setFieldId(fieldId);
		resource.setFieldLabel(fieldLabel);
		resource.setActiveFromCropYear(activeFromCropYear);
		resource.setActiveToCropYear(activeToCropYear);
		resource.setTransactionType(LandManagementEventTypes.FieldCreated);
				
		return resource;
	}	
	
	private static GrowerContractYearSyncRsrc createGrowerContractYear( 
			Integer growerContractYearId, 
			Integer contractId, 
			Integer growerId,
			Integer insurancePlanId,
			Integer cropYear, 
			Date createTransactionDate ) {

		GrowerContractYearSyncRsrc resource = new GrowerContractYearSyncRsrc();
		
		resource.setGrowerContractYearId(growerContractYearId);
		resource.setContractId(contractId);
		resource.setGrowerId(growerId);
		resource.setInsurancePlanId(insurancePlanId);
		resource.setCropYear(cropYear);
		resource.setDataSyncTransDate( createTransactionDate );
		resource.setTransactionType(PoliciesSyncEventTypes.GrowerContractYearCreated);
		
		return resource;
	}

	private static GrowerRsrc createGrower(Integer growerId, Date createTransactionDate) {
		GrowerRsrc resource = new GrowerRsrc();
		
		resource.setGrowerId(growerId);
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

		return resource;
	}
	
	private static PolicyRsrc createPolicy(Integer policyId, Integer growerId, Integer insurancePlanId, String policyNumber, String contractNumber, Integer contractId, Integer cropYear, Date createTransactionDate) {

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

		return resource;
	}
	
	
	private static AnnualFieldDetailRsrc createAnnualFieldDetail( Integer annualFieldDetailId, Integer legalLandId, Integer fieldId, Integer cropYear) {

		AnnualFieldDetailRsrc resource = new AnnualFieldDetailRsrc();
		
		resource.setAnnualFieldDetailId(annualFieldDetailId);
		resource.setLegalLandId(legalLandId);
		resource.setFieldId(fieldId);
		resource.setCropYear(cropYear);
		resource.setTransactionType(LandManagementEventTypes.AnnualFieldDetailCreated);
		
		return resource;
	}
	
	private static ContractedFieldDetailRsrc createContractedFieldDetail( 
			Integer contractedFieldDetailId, 
			Integer annualFieldDetailId, 
			Integer growerContractYearId, 
			Integer displayOrder) {

		ContractedFieldDetailRsrc resource = new ContractedFieldDetailRsrc();
		
		resource.setContractedFieldDetailId(contractedFieldDetailId);
		resource.setAnnualFieldDetailId(annualFieldDetailId);
		resource.setGrowerContractYearId(growerContractYearId);
		resource.setDisplayOrder(displayOrder);
		resource.setTransactionType(LandManagementEventTypes.ContractedFieldDetailCreated);
		
		return resource;
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
	
	public void checkInventoryCoverageTotalForage(InventoryCoverageTotalForage expected, InventoryCoverageTotalForage actual) {
		Assert.assertEquals("CropCommodityId", expected.getCropCommodityId(), actual.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", expected.getCropCommodityName(), actual.getCropCommodityName());
		Assert.assertEquals("PlantInsurabilityTypeCode", expected.getPlantInsurabilityTypeCode(), actual.getPlantInsurabilityTypeCode());
		Assert.assertEquals("PlantInsurabilityTypeDesc", expected.getPlantInsurabilityTypeDesc(), actual.getPlantInsurabilityTypeDesc());
		Assert.assertEquals("TotalFieldAcres", expected.getTotalFieldAcres(), actual.getTotalFieldAcres());	
		Assert.assertEquals("IsUnseededInsurableInd", expected.getIsUnseededInsurableInd(), actual.getIsUnseededInsurableInd());
	}

}
