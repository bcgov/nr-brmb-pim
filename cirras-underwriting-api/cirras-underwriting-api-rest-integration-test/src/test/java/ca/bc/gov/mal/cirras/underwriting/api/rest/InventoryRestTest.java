package ca.bc.gov.mal.cirras.underwriting.api.rest;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.ValidationException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.impl.CirrasUnderwritingServiceImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.InventoryContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.EndpointsRsrc;
import ca.bc.gov.nrs.common.wfone.rest.resource.CodeRsrc;
import ca.bc.gov.nrs.common.wfone.rest.resource.CodeTableListRsrc;
import ca.bc.gov.nrs.common.wfone.rest.resource.CodeTableRsrc;
import ca.bc.gov.nrs.wfone.common.model.Code;
import ca.bc.gov.nrs.wfone.common.rest.client.RestClientServiceException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.resource.AccessToken;
import ca.bc.gov.webade.oauth2.rest.test.client.AuthorizationCodeService;
import ca.bc.gov.webade.oauth2.rest.test.client.impl.AuthorizationCodeServiceImpl;


public class InventoryRestTest {
	
	private static final Logger logger = LoggerFactory.getLogger(InventoryRestTest.class);
	
	protected static boolean skipTests = false;
	
	private static ApplicationContext applicationContext;

	private static Properties properties;

	private static CirrasUnderwritingService service;
	
	private static final String REDIRECT_URL = "http://www.redirecturi.com";
	
    private static final String INTERNAL = "Internal";

	@Value("${CIRRAS_UNDERWRITING_REST_URI}")
	private static String restContext;

	@Value("${WEBADE_OAUTH2_AUTHORIZE_URL}")
	public static String authorizeUrl;
	
	@BeforeClass
	public static void beforeClass() throws Exception {
		logger.debug("<beforeClass");

		applicationContext = new ClassPathXmlApplicationContext(new String[] { "classpath:/test-spring-config.xml" });

		properties = (Properties) applicationContext.getBean("applicationProperties");

		String testClientId = properties.getProperty("test.client.id");
		
		String governmentUserGuid = properties.getProperty("government.user.guid");
		String governmentUserName = properties.getProperty("government.user.name");
		String governmentUserSecret = properties.getProperty("government.user.secret");
		
		Assert.assertNotNull("'CIRRAS_UNDERWRITING_REST_URI' is a required environment variable	", restContext);
		Assert.assertNotNull("'test.client.id' is a required property", testClientId);
		Assert.assertNotNull("'government.user.guid' is a required property", governmentUserGuid);
		Assert.assertNotNull("'government.user.name' is a required property", governmentUserName);
		Assert.assertNotNull("'government.user.secret' is a required property", governmentUserSecret);
		
		String topLevelRestURL = restContext+"/";
		String scope = "CIRRAS_UNDERWRITING.*";

		logger.debug("testClientId="+testClientId);
		logger.debug("userAuthorizationUri="+authorizeUrl);
		logger.debug("restContext="+restContext);
		logger.debug("governmentUserGuid="+governmentUserGuid);
		logger.debug("governmentUserName="+governmentUserName);
		logger.debug("governmentUserSecret="+governmentUserSecret);
		
		AuthorizationCodeService authorizationCodeService = new AuthorizationCodeServiceImpl(testClientId, authorizeUrl);
		
		AccessToken token = authorizationCodeService.getImplicitToken(
			scope, 
			REDIRECT_URL, 
			null, // organizationId
			INTERNAL, 
			governmentUserGuid, 
			null, // siteMinderAuthoritativePartyIdentifier
			governmentUserName, 
			governmentUserSecret
		);
		logger.debug("token.getAccessToken()="+token.getAccessToken());
		
		service = new CirrasUnderwritingServiceImpl("Bearer "+token.getAccessToken());
		((CirrasUnderwritingServiceImpl) service).setTopLevelRestURL(topLevelRestURL);
		
		logger.debug(">beforeClass");
	}
	
	@AfterClass
	public static void teardown() {
		service = null;
	}

	
	static {
		disableSSLHostnameChecking();
		disableSSLCertificateChecking();
	}

	
	private static void disableSSLHostnameChecking() {
		HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
			@Override
			public boolean verify(String arg0, SSLSession arg1) {
				return true;
			}}
		);
	}
	
	
	/**
     * Disables the SSL certificate checking for new instances of {@link HttpsURLConnection} This has been created to
     * aid testing on a local box, not for use on production.
     */
    private static void disableSSLCertificateChecking() {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        	X509Certificate[] acceptedIssuers = null;
        	
            @Override
			public X509Certificate[] getAcceptedIssuers() {
                return acceptedIssuers;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            	acceptedIssuers = arg0;
            }
        } };

        try {
            SSLContext sc = SSLContext.getInstance("TLS");

            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    
    
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    // tests
    /////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	//@Test
	public void testSwagger() throws RestClientServiceException, CirrasUnderwritingServiceException {
		logger.debug("<testSwagger");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();
		Assert.assertNotNull(topLevelEndpoints);
		
		String swaggerString = service.getSwaggerString();

		logger.debug(swaggerString);
		Assert.assertNotNull(swaggerString);
		
		logger.debug("<testSwagger");
	}

	
	@Test
	public void testTopLevelEndpoints() throws CirrasUnderwritingServiceException {
		logger.debug("<testTopLevelEndpoints");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();
		Assert.assertNotNull(topLevelEndpoints);
	}
	
	
	//@Test
	public void testCodeTables() throws CirrasUnderwritingServiceException {
		logger.debug("<testCodeTables");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();
		
		String codeTableName = null;
		LocalDate effectiveAsOfDate = LocalDate.now();
		CodeTableListRsrc codeTables = service.getCodeTables(topLevelEndpoints, codeTableName, effectiveAsOfDate);
		Assert.assertNotNull(codeTables);
		List<CodeTableRsrc> codeTableList = codeTables.getCodeTableList();
		Assert.assertNotNull(codeTableList);
		Assert.assertEquals(7, codeTableList.size());
		
		for(CodeTableRsrc codeTable:codeTableList) {
			codeTable = service.getCodeTable(codeTable, effectiveAsOfDate);
			Assert.assertNotNull(codeTable);
			
			List<CodeRsrc> codes = codeTable.getCodes();
			Assert.assertNotNull(codes);
			
			for(Code code:codes) {
				Assert.assertNotNull(code.getCode());
				Assert.assertNotNull(code.getDescription());
			}
		}

		logger.debug(">testCodeTables");
	}
	

}
