package ca.bc.gov.mal.cirras.underwriting.service.api.v1.reports;

import java.util.Map;

public interface JasperReportService
{
	byte[] generateSampleCuwsReport() throws JasperReportServiceException;

	byte[] generateDopGrainReport(Map<String, String> paramMap) throws JasperReportServiceException;

	byte[] generateDopForageReport(Map<String, String> paramMap) throws JasperReportServiceException;
	
	byte[] generateInvForageReport(Map<String, String> paramMap) throws JasperReportServiceException;

	byte[] generateInvGrainReport(Map<String, String> paramMap) throws JasperReportServiceException;

	void setReportServiceUrl(String reportServiceUrl);

	void setReportServicePassword(String reportServicePassword);

	void setReportServiceUsername(String reportServiceUsername);
}