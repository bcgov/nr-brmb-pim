package ca.bc.gov.mal.cirras.underwriting.listener.api.rest.client.v1.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.listener.api.rest.client.v1.CirrasUnderwritingListenerService;
import ca.bc.gov.nrs.wfone.common.rest.client.BaseRestServiceClient;

public class CirrasUnderwritingListenerServiceImpl extends BaseRestServiceClient implements CirrasUnderwritingListenerService {

	private static final Logger logger = LoggerFactory.getLogger(CirrasUnderwritingListenerServiceImpl.class);
	
	public static final String CLIENT_VERSION = "1";

	private static final String Scopes = "CIRRAS_UNDERWRITING.*";
	
	/**
	 * Constructor used for making OAuth2 Client Credentials requests
	 * @param webadeOauth2ClientId
	 * @param webadeOauth2ClientSecret
	 * @param webadeOauth2TokenUrl
	 */
	public CirrasUnderwritingListenerServiceImpl(String webadeOauth2ClientId, String webadeOauth2ClientSecret, String webadeOauth2TokenUrl) {
		super(webadeOauth2ClientId, webadeOauth2ClientSecret, webadeOauth2TokenUrl, Scopes);
		logger.debug("<PayrollEmployeeSyncServiceImpl");
		
		logger.debug(">PayrollEmployeeSyncServiceImpl");
	}
	
	/**
	 * Constructor used for making requests with basic credentials
	 * 
	 * @param headerValue
	 */
	public CirrasUnderwritingListenerServiceImpl(String webadeOauth2ClientId, String webadeOauth2ClientSecret) {
		super(webadeOauth2ClientId, webadeOauth2ClientSecret);
		logger.debug("<PayrollEmployeeSyncServiceImpl");
		
		logger.debug(">PayrollEmployeeSyncServiceImpl");
	}
	
	/**
	 * Constructor used for making requests with basic credentials
	 * 
	 * @param webadeOauth2ClientId
	 * @param webadeOauth2ClientSecret
	 */
	public CirrasUnderwritingListenerServiceImpl(String headerValue) {
		super(headerValue);
		logger.debug("<PayrollEmployeeSyncServiceImpl");
		
		logger.debug(">PayrollEmployeeSyncServiceImpl");
	}
	
	/**
	 * Constructor used for making requests using the authorization header of the current HttpServletRequest
	 * 
	 */
	public CirrasUnderwritingListenerServiceImpl() {
		super();
		logger.debug("<PayrollEmployeeSyncServiceImpl");
		
		logger.debug(">PayrollEmployeeSyncServiceImpl");
	}
	
	@Override
	public String getClientVersion() {
		return CLIENT_VERSION;
	}

	protected static String toQueryParam(String value) throws UnsupportedEncodingException {
		String result = null;
		if(value != null) {
			result = URLEncoder.encode(value, "UTF-8");
		}
		return result;
	}
	
	protected static String toQueryParam(LocalDate value) {
		String result = null;
		if(value != null) {
			result = value.format(DateTimeFormatter.ISO_LOCAL_DATE);
		}
		return result;
	}

	protected static String toQueryParam(Number value) {
		String result = null;
		
		if(value!=null) {
			result = value.toString();
		}
		
		return result;
	}

	protected static String toQueryParam(Boolean value) {
		String result = null;
		
		if(value!=null) {
			result = value.toString();
		}
		
		return result;
	}
}
