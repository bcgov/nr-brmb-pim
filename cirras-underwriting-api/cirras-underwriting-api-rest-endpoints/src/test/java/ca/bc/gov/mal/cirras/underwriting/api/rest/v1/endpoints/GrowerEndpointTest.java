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
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GrowerRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.PoliciesSyncEventTypes;


public class GrowerEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(GrowerEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.UPDATE_SYNC_UNDERWRITING,
		Scopes.DELETE_SYNC_UNDERWRITING,
		Scopes.GET_GROWER
	};
	
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;
	private Integer growerId = 99999999;
	
	@Before
	public void prepareTests() throws CirrasUnderwritingServiceException, Oauth2ClientException, NotFoundDaoException, DaoException{
		service = getService(SCOPES);
		topLevelEndpoints = service.getTopLevelEndpoints();

		deleteGrower();

	}

	@After 
	public void cleanUp() throws CirrasUnderwritingServiceException, NotFoundDaoException, DaoException {
		deleteGrower();
	}

	
	private void deleteGrower() throws NotFoundDaoException, DaoException, CirrasUnderwritingServiceException{

		delete(growerId);

	}
	
	private void delete(Integer growerId) throws NotFoundDaoException, DaoException, CirrasUnderwritingServiceException{

		
		service.deleteGrower(topLevelEndpoints, growerId.toString());
		
	}
	
	@Test
	public void testCreateUpdateDeleteGrower() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testCreateUpdateGrower");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);

		Integer growerNumber = 123456;
		String growerName = "grower test name";
		String growerAddressLine1 = "address line 1";
		String growerAddressLine2 = "address line 2";
		String growerPostalCode = "V8P 4N8";
		String growerCity = "Victoria";
		Integer cityId = 1;
		String growerProvince = "BC";

		//CREATE Grower
		GrowerRsrc resource = new GrowerRsrc();
		
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
		
		GrowerRsrc fetchedResource = service.getGrower(topLevelEndpoints, growerId.toString()); 

		Assert.assertEquals("GrowerId 1", resource.getGrowerId(), fetchedResource.getGrowerId());
		Assert.assertEquals("GrowerNumber 1", resource.getGrowerNumber(), fetchedResource.getGrowerNumber());
		Assert.assertEquals("GrowerName 1", resource.getGrowerName(), fetchedResource.getGrowerName());
		Assert.assertEquals("GrowerAddressLine1 1", resource.getGrowerAddressLine1(), fetchedResource.getGrowerAddressLine1());
		Assert.assertEquals("GrowerAddressLine2 1", resource.getGrowerAddressLine2(), fetchedResource.getGrowerAddressLine2());
		Assert.assertEquals("GrowerPostalCode 1", resource.getGrowerPostalCode(), fetchedResource.getGrowerPostalCode());
		Assert.assertEquals("GrowerCity 1", resource.getGrowerCity(), fetchedResource.getGrowerCity());
		Assert.assertEquals("CityId 1", resource.getCityId(), fetchedResource.getCityId());
		Assert.assertEquals("GrowerProvince 1", resource.getGrowerProvince(), fetchedResource.getGrowerProvince());
		Assert.assertTrue("DataSyncTransDate 1", resource.getDataSyncTransDate().compareTo(fetchedResource.getDataSyncTransDate()) == 0);
		
		//UPDATE CODE
		growerNumber = 555666;
		growerName = "grower test name updated";
		growerAddressLine1 = "address line 1 updated";
		growerAddressLine2 = "address line 2 updated";
		growerPostalCode = "K1K 2K2";
		growerCity = "Kelowna";
		cityId = 2;
		growerProvince = "AB";
		
		fetchedResource.setGrowerNumber(growerNumber);
		fetchedResource.setGrowerName(growerName);
		fetchedResource.setGrowerAddressLine1(growerAddressLine1);
		fetchedResource.setGrowerAddressLine2(growerAddressLine2);
		fetchedResource.setGrowerPostalCode(growerPostalCode);
		fetchedResource.setGrowerCity(growerCity);
		fetchedResource.setCityId(cityId);
		fetchedResource.setGrowerProvince(growerProvince);
		fetchedResource.setDataSyncTransDate(addSeconds(transactionDate, +1));
		fetchedResource.setTransactionType(PoliciesSyncEventTypes.GrowerUpdated);

		service.synchronizeGrower(fetchedResource);
		
		GrowerRsrc updatedResource = service.getGrower(topLevelEndpoints, growerId.toString()); 

		Assert.assertEquals("GrowerId 2", fetchedResource.getGrowerId(), updatedResource.getGrowerId());
		Assert.assertEquals("GrowerNumber 2", fetchedResource.getGrowerNumber(), updatedResource.getGrowerNumber());
		Assert.assertEquals("GrowerName 2", fetchedResource.getGrowerName(), updatedResource.getGrowerName());
		Assert.assertEquals("GrowerAddressLine1 2", fetchedResource.getGrowerAddressLine1(), updatedResource.getGrowerAddressLine1());
		Assert.assertEquals("GrowerAddressLine2 2", fetchedResource.getGrowerAddressLine2(), updatedResource.getGrowerAddressLine2());
		Assert.assertEquals("GrowerPostalCode 2", fetchedResource.getGrowerPostalCode(), updatedResource.getGrowerPostalCode());
		Assert.assertEquals("GrowerCity 2", fetchedResource.getGrowerCity(), updatedResource.getGrowerCity());
		Assert.assertEquals("CityId 2", fetchedResource.getCityId(), updatedResource.getCityId());
		Assert.assertEquals("GrowerProvince 2", fetchedResource.getGrowerProvince(), updatedResource.getGrowerProvince());
		Assert.assertTrue("DataSyncTransDate 2", fetchedResource.getDataSyncTransDate().compareTo(updatedResource.getDataSyncTransDate()) == 0);
		
		//CLEAN UP: DELETE CODE
		delete(growerId);
		
		logger.debug(">testCreateUpdateGrower");
	}

	
	@Test
	public void testUpdateGrowerWithoutRecordNoUpdate() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testUpdateGrowerWithoutRecordNoUpdate");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);

		Integer growerNumber = 123456;
		String growerName = "grower test name";
		String growerAddressLine1 = "address line 1";
		String growerAddressLine2 = "address line 2";
		String growerPostalCode = "V8P 4N8";
		String growerCity = "Victoria";
		Integer cityId = 1;
		String growerProvince = "BC";

		//CREATE Grower
		GrowerRsrc resource = new GrowerRsrc();
		
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
		
		
		//TRY TO DELETE A Code THAT DOESN'T EXIST (NO ERROR EXPECTED)
		resource.setTransactionType(PoliciesSyncEventTypes.GrowerDeleted);
		service.synchronizeGrower(resource);

		//SHOULD RESULT IN AN INSERT
		resource.setTransactionType(PoliciesSyncEventTypes.GrowerUpdated);

		service.synchronizeGrower(resource);
		
		GrowerRsrc fetchedResource = service.getGrower(topLevelEndpoints, growerId.toString()); 

		Assert.assertEquals("GrowerId 1", resource.getGrowerId(), fetchedResource.getGrowerId());
		Assert.assertEquals("GrowerNumber 1", resource.getGrowerNumber(), fetchedResource.getGrowerNumber());
		Assert.assertEquals("GrowerName 1", resource.getGrowerName(), fetchedResource.getGrowerName());
		Assert.assertEquals("GrowerAddressLine1 1", resource.getGrowerAddressLine1(), fetchedResource.getGrowerAddressLine1());
		Assert.assertEquals("GrowerAddressLine2 1", resource.getGrowerAddressLine2(), fetchedResource.getGrowerAddressLine2());
		Assert.assertEquals("GrowerPostalCode 1", resource.getGrowerPostalCode(), fetchedResource.getGrowerPostalCode());
		Assert.assertEquals("GrowerCity 1", resource.getGrowerCity(), fetchedResource.getGrowerCity());
		Assert.assertEquals("CityId 1", resource.getCityId(), fetchedResource.getCityId());
		Assert.assertEquals("GrowerProvince 1", resource.getGrowerProvince(), fetchedResource.getGrowerProvince());
		Assert.assertTrue("DataSyncTransDate 1", resource.getDataSyncTransDate().compareTo(fetchedResource.getDataSyncTransDate()) == 0);

		
		//NO UPDATE EXPECTED BECAUSE TRANSACTION DATE IS EARLIER THAN STORED ONE
		fetchedResource.setDataSyncTransDate(addSeconds(createTransactionDate, -1));
		fetchedResource.setTransactionType(PoliciesSyncEventTypes.GrowerUpdated);
		service.synchronizeGrower(fetchedResource);

		
		GrowerRsrc notUpdatedResource = service.getGrower(topLevelEndpoints, growerId.toString()); 

		Assert.assertTrue("DataSyncTransDate 2", resource.getDataSyncTransDate().compareTo(notUpdatedResource.getDataSyncTransDate()) == 0);

		
		//UPDATE GROWER
		growerNumber = 555666;
		growerName = "grower test name updated";
		growerAddressLine1 = "address line 1 updated";
		growerAddressLine2 = "address line 2 updated";
		growerPostalCode = "K1K 2K2";
		growerCity = "Kelowna";
		cityId = 2;
		growerProvince = "AB";
		
		notUpdatedResource.setGrowerNumber(growerNumber);
		notUpdatedResource.setGrowerName(growerName);
		notUpdatedResource.setGrowerAddressLine1(growerAddressLine1);
		notUpdatedResource.setGrowerAddressLine2(growerAddressLine2);
		notUpdatedResource.setGrowerPostalCode(growerPostalCode);
		notUpdatedResource.setGrowerCity(growerCity);
		notUpdatedResource.setCityId(cityId);
		notUpdatedResource.setGrowerProvince(growerProvince);

		//UPDATE EXPECTED BECAUSE RECORD EXISTS IT WILL UPDATE IT
		notUpdatedResource.setTransactionType(PoliciesSyncEventTypes.GrowerCreated);
		notUpdatedResource.setDataSyncTransDate(addSeconds(createTransactionDate, 10));
		service.synchronizeGrower(notUpdatedResource);
		
		GrowerRsrc updatedResource = service.getGrower(topLevelEndpoints, growerId.toString()); 

		Assert.assertEquals("GrowerId 2", notUpdatedResource.getGrowerId(), updatedResource.getGrowerId());
		Assert.assertEquals("GrowerNumber 2", notUpdatedResource.getGrowerNumber(), updatedResource.getGrowerNumber());
		Assert.assertEquals("GrowerName 2", notUpdatedResource.getGrowerName(), updatedResource.getGrowerName());
		Assert.assertEquals("GrowerAddressLine1 2", notUpdatedResource.getGrowerAddressLine1(), updatedResource.getGrowerAddressLine1());
		Assert.assertEquals("GrowerAddressLine2 2", notUpdatedResource.getGrowerAddressLine2(), updatedResource.getGrowerAddressLine2());
		Assert.assertEquals("GrowerPostalCode 2", notUpdatedResource.getGrowerPostalCode(), updatedResource.getGrowerPostalCode());
		Assert.assertEquals("GrowerCity 2", notUpdatedResource.getGrowerCity(), updatedResource.getGrowerCity());
		Assert.assertEquals("CityId 2", notUpdatedResource.getCityId(), updatedResource.getCityId());
		Assert.assertEquals("GrowerProvince 2", notUpdatedResource.getGrowerProvince(), updatedResource.getGrowerProvince());
		Assert.assertTrue("DataSyncTransDate 2", notUpdatedResource.getDataSyncTransDate().compareTo(updatedResource.getDataSyncTransDate()) == 0);

		//CLEAN UP: DELETE CODE
		delete(growerId);		
		
		logger.debug(">testUpdateGrowerWithoutRecordNoUpdate");
	}

	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}
	

}
