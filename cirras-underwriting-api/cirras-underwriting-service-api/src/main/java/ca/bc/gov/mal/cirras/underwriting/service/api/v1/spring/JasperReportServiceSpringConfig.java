package ca.bc.gov.mal.cirras.underwriting.service.api.v1.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.bc.gov.mal.cirras.underwriting.service.api.v1.reports.JasperReportService;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.reports.JasperReportServiceImpl;

@Configuration
public class JasperReportServiceSpringConfig
{
	private static final Logger logger = LoggerFactory.getLogger(JasperReportServiceSpringConfig.class);

	public JasperReportServiceSpringConfig()
	{
		logger.debug("<JasperReportServiceSpringConfig");

		logger.debug(">JasperReportServiceSpringConfig");
	}

	@Value("${cirras.underwriting.report.service.url}")
	private String reportServiceUrl;

	@Value("${cirras.underwriting.report.service.username}")
	private String reportServiceUsername;

	@Value("${cirras.underwriting.report.service.secret}")
	private String reportServicePassword;

	@Bean
	public JasperReportService jasperReportService()
	{
		JasperReportService result;

		logger.debug("reportServiceUrl: {}", reportServiceUrl);
		logger.debug("reportServiceUsername: {}", reportServiceUsername);
		logger.debug("reportServicePassword: {}", (reportServicePassword != null && reportServicePassword.length() > 0) ? "has value" : "DOES NOT have value");
		
		result = new JasperReportServiceImpl();

		result.setReportServiceUrl(reportServiceUrl);
		result.setReportServicePassword(reportServicePassword);
		result.setReportServiceUsername(reportServiceUsername);

		return result;
	}
}
