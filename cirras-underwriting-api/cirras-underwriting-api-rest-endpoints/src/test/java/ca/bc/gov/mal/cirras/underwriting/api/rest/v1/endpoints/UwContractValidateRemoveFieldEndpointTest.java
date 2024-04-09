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
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.PolicyRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.RemoveFieldValidationRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UwContractListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UwContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventorySeededForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventorySeededGrain;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryUnseeded;
import ca.bc.gov.mal.cirras.underwriting.model.v1.RemoveFieldValidation;
import ca.bc.gov.mal.cirras.underwriting.model.v1.UnderwritingComment;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.InventoryServiceEnums.InsurancePlans;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.common.wfone.rest.resource.MessageRsrc;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class UwContractValidateRemoveFieldEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(UwContractValidateRemoveFieldEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.SEARCH_UWCONTRACTS,
		Scopes.CREATE_INVENTORY_CONTRACT,		
		Scopes.UPDATE_INVENTORY_CONTRACT,
		Scopes.DELETE_INVENTORY_CONTRACT,
		Scopes.CREATE_SYNC_UNDERWRITING,
		Scopes.UPDATE_SYNC_UNDERWRITING,
		Scopes.DELETE_SYNC_UNDERWRITING,
		Scopes.GET_LAND,
		Scopes.GET_LEGAL_LAND,
		Scopes.GET_INVENTORY_CONTRACT,
		Scopes.GET_POLICY,
		Scopes.GET_GROWER,
		Scopes.DELETE_COMMENTS
	};
	

	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;

	private Integer growerId = 90000001;
	private Integer contractId1 = 90000002;
	private Integer policyId1 = 90000003;
	private String policyNumber1 = "998877-20";
	private String contractNumber1 = "998877";
	private Integer growerContractYearId1 = 90000004;

	private Integer contractId2 = 90000005;
	private Integer policyId2 = 90000006;
	private String policyNumber2 = null;  // Set by unit test as the year can change.
	private String contractNumber2 = "998899";
	private Integer growerContractYearId2 = 90000007;
	
	// Field
	private Integer fieldId1 = 90000008;

	// Contract 1
	private Integer annualFieldDetailId1 = 90000009;
	private Integer contractedFieldDetailId1 = 90000010;

	// Contract 2
	private Integer annualFieldDetailId2 = 90000011;
	private Integer contractedFieldDetailId2 = 90000012;

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

		if ( policyNumber2 != null ) { 
			deleteInventoryContract(policyNumber2);
		}
		
		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId1.toString());
		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId2.toString());

		service.deleteGrowerContractYear(topLevelEndpoints, growerContractYearId1.toString());
		service.deleteGrowerContractYear(topLevelEndpoints, growerContractYearId2.toString());

		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId1.toString());
		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId2.toString());
		
		service.deleteField(topLevelEndpoints, fieldId1.toString());

		service.deletePolicy(topLevelEndpoints, policyId1.toString());
		service.deletePolicy(topLevelEndpoints, policyId2.toString());

		service.deleteGrower(topLevelEndpoints, growerId.toString());
		
	}
	
	private void deleteInventoryContract(String policyNumber) throws NotFoundDaoException, DaoException, CirrasUnderwritingServiceException {

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
		
		if ( searchResults.getCollection() != null && searchResults.getCollection().size() == 1 ) {
			UwContractRsrc referrer = searchResults.getCollection().get(0);
						
			if ( referrer.getInventoryContractGuid() != null ) { 
				InventoryContractRsrc invContract = service.getInventoryContract(referrer);
				service.deleteInventoryContract(invContract);
			}
		}		
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

	private void createGrowerContractYear(
			Integer gcyId, 
			Integer contractId, 
			Integer growerId, 
			Integer cropYear, 
			Integer insurancePlanId, 
			Date createTransactionDate
	) throws DaoException, CirrasUnderwritingServiceException, ValidationException { 

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

	
	private void createInventoryContract(String policyNumber, Integer insurancePlanId, boolean hasUnseeded, boolean hasSeeded) throws ValidationException, CirrasUnderwritingServiceException {

		boolean addedUnseededGrain = false;
		boolean addedSeededGrain = false;
		boolean addedSeededForage = false;
		
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

		InventoryContractRsrc resource = service.rolloverInventoryContract(referrer);
		
		// add comment
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

		for ( AnnualFieldRsrc field : resource.getFields() ) {
			for ( InventoryField planting : field.getPlantings() ) {
				if ( planting.getInventorySeededForages() != null && insurancePlanId.equals(InsurancePlans.FORAGE.getInsurancePlanId()) ) {
					for ( InventorySeededForage isf : planting.getInventorySeededForages() ) {

						// Fix Seeded Forage, which sets null defaults for a few mandatory columns.
						isf.setIsIrrigatedInd(false);
						isf.setIsQuantityInsurableInd(false);
						
						if (hasSeeded) {
							isf.setCommodityTypeCode("CPSW");
							isf.setCropCommodityId(26);
							isf.setCropVarietyId(1010602);
							isf.setCropVarietyName("AAC ENTICE");
							isf.setFieldAcres(10.4);
							isf.setSeedingYear(2018);
							isf.setSeedingDate(null);
							isf.setPlantInsurabilityTypeCode("E1");
							isf.setIsAwpEligibleInd(true);
							
							addedSeededForage = true;
						}
					}
				} else if ( planting.getInventoryUnseeded() != null && insurancePlanId.equals(InsurancePlans.GRAIN.getInsurancePlanId()) ) {
					
					if ( hasUnseeded ) {
						InventoryUnseeded iu = planting.getInventoryUnseeded();
						iu.setAcresToBeSeeded(12.34);
						iu.setCropCommodityId(16);
						iu.setCropCommodityName("BARLEY");
						iu.setInventoryFieldGuid(null);
						iu.setInventoryUnseededGuid(null);
						iu.setIsUnseededInsurableInd(true);
						
						addedUnseededGrain = true;
					}
					
					if ( hasSeeded ) {
						Calendar cal = Calendar.getInstance();
						cal.clear();
						cal.set(2020, Calendar.JANUARY, 15);
						Date seededDate = cal.getTime();
						
						InventorySeededGrain invSeededGrain = new InventorySeededGrain();

						invSeededGrain.setCommodityTypeCode("Two Row");
						invSeededGrain.setCommodityTypeDesc("Two Row Standard");
						invSeededGrain.setCropCommodityId(16);
						invSeededGrain.setCropCommodityName("BARLEY");
						invSeededGrain.setCropVarietyId(1010430);
						invSeededGrain.setCropVarietyName("CHAMPION");
						invSeededGrain.setInventoryFieldGuid(null);
						invSeededGrain.setInventorySeededGrainGuid(null);
						invSeededGrain.setIsPedigreeInd(false);
						invSeededGrain.setIsSpotLossInsurableInd(true);
						invSeededGrain.setIsQuantityInsurableInd(true);
						invSeededGrain.setIsReplacedInd(false);
						invSeededGrain.setSeededAcres(23.45);
						invSeededGrain.setSeededDate(seededDate);
						
						List<InventorySeededGrain> seededGrains = new ArrayList<InventorySeededGrain>();
						seededGrains.add(invSeededGrain);

						planting.setInventorySeededGrains(seededGrains);
						
						addedSeededGrain = true;
					}					
				}
			}
		}
		
		service.createInventoryContract(topLevelEndpoints, resource);

		if ( insurancePlanId.equals(InsurancePlans.GRAIN.getInsurancePlanId()) ) {
			Assert.assertEquals(hasUnseeded, addedUnseededGrain);
			Assert.assertEquals(hasSeeded, addedSeededGrain);
			
		} else if ( insurancePlanId.equals(InsurancePlans.FORAGE.getInsurancePlanId()) ) {
			Assert.assertEquals(hasSeeded, addedSeededForage);
		}
	}
	
	
	@Test
	public void testValidateRemoveField() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, DaoException {
		logger.debug("<testValidateRemoveField");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		// These must be set to a real policy in CIRRAS.
		String policyNumberWithProducts = "212076-23";
		Integer fieldIdOnPolicyWithProducts = 1030024;
		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();
		Date createTransactionDate = addSeconds(transactionDate, -1);

		createGrower(growerId, 999888, "grower name", createTransactionDate);
		createPolicy(policyId1, growerId, 4, policyNumber1, contractNumber1, contractId1, 2020, createTransactionDate);
		createGrowerContractYear(growerContractYearId1, contractId1, growerId, 2020, 4, createTransactionDate);

		createField(fieldId1, "LOT 1", 1980, null);
		createAnnualFieldDetail(annualFieldDetailId1, null, fieldId1, 2020);
		createContractedFieldDetail(contractedFieldDetailId1, annualFieldDetailId1, growerContractYearId1, 1);
				
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

		// Test 1: No errors or warnings.
		RemoveFieldValidationRsrc removeFieldValidation = service.validateRemoveField(referrer, fieldId1.toString());
		checkRemoveFieldValidation(removeFieldValidation, true, true, null, null);
				
		// Test 2: Field on other Contract.
		// A. Other Contract in another Year.
		policyNumber2 = contractNumber2 + "-19";
		createPolicy(policyId2, growerId, 4, policyNumber2, contractNumber2, contractId2, 2019, createTransactionDate);
		createGrowerContractYear(growerContractYearId2, contractId2, growerId, 2019, 4, createTransactionDate);
		createAnnualFieldDetail(annualFieldDetailId2, null, fieldId1, 2019);
		createContractedFieldDetail(contractedFieldDetailId2, annualFieldDetailId2, growerContractYearId2, 1);
		
		removeFieldValidation = service.validateRemoveField(referrer, fieldId1.toString());
		checkRemoveFieldValidation(removeFieldValidation, true, false, null, new String[] { RemoveFieldValidation.FIELD_ON_OTHER_CONTRACTS_MSG.replace("[numOtherContracts]", "1").replace("[policy]", "policy")});

		// B. Other Contract in another Year With Empty Inventory and Comments.
		createInventoryContract(policyNumber2, 4, false, false);

		removeFieldValidation = service.validateRemoveField(referrer, fieldId1.toString());
		checkRemoveFieldValidation(
				removeFieldValidation, 
				true, 
				false, 
				null, 
				new String[] { RemoveFieldValidation.FIELD_ON_OTHER_CONTRACTS_MSG.replace("[numOtherContracts]", "1").replace("[policy]", "policy"), 
						       RemoveFieldValidation.FIELD_HAS_OTHER_COMMENTS_MSG
				});

		deleteInventoryContract(policyNumber2);

		// C. Other Contract in another Year With non-empty Unseeded Inventory and Comments.
		createInventoryContract(policyNumber2, 4, true, false);

		removeFieldValidation = service.validateRemoveField(referrer, fieldId1.toString());
		checkRemoveFieldValidation(
				removeFieldValidation, 
				true, 
				false, 
				null, 
				new String[] { RemoveFieldValidation.FIELD_ON_OTHER_CONTRACTS_MSG.replace("[numOtherContracts]", "1").replace("[policy]", "policy"), 
						       RemoveFieldValidation.FIELD_HAS_OTHER_INVENTORY_MSG,
						       RemoveFieldValidation.FIELD_HAS_OTHER_COMMENTS_MSG
				});

		deleteInventoryContract(policyNumber2);
		
		// D. Other Contract in another Year With non-empty Seeded Inventory and Comments.
		createInventoryContract(policyNumber2, 4, false, true);

		removeFieldValidation = service.validateRemoveField(referrer, fieldId1.toString());
		checkRemoveFieldValidation(
				removeFieldValidation, 
				true, 
				false, 
				null, 
				new String[] { RemoveFieldValidation.FIELD_ON_OTHER_CONTRACTS_MSG.replace("[numOtherContracts]", "1").replace("[policy]", "policy"), 
						       RemoveFieldValidation.FIELD_HAS_OTHER_INVENTORY_MSG,
						       RemoveFieldValidation.FIELD_HAS_OTHER_COMMENTS_MSG
				});

		deleteInventoryContract(policyNumber2);
		
		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId2.toString());
		service.deleteGrowerContractYear(topLevelEndpoints, growerContractYearId2.toString());
		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId2.toString());
		service.deletePolicy(topLevelEndpoints, policyId2.toString());
		
		
		// E. Other Contract in same year, different plan:
		policyNumber2 = contractNumber2 + "-20";
		createPolicy(policyId2, growerId, 5, policyNumber2, contractNumber2, contractId2, 2020, createTransactionDate);
		createGrowerContractYear(growerContractYearId2, contractId2, growerId, 2020, 5, createTransactionDate);
		createContractedFieldDetail(contractedFieldDetailId2, annualFieldDetailId1, growerContractYearId2, 1);
		
		removeFieldValidation = service.validateRemoveField(referrer, fieldId1.toString());
		checkRemoveFieldValidation(removeFieldValidation, true, false, null, new String[] { RemoveFieldValidation.FIELD_ON_OTHER_CONTRACTS_MSG.replace("[numOtherContracts]", "1").replace("[policy]", "policy")});

		// F. Other Contract in same year, different plan with Empty Inventory and Comments (however comment does not trigger an error since it's the same year).
		createInventoryContract(policyNumber2, 5, false, false);

		removeFieldValidation = service.validateRemoveField(referrer, fieldId1.toString());
		checkRemoveFieldValidation(
				removeFieldValidation, 
				true, 
				false, 
				null, 
				new String[] { RemoveFieldValidation.FIELD_ON_OTHER_CONTRACTS_MSG.replace("[numOtherContracts]", "1").replace("[policy]", "policy") 
				});

		deleteInventoryContract(policyNumber2);

		// G. Other Contract in same year, different plan with non-empty Seeded Inventory and Comments (however comment does not trigger an error since it's the same year).
		createInventoryContract(policyNumber2, 5, false, true);

		removeFieldValidation = service.validateRemoveField(referrer, fieldId1.toString());
		checkRemoveFieldValidation(
				removeFieldValidation, 
				true, 
				false, 
				null, 
				new String[] { RemoveFieldValidation.FIELD_ON_OTHER_CONTRACTS_MSG.replace("[numOtherContracts]", "1").replace("[policy]", "policy"),
						       RemoveFieldValidation.FIELD_HAS_OTHER_INVENTORY_MSG
				});

		deleteInventoryContract(policyNumber2);
		
		
		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId2.toString());
		service.deleteGrowerContractYear(topLevelEndpoints, growerContractYearId2.toString());
		service.deletePolicy(topLevelEndpoints, policyId2.toString());

		// Test 3: Policy has Products (cannot be fully automated since the Products are in CIRRAS)
		searchResults = service.getUwContractList(
				topLevelEndpoints, 
				null, 
				null, 
				null,
				null,
				policyNumberWithProducts,
				null,
				null, 
				null, 
				null, 
				pageNumber, pageRowCount);

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(1, searchResults.getCollection().size());

		referrer = searchResults.getCollection().get(0);

		removeFieldValidation = service.validateRemoveField(referrer, fieldIdOnPolicyWithProducts.toString());
		checkRemoveFieldValidation(removeFieldValidation, true, true, new String[] { RemoveFieldValidation.POLICY_HAS_PRODUCTS_MSG}, null);
		
		logger.debug(">testValidateRemoveField");
	}

	private void checkRemoveFieldValidation(RemoveFieldValidationRsrc resource,  Boolean expectedIsRemoveFromPolicyAllowed, Boolean expectedIsDeleteFieldAllowed, String[] expectedRemoveFromPolicyWarnings, String[] expectedDeleteFieldErrors) {
		Assert.assertNotNull(resource);

		Assert.assertEquals(expectedIsRemoveFromPolicyAllowed, resource.getIsRemoveFromPolicyAllowed());
		Assert.assertEquals(expectedIsDeleteFieldAllowed, resource.getIsDeleteFieldAllowed());

		checkValidationMessages(resource.getRemoveFromPolicyWarnings(), expectedRemoveFromPolicyWarnings);
		checkValidationMessages(resource.getDeleteFieldErrors(), expectedDeleteFieldErrors);
	}

	private void checkValidationMessages(List<MessageRsrc> actualMessages, String[] expectedMessages) {
		if ( expectedMessages == null ) {
			Assert.assertEquals(0, actualMessages.size());
		} else {
			Assert.assertEquals(expectedMessages.length, actualMessages.size());
			for ( int i = 0; i < expectedMessages.length; i++ ) {
				Assert.assertEquals(expectedMessages[i], actualMessages.get(i).getMessage());
			}
		}		
	}
	
	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}


}
