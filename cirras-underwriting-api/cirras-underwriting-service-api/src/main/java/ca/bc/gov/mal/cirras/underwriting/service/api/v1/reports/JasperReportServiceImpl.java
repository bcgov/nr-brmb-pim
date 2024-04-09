package ca.bc.gov.mal.cirras.underwriting.service.api.v1.reports;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JasperReportServiceImpl implements JasperReportService
{
	private static final Logger logger = LoggerFactory.getLogger(JasperReportServiceImpl.class);

	private String reportServiceUrl;
	private String reportServiceUsername;
	private String reportServicePassword;

	@Override
	public byte[] generateSampleCuwsReport() throws JasperReportServiceException
	{
		byte[] reportContent = generateJasperReport("Sample_CUWS_Report", "pdf", null);		
		return reportContent;
	}

	public byte[] generateDopReport(Map<String, String> paramMap) throws JasperReportServiceException {
		byte[] reportContent = generateJasperReport("CUWS_DOP", "pdf", paramMap);		
		return reportContent;
	}

	@Override
	public byte[] generateInvForageReport(Map<String, String> paramMap) throws JasperReportServiceException {
		byte[] reportContent = generateJasperReport("CUWS_Inventory_Forage", "pdf", paramMap);		
		return reportContent;
	}

	@Override
	public byte[] generateInvGrainReport(Map<String, String> paramMap) throws JasperReportServiceException {
		byte[] reportContent = generateJasperReport("CUWS_Inventory_Grain", "pdf", paramMap);		
		return reportContent;
	}
	
	
	private byte[] generateJasperReport(String reportName, String reportFormat, Map<String, String> paramMap) throws JasperReportServiceException
	{
		// Check config settings.
		if ( reportServiceUrl == null || reportServiceUrl.trim().isEmpty() ) {
			throw new JasperReportServiceException("reportServiceUrl is not set");

		} else if ( reportServiceUsername == null || reportServiceUsername.trim().isEmpty() ) {
			throw new JasperReportServiceException("reportServiceUsername is not set");

		} else if ( reportServicePassword == null || reportServicePassword.trim().isEmpty() ) {
			throw new JasperReportServiceException("reportServicePassword is not set");
		}
		
		HttpURLConnection httpConn = null;
		String charset = "UTF-8";

		byte[] reportContent = null;
		String contentType = "application/" + reportFormat;

		try
		{
			// Add basic authentication to the request header.
			String authText = this.reportServiceUsername + ":" + this.reportServicePassword;
			byte[] authTextBytes = authText.getBytes();
			byte[] authTextBytesEncoded = Base64.getEncoder().encode(authTextBytes);
			String authTextEncoded = new String(authTextBytesEncoded);

			String basicAuth = "Basic " + authTextEncoded;

			String reportUrl = buildReportUrl(reportName, reportFormat, paramMap, charset);

			logger.info("Application Report Url: {}", reportUrl);

			httpConn = (HttpURLConnection)new URL(reportUrl).openConnection();
			HttpURLConnection.setFollowRedirects(false);
			httpConn.setRequestProperty("Accept-Charset", charset);
			httpConn.setRequestProperty("Authorization", basicAuth);
			httpConn.setRequestProperty("Accept", contentType);
			httpConn.setRequestMethod("GET");
			httpConn.setDoInput(true);
			httpConn.setDoOutput(false);
			
			int responseCode = httpConn.getResponseCode();

			logger.info("Response Code: {}", responseCode);

			if (responseCode == HttpURLConnection.HTTP_OK)
			{
				String respContentType = httpConn.getContentType();
				
				if (contentType.equals(respContentType)) {
					InputStream response = httpConn.getInputStream();
					reportContent = IOUtils.toByteArray(response);
					response.close();

				} else {
					throw new JasperReportServiceException("Call to Jasper Server returned unexpected content type: " + respContentType);
				}
			} 
			else 
			{
				throw new JasperReportServiceException("Call to Jasper Server returned error: response code " + responseCode);
			}
		}
		catch (IOException e)
		{
			throw new JasperReportServiceException(e.getMessage(), e);
		}
		finally
		{
			if (httpConn != null)
			{
				httpConn.disconnect();
			}
		}

		// TODO: Should we limit the possible response size?
		return reportContent;
	}
	
	private String buildReportUrl(String reportName, String reportFormat, Map<String, String> paramMap, String charset) throws UnsupportedEncodingException {
		StringBuilder reportUrl = new StringBuilder();
		
		reportUrl.append(reportServiceUrl)
		         .append("rest_v2/reports/NRSRS/CUWS/Reports/")
		         .append(reportName)
		         .append(".")
		         .append(reportFormat);

		if ( paramMap != null && !paramMap.isEmpty() ) {
			
			String delim = "?";
			for ( Map.Entry<String, String> param : paramMap.entrySet() ) {
				reportUrl.append(delim)
				         .append(param.getKey())
				         .append("=")
				         .append(URLEncoder.encode(param.getValue(), charset));
				
				delim = "&";
			}
		}
		
		return reportUrl.toString();
	}
	
	@Override
	public void setReportServiceUsername(String reportServiceUsername)
	{
		this.reportServiceUsername = reportServiceUsername;
	}

	@Override
	public void setReportServicePassword(String reportServicePassword)
	{
		this.reportServicePassword = reportServicePassword;
	}

	@Override
	public void setReportServiceUrl(String reportServiceUrl)
	{
		this.reportServiceUrl = reportServiceUrl;
	}
}
