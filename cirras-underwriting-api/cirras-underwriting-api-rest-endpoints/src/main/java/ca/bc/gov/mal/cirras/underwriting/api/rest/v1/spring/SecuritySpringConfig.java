package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.spring;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import ca.bc.gov.nrs.wfone.common.webade.oauth2.authentication.WebadeOauth2AuthenticationProvider;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.TokenService;

@Configuration
@EnableWebSecurity(debug = false)
@Import({
	TokenServiceSpringConfig.class
})
public class SecuritySpringConfig {

	private static final Logger logger = LoggerFactory.getLogger(SecuritySpringConfig.class);
	
	private static final String DefaultScopes = "CIRRAS_UNDERWRITING.*";

	// Beans provided by TokenServiceSpringConfig
	// This allows Spring to use the proxied service
	@Autowired 
	@Qualifier("tokenService")
	TokenService tokenService;
	
	public SecuritySpringConfig() {
		super();
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
	AuthenticationEntryPoint authenticationEntryPoint() {
		BasicAuthenticationEntryPoint result;
		
		result = new BasicAuthenticationEntryPoint();
		result.setRealmName("cirras-underwriting-api");
		
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
	  public WebSecurityCustomizer webSecurityCustomizer() {
		return (web)-> web.ignoring().requestMatchers(
	        new AntPathRequestMatcher("/openapi.*", HttpMethod.OPTIONS.name()),
	        new AntPathRequestMatcher("/openapi.*", HttpMethod.GET.name()),
	        new AntPathRequestMatcher("/checkHealth", HttpMethod.OPTIONS.name()),
	        new AntPathRequestMatcher("/checkHealth", HttpMethod.GET.name())
	    );		
	  }

	  @Bean
	  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
		  .cors(cors -> cors.disable())
	      .oauth2ResourceServer(oauth2 -> oauth2.authenticationManagerResolver(authenticationManagerResolver()) )
	      .httpBasic(Customizer.withDefaults())
	      .authorizeHttpRequests(authorize -> authorize
	              .requestMatchers(HttpMethod.OPTIONS, "/openapi.*", "/checkHealth").permitAll()
	              .requestMatchers(HttpMethod.GET, "/openapi.*", "/checkHealth").permitAll()
	              .requestMatchers("/**").hasAuthority("CIRRAS_UNDERWRITING.GET_TOP_LEVEL")
	              .anyRequest().denyAll()
	      ).exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint()) );		
		return http.build();
	  }	

}
