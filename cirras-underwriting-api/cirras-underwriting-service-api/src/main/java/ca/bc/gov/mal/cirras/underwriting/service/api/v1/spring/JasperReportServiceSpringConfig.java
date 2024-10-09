package ca.bc.gov.mal.cirras.underwriting.service.api.v1.spring;

import javax.sql.DataSource;

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

	@Value("${JASPER_URL}")
	private String reportServiceUrl;

	@Value("${JASPER_USERNAME}")
	private String reportServiceUsername;

	@Value("${JASPER_PASSWORD}")
	private String reportServicePassword;

	@Bean
	public JasperReportService jasperReportService(DataSource cirrasUnderwritingDataSource)
	{
		JasperReportService result;

		logger.debug("reportServiceUrl: {}", reportServiceUrl);
		logger.debug("reportServiceUsername: {}", reportServiceUsername);
		logger.debug("reportServicePassword: {}", (reportServicePassword != null && reportServicePassword.length() > 0) ? "has value" : "DOES NOT have value");
		logger.debug("cirrasUnderwritingDataSource: {}", cirrasUnderwritingDataSource != null ? "has value" : "DOES NOT have value");
		
		result = new JasperReportServiceImpl();

		//PIM-1557: These three properties are for accessing the Jasper Server, which is no longer
		//          being used. They are still in place for now, but it's safe to set to null.
		result.setReportServiceUrl(reportServiceUrl);
		result.setReportServicePassword(reportServicePassword);
		result.setReportServiceUsername(reportServiceUsername);

		result.setCirrasUnderwritingDataSource(cirrasUnderwritingDataSource);

		return result;
	}
}
