package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints;

import java.util.Calendar;
import java.util.Date;

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
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandFieldXrefRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.LandManagementEventTypes;


public class LegalLandFieldXrefEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(LegalLandFieldXrefEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.UPDATE_SYNC_UNDERWRITING,
		Scopes.DELETE_SYNC_UNDERWRITING,
		Scopes.GET_LAND
	};
	
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;
	private Integer legalLandId = 999999999;
	private Integer fieldId = 888888888;
	
	@Before
	public void prepareTests() throws CirrasUnderwritingServiceException, Oauth2ClientException, NotFoundDaoException, DaoException{
		service = getService(SCOPES);
		topLevelEndpoints = service.getTopLevelEndpoints();

		deleteLegalLandFieldXref();

	}

	@After 
	public void cleanUp() throws CirrasUnderwritingServiceException, NotFoundDaoException, DaoException {
		deleteLegalLandFieldXref();
	}

	
	private void deleteLegalLandFieldXref() throws NotFoundDaoException, DaoException, CirrasUnderwritingServiceException{
		
		delete(legalLandId, fieldId);

	}
	
	private void delete(Integer legalLandId, Integer fieldId) throws NotFoundDaoException, DaoException, CirrasUnderwritingServiceException{

		// delete legal land - field xref
		LegalLandFieldXrefRsrc llfx = service.getLegalLandFieldXref(topLevelEndpoints, legalLandId.toString(), fieldId.toString());
		if ( llfx != null ) { 
			service.deleteLegalLandFieldXref(topLevelEndpoints, legalLandId.toString(), fieldId.toString());
		}

		// delete legal land
		service.deleteLegalLandSync(topLevelEndpoints, legalLandId.toString());
		
		// delete field				
		service.deleteField(topLevelEndpoints, fieldId.toString());
	}
	
	@Test
	public void testCreateDeleteLegalLandFieldXref() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testCreateDeleteLegalLandFieldXref");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		//CREATE LegalLand
		LegalLandRsrc legalLandResource = new LegalLandRsrc();
		
		legalLandResource.setLegalLandId(legalLandId);
		legalLandResource.setPrimaryReferenceTypeCode("OTHER");
		legalLandResource.setLegalDescription("Legal Description");
		legalLandResource.setLegalShortDescription("Short Legal");
		legalLandResource.setOtherDescription("Other Description");
		legalLandResource.setActiveFromCropYear(2011);
		legalLandResource.setActiveToCropYear(2022);
		legalLandResource.setTransactionType(LandManagementEventTypes.LegalLandCreated);

		service.synchronizeLegalLand(legalLandResource);
		
		
		//CREATE Field
		FieldRsrc fieldResource = new FieldRsrc();
		
		fieldResource.setFieldId(fieldId);
		fieldResource.setFieldLabel("FIELD LABEL");
		fieldResource.setActiveFromCropYear(2011);
		fieldResource.setActiveToCropYear(2022);
		fieldResource.setTransactionType(LandManagementEventTypes.FieldCreated);

		service.synchronizeField(fieldResource);
				
		
		//CREATE Legal Land - Field Xref
		LegalLandFieldXrefRsrc legalLandFieldXrefResource = new LegalLandFieldXrefRsrc();
		
		legalLandFieldXrefResource.setLegalLandId(legalLandId);
		legalLandFieldXrefResource.setFieldId(fieldId);
		legalLandFieldXrefResource.setTransactionType(LandManagementEventTypes.LegalLandFieldXrefCreated);

		service.synchronizeLegalLandFieldXref(legalLandFieldXrefResource);
				
		// compare
		LegalLandFieldXrefRsrc fetchedResource = service.getLegalLandFieldXref(topLevelEndpoints, legalLandId.toString(), fieldId.toString()); 

		Assert.assertEquals("LegalLandId", legalLandFieldXrefResource.getLegalLandId(), fetchedResource.getLegalLandId());
		Assert.assertEquals("FieldId", legalLandFieldXrefResource.getFieldId(), fetchedResource.getFieldId());
		
		//TRY to create same record Legal Land - Field Xref
		fetchedResource.setTransactionType(LandManagementEventTypes.LegalLandFieldXrefCreated);

		//Successful if no error is thrown
		service.synchronizeLegalLandFieldXref(legalLandFieldXrefResource);

		//CLEAN UP: DELETE CODE
		delete(legalLandId, fieldId);
		
		logger.debug(">testCreateDeleteLegalLandFieldXref");
	}

	
	@Test
    public void testDeleteLegalLandFieldXrefWithoutRecordToDelete() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
        logger.debug("<testDeleteLegalLandFieldXrefWithoutRecordToDelete");
        
        if(skipTests) {
            logger.warn("Skipping tests");
            return;
        }
                        
        //CREATE Legal Land - Field Xref
        LegalLandFieldXrefRsrc legalLandFieldXrefResource = new LegalLandFieldXrefRsrc();
        
        legalLandFieldXrefResource.setLegalLandId(legalLandId);
        legalLandFieldXrefResource.setFieldId(fieldId);
                
        //TRY TO DELETE A Code THAT DOESN'T EXIST (NO ERROR EXPECTED)
        legalLandFieldXrefResource.setTransactionType(LandManagementEventTypes.LegalLandFieldXrefDeleted);
        service.synchronizeLegalLandFieldXref(legalLandFieldXrefResource);       
        
        logger.debug(">testDeleteLegalLandFieldXrefWithoutRecordToDelete");
    }

	
	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}
	

}
