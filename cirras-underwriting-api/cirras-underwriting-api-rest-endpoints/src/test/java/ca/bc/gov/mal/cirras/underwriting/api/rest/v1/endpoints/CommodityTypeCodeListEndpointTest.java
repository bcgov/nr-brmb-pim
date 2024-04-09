package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.CommodityTypeCodeListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SyncCommodityTypeCodeRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class CommodityTypeCodeListEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(CommodityTypeCodeListEndpointTest.class);


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

		delete();

	}

	@After 
	public void cleanUp() throws CirrasUnderwritingServiceException, NotFoundDaoException, DaoException {
		delete();
	}
	
	private void delete() throws NotFoundDaoException, DaoException, CirrasUnderwritingServiceException{

	}

	@Test
	public void testGetCommodityTypeCodeList() throws CirrasUnderwritingServiceException, Oauth2ClientException {
		logger.debug("<testGetCommodityTypeCodeList");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		// Set to plan with existing data.
		Integer insurancePlanId = 4;

		// TEST 1: Search with results.
		CommodityTypeCodeListRsrc searchResults = service.getCommodityTypeCodeList(
				topLevelEndpoints, 
				insurancePlanId.toString()
		);

		Assert.assertNotNull(searchResults);
		Assert.assertTrue(searchResults.getCollection().size() > 0);

		//Check that expectedGm is one of the results.
		for (SyncCommodityTypeCodeRsrc rsrc : searchResults.getCollection()) {
			Assert.assertEquals("insurancePlanId", insurancePlanId, rsrc.getInsurancePlanId());
			Assert.assertNotNull(rsrc.getCommodityTypeCode());
			Assert.assertNotNull(rsrc.getDescription());
			Assert.assertNotNull(rsrc.getCropCommodityId());
			Assert.assertNotNull(rsrc.getEffectiveDate());

		}


		// TEST 2: insurance plan with no results.
		insurancePlanId = 0;
		searchResults = service.getCommodityTypeCodeList(
				topLevelEndpoints, 
				insurancePlanId.toString()
		);

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(0, searchResults.getCollection().size());
		
		logger.debug(">testGetCommodityTypeCodeList");
	}

}
