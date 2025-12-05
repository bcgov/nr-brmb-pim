package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints;

import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.common.wfone.rest.resource.HealthCheckResponseRsrc;
import ca.bc.gov.nrs.wfone.common.http.AbstractHttpServletRequest;
import ca.bc.gov.nrs.wfone.common.rest.client.RestClientServiceException;
import ca.bc.gov.nrs.wfone.common.utils.HttpServletRequestHolder;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.resource.AccessToken;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.impl.CirrasUnderwritingServiceImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;

public class CheckHealthEndpointsTest extends EndpointsTest {
	
	private static final Logger logger = LoggerFactory.getLogger(CheckHealthEndpointsTest.class);

	private static final String WebadeOauth2ClientGuid = "9374JD83HD94JSLE893H3N58DJE74999";
	private static final String WebadeOauth2ClientId = "TEST_SERVICE_CLIENT";
	private static final String WebadeOauth2ClientSecret = "password";
	private static final String Scope = "CIRRAS_UNDERWRITING.GET_TOP_LEVEL";
	
	@Test
	public void testNoAuthorization() throws RestClientServiceException {
		logger.debug("<testNoAuthorization");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		CirrasUnderwritingService service = new CirrasUnderwritingServiceImpl();
		((CirrasUnderwritingServiceImpl) service).setTopLevelRestURL(topLevelRestURL);
		
		HealthCheckResponseRsrc healthCheckResponse = service.getHealthCheck("test");
		
		Assert.assertNotNull(healthCheckResponse);
		
		logger.debug(">testNoAuthorization");
	}
	
	@Test
	public void testAuthorizationCode() throws Oauth2ClientException, RestClientServiceException {
		logger.debug("<testAuthorizationCode");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		String authorizationCode = UUID.randomUUID().toString();
		
		tokenService.selectUser("GOV", "C142019F4E3B4E969F148A156F07E8C7", null, null);
		AccessToken token = tokenService.getToken(WebadeOauth2ClientId, WebadeOauth2ClientSecret, Scope, authorizationCode, Scope);

		CirrasUnderwritingService service = new CirrasUnderwritingServiceImpl("Bearer "+token.getAccessToken());
		((CirrasUnderwritingServiceImpl) service).setTopLevelRestURL(topLevelRestURL);
		
		HealthCheckResponseRsrc healthCheckResponse = service.getHealthCheck("test");
		
		Assert.assertNotNull(healthCheckResponse);
		
		logger.debug(">testAuthorizationCode");
	}
	
	@Test
	public void testClientCredentials() throws Oauth2ClientException, RestClientServiceException {
		logger.debug("<testClientCredentials");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		tokenService.selectServiceClient(WebadeOauth2ClientId, WebadeOauth2ClientGuid, Scope);
		AccessToken token = tokenService.getToken(WebadeOauth2ClientId, WebadeOauth2ClientSecret, Scope);

		CirrasUnderwritingService service = new CirrasUnderwritingServiceImpl("Bearer "+token.getAccessToken());
		((CirrasUnderwritingServiceImpl) service).setTopLevelRestURL(topLevelRestURL);
		
		HealthCheckResponseRsrc healthCheckResponse = service.getHealthCheck("test");
		
		Assert.assertNotNull(healthCheckResponse);
		
		logger.debug(">testClientCredentials");
	}
	
	@Test
	public void testBasicCredentials() throws RestClientServiceException {
		logger.debug("<testBasicCredentials");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		CirrasUnderwritingService service = new CirrasUnderwritingServiceImpl("TEST_SERVICE_CLIENT", "password");
		((CirrasUnderwritingServiceImpl) service).setTopLevelRestURL(topLevelRestURL);
		
		tokenService.selectServiceClient(WebadeOauth2ClientId, WebadeOauth2ClientGuid, Scope);
		
		HealthCheckResponseRsrc healthCheckResponse = service.getHealthCheck("test");
		
		Assert.assertNotNull(healthCheckResponse);
		
		logger.debug(">testBasicCredentials");
	}
	
	@Test
	public void testChainedAuthorization() throws RestClientServiceException, Oauth2ClientException {
		logger.debug("<testChainedAuthorization");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		tokenService.selectServiceClient(WebadeOauth2ClientId, WebadeOauth2ClientGuid, Scope);
		AccessToken token = tokenService.getToken(WebadeOauth2ClientId, WebadeOauth2ClientSecret, Scope);
		
		String authorizationHeaderValue = "Bearer "+token.getAccessToken();
		
		HttpServletRequest httpServletRequest = new AbstractHttpServletRequest() {
			
			@Override
			public String getHeader(String name) {
				String result = null;
				
				if("Authorization".equals(name)) {
					
					result = authorizationHeaderValue;
				}
				
				return result;
			}
		};
		
		HttpServletRequestHolder.setHttpServletRequest(httpServletRequest);

		CirrasUnderwritingService service = new CirrasUnderwritingServiceImpl();
		((CirrasUnderwritingServiceImpl) service).setTopLevelRestURL(topLevelRestURL);
		
		HealthCheckResponseRsrc healthCheckResponse = service.getHealthCheck("test");
		
		Assert.assertNotNull(healthCheckResponse);
		
		logger.debug(">testChainedAuthorization");
	}
	
}
