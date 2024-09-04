package ca.bc.gov.mal.cirras.underwriting.spring;

import ca.bc.gov.nrs.wfone.common.webade.oauth2.authentication.WebadeOauth2AuthenticationProvider;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity(debug = false)
@Import({
	TokenServiceSpringConfig.class
})
public class SecuritySpringConfig extends WebSecurityConfigurerAdapter  {

	private static final Logger logger = LoggerFactory.getLogger(SecuritySpringConfig.class);

	private static final String DefaultScopes = "CIRRAS_UNDERWRITING.*";

	@Autowired
	@Qualifier("tokenService")
	TokenService tokenService;

	@Value("#{systemEnvironment['WEBADE_GET_TOKEN_URL']}")
	private String webadeOauth2TokenUrl;


	public SecuritySpringConfig() {
		super(true);
		logger.info("<SecuritySpringConfig");

		logger.info(">SecuritySpringConfig");
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		WebadeOauth2AuthenticationProvider result;

		result = new WebadeOauth2AuthenticationProvider(tokenService, DefaultScopes);

		return result;
	}

	@Bean
	public AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver() {
		AuthenticationManagerResolver<HttpServletRequest> result;

		result = new AuthenticationManagerResolver<HttpServletRequest>() {

			@Override
			public AuthenticationManager resolve(HttpServletRequest httpServletRequest) {

				return new AuthenticationManager() {

					@Override
					public Authentication authenticate(Authentication authentication) throws AuthenticationException {

						return authenticationProvider().authenticate(authentication);
					}};
			}};

		return result;
	}

	@Bean
	AuthenticationEntryPoint authenticationEntryPoint() {
		BasicAuthenticationEntryPoint result;

		result = new BasicAuthenticationEntryPoint();
		result.setRealmName("cirras-underwriting-ui-war");

		return result;
	}

	private static final Set<String> STATIC_METHODS = Arrays.asList(HttpMethod.OPTIONS, HttpMethod.GET).stream()
			.map(HttpMethod::name)
			.collect(Collectors.toSet());  
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		
		web.ignoring().requestMatchers(request->{
			String method = request.getMethod();
			return STATIC_METHODS.contains(method);
		});
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable();

		http.oauth2ResourceServer(oauth2 -> oauth2
						.authenticationManagerResolver(authenticationManagerResolver())
				)
				.authorizeRequests(authorize -> authorize
						.anyRequest().permitAll()
				)
				.exceptionHandling()
				.authenticationEntryPoint(authenticationEntryPoint());
	}
}
