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
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldDetailRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.FieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandFieldXrefRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandRsrc;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.LandManagementEventTypes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class AnnualFieldListEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(AnnualFieldListEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.SEARCH_ANNUAL_FIELDS,
		Scopes.CREATE_SYNC_UNDERWRITING,
		Scopes.UPDATE_SYNC_UNDERWRITING,
		Scopes.DELETE_SYNC_UNDERWRITING,
		Scopes.GET_LAND
	};
	

	@Test
	public void testSearchAnnualFieldsByLegalLand() throws CirrasUnderwritingServiceException, Oauth2ClientException {
		logger.debug("<testSearchAnnualFieldsByLegalLand");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasUnderwritingService service = getService(SCOPES);

		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();
		Integer legalLandId = 1000013;
		String cropYear = "2023";
		String otherLegalDescription = "NW 11 22 33";

		AnnualFieldListRsrc searchResults = service.getAnnualFieldList(
				topLevelEndpoints, 
				legalLandId.toString(), 
				null,
				null,
				cropYear);

		Assert.assertNotNull(searchResults);
		
		List<AnnualFieldRsrc> fields = searchResults.getCollection();
		Assert.assertNotNull(fields);
		Assert.assertFalse(fields.isEmpty());
		
		for (AnnualFieldRsrc resource: fields) {
			Assert.assertNotNull(resource.getPolicies());
			Assert.assertEquals("LegalLandId", legalLandId, resource.getLegalLandId());
			Assert.assertEquals("OtherLegalDescription", otherLegalDescription, resource.getOtherLegalDescription());
		}
		
		logger.debug(">testSearchAnnualFieldsByLegalLand");
	}

	@Test
	public void testSearchAnnualFieldsByField() throws CirrasUnderwritingServiceException, Oauth2ClientException {
		logger.debug("<testSearchAnnualFieldsByField");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasUnderwritingService service = getService(SCOPES);

		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();
		Integer legalLandId = 8129;
		String fieldId = "22173";
		String cropYear = "2022";
		String otherLegalDescription = "NE SEC 19 TP 86 RNG 19 W 6TH M";

		AnnualFieldListRsrc searchResults = service.getAnnualFieldList(
				topLevelEndpoints, 
				null,
				fieldId,
				null,
				cropYear);

		Assert.assertNotNull(searchResults);
		
		List<AnnualFieldRsrc> fields = searchResults.getCollection();
		Assert.assertNotNull(fields);
		
		for (AnnualFieldRsrc resource: fields) {
			Assert.assertNotNull(resource.getPolicies());
			Assert.assertEquals("LegalLandId", legalLandId, resource.getLegalLandId());
			Assert.assertEquals("OtherLegalDescription", otherLegalDescription, resource.getOtherLegalDescription());
		}
		
		logger.debug(">testSearchAnnualFieldsByField");
	}
	
	@Test
	public void testSearchAnnualFieldsByFieldLocation() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testSearchAnnualFieldsByFieldLocation");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		createLegalLand(legalLandId1, pid1);
		createLegalLand(legalLandId2, pid2);
		createLegalLand(legalLandId3, pid3);
		createField(fieldId1, fieldLocation1);
		createLegalLandFieldXref(legalLandId1, fieldId1);
		createLegalLandFieldXref(legalLandId2, fieldId1);
		createLegalLandFieldXref(legalLandId3, fieldId1);
		createAnnualFieldDetail(annualFieldDetailId1, legalLandId1, fieldId1, cropYear);

		AnnualFieldListRsrc searchResults = service.getAnnualFieldList(
				topLevelEndpoints, 
				null,
				null,
				fieldLocation1,
				cropYear.toString());

		Assert.assertNotNull(searchResults);
		
		List<AnnualFieldRsrc> fields = searchResults.getCollection();
		Assert.assertNotNull(fields);
		Assert.assertEquals(1, fields.size());
		
		AnnualFieldRsrc field = fields.get(0);
		
		String associatedPids = pid2 + ", " + pid3;
		
		Assert.assertEquals("LegalLandId", legalLandId1, field.getLegalLandId());
		Assert.assertEquals("Primary PID", pid1, field.getPrimaryPropertyIdentifier());
		Assert.assertEquals("field Location", fieldLocation1, field.getFieldLocation());
		Assert.assertEquals("PIDs", associatedPids, field.getAssociatedPropertyIdentifiers());
		
		delete();
		
		logger.debug(">testSearchAnnualFieldsByFieldLocation");
	}	
	
	private Integer cropYear = 2021;

	private Integer legalLandId1 = 90040015;
	private Integer legalLandId2 = 90040016;
	private Integer legalLandId3 = 90050016;
	
	private String pid1 = "111-222-333";
	private String pid2 = "999-222-333";
	private String pid3 = "687-555-444";
	
	private Integer fieldId1 = 90047016;
	private String fieldLocation1 = "Field Location";
	
	
	private Integer annualFieldDetailId1 = 90050017;
	private Integer annualFieldDetailId2 = 92050018;


	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;

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

	
	private void delete() throws CirrasUnderwritingServiceException {

		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId1.toString());
		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId2.toString());
		
		deleteLegalLandFieldXref(legalLandId1, fieldId1);
		deleteLegalLandFieldXref(legalLandId2, fieldId1);
		deleteLegalLandFieldXref(legalLandId3, fieldId1);

		service.deleteField(topLevelEndpoints, fieldId1.toString());
		service.deleteLegalLandSync(topLevelEndpoints, legalLandId1.toString());
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
	
	private void createLegalLand(Integer legalLandId, String pid) throws CirrasUnderwritingServiceException, ValidationException {
		
		//CREATE LegalLand
		LegalLandRsrc resource = new LegalLandRsrc();
		
		resource.setLegalLandId(legalLandId);
		resource.setPrimaryPropertyIdentifier(pid);
		resource.setPrimaryReferenceTypeCode("OTHER");
		resource.setLegalDescription(null);
		resource.setLegalShortDescription(null);
		resource.setOtherDescription("TEST LEGAL LOC 123");
		resource.setActiveFromCropYear(2011);
		resource.setActiveToCropYear(2030);
		resource.setTransactionType(LandManagementEventTypes.LegalLandCreated);
		
		service.synchronizeLegalLand(resource);

	}
	
	private void createField(Integer fieldId, String fieldLocation) throws CirrasUnderwritingServiceException, ValidationException {

		FieldRsrc resource = new FieldRsrc();
		
		resource.setFieldId(fieldId);
		resource.setFieldLabel("Field Label");
		resource.setFieldLocation(fieldLocation);
		resource.setActiveFromCropYear(2011);
		resource.setActiveToCropYear(2030);
		resource.setTransactionType(LandManagementEventTypes.FieldCreated);
		
		service.synchronizeField(resource);
	}
	
	private void createLegalLandFieldXref(Integer legalLandId, Integer fieldId) throws CirrasUnderwritingServiceException, ValidationException {
		//CREATE Legal Land - Field Xref
		LegalLandFieldXrefRsrc legalLandFieldXrefResource = new LegalLandFieldXrefRsrc();
		
		legalLandFieldXrefResource.setLegalLandId(legalLandId);
		legalLandFieldXrefResource.setFieldId(fieldId);
		legalLandFieldXrefResource.setTransactionType(LandManagementEventTypes.LegalLandFieldXrefCreated);

		service.synchronizeLegalLandFieldXref(legalLandFieldXrefResource);
	}
	
	private void createAnnualFieldDetail(Integer annualFieldDetailId, Integer legalLandId, Integer fieldId, Integer cropYear) throws CirrasUnderwritingServiceException, ValidationException {

		AnnualFieldDetailRsrc resource = new AnnualFieldDetailRsrc();
		
		resource.setAnnualFieldDetailId(annualFieldDetailId);
		resource.setLegalLandId(legalLandId);
		resource.setFieldId(fieldId);
		resource.setCropYear(cropYear);
		resource.setTransactionType(LandManagementEventTypes.AnnualFieldDetailCreated);
		
		service.synchronizeAnnualFieldDetail(resource);
	}
}
