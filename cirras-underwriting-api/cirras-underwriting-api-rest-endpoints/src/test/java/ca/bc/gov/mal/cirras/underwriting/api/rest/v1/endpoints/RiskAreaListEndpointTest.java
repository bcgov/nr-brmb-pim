package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.RiskAreaListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.RiskAreaRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class RiskAreaListEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(RiskAreaListEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.GET_CODE_TABLES
	};
	
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;


	@Before
	public void prepareTests() throws CirrasUnderwritingServiceException, Oauth2ClientException, NotFoundDaoException, DaoException{
		service = getService(SCOPES);
		topLevelEndpoints = service.getTopLevelEndpoints();

	}


	@Test
	public void testGetRiskAreaList() throws CirrasUnderwritingServiceException, Oauth2ClientException {
		logger.debug("<testGetRiskAreaList");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		Integer insurancePlanId = null;
		String insurancePlanName = null;
		Integer totalPlanRecords = 0;

		// TEST 1: Get all risk areas
		RiskAreaListRsrc searchResults = service.getRiskAreaList(topLevelEndpoints, null);

		Assert.assertNotNull(searchResults);

		for(RiskAreaRsrc resource : searchResults.getCollection()) {
			if(insurancePlanId == null) {
				//set a plan id (doesn't matter which one)
				insurancePlanId = resource.getInsurancePlanId();
				insurancePlanName = resource.getInsurancePlanName();
			}
			if(insurancePlanId.equals(resource.getInsurancePlanId())) {
				//store total records of planId
				totalPlanRecords++;
			}
			Assert.assertNotNull("RiskAreaId", resource.getRiskAreaId());
			Assert.assertNotNull("InsurancePlanId", resource.getInsurancePlanId());
			Assert.assertNotNull("InsurancePlanName", resource.getInsurancePlanName());
			Assert.assertNotNull("RiskAreaName", resource.getRiskAreaName());
			Assert.assertNotNull("Description", resource.getDescription());
		}
		
		
		// TEST 2: Get risk areas of insurance plan stored in insurancePlanId
		searchResults = service.getRiskAreaList(topLevelEndpoints, insurancePlanId.toString());
		
		Assert.assertNotNull(searchResults);
		Assert.assertEquals(totalPlanRecords.intValue(), searchResults.getCollection().size());
		
		for(RiskAreaRsrc resource : searchResults.getCollection()) {
			Assert.assertEquals(insurancePlanId, resource.getInsurancePlanId());
			Assert.assertEquals(insurancePlanName, resource.getInsurancePlanName());
		}


		// TEST 3: Plan with no results.
		searchResults = service.getRiskAreaList(topLevelEndpoints, "111");

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(0, searchResults.getCollection().size());

		logger.debug(">testGetRiskAreaList");
	}

}
