package ca.bc.gov.mal.cirras.underwriting.controllers;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ca.bc.gov.mal.cirras.underwriting.clients.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.clients.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.test.EndpointsTest;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;
import ca.bc.gov.nrs.common.wfone.rest.resource.RelLink;
import ca.bc.gov.nrs.wfone.common.rest.client.GenericRestDAO;
import ca.bc.gov.nrs.wfone.common.rest.client.Response;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.resource.CheckedToken;


public class CheckControllerTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(CheckControllerTest.class);
	
	private static final String[] SCOPES = {
			Scopes.GET_TOP_LEVEL
		};
	
	@Test
	public void testCheckToken() throws Exception { 
		logger.debug("<testCheckToken");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasUnderwritingService service = getService(SCOPES);
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		// this should return some token as we logged in in getService(SCOPES);
		CheckedToken results = service.checkToken( topLevelEndpoints);
		Assert.assertNotNull("Result should not be null", results);
		
		logger.debug(">testCheckToken");
	}
	
	// test with no token
	@Test
	public void testNoAuthorization() {
		logger.debug("<testNoAuthorization");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		CirrasUnderwritingService service = new CirrasUnderwritingService() {
			@Override
			public CheckedToken checkToken(EndpointsRsrc parent)
					throws CirrasUnderwritingServiceException  {
				logger.debug("<getCheckTokenNoAuth");

				CheckedToken result = null;

				try {
					
					Map<String,String> queryParams = new HashMap<String,String>();

					GenericRestDAO<CheckedToken> dao = this.getRestDAOFactory()
							.getGenericRestDAO(CheckedToken.class);
					
					Response<CheckedToken> response = dao.Process(
							ResourceTypes.CHECK_TOKEN, getTransformer(), new BaseResource() {

								private static final long serialVersionUID = 1L;

								@Override
								public List<RelLink> getLinks() {
									List<RelLink> links = new ArrayList<RelLink>();
									links.add(new RelLink(ResourceTypes.CHECK_TOKEN,
											getTopLevelRestURL() + "checkToken", "GET"));
									return links;
								}
							}, queryParams, getWebClient()); // this throws an error: Unauthorized

					result = response.getResource();

				} catch (Throwable e) {
					logger.error(e.getMessage(), e);
					throw new CirrasUnderwritingServiceException(e);
				}

				logger.debug(">getCheckTokenNoAuth");
				return result;
			}
		};
		((CirrasUnderwritingService) service).setTopLevelRestURL(topLevelRestURL);
		
		try {
			CheckedToken checkToken = service.checkToken( null); // should throw an error
			Assert.fail();
		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
			// test has passed
			Assert.assertTrue(t.getMessage().toLowerCase().contains("unauthorized"));
		}
		
		logger.debug(">testNoAuthorization");
	}
}
