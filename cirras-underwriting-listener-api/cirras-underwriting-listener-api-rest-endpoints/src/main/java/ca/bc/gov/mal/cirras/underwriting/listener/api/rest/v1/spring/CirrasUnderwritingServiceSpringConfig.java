package ca.bc.gov.mal.cirras.underwriting.listener.api.rest.v1.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.target.HotSwappableTargetSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.bc.gov.mal.cirras.underwriting.clients.CirrasUnderwritingService;

@Configuration
public class CirrasUnderwritingServiceSpringConfig  {

	private static final Logger logger = LoggerFactory.getLogger(CirrasUnderwritingServiceSpringConfig.class);
	
	public CirrasUnderwritingServiceSpringConfig() {
		logger.info("<CirrasUnderwritingServiceSpringConfig");
		
		logger.info(">CirrasUnderwritingServiceSpringConfig");
	}

	@Value("${webade-oauth2.cirras-underwriting-sync-rest.client.id}")
	private String webadeOauth2ClientId;

	@Value("${webade-oauth2.cirras-underwriting-sync-rest.client.secret}")
	private String webadeOauth2ClientSecret;

	@Value("${webade-oauth2.token.url}")
	private String webadeOauth2TokenUrl;
	
	@Value("${cirras-underwriting-rest.url}")
	private String cirrasUnderwritingServiceTopLevelRestURL;
	
	// I don't know if this should be a preference or not
	private String scopes = "CIRRAS_UNDERWRITING.*";
	
	
	@Bean
	public CirrasUnderwritingService cirrasUnderwritingServiceImpl() {
		CirrasUnderwritingService serviceImpl = new CirrasUnderwritingService(webadeOauth2ClientId, webadeOauth2ClientSecret, webadeOauth2TokenUrl, scopes);
		
		serviceImpl.setTopLevelRestURL(cirrasUnderwritingServiceTopLevelRestURL);

		return serviceImpl;
	}

	
	@Bean
	public HotSwappableTargetSource swappableCirrasUnderwritingService() {
		HotSwappableTargetSource result = new HotSwappableTargetSource(cirrasUnderwritingServiceImpl());
		
		return result;
	}
	
	
	@Bean
	public ProxyFactoryBean cirrasUnderwritingService() {
		ProxyFactoryBean result = new ProxyFactoryBean();
		
		result.setTargetSource(swappableCirrasUnderwritingService());
		
		return result;
	}
}
