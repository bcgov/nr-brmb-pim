package ca.bc.gov.mal.cirras.underwriting.controllers;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.clients.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.clients.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.clients.ValidationException;
import ca.bc.gov.mal.cirras.underwriting.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.AnnualFieldDetailRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.AnnualFieldListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.AnnualFieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.FieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.LegalLandRsrc;
import ca.bc.gov.mal.cirras.underwriting.services.utils.LandManagementEventTypes;
import ca.bc.gov.mal.cirras.underwriting.test.EndpointsTest;
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
		String cropYear = "2024";
		String otherLegalDescription = "DL 9715";

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
		String fieldId = "25245";
		String cropYear = "2024";
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
		Assert.assertTrue(fields.size() > 0);
		
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
		createField(fieldId1, fieldLocation1);
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
		
		Assert.assertEquals("LegalLandId", legalLandId1, field.getLegalLandId());
		Assert.assertEquals("Primary PID", pid1, field.getPrimaryPropertyIdentifier());
		Assert.assertEquals("field Location", fieldLocation1, field.getFieldLocation());
		
		delete();
		
		logger.debug(">testSearchAnnualFieldsByFieldLocation");
	}	
	
	private Integer cropYear = 2021;

	private Integer legalLandId1 = 90040015;
	
	private String pid1 = "111-222-333";
	
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
		
		service.deleteField(topLevelEndpoints, fieldId1.toString());
		service.deleteLegalLandSync(topLevelEndpoints, legalLandId1.toString());
		
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
