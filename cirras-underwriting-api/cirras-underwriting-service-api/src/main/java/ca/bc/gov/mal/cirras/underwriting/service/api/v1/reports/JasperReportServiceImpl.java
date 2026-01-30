package ca.bc.gov.mal.cirras.underwriting.service.api.v1.reports;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

public class JasperReportServiceImpl implements JasperReportService
{
	private static final Logger logger = LoggerFactory.getLogger(JasperReportServiceImpl.class);

	private String reportServiceUrl;
	private String reportServiceUsername;
	private String reportServicePassword;
	private DataSource cirrasUnderwritingDataSource;

	@Override
	public byte[] generateSampleCuwsReport() throws JasperReportServiceException
	{
		byte[] reportContent = generateJasperReportInMemory("Sample_CUWS_Report", new HashMap<String, Object>());
		return reportContent;
	}

	@Override
	public byte[] generateDopGrainReport(Map<String, Object> paramMap) throws JasperReportServiceException {
		byte[] reportContent = generateJasperReportInMemory("CUWS_DOP", paramMap);		
		return reportContent;
	}

	@Override
	public byte[] generateDopForageReport(Map<String, Object> paramMap) throws JasperReportServiceException {
		byte[] reportContent = generateJasperReportInMemory("CUWS_DOP_Forage", paramMap);		
		return reportContent;
	}
	
	
	@Override
	public byte[] generateInvForageReport(Map<String, Object> paramMap) throws JasperReportServiceException {
		byte[] reportContent = generateJasperReportInMemory("CUWS_Inventory_Forage", paramMap);		
		return reportContent;
	}

	@Override
	public byte[] generateUnseededGrainReport(Map<String, Object> paramMap) throws JasperReportServiceException {
		byte[] reportContent = generateJasperReportInMemory("CUWS_Unseeded_Grain", paramMap);		
		return reportContent;
	}

	@Override
	public byte[] generateSeededGrainReport(Map<String, Object> paramMap) throws JasperReportServiceException {
		byte[] reportContent = generateJasperReportInMemory("CUWS_Seeded_Grain", paramMap);		
		return reportContent;
	}
	
	@Override
	public byte[] generateInvBerriesReport(Map<String, Object> paramMap) throws JasperReportServiceException {
		byte[] reportContent = generateJasperReportInMemory("CUWS_Inventory_Berries", paramMap);		
		return reportContent;
	}
	
	private byte[] generateJasperReportInMemory(String reportName, Map<String, Object> paramMap) throws JasperReportServiceException
	{
		// Check config settings.
		if ( cirrasUnderwritingDataSource == null ) {
			throw new JasperReportServiceException("cirrasUnderwritingDataSource is not set");
		}
		
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		InputStream reportInputStream = null;
		Connection dbConn = null;
		
		try {
			ClassPathResource reportResource = new ClassPathResource("reports/" + reportName + ".jasper");
			reportInputStream = reportResource.getInputStream();
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportInputStream);

			dbConn = cirrasUnderwritingDataSource.getConnection();
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, paramMap, dbConn);

			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));

			exporter.exportReport();
		} catch (JRException | IOException | SQLException e) {
			throw new JasperReportServiceException(e.getMessage(), e);
		} finally {
			if ( dbConn != null )  {
				try {
					dbConn.close();
				} catch ( SQLException e ) {
					logger.warn("An error occured when releasing the database connection for the report", e);
				}
			}
			
			if ( reportInputStream != null ) {
				try {
					reportInputStream.close();
				} catch (IOException e) {
					logger.warn("An error occured when releasing the jasper report input stream", e);
				}
			}
		}

		return byteArrayOutputStream.toByteArray();
	}

	//PIM-1557: Jasper reports are now generated from within UW API. So this method for accessing the Jasper Server is no longer used, and may 
	//          be removed in a future release.
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

	@Override
	public void setCirrasUnderwritingDataSource(DataSource cirrasUnderwritingDataSource) {
		this.cirrasUnderwritingDataSource = cirrasUnderwritingDataSource;
	}
}
