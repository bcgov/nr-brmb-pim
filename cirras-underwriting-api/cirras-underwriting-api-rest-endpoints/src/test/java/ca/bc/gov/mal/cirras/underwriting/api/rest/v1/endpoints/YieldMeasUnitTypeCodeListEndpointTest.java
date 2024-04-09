package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints;

import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.PoliciesSyncEventTypes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.ValidationException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SyncCommodityTypeCodeRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SyncCommodityTypeVarietyXrefRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SyncCommodityVarietyRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.YieldMeasUnitTypeCodeListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.YieldMeasUnitTypeCodeRsrc;
import ca.bc.gov.mal.cirras.underwriting.model.v1.CropVariety;
import ca.bc.gov.mal.cirras.underwriting.model.v1.CropVarietyCommodityType;
import ca.bc.gov.mal.cirras.underwriting.model.v1.YieldMeasUnitTypeCodeList;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.CropCommodityListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.CropCommodityRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class YieldMeasUnitTypeCodeListEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(YieldMeasUnitTypeCodeListEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.GET_YIELD_MEAS_UNIT_TYPE_CODES
	};
	
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;
	private Integer insurancePlanId = 4;


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
	public void testGetYieldMeasUnitTypeCodeList() throws CirrasUnderwritingServiceException, Oauth2ClientException {
		logger.debug("<testGetYieldMeasUnitTypeCodeList");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		YieldMeasUnitTypeCodeListRsrc searchResults = service.getYieldMeasUnitTypeCodeList(
				topLevelEndpoints, 
				insurancePlanId.toString()
				);

		Assert.assertNotNull(searchResults);
		Assert.assertTrue(searchResults.getCollection().size() == 2); //This might change in the future

		//Check if the order is by default and then the others
		Boolean isDefault = true;
		for (YieldMeasUnitTypeCodeRsrc rsrc : searchResults.getCollection()) {
			Assert.assertEquals("Insurance Plan doesn't match", insurancePlanId, rsrc.getInsurancePlanId());
			Assert.assertEquals("Order is wrong", isDefault, rsrc.getIsDefaultYieldUnitInd());
			isDefault = false;
			Assert.assertNotNull("YieldMeasUnitTypeCode", rsrc.getYieldMeasUnitTypeCode());
			Assert.assertNotNull("Description", rsrc.getDescription());
			Assert.assertNotNull("DecimalPrecision", rsrc.getDecimalPrecision());
			Assert.assertNotNull("EffectiveDate", rsrc.getEffectiveDate());
			Assert.assertNotNull("ExpiryDate", rsrc.getExpiryDate());

		}
		
		logger.debug(">testGetYieldMeasUnitTypeCodeList");
	}

}
