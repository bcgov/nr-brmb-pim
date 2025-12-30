package ca.bc.gov.mal.cirras.underwriting.controllers;

import org.junit.After;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.services.utils.LandManagementEventTypes;
import ca.bc.gov.mal.cirras.underwriting.clients.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.clients.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.clients.ValidationException;
import ca.bc.gov.mal.cirras.underwriting.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.RenameLegalValidationRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.AnnualFieldDetailRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.ContractedFieldDetailRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.FieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.LegalLandListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.LegalLandRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.UwContractListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.UwContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class UwContractValidateRenameLegalEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(UwContractValidateRenameLegalEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.SEARCH_UWCONTRACTS,
		Scopes.CREATE_INVENTORY_CONTRACT,
		Scopes.UPDATE_SYNC_UNDERWRITING,
		Scopes.DELETE_SYNC_UNDERWRITING,
		Scopes.GET_LAND,
		Scopes.GET_LEGAL_LAND
	};
	

	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;

	// Legal Lands
	private Integer legalLandId = 90000001;
	private Integer legalLandId2 = 90000002;
	private Integer legalLandId3 = 90000003;

	// Field 1
	private Integer fieldId = 90000004;
	private Integer annualFieldDetailId = 90000005;
	private Integer contractedFieldDetailId = 90000006;

	// Field 2
	private Integer fieldId2 = 90000007;
	private Integer annualFieldDetailId2 = 90000008;
	private Integer contractedFieldDetailId2 = 90000009;

	// Field 3
	private Integer fieldId3 = 90000010;
	private Integer annualFieldDetailId3 = 90000011;
	private Integer contractedFieldDetailId3 = 90000012;


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

	
	private void delete() throws CirrasUnderwritingServiceException{

		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId.toString());
		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId2.toString());
		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId3.toString());

		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId.toString());
		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId2.toString());
		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId3.toString());

		service.deleteField(topLevelEndpoints, fieldId.toString());
		service.deleteField(topLevelEndpoints, fieldId2.toString());
		service.deleteField(topLevelEndpoints, fieldId3.toString());

		service.deleteLegalLandSync(topLevelEndpoints, legalLandId.toString());
		service.deleteLegalLandSync(topLevelEndpoints, legalLandId2.toString());
		service.deleteLegalLandSync(topLevelEndpoints, legalLandId3.toString());
		
	}
	

	private void createLegalLand(String legalLocation, String legalDescription, String primaryPropertyIdentifier, String legalShortDescription, Integer llId) throws CirrasUnderwritingServiceException, ValidationException {
		
		String primaryReferenceTypeCode = "OTHER";
		String otherDescription = legalLocation;
		Integer activeFromCropYear = 2011;
		Integer activeToCropYear = 2022;
		
		//CREATE LegalLand
		LegalLandRsrc resource = new LegalLandRsrc();
		
		resource.setLegalLandId(llId);
		resource.setPrimaryPropertyIdentifier(primaryPropertyIdentifier);
		resource.setPrimaryReferenceTypeCode(primaryReferenceTypeCode);
		resource.setLegalDescription(legalDescription);
		resource.setLegalShortDescription(legalShortDescription);
		resource.setOtherDescription(otherDescription);
		resource.setActiveFromCropYear(activeFromCropYear);
		resource.setActiveToCropYear(activeToCropYear);
		resource.setTransactionType(LandManagementEventTypes.LegalLandCreated);
		
		service.synchronizeLegalLand(resource);

	}
	
	private void createField( Integer fieldId, String fieldLabel, String fieldLocation, Integer activeFromCropYear, Integer activeToCropYear) throws CirrasUnderwritingServiceException, ValidationException {

		FieldRsrc resource = new FieldRsrc();
		
		resource.setFieldId(fieldId);
		resource.setFieldLabel(fieldLabel);
		resource.setFieldLocation(fieldLocation);
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
	
	@Test
	public void testValidateRenameLegalGrainForage() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testValidateRenameLegalGrainForage");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		// Main Policy
		String policyNumber = "712174-22";
		Integer gcyId = 95401;

		// Same Contract as Main Policy, but prior crop year.
		String policyNumber2 = "712174-21";
		Integer gcyId2 = 92573;

		// Different Contract but same Crop Year as Main Policy.
		String policyNumber3 = "140160-22";
		Integer gcyId3 = 95123;

		// Different Contract, older Crop Year than Main Policy.
		String policyNumber4 = "140160-21";
		Integer gcyId4 = 92491;

		String primaryPropertyIdentifier = "GF0099999";
		createLegalLand("TEST LEGAL LOC 123", null, primaryPropertyIdentifier, null, legalLandId);
		createField( fieldId, "Field Label", null, 2011, 2022);
		createAnnualFieldDetail(annualFieldDetailId, legalLandId, fieldId, 2022);
		createContractedFieldDetail(contractedFieldDetailId, annualFieldDetailId, gcyId, 1);
		
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
		
		String legalLocationOrPid = "Legal Location"; //Default
		String pidOrLegalLocation = "PID"; //Default
		
		String legalsWithSameLocMsg = RenameLegalValidationRsrc.LEGALS_WITH_SAME_LOC_MSG.replace("[LegalLocationOrPID]", legalLocationOrPid);
		String otherLegalDataMsg = RenameLegalValidationRsrc.OTHER_LEGAL_DATA_MSG
									.replace("[PidOrLegalLocation]", pidOrLegalLocation)
									.replace("[LegalLocationOrPID]", legalLocationOrPid);
		String otherFieldOnPolicyMsg = RenameLegalValidationRsrc.OTHER_FIELD_ON_POLICY_MSG.replace("[LegalLocationOrPID]", legalLocationOrPid);
		String fieldOnOtherPolicyMsg = RenameLegalValidationRsrc.FIELD_ON_OTHER_POLICY_MSG.replace("[LegalLocationOrPID]", legalLocationOrPid);

		// Test 1: Legal Location already exists.
		createLegalLand("test legal 1234", "Legal Description", "111-222-333", "Short Legal", legalLandId2);
		createLegalLand("test legal 1234", "Legal Description 2", "111-222-333", "Short Legal",  legalLandId3);
		
		// 1A. Legal Location matches 2 legal lands.
		RenameLegalValidationRsrc renameLegalValidation = service.validateRenameLegal(referrer, annualFieldDetailId.toString(), "test legal 1234", null);
		Assert.assertNotNull(renameLegalValidation);

		Assert.assertEquals(true, renameLegalValidation.getIsWarningLegalsWithSameLoc());
		Assert.assertEquals(legalsWithSameLocMsg, renameLegalValidation.getLegalsWithSameLocMsg().getMessage());
		Assert.assertEquals(2, renameLegalValidation.getLegalsWithSameLocList().size());
		
		Assert.assertEquals(legalLandId2, renameLegalValidation.getLegalsWithSameLocList().get(0).getLegalLandId());
		Assert.assertEquals(legalLandId3, renameLegalValidation.getLegalsWithSameLocList().get(1).getLegalLandId());
		
		service.deleteLegalLandSync(topLevelEndpoints, legalLandId2.toString());
		service.deleteLegalLandSync(topLevelEndpoints, legalLandId3.toString());

		// 1B. Legal Location matches 0 legal lands.
		renameLegalValidation = service.validateRenameLegal(referrer, annualFieldDetailId.toString(), "test legal 1234", null);
		Assert.assertNotNull(renameLegalValidation);

		Assert.assertEquals(false, renameLegalValidation.getIsWarningLegalsWithSameLoc());
		Assert.assertNull(renameLegalValidation.getLegalsWithSameLocMsg());
		Assert.assertNull(renameLegalValidation.getLegalsWithSameLocList());

		// Test 2: Non-default legal description, short legal description or PID.

		// 2A. Non-null legal description
		LegalLandRsrc ll = service.getLegalLand(findLegalLandResource(primaryPropertyIdentifier)); 
		ll.setLegalDescription("legal description");
		ll.setTransactionType(LandManagementEventTypes.LegalLandUpdated);
		
		service.synchronizeLegalLand(ll);
		
		renameLegalValidation = service.validateRenameLegal(referrer, annualFieldDetailId.toString(), "NEW LEGAL LOC", null);
		Assert.assertNotNull(renameLegalValidation);
		
		Assert.assertEquals(true, renameLegalValidation.getIsWarningOtherLegalData());
		Assert.assertEquals(otherLegalDataMsg, renameLegalValidation.getOtherLegalDataMsg().getMessage());
		Assert.assertNotNull(renameLegalValidation.getOtherLegalData());
		Assert.assertEquals(legalLandId, renameLegalValidation.getOtherLegalData().getLegalLandId());
		Assert.assertEquals("legal description", renameLegalValidation.getOtherLegalData().getLegalDescription());
		Assert.assertNull(renameLegalValidation.getOtherLegalData().getLegalShortDescription());
		Assert.assertEquals(primaryPropertyIdentifier, renameLegalValidation.getOtherLegalData().getPrimaryPropertyIdentifier());
		
		// 2B. Non-null short legal description.
		ll = service.getLegalLand(findLegalLandResource(primaryPropertyIdentifier)); 
		ll.setLegalDescription(null);
		ll.setLegalShortDescription("Short Legal");
		ll.setTransactionType(LandManagementEventTypes.LegalLandUpdated);
		
		service.synchronizeLegalLand(ll);
		
		renameLegalValidation = service.validateRenameLegal(referrer, annualFieldDetailId.toString(), "NEW LEGAL LOC", null);
		Assert.assertNotNull(renameLegalValidation);
		
		Assert.assertEquals(true, renameLegalValidation.getIsWarningOtherLegalData());
		Assert.assertEquals(otherLegalDataMsg, renameLegalValidation.getOtherLegalDataMsg().getMessage());
		Assert.assertNotNull(renameLegalValidation.getOtherLegalData());
		Assert.assertEquals(legalLandId, renameLegalValidation.getOtherLegalData().getLegalLandId());
		Assert.assertNull(renameLegalValidation.getOtherLegalData().getLegalDescription());
		Assert.assertEquals("Short Legal", renameLegalValidation.getOtherLegalData().getLegalShortDescription());
		Assert.assertEquals(primaryPropertyIdentifier, renameLegalValidation.getOtherLegalData().getPrimaryPropertyIdentifier());

		// 2C. Non-default PID
		String primaryPropertyIdentifier2 = "953-165-151";
		ll = service.getLegalLand(findLegalLandResource(primaryPropertyIdentifier)); 
		ll.setLegalShortDescription(null);
		ll.setPrimaryPropertyIdentifier(primaryPropertyIdentifier2);
		ll.setTransactionType(LandManagementEventTypes.LegalLandUpdated);
		
		service.synchronizeLegalLand(ll);

		renameLegalValidation = service.validateRenameLegal(referrer, annualFieldDetailId.toString(), "NEW LEGAL LOC", null);
		Assert.assertNotNull(renameLegalValidation);
		
		Assert.assertEquals(true, renameLegalValidation.getIsWarningOtherLegalData());
		Assert.assertEquals(otherLegalDataMsg, renameLegalValidation.getOtherLegalDataMsg().getMessage());
		Assert.assertNotNull(renameLegalValidation.getOtherLegalData());
		Assert.assertEquals(legalLandId, renameLegalValidation.getOtherLegalData().getLegalLandId());
		Assert.assertNull(renameLegalValidation.getOtherLegalData().getLegalDescription());
		Assert.assertNull(renameLegalValidation.getOtherLegalData().getLegalShortDescription());
		Assert.assertEquals(primaryPropertyIdentifier2, renameLegalValidation.getOtherLegalData().getPrimaryPropertyIdentifier());

		// 2D. All default values.
		ll = service.getLegalLand(findLegalLandResource(primaryPropertyIdentifier2)); 
		ll.setPrimaryPropertyIdentifier(primaryPropertyIdentifier);
		ll.setTransactionType(LandManagementEventTypes.LegalLandUpdated);
		
		service.synchronizeLegalLand(ll);
		
		renameLegalValidation = service.validateRenameLegal(referrer, annualFieldDetailId.toString(), "NEW LEGAL LOC", null);
		Assert.assertNotNull(renameLegalValidation);
		
		Assert.assertEquals(false, renameLegalValidation.getIsWarningOtherLegalData());
		Assert.assertNull(renameLegalValidation.getOtherLegalDataMsg());
		Assert.assertNull(renameLegalValidation.getOtherLegalData());

		// Test 3: Other fields on same policy
		// Field on same policy
		createField( fieldId2, "Field Label 2", null, 2011, 2022);
		createAnnualFieldDetail(annualFieldDetailId2, legalLandId, fieldId2, 2022);
		createContractedFieldDetail(contractedFieldDetailId2, annualFieldDetailId2, gcyId, 1);

		// Field on same contract, older policy.
		createField( fieldId3, "Field Label 3", null, 2011, 2022);
		createAnnualFieldDetail(annualFieldDetailId3, legalLandId, fieldId3, 2022);
		createContractedFieldDetail(contractedFieldDetailId3, annualFieldDetailId3, gcyId2, 1);
		
		// 3A. Other fields on same contract.
		renameLegalValidation = service.validateRenameLegal(referrer, annualFieldDetailId.toString(), "NEW LEGAL LOC", null);
		Assert.assertNotNull(renameLegalValidation);

		Assert.assertEquals(true, renameLegalValidation.getIsWarningOtherFieldOnPolicy());
		Assert.assertEquals(otherFieldOnPolicyMsg, renameLegalValidation.getOtherFieldOnPolicyMsg().getMessage());
		Assert.assertEquals(2, renameLegalValidation.getOtherFieldOnPolicyList().size());
		
		Assert.assertEquals(fieldId2, renameLegalValidation.getOtherFieldOnPolicyList().get(0).getFieldId());
		Assert.assertEquals("Field Label 2", renameLegalValidation.getOtherFieldOnPolicyList().get(0).getFieldLabel());
		Assert.assertEquals(1, renameLegalValidation.getOtherFieldOnPolicyList().get(0).getPolicies().size());
		Assert.assertEquals(policyNumber, renameLegalValidation.getOtherFieldOnPolicyList().get(0).getPolicies().get(0).getPolicyNumber());

		Assert.assertEquals(fieldId3, renameLegalValidation.getOtherFieldOnPolicyList().get(1).getFieldId());
		Assert.assertEquals("Field Label 3", renameLegalValidation.getOtherFieldOnPolicyList().get(1).getFieldLabel());
		Assert.assertEquals(1, renameLegalValidation.getOtherFieldOnPolicyList().get(1).getPolicies().size());
		Assert.assertEquals(policyNumber2, renameLegalValidation.getOtherFieldOnPolicyList().get(1).getPolicies().get(0).getPolicyNumber());

		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId2.toString());
		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId3.toString());

		// 3B. No other fields on same contract.
		renameLegalValidation = service.validateRenameLegal(referrer, annualFieldDetailId.toString(), "NEW LEGAL LOC", null);
		Assert.assertNotNull(renameLegalValidation);
		
		Assert.assertEquals(false, renameLegalValidation.getIsWarningOtherFieldOnPolicy());
		Assert.assertNull(renameLegalValidation.getOtherFieldOnPolicyMsg());
		Assert.assertNull(renameLegalValidation.getOtherFieldOnPolicyList());

		// Test 4: Fields on other policies.
		// Field on other policy in same year.
		createContractedFieldDetail(contractedFieldDetailId2, annualFieldDetailId2, gcyId3, 1);

		// Field on other policy in prior year.
		createContractedFieldDetail(contractedFieldDetailId3, annualFieldDetailId3, gcyId4, 1);
		
		// 4A. Fields on other policies.
		renameLegalValidation = service.validateRenameLegal(referrer, annualFieldDetailId.toString(), "NEW LEGAL LOC", null);
		Assert.assertNotNull(renameLegalValidation);

		Assert.assertEquals(true, renameLegalValidation.getIsWarningFieldOnOtherPolicy());
		Assert.assertEquals(fieldOnOtherPolicyMsg, renameLegalValidation.getFieldOnOtherPolicyMsg().getMessage());
		Assert.assertEquals(2, renameLegalValidation.getFieldOnOtherPolicyList().size());
		
		Assert.assertEquals(fieldId2, renameLegalValidation.getFieldOnOtherPolicyList().get(0).getFieldId());
		Assert.assertEquals("Field Label 2", renameLegalValidation.getFieldOnOtherPolicyList().get(0).getFieldLabel());
		Assert.assertEquals(1, renameLegalValidation.getFieldOnOtherPolicyList().get(0).getPolicies().size());
		Assert.assertEquals(policyNumber3, renameLegalValidation.getFieldOnOtherPolicyList().get(0).getPolicies().get(0).getPolicyNumber());

		Assert.assertEquals(fieldId3, renameLegalValidation.getFieldOnOtherPolicyList().get(1).getFieldId());
		Assert.assertEquals("Field Label 3", renameLegalValidation.getFieldOnOtherPolicyList().get(1).getFieldLabel());
		Assert.assertEquals(1, renameLegalValidation.getFieldOnOtherPolicyList().get(1).getPolicies().size());
		Assert.assertEquals(policyNumber4, renameLegalValidation.getFieldOnOtherPolicyList().get(1).getPolicies().get(0).getPolicyNumber());

		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId2.toString());
		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId3.toString());

		// 4B. No fields on other policies.
		renameLegalValidation = service.validateRenameLegal(referrer, annualFieldDetailId.toString(), "NEW LEGAL LOC", null);
		Assert.assertNotNull(renameLegalValidation);
		
		Assert.assertEquals(false, renameLegalValidation.getIsWarningFieldOnOtherPolicy());
		Assert.assertNull(renameLegalValidation.getFieldOnOtherPolicyMsg());
		Assert.assertNull(renameLegalValidation.getFieldOnOtherPolicyList());
		
		delete();

		logger.debug(">testValidateRenameLegalGrainForage");
	}
	
	@Test
	public void testValidateRenameLegalBerries() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testValidateRenameLegalBerries");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		//Set variables in warnings
		String legalLocationOrPid = "PID";
		String pidOrLegalLocation = "Legal Location";
		
		String legalsWithSameLocMsg = RenameLegalValidationRsrc.LEGALS_WITH_SAME_LOC_MSG.replace("[LegalLocationOrPID]", legalLocationOrPid);
		String otherLegalDataMsg = RenameLegalValidationRsrc.OTHER_LEGAL_DATA_MSG
									.replace("[PidOrLegalLocation]", pidOrLegalLocation)
									.replace("[LegalLocationOrPID]", legalLocationOrPid);
		String otherFieldOnPolicyMsg = RenameLegalValidationRsrc.OTHER_FIELD_ON_POLICY_MSG.replace("[LegalLocationOrPID]", legalLocationOrPid);
		String fieldOnOtherPolicyMsg = RenameLegalValidationRsrc.FIELD_ON_OTHER_POLICY_MSG.replace("[LegalLocationOrPID]", legalLocationOrPid);


		// Main Policy
		String policyNumber = "126334-22";
		Integer gcyId = 93912;

		// Same Contract as Main Policy, but prior crop year.
		String policyNumber2 = "126334-21";
		Integer gcyId2 = 90408;

		// Different Contract but same Crop Year as Main Policy.
		String policyNumber3 = "114421-22";
		Integer gcyId3 = 93738;

		// Different Contract, older Crop Year than Main Policy.
		String policyNumber4 = "114421-21";
		Integer gcyId4 = 90406;

		String primaryPropertyIdentifier = "164-861-756";
		String primaryPropertyIdentifier2 = "000-999-123";
		String primaryPropertyIdentifierValidate = "515-999-991";
		createLegalLand(null, null, primaryPropertyIdentifier, null, legalLandId);
		createField( fieldId, "Field Label", "Location", 2011, 2022);
		createAnnualFieldDetail(annualFieldDetailId, legalLandId, fieldId, 2022);
		createContractedFieldDetail(contractedFieldDetailId, annualFieldDetailId, gcyId, 1);

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
				1, 20);

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(1, searchResults.getCollection().size());

		UwContractRsrc referrer = searchResults.getCollection().get(0);
		
		// Test 1: Legal Location already exists.
		createLegalLand(null, null, primaryPropertyIdentifier2, null, legalLandId2);
		createLegalLand(null, null, primaryPropertyIdentifier2, null,  legalLandId3);
		
		// 1A. PID matches 2 legal lands.
		RenameLegalValidationRsrc renameLegalValidation = service.validateRenameLegal(referrer, annualFieldDetailId.toString(), null, primaryPropertyIdentifier2);
		Assert.assertNotNull(renameLegalValidation);

		Assert.assertEquals(true, renameLegalValidation.getIsWarningLegalsWithSameLoc());
		Assert.assertEquals(legalsWithSameLocMsg, renameLegalValidation.getLegalsWithSameLocMsg().getMessage());
		Assert.assertEquals(2, renameLegalValidation.getLegalsWithSameLocList().size());
		
		Assert.assertEquals(legalLandId2, renameLegalValidation.getLegalsWithSameLocList().get(0).getLegalLandId());
		Assert.assertEquals(legalLandId3, renameLegalValidation.getLegalsWithSameLocList().get(1).getLegalLandId());
		
		service.deleteLegalLandSync(topLevelEndpoints, legalLandId2.toString());
		service.deleteLegalLandSync(topLevelEndpoints, legalLandId3.toString());

		// 1B. PID matches 0 legal lands.
		renameLegalValidation = service.validateRenameLegal(referrer, annualFieldDetailId.toString(), null, primaryPropertyIdentifier2);
		Assert.assertNotNull(renameLegalValidation);

		Assert.assertEquals(false, renameLegalValidation.getIsWarningLegalsWithSameLoc());
		Assert.assertNull(renameLegalValidation.getLegalsWithSameLocMsg());
		Assert.assertNull(renameLegalValidation.getLegalsWithSameLocList());

		// Test 2: Non-default legal description, short legal description or other description.

		// 2A. Non-null legal description
		LegalLandRsrc ll = service.getLegalLand(findLegalLandResource(primaryPropertyIdentifier)); 
		ll.setLegalDescription("legal description");
		ll.setTransactionType(LandManagementEventTypes.LegalLandUpdated);
		
		service.synchronizeLegalLand(ll);
		
		renameLegalValidation = service.validateRenameLegal(referrer, annualFieldDetailId.toString(), null, primaryPropertyIdentifierValidate);
		Assert.assertNotNull(renameLegalValidation);
		
		Assert.assertEquals(true, renameLegalValidation.getIsWarningOtherLegalData());
		Assert.assertEquals(otherLegalDataMsg, renameLegalValidation.getOtherLegalDataMsg().getMessage());
		Assert.assertNotNull(renameLegalValidation.getOtherLegalData());
		Assert.assertEquals(legalLandId, renameLegalValidation.getOtherLegalData().getLegalLandId());
		Assert.assertEquals("legal description", renameLegalValidation.getOtherLegalData().getLegalDescription());
		Assert.assertNull(renameLegalValidation.getOtherLegalData().getLegalShortDescription());
		Assert.assertEquals(primaryPropertyIdentifier, renameLegalValidation.getOtherLegalData().getPrimaryPropertyIdentifier());
		
		// 2B. Non-null short legal description.
		ll = service.getLegalLand(findLegalLandResource(primaryPropertyIdentifier)); 
		ll.setLegalDescription(null);
		ll.setLegalShortDescription("Short Legal");
		ll.setTransactionType(LandManagementEventTypes.LegalLandUpdated);
		
		service.synchronizeLegalLand(ll);
		
		renameLegalValidation = service.validateRenameLegal(referrer, annualFieldDetailId.toString(), null, primaryPropertyIdentifierValidate);
		Assert.assertNotNull(renameLegalValidation);
		
		Assert.assertEquals(true, renameLegalValidation.getIsWarningOtherLegalData());
		Assert.assertEquals(otherLegalDataMsg, renameLegalValidation.getOtherLegalDataMsg().getMessage());
		Assert.assertNotNull(renameLegalValidation.getOtherLegalData());
		Assert.assertEquals(legalLandId, renameLegalValidation.getOtherLegalData().getLegalLandId());
		Assert.assertNull(renameLegalValidation.getOtherLegalData().getLegalDescription());
		Assert.assertEquals("Short Legal", renameLegalValidation.getOtherLegalData().getLegalShortDescription());
		Assert.assertEquals(primaryPropertyIdentifier, renameLegalValidation.getOtherLegalData().getPrimaryPropertyIdentifier());

		// 2C. Non-default PID
		ll = service.getLegalLand(findLegalLandResource(primaryPropertyIdentifier)); 
		ll.setLegalShortDescription(null);
		ll.setOtherDescription("other description");
		ll.setTransactionType(LandManagementEventTypes.LegalLandUpdated);
		
		service.synchronizeLegalLand(ll);

		renameLegalValidation = service.validateRenameLegal(referrer, annualFieldDetailId.toString(), null, primaryPropertyIdentifierValidate);
		Assert.assertNotNull(renameLegalValidation);
		
		Assert.assertEquals(true, renameLegalValidation.getIsWarningOtherLegalData());
		Assert.assertEquals(otherLegalDataMsg, renameLegalValidation.getOtherLegalDataMsg().getMessage());
		Assert.assertNotNull(renameLegalValidation.getOtherLegalData());
		Assert.assertEquals(legalLandId, renameLegalValidation.getOtherLegalData().getLegalLandId());
		Assert.assertNull(renameLegalValidation.getOtherLegalData().getLegalDescription());
		Assert.assertNull(renameLegalValidation.getOtherLegalData().getLegalShortDescription());
		Assert.assertEquals("other description", renameLegalValidation.getOtherLegalData().getOtherDescription());

		// 2D. All default values.
		ll = service.getLegalLand(findLegalLandResource(primaryPropertyIdentifier)); 
		ll.setOtherDescription(null);
		ll.setTransactionType(LandManagementEventTypes.LegalLandUpdated);
		
		service.synchronizeLegalLand(ll);
		
		renameLegalValidation = service.validateRenameLegal(referrer, annualFieldDetailId.toString(), null, primaryPropertyIdentifierValidate);
		Assert.assertNotNull(renameLegalValidation);
		
		Assert.assertEquals(false, renameLegalValidation.getIsWarningOtherLegalData());
		Assert.assertNull(renameLegalValidation.getOtherLegalDataMsg());
		Assert.assertNull(renameLegalValidation.getOtherLegalData());

		// Test 3: Other fields on same policy
		// Field on same policy
		createField( fieldId2, "Field Label 2", "Location 2", 2011, 2022);
		createAnnualFieldDetail(annualFieldDetailId2, legalLandId, fieldId2, 2022);
		createContractedFieldDetail(contractedFieldDetailId2, annualFieldDetailId2, gcyId, 1);

		// Field on same contract, older policy.
		createField( fieldId3, "Field Label 3", "Location 3", 2011, 2022);
		createAnnualFieldDetail(annualFieldDetailId3, legalLandId, fieldId3, 2022);
		createContractedFieldDetail(contractedFieldDetailId3, annualFieldDetailId3, gcyId2, 1);
		
		// 3A. Other fields on same contract.
		renameLegalValidation = service.validateRenameLegal(referrer, annualFieldDetailId.toString(), null, primaryPropertyIdentifierValidate);
		Assert.assertNotNull(renameLegalValidation);

		Assert.assertEquals(true, renameLegalValidation.getIsWarningOtherFieldOnPolicy());
		Assert.assertEquals(otherFieldOnPolicyMsg, renameLegalValidation.getOtherFieldOnPolicyMsg().getMessage());
		Assert.assertEquals(2, renameLegalValidation.getOtherFieldOnPolicyList().size());
		
		Assert.assertEquals(fieldId2, renameLegalValidation.getOtherFieldOnPolicyList().get(0).getFieldId());
		Assert.assertEquals("Field Label 2", renameLegalValidation.getOtherFieldOnPolicyList().get(0).getFieldLabel());
		Assert.assertEquals(1, renameLegalValidation.getOtherFieldOnPolicyList().get(0).getPolicies().size());
		Assert.assertEquals(policyNumber, renameLegalValidation.getOtherFieldOnPolicyList().get(0).getPolicies().get(0).getPolicyNumber());

		Assert.assertEquals(fieldId3, renameLegalValidation.getOtherFieldOnPolicyList().get(1).getFieldId());
		Assert.assertEquals("Field Label 3", renameLegalValidation.getOtherFieldOnPolicyList().get(1).getFieldLabel());
		Assert.assertEquals(1, renameLegalValidation.getOtherFieldOnPolicyList().get(1).getPolicies().size());
		Assert.assertEquals(policyNumber2, renameLegalValidation.getOtherFieldOnPolicyList().get(1).getPolicies().get(0).getPolicyNumber());

		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId2.toString());
		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId3.toString());

		// 3B. No other fields on same contract.
		renameLegalValidation = service.validateRenameLegal(referrer, annualFieldDetailId.toString(), null, primaryPropertyIdentifierValidate);
		Assert.assertNotNull(renameLegalValidation);
		
		Assert.assertEquals(false, renameLegalValidation.getIsWarningOtherFieldOnPolicy());
		Assert.assertNull(renameLegalValidation.getOtherFieldOnPolicyMsg());
		Assert.assertNull(renameLegalValidation.getOtherFieldOnPolicyList());

		// Test 4: Fields on other policies.
		// Field on other policy in same year.
		createContractedFieldDetail(contractedFieldDetailId2, annualFieldDetailId2, gcyId3, 1);

		// Field on other policy in prior year.
		createContractedFieldDetail(contractedFieldDetailId3, annualFieldDetailId3, gcyId4, 1);
		
		// 4A. Fields on other policies.
		renameLegalValidation = service.validateRenameLegal(referrer, annualFieldDetailId.toString(), null, primaryPropertyIdentifierValidate);
		Assert.assertNotNull(renameLegalValidation);

		Assert.assertEquals(true, renameLegalValidation.getIsWarningFieldOnOtherPolicy());
		Assert.assertEquals(fieldOnOtherPolicyMsg, renameLegalValidation.getFieldOnOtherPolicyMsg().getMessage());
		Assert.assertEquals(2, renameLegalValidation.getFieldOnOtherPolicyList().size());
		
		Assert.assertEquals(fieldId2, renameLegalValidation.getFieldOnOtherPolicyList().get(0).getFieldId());
		Assert.assertEquals("Field Label 2", renameLegalValidation.getFieldOnOtherPolicyList().get(0).getFieldLabel());
		Assert.assertEquals("Location 2", renameLegalValidation.getFieldOnOtherPolicyList().get(0).getFieldLocation());
		Assert.assertEquals(1, renameLegalValidation.getFieldOnOtherPolicyList().get(0).getPolicies().size());
		Assert.assertEquals(policyNumber3, renameLegalValidation.getFieldOnOtherPolicyList().get(0).getPolicies().get(0).getPolicyNumber());

		Assert.assertEquals(fieldId3, renameLegalValidation.getFieldOnOtherPolicyList().get(1).getFieldId());
		Assert.assertEquals("Field Label 3", renameLegalValidation.getFieldOnOtherPolicyList().get(1).getFieldLabel());
		Assert.assertEquals("Location 3", renameLegalValidation.getFieldOnOtherPolicyList().get(1).getFieldLocation());
		Assert.assertEquals(1, renameLegalValidation.getFieldOnOtherPolicyList().get(1).getPolicies().size());
		Assert.assertEquals(policyNumber4, renameLegalValidation.getFieldOnOtherPolicyList().get(1).getPolicies().get(0).getPolicyNumber());

		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId2.toString());
		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId3.toString());

		// 4B. No fields on other policies.
		renameLegalValidation = service.validateRenameLegal(referrer, annualFieldDetailId.toString(), null, primaryPropertyIdentifierValidate);
		Assert.assertNotNull(renameLegalValidation);
		
		Assert.assertEquals(false, renameLegalValidation.getIsWarningFieldOnOtherPolicy());
		Assert.assertNull(renameLegalValidation.getFieldOnOtherPolicyMsg());
		Assert.assertNull(renameLegalValidation.getFieldOnOtherPolicyList());
		
		delete();

		logger.debug(">testValidateRenameLegalBerries");
	}

	public LegalLandRsrc findLegalLandResource(String primaryPropertyIdentifier)
			throws CirrasUnderwritingServiceException {
		LegalLandListRsrc legalLandList = service.getLegalLandList(topLevelEndpoints, null, primaryPropertyIdentifier, null, null, "false", "false", null, null, null, null);
		Assert.assertNotNull(legalLandList);
		Assert.assertEquals("Legal Land Returned Records (exact)", 1, legalLandList.getCollection().size());
		return legalLandList.getCollection().get(0);
	}

}
