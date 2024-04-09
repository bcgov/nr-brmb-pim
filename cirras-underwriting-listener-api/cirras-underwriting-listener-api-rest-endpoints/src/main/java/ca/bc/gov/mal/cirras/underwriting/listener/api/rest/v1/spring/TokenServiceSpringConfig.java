package ca.bc.gov.mal.cirras.underwriting.listener.api.rest.v1.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.target.HotSwappableTargetSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.TokenService;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.impl.TokenServiceImpl;

@Configuration
public class TokenServiceSpringConfig  {

	private static final Logger logger = LoggerFactory.getLogger(TokenServiceSpringConfig.class);
	
	public TokenServiceSpringConfig() {
		logger.info("<TokenServiceSpringConfig");
		
		logger.info(">TokenServiceSpringConfig");
	}

	@Value("${CIRRAS_UNDERWRITING_SYNC_USER}")
	private String webadeOauth2ClientId;

	@Value("${CIRRAS_UNDERWRITING_SYNC_SECRET}")
	private String webadeOauth2ClientSecret;
	
	@Value("${WEBADE_CHECK_TOKEN_URL}")
	private String webadeOauth2CheckTokenUrl;

	@Value("${WEBADE_GET_TOKEN_URL}")
	private String webadeOauth2TokenUrl;
	
	@Bean
	public TokenService tokenServiceImpl() {
		TokenServiceImpl result;
		
		result = new TokenServiceImpl(
				webadeOauth2ClientId, 
				webadeOauth2ClientSecret, 
				webadeOauth2CheckTokenUrl, 
				webadeOauth2TokenUrl);
		
		return result;
	}

	@Bean
	public HotSwappableTargetSource swappableTokenService() {
		HotSwappableTargetSource result;
		
		result = new HotSwappableTargetSource(tokenServiceImpl());
		
		return result;
	}

	@Bean
	public ProxyFactoryBean tokenService() {
		ProxyFactoryBean result;
		
		result = new ProxyFactoryBean();
		result.setTargetSource(swappableTokenService());
		
		return result;
	}
}
