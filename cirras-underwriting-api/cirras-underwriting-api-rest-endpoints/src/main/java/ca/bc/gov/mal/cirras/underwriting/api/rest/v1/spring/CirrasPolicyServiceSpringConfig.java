package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.target.HotSwappableTargetSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.bc.gov.mal.cirras.policies.api.rest.client.v1.CirrasPolicyService;
import ca.bc.gov.mal.cirras.policies.api.rest.client.v1.impl.CirrasPolicyServiceImpl;

@Configuration
public class CirrasPolicyServiceSpringConfig  {

	private static final Logger logger = LoggerFactory.getLogger(CirrasPolicyServiceSpringConfig.class);
	
	public CirrasPolicyServiceSpringConfig() {
		logger.info("<CirrasPolicyServiceSpringConfig");
		
		logger.info(">CirrasPolicyServiceSpringConfig");
	}

	
	@Value("${webade-oauth2.cirras-underwriting-rest.client.id}")
	private String webadeOauth2ClientId;

	@Value("${webade-oauth2.cirras-underwriting-rest.client.secret}")
	private String webadeOauth2ClientSecret;

	@Value("${webade-oauth2.token.url}")
	private String webadeOauth2TokenUrl;
	
	@Value("${cirras-underwriting-rest.policies-api-url}")
	private String cirrasPoliciesServiceTopLevelRestURL;
	
	// I don't know if this should be a preference or not
	private String scopes = "CIRRAS_CLAIMS.*";
	
	@Bean
	public CirrasPolicyService cirrasPolicyServiceImpl() {
		CirrasPolicyServiceImpl serviceImpl = new CirrasPolicyServiceImpl(webadeOauth2ClientId, webadeOauth2ClientSecret, webadeOauth2TokenUrl, scopes);
		
		serviceImpl.setTopLevelRestURL(cirrasPoliciesServiceTopLevelRestURL);

		return serviceImpl;
	}
	

	
	@Bean
	public HotSwappableTargetSource swappableCirrasPolicyService() {
		HotSwappableTargetSource result = new HotSwappableTargetSource(cirrasPolicyServiceImpl());
		
		return result;
	}

	
	@Bean
	public ProxyFactoryBean cirrasPolicyService() {
		ProxyFactoryBean result = new ProxyFactoryBean();
		
		result.setTargetSource(swappableCirrasPolicyService());
		
		return result;
	}
}
