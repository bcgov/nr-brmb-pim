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


public class UnderwritingYearListEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(UnderwritingYearListEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL,
		Scopes.GET_UNDERWRITING_YEAR,
		Scopes.CREATE_UNDERWRITING_YEAR,
		Scopes.DELETE_UNDERWRITING_YEAR
	};
	
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;
	private Integer cropYear1 = 2009;
	private Integer cropYear2 = 2010;
	
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
			deleteUwYear(results, cropYear1);
			deleteUwYear(results, cropYear2);
		}
	}

	protected void deleteUwYear(UnderwritingYearListRsrc results, Integer cropYear) throws CirrasUnderwritingServiceException {
		List<UnderwritingYearRsrc> resources = results.getCollection().stream()
				.filter(x -> x.getCropYear().equals(cropYear))
				.collect(Collectors.toList());
		
		if(resources != null && resources.size() > 0) {
			//DELETE
			service.deleteUnderwritingYear(resources.get(0));
		}
	}
	
	@Test
	public void testCreateSelectUnderwritingYears() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testCreateSelectUnderwritingYears");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//Count existing records
		UnderwritingYearListRsrc results = service.getUnderwritingYearList(topLevelEndpoints);
		
		Assert.assertNotNull(results);
		Assert.assertNotNull(results.getCollection());
		
		int totalRecords = results.getCollection().size();

		//CREATE Underwriting Years
		UnderwritingYearRsrc newResource = new UnderwritingYearRsrc();
		newResource.setCropYear(cropYear1);
		service.createUnderwritingYear(topLevelEndpoints, newResource);

		newResource.setCropYear(cropYear2);
		service.createUnderwritingYear(topLevelEndpoints, newResource);

		results = service.getUnderwritingYearList(topLevelEndpoints);
		
		Assert.assertNotNull(results);
		Assert.assertNotNull(results.getCollection());
		Assert.assertEquals(totalRecords + 2, results.getCollection().size());
		
		//Check if all fields are set
		for (UnderwritingYearRsrc resource : results.getCollection()) {
			Assert.assertNotNull(resource.getUnderwritingYearGuid());
			Assert.assertNotNull(resource.getCropYear());
		}
		
		//Check if the inserted years are in the list
		List<UnderwritingYearRsrc> resources = results.getCollection().stream()
			.filter(x -> x.getCropYear().equals(cropYear1))
			.collect(Collectors.toList());
		
		Assert.assertNotNull(resources);
		Assert.assertEquals(1, resources.size());

		resources = results.getCollection().stream()
				.filter(x -> x.getCropYear().equals(cropYear2))
				.collect(Collectors.toList());
			
		Assert.assertNotNull(resources);
		Assert.assertEquals(1, resources.size());
		
		logger.debug(">testCreateSelectUnderwritingYears");
	}
	
}
