package ca.bc.gov.mal.cirras.underwriting.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
  private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
    .allowedHeaders("*")
    //TODO: Update origins with openshift URL patterns instead of allowing everything
    .allowedOrigins("*")
    .allowedMethods("*");
  }
}