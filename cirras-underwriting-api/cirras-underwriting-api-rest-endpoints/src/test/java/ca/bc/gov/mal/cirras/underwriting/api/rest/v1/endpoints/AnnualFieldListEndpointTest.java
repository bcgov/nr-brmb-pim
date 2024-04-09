package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.ValidationException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.model.v1.PolicySimple;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.PolicyDto;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class AnnualFieldListEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(AnnualFieldListEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.SEARCH_ANNUAL_FIELDS
	};
	

	@Test
	public void testSearchAnnualFieldsByLegalLand() throws CirrasUnderwritingServiceException, Oauth2ClientException {
		logger.debug("<testSearchAnnualFieldsByLegalLand");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasUnderwritingService service = getService(SCOPES);

		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();
		Integer legalLandId = 1000013;
		String cropYear = "2023";
		String otherLegalDescription = "NW 11 22 33";

		AnnualFieldListRsrc searchResults = service.getAnnualFieldList(
				topLevelEndpoints, 
				legalLandId.toString(), 
				null,
				cropYear);

		Assert.assertNotNull(searchResults);
		
		List<AnnualFieldRsrc> fields = searchResults.getCollection();
		Assert.assertNotNull(fields);
		Assert.assertFalse(fields.isEmpty());
		
		for (AnnualFieldRsrc resource: fields) {
			Assert.assertNotNull(resource.getPolicies());
			Assert.assertEquals("LegalLandId", legalLandId, resource.getLegalLandId());
			Assert.assertEquals("OtherLegalDescription", otherLegalDescription, resource.getOtherLegalDescription());
		}
		
		logger.debug(">testSearchAnnualFieldsByLegalLand");
	}

	@Test
	public void testSearchAnnualFieldsByField() throws CirrasUnderwritingServiceException, Oauth2ClientException {
		logger.debug("<testSearchAnnualFieldsByField");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasUnderwritingService service = getService(SCOPES);

		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();
		Integer legalLandId = 8129;
		String fieldId = "22173";
		String cropYear = "2022";
		String otherLegalDescription = "NE SEC 19 TP 86 RNG 19 W 6TH M";

		AnnualFieldListRsrc searchResults = service.getAnnualFieldList(
				topLevelEndpoints, 
				null,
				fieldId,
				cropYear);

		Assert.assertNotNull(searchResults);
		
		List<AnnualFieldRsrc> fields = searchResults.getCollection();
		Assert.assertNotNull(fields);
		
		for (AnnualFieldRsrc resource: fields) {
			Assert.assertNotNull(resource.getPolicies());
			Assert.assertEquals("LegalLandId", legalLandId, resource.getLegalLandId());
			Assert.assertEquals("OtherLegalDescription", otherLegalDescription, resource.getOtherLegalDescription());
		}
		
		logger.debug(">testSearchAnnualFieldsByField");
	}
}
