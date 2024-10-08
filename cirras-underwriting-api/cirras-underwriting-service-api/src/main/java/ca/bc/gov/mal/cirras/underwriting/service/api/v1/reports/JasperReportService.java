package ca.bc.gov.mal.cirras.underwriting.service.api.v1.reports;

import java.util.Map;

import javax.sql.DataSource;

public interface JasperReportService
{
	byte[] generateSampleCuwsReport() throws JasperReportServiceException;

	byte[] generateDopGrainReport(Map<String, Object> paramMap) throws JasperReportServiceException;

	byte[] generateDopForageReport(Map<String, Object> paramMap) throws JasperReportServiceException;
	
	byte[] generateInvForageReport(Map<String, Object> paramMap) throws JasperReportServiceException;

	byte[] generateInvGrainReport(Map<String, Object> paramMap) throws JasperReportServiceException;

	void setReportServiceUrl(String reportServiceUrl);

	void setReportServicePassword(String reportServicePassword);

	void setReportServiceUsername(String reportServiceUsername);

	void setCirrasUnderwritingDataSource(DataSource cirrasUnderwritingDataSource);
}