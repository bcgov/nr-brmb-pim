package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints;

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
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.FieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandRsrc;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.LandManagementEventTypes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;


public class AnnualFieldDetailEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(AnnualFieldDetailEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.UPDATE_SYNC_UNDERWRITING,
		Scopes.DELETE_SYNC_UNDERWRITING,
		Scopes.GET_LAND
	};
	
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;
	
	private Integer annualFieldDetailId = 999999999;
	private Integer legalLandId = 999999998;
	private Integer fieldId = 999999997;
	
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

		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId.toString());
		service.deleteField(topLevelEndpoints, fieldId.toString());
		service.deleteLegalLandSync(topLevelEndpoints, legalLandId.toString());
		
	}
	
	@Test
	public void testCreateUpdateDeleteAnnualFieldDetail() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testCreateUpdateDeleteAnnualFieldDetail");
		
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
		
		//CREATE Annual Field Detail
		AnnualFieldDetailRsrc annualFieldDetailResource = createAnnualFieldDetail(annualFieldDetailId, legalLandId, fieldId, 2022);
		service.synchronizeAnnualFieldDetail(annualFieldDetailResource);

		//check if it matches
		AnnualFieldDetailRsrc fetchedResource = service.getAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId.toString()); 

		Assert.assertEquals("AnnualFieldDetailId 1", annualFieldDetailResource.getAnnualFieldDetailId(), fetchedResource.getAnnualFieldDetailId());
		Assert.assertEquals("LegalLandId 1", annualFieldDetailResource.getLegalLandId(), fetchedResource.getLegalLandId());
		Assert.assertEquals("FieldId 1", annualFieldDetailResource.getFieldId(), fetchedResource.getFieldId());
		Assert.assertEquals("CropYear 1", annualFieldDetailResource.getCropYear(), fetchedResource.getCropYear());
		
				
		//UPDATE CODE -> just changing the crop year
		fetchedResource.setCropYear(2023);
		fetchedResource.setTransactionType(LandManagementEventTypes.AnnualFieldDetailUpdated);

		service.synchronizeAnnualFieldDetail(fetchedResource);
		
		//check if it was updated
		AnnualFieldDetailRsrc updatedResource = service.getAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId.toString()); 

		Assert.assertEquals("CropYear 2", fetchedResource.getCropYear(), updatedResource.getCropYear());
		
		//CLEAN UP: DELETE CODE
		delete();
		
		logger.debug(">testCreateUpdateDeleteAnnualFieldDetail");
	}

	
	@Test
	public void testUpdateAnnualFieldDetailWithoutRecordNoUpdate() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testUpdateAnnualFieldDetailWithoutRecordNoUpdate");
		
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
		
		//CREATE Annual Field Detail
		AnnualFieldDetailRsrc annualFieldDetailResource = createAnnualFieldDetail(annualFieldDetailId, legalLandId, fieldId, 2022);
		
		
		//TRY TO DELETE A record THAT DOESN'T EXIST (NO ERROR EXPECTED)
		annualFieldDetailResource.setTransactionType(LandManagementEventTypes.AnnualFieldDetailDeleted);
		service.synchronizeAnnualFieldDetail(annualFieldDetailResource);
		
		//SHOULD RESULT IN AN INSERT
		annualFieldDetailResource.setTransactionType(LandManagementEventTypes.AnnualFieldDetailUpdated);
		service.synchronizeAnnualFieldDetail(annualFieldDetailResource);
		
		
		AnnualFieldDetailRsrc fetchedResource = service.getAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId.toString()); 

		Assert.assertEquals("AnnualFieldDetailId 1", annualFieldDetailResource.getAnnualFieldDetailId(), fetchedResource.getAnnualFieldDetailId());
		Assert.assertEquals("LegalLandId 1", annualFieldDetailResource.getLegalLandId(), fetchedResource.getLegalLandId());
		Assert.assertEquals("FieldId 1", annualFieldDetailResource.getFieldId(), fetchedResource.getFieldId());
		Assert.assertEquals("CropYear 1", annualFieldDetailResource.getCropYear(), fetchedResource.getCropYear());
	
		//UPDATE Annual Field Detail 
		fetchedResource.setCropYear(2023);

		//UPDATE EXPECTED BECAUSE RECORD EXISTS IT WILL UPDATE IT
		fetchedResource.setTransactionType(LandManagementEventTypes.AnnualFieldDetailCreated);
		service.synchronizeAnnualFieldDetail(fetchedResource);
		
		AnnualFieldDetailRsrc updatedResource = service.getAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId.toString()); 

		Assert.assertEquals("AnnualFieldDetailId 2", fetchedResource.getAnnualFieldDetailId(), updatedResource.getAnnualFieldDetailId());
		Assert.assertEquals("LegalLandId 2", fetchedResource.getLegalLandId(), updatedResource.getLegalLandId());
		Assert.assertEquals("FieldId 2", fetchedResource.getFieldId(), updatedResource.getFieldId());
		Assert.assertEquals("CropYear 2", fetchedResource.getCropYear(), updatedResource.getCropYear());

		//CLEAN UP: DELETE CODE
		delete();		
		
		logger.debug(">testUpdateAnnualFieldDetailWithoutRecordNoUpdate");
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
	
	private static AnnualFieldDetailRsrc createAnnualFieldDetail( Integer annualFieldDetailId, Integer legalLandId, Integer fieldId, Integer cropYear) {

		AnnualFieldDetailRsrc resource = new AnnualFieldDetailRsrc();
		
		resource.setAnnualFieldDetailId(annualFieldDetailId);
		resource.setLegalLandId(legalLandId);
		resource.setFieldId(fieldId);
		resource.setCropYear(cropYear);
		resource.setTransactionType(LandManagementEventTypes.AnnualFieldDetailCreated);
		
		return resource;
	}
	

}
