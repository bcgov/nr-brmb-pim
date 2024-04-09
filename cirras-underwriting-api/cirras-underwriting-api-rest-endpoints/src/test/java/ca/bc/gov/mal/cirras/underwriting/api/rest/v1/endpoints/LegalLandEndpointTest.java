package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints;

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
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.FieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandFieldXrefRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.RiskAreaListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.RiskAreaRsrc;
import ca.bc.gov.mal.cirras.underwriting.model.v1.LegalLandRiskArea;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.LandManagementEventTypes;


public class LegalLandEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(LegalLandEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.GET_LAND,
		Scopes.GET_LEGAL_LAND,
		Scopes.CREATE_LEGAL_LAND,
		Scopes.UPDATE_LEGAL_LAND,
		Scopes.DELETE_LEGAL_LAND,
		Scopes.GET_CODE_TABLES,
		Scopes.UPDATE_SYNC_UNDERWRITING,
		Scopes.DELETE_SYNC_UNDERWRITING
	};
	
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;
	private String otherDescription = "Other Description 32321";
	private Integer fieldId1 = 90000156;
	private Integer fieldId2 = 90000157;
	private Integer fieldId3 = 90000158;
	private Integer legalLandId = null;
	private Integer legalLandId2 = 999977999;


	
	@Before
	public void prepareTests() throws CirrasUnderwritingServiceException, Oauth2ClientException, NotFoundDaoException, DaoException{
		service = getService(SCOPES);
		topLevelEndpoints = service.getTopLevelEndpoints();
	}
	
	@After 
	public void cleanUp() throws CirrasUnderwritingServiceException, NotFoundDaoException, DaoException {
		deleteFields();
	}

	private void deleteFields() throws CirrasUnderwritingServiceException {
		
		LegalLandListRsrc legalLandList = service.getLegalLandList(topLevelEndpoints, otherDescription, null, null, null, "false", "false", null, null, null, null);

		//Only delete if the original legal land still exists. If it doesn't exist anymore the data has already been deleted
		if(legalLandList.getCollection().size() > 0) {
			
			// delete legal land - field xref
			service.deleteLegalLandFieldXref(topLevelEndpoints, legalLandId.toString(), fieldId1.toString());
			service.deleteLegalLandFieldXref(topLevelEndpoints, legalLandId.toString(), fieldId2.toString());
			service.deleteLegalLandFieldXref(topLevelEndpoints, legalLandId.toString(), fieldId3.toString());
			service.deleteLegalLandFieldXref(topLevelEndpoints, legalLandId2.toString(), fieldId3.toString());
			
			service.deleteLegalLandSync(topLevelEndpoints, legalLandId2.toString());
			
			// delete field				
			service.deleteField(topLevelEndpoints, fieldId1.toString());
			service.deleteField(topLevelEndpoints, fieldId2.toString());
			service.deleteField(topLevelEndpoints, fieldId3.toString());
			
		}
	}
	
	@Test
	public void testCreateUpdateDeleteLegalLand() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testCreateUpdateDeleteLegalLand");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		String primaryPropertyIdentifier = "111-222-333";
		String primaryReferenceTypeCode = "OTHER";
		String legalDescription = "Legal Description";
		String legalShortDescription = "Short Legal";
		Integer activeFromCropYear = 2011;
		Integer activeToCropYear = 2022;
		Double totalAcres = 12.3;
		String primaryLandIdentifierTypeCode = "PID";

		//CREATE Legal Land
		LegalLandRsrc resource = new LegalLandRsrc();
		
		resource.setLegalLandId(null);
		resource.setPrimaryPropertyIdentifier(primaryPropertyIdentifier);
		resource.setPrimaryReferenceTypeCode(primaryReferenceTypeCode);
		resource.setLegalDescription(legalDescription);
		resource.setLegalShortDescription(legalShortDescription);
		resource.setOtherDescription(otherDescription);
		resource.setActiveFromCropYear(activeFromCropYear);
		resource.setActiveToCropYear(activeToCropYear);
		resource.setTransactionType(LandManagementEventTypes.LegalLandCreated);
		resource.setTotalAcres(totalAcres);
		resource.setPrimaryLandIdentifierTypeCode(primaryLandIdentifierTypeCode);
		
		addRiskAreas(resource);

		LegalLandRsrc fetchedResource = service.createLegalLand(topLevelEndpoints, resource);
		
		Assert.assertNotNull("LegalLandId 1", fetchedResource.getLegalLandId());
		Assert.assertEquals("PrimaryPropertyIdentifier", resource.getPrimaryPropertyIdentifier(), fetchedResource.getPrimaryPropertyIdentifier());
		Assert.assertEquals("PrimaryReferenceTypeCode 1", resource.getPrimaryReferenceTypeCode(), fetchedResource.getPrimaryReferenceTypeCode());
		Assert.assertEquals("LegalDescription 1", resource.getLegalDescription(), fetchedResource.getLegalDescription());
		Assert.assertEquals("LegalShortDescription 1", resource.getLegalShortDescription(), fetchedResource.getLegalShortDescription());
		Assert.assertEquals("OtherDescription 1", resource.getOtherDescription(), fetchedResource.getOtherDescription());
		Assert.assertEquals("ActiveFromCropYear 1", resource.getActiveFromCropYear(), fetchedResource.getActiveFromCropYear());
		Assert.assertEquals("ActiveToCropYear 1", resource.getActiveToCropYear(), fetchedResource.getActiveToCropYear());
		Assert.assertEquals("TotalAcres", resource.getTotalAcres(), fetchedResource.getTotalAcres());
		Assert.assertEquals("PrimaryLandIdentifierTypeCode", resource.getPrimaryLandIdentifierTypeCode(), fetchedResource.getPrimaryLandIdentifierTypeCode());
		Assert.assertEquals("RiskAreas", 3, fetchedResource.getRiskAreas().size());
		Assert.assertEquals("Fields", 0, fetchedResource.getFields().size());
		checkRiskAreas(fetchedResource.getLegalLandId(), fetchedResource.getRiskAreas());
		
		legalLandId = fetchedResource.getLegalLandId();

		addAssociatedFields(resource);
		
		//Get legal land again so that the preconditions don't fail on update
		fetchedResource = service.getLegalLand(fetchedResource);

		//UPDATE
		primaryPropertyIdentifier = "999-999-999";
		primaryReferenceTypeCode = "LEGAL";
		legalDescription = "Legal Description 2";
		legalShortDescription = "Short Legal 2";
		otherDescription = "Other Description 215151";
		activeFromCropYear = 2000;
		activeToCropYear = 2011;
		totalAcres = 55.55;
		primaryLandIdentifierTypeCode = "PIN";

		fetchedResource.setPrimaryPropertyIdentifier(primaryPropertyIdentifier);
		fetchedResource.setPrimaryReferenceTypeCode(primaryReferenceTypeCode);
		fetchedResource.setLegalDescription(legalDescription);
		fetchedResource.setLegalShortDescription(legalShortDescription);
		fetchedResource.setOtherDescription(otherDescription);
		fetchedResource.setActiveFromCropYear(activeFromCropYear);
		fetchedResource.setActiveToCropYear(activeToCropYear);
		fetchedResource.setTotalAcres(totalAcres);
		fetchedResource.setPrimaryLandIdentifierTypeCode(primaryLandIdentifierTypeCode);
		fetchedResource.getRiskAreas().get(0).setDeletedByUserInd(true); //Remove 1 risk area
		
		LegalLandRsrc updatedResource = service.updateLegalLand(fetchedResource);

		Assert.assertEquals("LegalLandId 2", legalLandId, updatedResource.getLegalLandId());
		Assert.assertEquals("PrimaryPropertyIdentifier 2", fetchedResource.getPrimaryPropertyIdentifier(), updatedResource.getPrimaryPropertyIdentifier());		
		Assert.assertEquals("PrimaryReferenceTypeCode 2", fetchedResource.getPrimaryReferenceTypeCode(), updatedResource.getPrimaryReferenceTypeCode());
		Assert.assertEquals("LegalDescription 2", fetchedResource.getLegalDescription(), updatedResource.getLegalDescription());
		Assert.assertEquals("LegalShortDescription 2", fetchedResource.getLegalShortDescription(), updatedResource.getLegalShortDescription());
		Assert.assertEquals("OtherDescription 2", fetchedResource.getOtherDescription(), updatedResource.getOtherDescription());
		Assert.assertEquals("ActiveFromCropYear 2", fetchedResource.getActiveFromCropYear(), updatedResource.getActiveFromCropYear());
		Assert.assertEquals("ActiveToCropYear 2", fetchedResource.getActiveToCropYear(), updatedResource.getActiveToCropYear());
		Assert.assertEquals("TotalAcres 2", fetchedResource.getTotalAcres(), updatedResource.getTotalAcres());
		Assert.assertEquals("PrimaryLandIdentifierTypeCode 2", fetchedResource.getPrimaryLandIdentifierTypeCode(), updatedResource.getPrimaryLandIdentifierTypeCode());
		Assert.assertEquals("RiskAreas 2", 2, updatedResource.getRiskAreas().size());
		Assert.assertEquals("Fields 2", 3, fetchedResource.getFields().size());
		checkFields(fetchedResource.getFields());

		//Delete fields and mapping records
		deleteFields();

		LegalLandRsrc getResource = service.getLegalLand(updatedResource);
		Assert.assertNotNull(getResource);

		//DELETE
		service.deleteLegalLand(getResource);
		
		LegalLandListRsrc legalLandList = service.getLegalLandList(topLevelEndpoints, otherDescription, null, null, null, "false", "false", null, null, null, null);
		Assert.assertNotNull(legalLandList);
		Assert.assertEquals("Legal Land Returned Records (exact)", 0, legalLandList.getCollection().size());

		logger.debug(">testCreateUpdateDeleteLegalLand");
	}

	private void checkFields(List<FieldRsrc> fields) {
		for(FieldRsrc field : fields) {
			Assert.assertNotNull(field.getFieldId());
			Assert.assertNotNull(field.getFieldLabel());
			Assert.assertNotNull(field.getTotalLegalLand());
			if(field.getFieldId().equals(fieldId3)) {
				Assert.assertEquals(2, field.getTotalLegalLand().intValue());
			} else {
				Assert.assertEquals(1, field.getTotalLegalLand().intValue());
			}
		}
	}

	private void checkRiskAreas(Integer legalLandId, List<LegalLandRiskArea> riskAreas) {
		for(LegalLandRiskArea ra : riskAreas) {
			Assert.assertNotNull(ra.getRiskAreaId());
			Assert.assertNotNull(ra.getRiskAreaName());
			Assert.assertNotNull(ra.getInsurancePlanId());
			Assert.assertNotNull(ra.getInsurancePlanName());
			Assert.assertEquals(legalLandId, ra.getLegalLandId());
		}
	}

	private void addRiskAreas(LegalLandRsrc resource) throws CirrasUnderwritingServiceException {
		
		RiskAreaListRsrc searchResults = service.getRiskAreaList(topLevelEndpoints, null);

		Assert.assertNotNull(searchResults);
		Assert.assertTrue(searchResults.getCollection().size() >= 3);
		
		RiskAreaRsrc ra0 = searchResults.getCollection().get(0);
		RiskAreaRsrc ra1 = searchResults.getCollection().get(1);
		RiskAreaRsrc ra2 = searchResults.getCollection().get(2);

		List<LegalLandRiskArea> riskAreas = resource.getRiskAreas();
		
		createLegalLandRiskArea(riskAreas, ra0);
		createLegalLandRiskArea(riskAreas, ra1);
		createLegalLandRiskArea(riskAreas, ra2);
		
		resource.setRiskAreas(riskAreas);
		
	}

	private void createLegalLandRiskArea(List<LegalLandRiskArea> raList, RiskAreaRsrc ra0) {
		LegalLandRiskArea model = new LegalLandRiskArea();
		model.setRiskAreaId(ra0.getRiskAreaId());
		raList.add(model);
	}
	
	private void addAssociatedFields(LegalLandRsrc resource) throws CirrasUnderwritingServiceException, ValidationException, DaoException {
		
		createField(fieldId1);
		createField(fieldId2);
		createField(fieldId3);
		
		createLegalLand(legalLandId2);
		
		createLegalLandFieldXref(legalLandId, fieldId1);
		createLegalLandFieldXref(legalLandId, fieldId2);
		createLegalLandFieldXref(legalLandId, fieldId3);
		createLegalLandFieldXref(legalLandId2, fieldId3);
		
	}
	
	private void createLegalLandFieldXref(Integer legalLandId, Integer fieldId) throws CirrasUnderwritingServiceException, ValidationException {
                        
        //CREATE Legal Land - Field Xref
        LegalLandFieldXrefRsrc legalLandFieldXrefResource = new LegalLandFieldXrefRsrc();
        
        legalLandFieldXrefResource.setLegalLandId(legalLandId);
        legalLandFieldXrefResource.setFieldId(fieldId);
                
        legalLandFieldXrefResource.setTransactionType(LandManagementEventTypes.LegalLandFieldXrefCreated);
        service.synchronizeLegalLandFieldXref(legalLandFieldXrefResource);       
		
	}

	private void createField(Integer fieldId) throws CirrasUnderwritingServiceException, ValidationException {

		FieldRsrc resource = new FieldRsrc();
		
		resource.setFieldId(fieldId);
		resource.setFieldLabel("Field Label");
		resource.setActiveFromCropYear(2011);
		resource.setActiveToCropYear(2035);
		resource.setTransactionType(LandManagementEventTypes.FieldCreated);
		
		service.synchronizeField(resource);
		
	}
	
	private void createLegalLand(Integer llId) throws DaoException, CirrasUnderwritingServiceException, ValidationException {
		
		//CREATE LegalLand
		LegalLandRsrc resource = new LegalLandRsrc();
		
		resource.setLegalLandId(llId);
		resource.setPrimaryPropertyIdentifier("123-128-976");
		resource.setPrimaryReferenceTypeCode("OTHER");
		resource.setLegalDescription("legal test");
		resource.setLegalShortDescription("Short Legal");
		resource.setOtherDescription("Other Description");
		resource.setActiveFromCropYear(2011);
		resource.setActiveToCropYear(2039);
		resource.setPrimaryLandIdentifierTypeCode(null);
		resource.setTotalAcres(null);
		resource.setTransactionType(LandManagementEventTypes.LegalLandCreated);
		service.synchronizeLegalLand(resource);

	}
	
}
