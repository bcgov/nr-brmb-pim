package ca.bc.gov.mal.cirras.underwriting.spring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.CorsConfigurationSource;
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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

	@Autowired
	@Qualifier("tokenService")
	TokenService tokenService;

	@Value("#{systemEnvironment['WEBADE_GET_TOKEN_URL']}")
	private String webadeOauth2TokenUrl;


	public SecuritySpringConfig() {
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

	@Bean
	@Order(0)
	SecurityFilterChain resources(HttpSecurity http) throws Exception {
		RequestMatcher optionsMatcher = new AntPathRequestMatcher("/**", HttpMethod.OPTIONS.name());
		RequestMatcher getMatcher = new AntPathRequestMatcher("/**", HttpMethod.GET.name());

		http
			.securityMatchers(matchers -> matchers.requestMatchers(optionsMatcher, getMatcher))
			.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
			.requestCache(cache -> cache.disable())
			.securityContext(context -> context.disable())
			.sessionManagement(session -> session.disable());

		return http.build();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
        //     .csrf(csrf -> csrf.disable());
            
		http.oauth2ResourceServer(oauth2 -> oauth2
						.authenticationManagerResolver(authenticationManagerResolver())
				)
				.authorizeHttpRequests(authorize -> authorize
						.anyRequest().permitAll()
				)
				.exceptionHandling(exception -> exception
				    .authenticationEntryPoint(authenticationEntryPoint()));

		return http.build();
	}

	// @Bean
	// public CorsConfigurationSource corsConfigurationSource() {
	// 	final CorsConfiguration configuration = new CorsConfiguration();

	// 	List<String> origins = new ArrayList<>();
	// 	origins.add("*");

	// 	configuration.setAllowedOrigins(origins);
	// 	configuration.setAllowedMethods(Collections.unmodifiableList(Arrays.asList("HEAD", "GET", "POST", "OPTIONS")));
	// 	configuration.setAllowCredentials(true);
	// 	configuration.setAllowedHeaders(origins);

	// 	final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	// 	source.registerCorsConfiguration("/**", configuration);

	// 	return source;
	// }
}
