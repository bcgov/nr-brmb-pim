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
import ca.bc.gov.mal.cirras.underwriting.data.resources.ReplaceLegalValidationRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.AnnualFieldDetailRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.AnnualFieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.ContractedFieldDetailRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.FieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.LegalLandFieldXrefRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.LegalLandRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.UwContractListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.UwContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.models.ReplaceLegalValidation;
import ca.bc.gov.mal.cirras.underwriting.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class UwContractValidateReplaceLegalEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(UwContractValidateReplaceLegalEndpointTest.class);


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

		deleteLegalLandFieldXref(legalLandId, fieldId);
		deleteLegalLandFieldXref(legalLandId2, fieldId);
		deleteLegalLandFieldXref(legalLandId3, fieldId);
		deleteLegalLandFieldXref(legalLandId2, fieldId2);
		deleteLegalLandFieldXref(legalLandId2, fieldId3);

		service.deleteField(topLevelEndpoints, fieldId.toString());
		service.deleteField(topLevelEndpoints, fieldId2.toString());
		service.deleteField(topLevelEndpoints, fieldId3.toString());

		service.deleteLegalLandSync(topLevelEndpoints, legalLandId.toString());
		service.deleteLegalLandSync(topLevelEndpoints, legalLandId2.toString());
		service.deleteLegalLandSync(topLevelEndpoints, legalLandId3.toString());
		
	}
	
	private void deleteLegalLandFieldXref(Integer legalLandId, Integer fieldId) throws CirrasUnderwritingServiceException {
		// delete legal land - field xref
		LegalLandFieldXrefRsrc llfx = service.getLegalLandFieldXref(topLevelEndpoints, legalLandId.toString(), fieldId.toString());
		if ( llfx != null ) { 
			service.deleteLegalLandFieldXref(topLevelEndpoints, legalLandId.toString(), fieldId.toString());
		}

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
	
	private void createField( Integer fieldId, String fieldLabel, Integer activeFromCropYear, Integer activeToCropYear, String fieldLocation) throws CirrasUnderwritingServiceException, ValidationException {

		FieldRsrc resource = new FieldRsrc();
		
		resource.setFieldId(fieldId);
		resource.setFieldLabel(fieldLabel);
		resource.setFieldLocation(fieldLocation);
		resource.setActiveFromCropYear(activeFromCropYear);
		resource.setActiveToCropYear(activeToCropYear);
		resource.setTransactionType(LandManagementEventTypes.FieldCreated);
		
		service.synchronizeField(resource);
	}
	
	private void createLegalLandFieldXref(Integer fieldId, Integer legalLandId) throws CirrasUnderwritingServiceException, ValidationException {
	
		//CREATE Legal Land - Field Xref
		LegalLandFieldXrefRsrc legalLandFieldXrefResource = new LegalLandFieldXrefRsrc();
		
		legalLandFieldXrefResource.setLegalLandId(legalLandId);
		legalLandFieldXrefResource.setFieldId(fieldId);
		legalLandFieldXrefResource.setTransactionType(LandManagementEventTypes.LegalLandFieldXrefCreated);

		service.synchronizeLegalLandFieldXref(legalLandFieldXrefResource);
	
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
	public void testValidateReplaceLegalGrainForage() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testValidateReplaceLegalGrainForage");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		// Main Policy
		String policyNumber1 = "712174-22";
		Integer gcyId1 = 95401;

		// Different Contract but same Crop Year as Main Policy.
		String policyNumber2 = "140160-22";
		Integer gcyId2 = 95123;

		String fieldLabel = "Field Label";
		String fieldLocation = "field location 1";
		String legalLocation2 = "test legal 1234 22";
		String legalLocationOrPid = "Legal Location";
		
		//Create a field and associate it with a policy
		createLegalLand("TEST LEGAL LOC 123", null, "GF0099999", null, legalLandId);
		createField( fieldId, fieldLabel, 2011, 2022, fieldLocation);
		createLegalLandFieldXref(fieldId, legalLandId);
		createAnnualFieldDetail(annualFieldDetailId, legalLandId, fieldId, 2022);
		createContractedFieldDetail(contractedFieldDetailId, annualFieldDetailId, gcyId1, 1);
		
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

		//Associate field with a second policy 
		createContractedFieldDetail(contractedFieldDetailId2, annualFieldDetailId, gcyId2, 1);
		
		// 1. Field on multiple policies
		ReplaceLegalValidationRsrc replaceLegalValidation = service.validateReplaceLegal(referrer, annualFieldDetailId.toString(), fieldLabel, legalLandId.toString(), null);
		Assert.assertNotNull(replaceLegalValidation);

		String fieldOnOtherPolicyMsg = ReplaceLegalValidation.FIELD_ON_OTHER_POLICY_MSG
				.replace("[fieldLocationOrfieldLabel]", fieldLabel)
				.replace("[fieldId]", fieldId.toString())
				.replace("[policyNumber]", policyNumber2);

		Assert.assertEquals(true, replaceLegalValidation.getIsWarningFieldOnOtherPolicy());
		Assert.assertEquals(fieldOnOtherPolicyMsg, replaceLegalValidation.getFieldOnOtherPolicyMsg().getMessage());

		// 2. Multiple legal location associated with the field
		createLegalLand(legalLocation2, "Legal Description", "111-222-333", "Short Legal", legalLandId2);
		createLegalLand("test legal 1234", "Legal Description 2", "111-222-333", "Short Legal", legalLandId3);
		createLegalLandFieldXref(fieldId, legalLandId2);
		createLegalLandFieldXref(fieldId, legalLandId3);

		replaceLegalValidation = service.validateReplaceLegal(referrer, annualFieldDetailId.toString(), fieldLabel, legalLandId.toString(), null);
		Assert.assertNotNull(replaceLegalValidation);
		
		String fieldHasOtherLegalLandMsg = ReplaceLegalValidation.FIELD_HAS_OTHER_LEGAL_MSG
				.replace("[legalLocationOrPid]", legalLocationOrPid)
				.replace("[fieldLocationOrfieldLabel]", fieldLabel)
				.replace("[fieldId]", fieldId.toString());
		
		Assert.assertEquals(true, replaceLegalValidation.getIsWarningFieldHasOtherLegalLand());
		Assert.assertEquals(fieldHasOtherLegalLandMsg, replaceLegalValidation.getFieldHasOtherLegalLandMsg().getMessage());
		Assert.assertEquals(2, replaceLegalValidation.getOtherLegalLandOfFieldList().size());
		
		Assert.assertEquals(legalLandId2, replaceLegalValidation.getOtherLegalLandOfFieldList().get(0).getLegalLandId());
		Assert.assertEquals(legalLandId3, replaceLegalValidation.getOtherLegalLandOfFieldList().get(1).getLegalLandId());
		
		// 3. Multiple fields on legal land
		createField( fieldId2, "Field Label 2", 2011, 2022, "Field Location 2");
		createField( fieldId3, "Field Label 3", 2011, 2022, "Field Location 3");
		createLegalLandFieldXref(fieldId2, legalLandId2);
		createLegalLandFieldXref(fieldId3, legalLandId2);
		
		replaceLegalValidation = service.validateReplaceLegal(referrer, annualFieldDetailId.toString(), fieldLabel, legalLandId2.toString(), null);
		Assert.assertNotNull(replaceLegalValidation);

		String otherFieldsOnLegalMsg = ReplaceLegalValidation.OTHER_FIELD_ON_LEGAL_MSG
											.replace("[legalLocationOrPid]", legalLocationOrPid)
											.replace("[otherDescriptionOrPid]", legalLocation2);
		
		Assert.assertEquals(true, replaceLegalValidation.getIsWarningOtherFieldsOnLegal());
		Assert.assertEquals(otherFieldsOnLegalMsg, replaceLegalValidation.getOtherFieldsOnLegalMsg().getMessage());
		Assert.assertEquals(2, replaceLegalValidation.getOtherFieldsOnLegalLandList().size());
		
		Assert.assertEquals(fieldId2, replaceLegalValidation.getOtherFieldsOnLegalLandList().get(0).getFieldId());
		Assert.assertEquals(fieldId3, replaceLegalValidation.getOtherFieldsOnLegalLandList().get(1).getFieldId());

		// 4. new legal land  (no check on other fields on legal land) - endpoint accepts null/empty string
		replaceLegalValidation = service.validateReplaceLegal(referrer, annualFieldDetailId.toString(), fieldLabel, "", null);
		Assert.assertNotNull(replaceLegalValidation);

		Assert.assertEquals(false, replaceLegalValidation.getIsWarningOtherFieldsOnLegal());
		Assert.assertNull(replaceLegalValidation.getOtherFieldsOnLegalMsg());
		Assert.assertNull(replaceLegalValidation.getOtherFieldsOnLegalLandList());
		
		delete();

		logger.debug(">testValidateReplaceLegalGrainForage");
	}
	
	@Test
	public void testValidateReplaceLegalBerries() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testValidateReplaceLegalBerries");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		// Main Policy
		String policyNumber1 = "126334-22";
		Integer gcyId1 = 93912;

		// Same Contract as Main Policy, but prior crop year.
		String policyNumber2 = "126334-21";
		Integer gcyId2 = 90408;


		String fieldLocation = "field location 1";
		String pid = "125-943-844";
		String legalLocationOrPid = "PID";
		
		//Create a field and associate it with a policy
		createLegalLand("TEST LEGAL LOC 123", null, "GF0099999", null, legalLandId);
		createField( fieldId, "Field Label 1", 2011, 2022, fieldLocation);
		createLegalLandFieldXref(fieldId, legalLandId);
		createAnnualFieldDetail(annualFieldDetailId, legalLandId, fieldId, 2022);
		createContractedFieldDetail(contractedFieldDetailId, annualFieldDetailId, gcyId1, 1);
		
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

		//Associate field with a second policy 
		createContractedFieldDetail(contractedFieldDetailId2, annualFieldDetailId, gcyId2, 1);
		
		// 1. Field on multiple policies
		ReplaceLegalValidationRsrc replaceLegalValidation = service.validateReplaceLegal(referrer, annualFieldDetailId.toString(), null, legalLandId.toString(), fieldLocation);
		Assert.assertNotNull(replaceLegalValidation);

		String fieldOnOtherPolicyMsg = ReplaceLegalValidation.FIELD_ON_OTHER_POLICY_MSG
				.replace("[fieldLocationOrfieldLabel]", fieldLocation)
				.replace("[fieldId]", fieldId.toString())
				.replace("[policyNumber]", policyNumber2);

		Assert.assertEquals(true, replaceLegalValidation.getIsWarningFieldOnOtherPolicy());
		Assert.assertEquals(fieldOnOtherPolicyMsg, replaceLegalValidation.getFieldOnOtherPolicyMsg().getMessage());

		// 2. Multiple legal location associated with the field
		createLegalLand("test legal 55", "Legal Description", pid, "Short Legal", legalLandId2);
		createLegalLand("test legal 1234", "Legal Description 2", pid, "Short Legal", legalLandId3);
		createLegalLandFieldXref(fieldId, legalLandId2);
		createLegalLandFieldXref(fieldId, legalLandId3);

		replaceLegalValidation = service.validateReplaceLegal(referrer, annualFieldDetailId.toString(), null, legalLandId.toString(), fieldLocation);
		Assert.assertNotNull(replaceLegalValidation);
		
		String fieldHasOtherLegalLandMsg = ReplaceLegalValidation.FIELD_HAS_OTHER_LEGAL_MSG
				.replace("[legalLocationOrPid]", legalLocationOrPid)
				.replace("[fieldLocationOrfieldLabel]", fieldLocation)
				.replace("[fieldId]", fieldId.toString());
		
		Assert.assertEquals(true, replaceLegalValidation.getIsWarningFieldHasOtherLegalLand());
		Assert.assertEquals(fieldHasOtherLegalLandMsg, replaceLegalValidation.getFieldHasOtherLegalLandMsg().getMessage());
		Assert.assertEquals(2, replaceLegalValidation.getOtherLegalLandOfFieldList().size());
		
		Assert.assertEquals(legalLandId2, replaceLegalValidation.getOtherLegalLandOfFieldList().get(0).getLegalLandId());
		Assert.assertEquals(legalLandId3, replaceLegalValidation.getOtherLegalLandOfFieldList().get(1).getLegalLandId());
		
		// 3. Multiple fields on legal land
		String fieldLocation2 = "field location 2";
		String fieldLocation3 = "field location 3";

		createField( fieldId2, "Field Label 2", 2011, 2022, fieldLocation2);
		createField( fieldId3, "Field Label 3", 2011, 2022, fieldLocation3);
		createLegalLandFieldXref(fieldId2, legalLandId2);
		createLegalLandFieldXref(fieldId3, legalLandId2);
		
		replaceLegalValidation = service.validateReplaceLegal(referrer, annualFieldDetailId.toString(), null, legalLandId2.toString(), fieldLocation);
		Assert.assertNotNull(replaceLegalValidation);

		String otherFieldsOnLegalMsg = ReplaceLegalValidation.OTHER_FIELD_ON_LEGAL_MSG
											.replace("[legalLocationOrPid]", legalLocationOrPid)
											.replace("[otherDescriptionOrPid]", pid);
		
		Assert.assertEquals(true, replaceLegalValidation.getIsWarningOtherFieldsOnLegal());
		Assert.assertEquals(otherFieldsOnLegalMsg, replaceLegalValidation.getOtherFieldsOnLegalMsg().getMessage());
		Assert.assertEquals(2, replaceLegalValidation.getOtherFieldsOnLegalLandList().size());
		
		for (AnnualFieldRsrc field : replaceLegalValidation.getOtherFieldsOnLegalLandList()) {
			//Make sure field location is not empty
			Assert.assertNotNull(field.getFieldLocation());
		}
		
		Assert.assertEquals(fieldId2, replaceLegalValidation.getOtherFieldsOnLegalLandList().get(0).getFieldId());
		Assert.assertEquals(fieldId3, replaceLegalValidation.getOtherFieldsOnLegalLandList().get(1).getFieldId());

		// 4. new legal land  (no check on other fields on legal land) - endpoint accepts null/empty string
		replaceLegalValidation = service.validateReplaceLegal(referrer, annualFieldDetailId.toString(), null, "", fieldLocation);
		Assert.assertNotNull(replaceLegalValidation);

		Assert.assertEquals(false, replaceLegalValidation.getIsWarningOtherFieldsOnLegal());
		Assert.assertNull(replaceLegalValidation.getOtherFieldsOnLegalMsg());
		Assert.assertNull(replaceLegalValidation.getOtherFieldsOnLegalLandList());
		
		delete();

		logger.debug(">testValidateReplaceLegalBerries");
	}

}
