package ca.bc.gov.mal.cirras.underwriting.controllers;

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
import ca.bc.gov.mal.cirras.underwriting.data.resources.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.LegalLandListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.LegalLandRsrc;
import ca.bc.gov.mal.cirras.underwriting.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;
import ca.bc.gov.mal.cirras.underwriting.services.utils.LandManagementEventTypes;


public class LegalLandSyncEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(LegalLandSyncEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.UPDATE_SYNC_UNDERWRITING,
		Scopes.DELETE_SYNC_UNDERWRITING,
		Scopes.GET_LAND,
		Scopes.GET_LEGAL_LAND
	};
	
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;
	private Integer legalLandId = 999999999;
	
	@Before
	public void prepareTests() throws CirrasUnderwritingServiceException, Oauth2ClientException, NotFoundDaoException, DaoException{
		service = getService(SCOPES);
		topLevelEndpoints = service.getTopLevelEndpoints();

		deleteLegalLand();

	}

	@After 
	public void cleanUp() throws CirrasUnderwritingServiceException, NotFoundDaoException, DaoException {
		deleteLegalLand();
	}

	
	private void deleteLegalLand() throws NotFoundDaoException, DaoException, CirrasUnderwritingServiceException{

		delete(legalLandId);

	}
	
	private void delete(Integer legalLandId) throws NotFoundDaoException, DaoException, CirrasUnderwritingServiceException{

		service.deleteLegalLandSync(topLevelEndpoints, legalLandId.toString());
	
	}
	
	@Test
	public void testCreateUpdateDeleteLegalLand() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testCreateUpdateLegalLand");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		String primaryPropertyIdentifier = "111-222-333";
		String primaryReferenceTypeCode = "OTHER";
		String legalDescription = "Legal Description";
		String legalShortDescription = "Short Legal";
		String otherDescription = "Other Description 32321";
		Integer activeFromCropYear = 2011;
		Integer activeToCropYear = 2022;
		Double totalAcres = 12.3;
		String primaryLandIdentifierTypeCode = "PID";

		//CREATE LegalLand
		LegalLandRsrc resource = new LegalLandRsrc();
		
		resource.setLegalLandId(legalLandId);
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

		service.synchronizeLegalLand(resource);
		
		
		LegalLandRsrc fetchedResource = service.getLegalLand(findLegalLandResource(otherDescription)); 

		Assert.assertEquals("LegalLandId 1", resource.getLegalLandId(), fetchedResource.getLegalLandId());
		Assert.assertEquals("PrimaryPropertyIdentifier", resource.getPrimaryPropertyIdentifier(), fetchedResource.getPrimaryPropertyIdentifier());
		Assert.assertEquals("PrimaryReferenceTypeCode 1", resource.getPrimaryReferenceTypeCode(), fetchedResource.getPrimaryReferenceTypeCode());
		Assert.assertEquals("LegalDescription 1", resource.getLegalDescription(), fetchedResource.getLegalDescription());
		Assert.assertEquals("LegalShortDescription 1", resource.getLegalShortDescription(), fetchedResource.getLegalShortDescription());
		Assert.assertEquals("OtherDescription 1", resource.getOtherDescription(), fetchedResource.getOtherDescription());
		Assert.assertEquals("ActiveFromCropYear 1", resource.getActiveFromCropYear(), fetchedResource.getActiveFromCropYear());
		Assert.assertEquals("ActiveToCropYear 1", resource.getActiveToCropYear(), fetchedResource.getActiveToCropYear());
		
		//UPDATE CODE
		primaryPropertyIdentifier = "999-999-999";
		primaryReferenceTypeCode = "SHORT";
		legalDescription = "Legal Description 2";
		legalShortDescription = "Short Legal 2";
		otherDescription = "Other Description 215151";
		activeFromCropYear = 2000;
		activeToCropYear = 2011;

		fetchedResource.setPrimaryPropertyIdentifier(primaryPropertyIdentifier);
		fetchedResource.setPrimaryReferenceTypeCode(primaryReferenceTypeCode);
		fetchedResource.setLegalDescription(legalDescription);
		fetchedResource.setLegalShortDescription(legalShortDescription);
		fetchedResource.setOtherDescription(otherDescription);
		fetchedResource.setActiveFromCropYear(activeFromCropYear);
		fetchedResource.setActiveToCropYear(activeToCropYear);
		fetchedResource.setTransactionType(LandManagementEventTypes.LegalLandUpdated);

		service.synchronizeLegalLand(fetchedResource);
		
		LegalLandRsrc updatedResource = service.getLegalLand(findLegalLandResource(otherDescription));

		Assert.assertEquals("LegalLandId 2", fetchedResource.getLegalLandId(), updatedResource.getLegalLandId());
		Assert.assertEquals("PrimaryPropertyIdentifier 2", fetchedResource.getPrimaryPropertyIdentifier(), updatedResource.getPrimaryPropertyIdentifier());		
		Assert.assertEquals("PrimaryReferenceTypeCode 2", fetchedResource.getPrimaryReferenceTypeCode(), updatedResource.getPrimaryReferenceTypeCode());
		Assert.assertEquals("LegalDescription 2", fetchedResource.getLegalDescription(), updatedResource.getLegalDescription());
		Assert.assertEquals("LegalShortDescription 2", fetchedResource.getLegalShortDescription(), updatedResource.getLegalShortDescription());
		Assert.assertEquals("OtherDescription 2", fetchedResource.getOtherDescription(), updatedResource.getOtherDescription());
		Assert.assertEquals("ActiveFromCropYear 2", fetchedResource.getActiveFromCropYear(), updatedResource.getActiveFromCropYear());
		Assert.assertEquals("ActiveToCropYear 2", fetchedResource.getActiveToCropYear(), updatedResource.getActiveToCropYear());
		
		//CLEAN UP: DELETE CODE
		delete(legalLandId);
		
		logger.debug(">testCreateUpdateLegalLand");
	}

	public LegalLandRsrc findLegalLandResource(String legalLocation)
			throws CirrasUnderwritingServiceException {
		LegalLandListRsrc legalLandList = service.getLegalLandList(topLevelEndpoints, legalLocation, null, null, null, "false", "false", null, null, null, null);
		Assert.assertNotNull(legalLandList);
		Assert.assertEquals("Legal Land Returned Records (exact)", 1, legalLandList.getCollection().size());
		return legalLandList.getCollection().get(0);
	}
	

}
