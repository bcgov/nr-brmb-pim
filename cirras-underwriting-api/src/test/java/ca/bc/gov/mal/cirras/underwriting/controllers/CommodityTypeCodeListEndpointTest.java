package ca.bc.gov.mal.cirras.underwriting.controllers;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.clients.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.clients.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.CommodityTypeCodeListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.SyncCommodityTypeCodeRsrc;
import ca.bc.gov.mal.cirras.underwriting.test.EndpointsTest;
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
