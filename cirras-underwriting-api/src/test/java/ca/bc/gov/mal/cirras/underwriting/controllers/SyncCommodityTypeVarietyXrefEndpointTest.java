package ca.bc.gov.mal.cirras.underwriting.controllers;

import java.util.Calendar;
import java.util.Date;

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
import ca.bc.gov.mal.cirras.underwriting.data.resources.SyncCommodityTypeCodeRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.SyncCommodityTypeVarietyXrefRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.SyncCommodityVarietyRsrc;
import ca.bc.gov.mal.cirras.underwriting.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;
import ca.bc.gov.mal.cirras.underwriting.data.resources.PoliciesSyncEventTypes;



public class SyncCommodityTypeVarietyXrefEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(SyncCommodityTypeVarietyXrefEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.UPDATE_SYNC_UNDERWRITING,
		Scopes.DELETE_SYNC_UNDERWRITING,
		Scopes.GET_COMMODITY_TYPE_VARIETY_XREF
	};
	
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;
	private String commodityTypeCode = "TESTCODE";
	private Integer cropCommodityId = 88888888;
	private Integer cropVarietyId = 99999999;
	
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

		// delete commodity type variety xref
		service.deleteCommodityTypeVarietyXref(topLevelEndpoints, commodityTypeCode, cropVarietyId.toString());

		// delete commodity type code
		service.deleteCommodityTypeCode(topLevelEndpoints, commodityTypeCode);

		// delete variety
		service.deleteSyncCommodityVariety(topLevelEndpoints, cropVarietyId.toString());

		// delete commodity
		service.deleteSyncCommodityVariety(topLevelEndpoints, cropCommodityId.toString());

	}
	
	@Test
	public void testCreateDeleteCommodityTypeVarietyXref() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testCreateDeleteCommodityTypeVarietyXref");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);
	
		//CREATE Commodity, Variety and Commodity Type Code
		createCommodity(createTransactionDate);
		createVariety(createTransactionDate);
		createCommodityTypeCode(createTransactionDate);
		
		//CREATE Commodity Type Variety Xref
		SyncCommodityTypeVarietyXrefRsrc resource = new SyncCommodityTypeVarietyXrefRsrc();
		
		resource.setCommodityTypeCode(commodityTypeCode);
		resource.setCropVarietyId(cropVarietyId);
		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.CommodityTypeVarietyXrefCreated);

		service.synchronizeCommodityTypeVarietyXref(resource);
				
		// compare
		SyncCommodityTypeVarietyXrefRsrc fetchedResource = service.getCommodityTypeVarietyXref(topLevelEndpoints, commodityTypeCode, cropVarietyId.toString()); 

		Assert.assertEquals("LegalLandId", resource.getCommodityTypeCode(), fetchedResource.getCommodityTypeCode());
		Assert.assertEquals("FieldId", resource.getCropVarietyId(), fetchedResource.getCropVarietyId());
		Assert.assertTrue("DataSyncTransDate 1", resource.getDataSyncTransDate().compareTo(fetchedResource.getDataSyncTransDate()) == 0);
		
		//TRY to create same record Commodity Type Variety Xref
		fetchedResource.setDataSyncTransDate(addSeconds(transactionDate, +1));
		fetchedResource.setTransactionType(PoliciesSyncEventTypes.CommodityTypeVarietyXrefCreated);

		//Successful if no error is thrown
		service.synchronizeCommodityTypeVarietyXref(fetchedResource);

		//Get the resource again to test delete
		fetchedResource = service.getCommodityTypeVarietyXref(topLevelEndpoints, commodityTypeCode, cropVarietyId.toString()); 

		fetchedResource.setTransactionType(PoliciesSyncEventTypes.CommodityTypeVarietyXrefDeleted);
		service.synchronizeCommodityTypeVarietyXref(fetchedResource);
		
		//CLEAN UP: DELETE RECORDS
		delete();
		
		logger.debug(">testCreateDeleteCommodityTypeVarietyXref");
	}

	
	@Test
    public void testDeleteCommodityTypeVarietyXrefWithoutRecordToDelete() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
        logger.debug("<testDeleteCommodityTypeVarietyXrefWithoutRecordToDelete");
        
        if(skipTests) {
            logger.warn("Skipping tests");
            return;
        }

        //Date and Time without millisecond
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
        Date transactionDate = cal.getTime();

        Date createTransactionDate = addSeconds(transactionDate, -1);
                        
        //CREATE Legal Land - Field Xref
        SyncCommodityTypeVarietyXrefRsrc resource = new SyncCommodityTypeVarietyXrefRsrc();
        
        resource.setCommodityTypeCode(commodityTypeCode);
        resource.setCropVarietyId(cropVarietyId);
        resource.setDataSyncTransDate(createTransactionDate);
                
        //TRY TO DELETE A Record THAT DOESN'T EXIST (NO ERROR EXPECTED)
        resource.setTransactionType(PoliciesSyncEventTypes.CommodityTypeVarietyXrefDeleted);
        service.synchronizeCommodityTypeVarietyXref(resource);       
        
        logger.debug(">testDeleteCommodityTypeVarietyXrefWithoutRecordToDelete");
    }

	private void createCommodityTypeCode(Date createTransactionDate)
			throws CirrasUnderwritingServiceException, ValidationException {
		SyncCommodityTypeCodeRsrc resource = new SyncCommodityTypeCodeRsrc();

		resource.setCommodityTypeCode(commodityTypeCode);
		resource.setCropCommodityId(cropCommodityId);
		resource.setDescription("Test Code");
		resource.setIsActive(true);
		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.CommodityTypeCodeCreated);

		service.synchronizeCommodityTypeCode(resource);

	}

	private void createVariety(Date createTransactionDate)
			throws CirrasUnderwritingServiceException, ValidationException {
		SyncCommodityVarietyRsrc resource = new SyncCommodityVarietyRsrc();

		resource.setCropId(cropVarietyId);
		resource.setParentCropId(cropCommodityId);
		resource.setCropName("TEST VARIETY");
		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.CommodityVarietyCreated);

		service.synchronizeCommodityVariety(resource);

	}

	private void createCommodity(Date createTransactionDate)
			throws CirrasUnderwritingServiceException, ValidationException {
		//CREATE PARENT COMMODITY
		SyncCommodityVarietyRsrc resource = new SyncCommodityVarietyRsrc();

		resource.setCropId(cropCommodityId);
		resource.setCropName("TEST COMMODITY");
		resource.setInsurancePlanId(4);
		resource.setShortLabel("SHRT");
		resource.setPlantDurationTypeCode("ANNUAL");
		resource.setIsInventoryCrop(true);
		resource.setIsYieldCrop(true);
		resource.setIsUnderwritingCrop(true);
		resource.setIsProductInsurableInd(true);
		resource.setIsCropInsuranceEligibleInd(true);
		resource.setIsPlantInsuranceEligibleInd(true);
		resource.setIsOtherInsuranceEligibleInd(true);
		resource.setYieldMeasUnitTypeCode("TONNE");
		resource.setYieldDecimalPrecision(2);
		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.CommodityVarietyCreated);

		service.synchronizeCommodityVariety(resource);
	}

	
	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}
	

}
