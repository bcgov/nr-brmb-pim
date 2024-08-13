package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints;

import java.util.List;
import java.util.stream.Collectors;

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
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UnderwritingYearListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UnderwritingYearRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;


public class UnderwritingYearEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(UnderwritingYearEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL,
		Scopes.GET_UNDERWRITING_YEAR,
		Scopes.CREATE_UNDERWRITING_YEAR,
		Scopes.DELETE_UNDERWRITING_YEAR
	};
	
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;
	private Integer cropYear1 = 2009;

	
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
			
		UnderwritingYearListRsrc results = service.getUnderwritingYearList(topLevelEndpoints);
		
		if(results != null && results.getCollection().size() > 0) {
			List<UnderwritingYearRsrc> resources = results.getCollection().stream()
					.filter(x -> x.getCropYear().equals(cropYear1))
					.collect(Collectors.toList());
			
			if(resources != null && resources.size() > 0) {

				//FETCH then DELETE.
				UnderwritingYearRsrc uwYearRsrc = service.getUnderwritingYear(resources.get(0));				
				service.deleteUnderwritingYear(uwYearRsrc);
			}
		}
	}
	
	@Test
	public void testCreateSelectDeleteUnderwritingYear() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testCreateSelectDeleteUnderwritingYear");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//Count existing records
		UnderwritingYearListRsrc results = service.getUnderwritingYearList(topLevelEndpoints);
		
		Assert.assertNotNull(results);
		Assert.assertNotNull(results.getCollection());
		
		int totalRecords = results.getCollection().size();

		//CREATE Underwriting Year
		UnderwritingYearRsrc resource = new UnderwritingYearRsrc();
		
		resource.setCropYear(cropYear1);

		UnderwritingYearRsrc insertedResource = service.createUnderwritingYear(topLevelEndpoints, resource);
		
		Assert.assertNotNull("UnderwritingYearGuid", insertedResource.getUnderwritingYearGuid());
		
		//String underwritingYearGuid = resource.getUnderwritingYearGuid();
		
		UnderwritingYearRsrc fetchedResource = service.getUnderwritingYear(insertedResource);
		
		Assert.assertEquals("UnderwritingYearGuid", insertedResource.getUnderwritingYearGuid(), fetchedResource.getUnderwritingYearGuid());
		Assert.assertEquals("CropYear", insertedResource.getCropYear(), fetchedResource.getCropYear());

		
		try {
			//Try to add a second record of the same year, an error is expected
			UnderwritingYearRsrc resource2 = new UnderwritingYearRsrc();
			resource2.setCropYear(cropYear1);
			service.createUnderwritingYear(topLevelEndpoints, resource2);
			Assert.fail("Attempt to add another recorde with existing year did not trigger ServiceException");
		} catch (CirrasUnderwritingServiceException e) {
			Assert.assertTrue(e.getMessage().contains("Underwriting Year " + cropYear1 + " already exists"));
		}
		
		results = service.getUnderwritingYearList(topLevelEndpoints);
		
		Assert.assertNotNull(results);
		Assert.assertNotNull(results.getCollection());
		Assert.assertEquals(totalRecords + 1, results.getCollection().size());
		
		//DELETE
		service.deleteUnderwritingYear(fetchedResource);
		
		//Check if record has been deleted
		results = service.getUnderwritingYearList(topLevelEndpoints);
		Assert.assertNotNull(results);
		Assert.assertNotNull(results.getCollection());
		Assert.assertEquals(totalRecords, results.getCollection().size());
		
		List<UnderwritingYearRsrc> resources = results.getCollection().stream()
			.filter(x -> x.getCropYear().equals(cropYear1))
			.collect(Collectors.toList());
		
		Assert.assertNotNull(resources);
		Assert.assertEquals(0, resources.size());

		logger.debug(">testCreateSelectDeleteUnderwritingYear");
	}
	
}
