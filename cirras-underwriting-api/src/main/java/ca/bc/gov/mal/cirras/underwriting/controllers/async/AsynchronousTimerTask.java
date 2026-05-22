package ca.bc.gov.mal.cirras.underwriting.controllers.async;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimerTask;
import java.util.UUID;

import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import ca.bc.gov.mal.cirras.underwriting.services.utils.EmailUtils;
import ca.bc.gov.mal.cirras.underwriting.services.utils.PropertyUtils;
import ca.bc.gov.nrs.wfone.common.model.Message;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

public abstract class AsynchronousTimerTask extends TimerTask {
	
	public static final String EMAIL_FROM_ADDRESS_KEY = "EMAIL_FROM_ADDRESS";
	public static final String EMAIL_ERROR_TO_KEY = "EMAIL_ADMIN_ADDRESS";
	public static final String EMAIL_ERROR_SEND_FREQUENCY_KEY = "EMAIL_ERROR_SEND_FREQUENCY";
	public static final String ENVIRONMENT_KEY = "APPLICATION_ENVIRONMENT_NAME";
	
	private static final String ENVIRONMENT_PLACE_HOLDER = "%environment%";
	
	protected static final String ERROR_TYPE_UNRECOVERABLE = "Unrecoverable error";

	private static final String NEW_EXPIRY_DATE_MINUTES = "NODE_NEW_EXPIRY_DATE_MINUTES";
	
	public static final String OUTBOX_POLLING_ENABLED_KEY = "POLLING_OUTBOX_ENABLED";
	public static final String OUTBOX_POLLING_TIME_KEY = "POLLING_OUTBOX_TIME";
	public static final String OUTBOX_POLLING_SECONDS_FREQUENCY_KEY = "POLLING_OUTBOX_SECONDS_FREQUENCY";
	public static long outboxPoolingSecondsFrequency = 2*60*60;
	
	private AuthenticationProvider authenticationProvider;
	private UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken;

	private Session emailSession;
	private String emailFrom;
	private String rawAddresses;
	private InternetAddress[] toAddresses;
	private String environment;
	private String subjectTemplate;
	private String subject;
	private long emailFrequency;

	protected int nodeExpiryMinutes;
	
	private List<String> errorTypes = new ArrayList<>();
	private Map<String, List<String>> errorMessages = new HashMap<>();
	private Instant lastErrorEmailSent;

	protected Properties applicationProperties;

	private static final Logger logger = LoggerFactory.getLogger(AsynchronousTimerTask.class);
	
	protected AsynchronousTimerTask(Properties applicationProperties) throws AddressException {

		this.applicationProperties = applicationProperties;

		String webadeCheckTokenURL = applicationProperties.getProperty("WEBADE_CHECK_TOKEN_URL");
		if ( webadeCheckTokenURL == null ) {
			logger.info("TEST WEBADE URL PROP: NULL");
		} else {
			logger.info("TEST WEBADE URL PROP: " + webadeCheckTokenURL);
		}
		
		emailSession = EmailUtils.getEmailSession(applicationProperties);

		emailFrom = PropertyUtils.getProperty(applicationProperties, EMAIL_FROM_ADDRESS_KEY);
		if(emailFrom == null || emailFrom.trim().length() == 0) {
			
			throw new RuntimeException("Missing property '" + EMAIL_FROM_ADDRESS_KEY + "'");
		}
		
		rawAddresses = PropertyUtils.getProperty(applicationProperties, EMAIL_ERROR_TO_KEY);
		if(rawAddresses==null||rawAddresses.trim().length()==0) {
			
			throw new RuntimeException("Missing property '"+EMAIL_ERROR_TO_KEY+"'");
		}
		
		if(rawAddresses==null) {
			toAddresses = new InternetAddress[] {};
		} else {
			String[] split = rawAddresses.split(";");
			
			toAddresses = new InternetAddress[split.length];
			
			for(int i=0;i<split.length;++i) {
				
				toAddresses[i] = new InternetAddress(split[i]);
			}
		}
		environment = PropertyUtils.getProperty(applicationProperties, ENVIRONMENT_KEY);
		if(environment==null||environment.trim().length()==0) {
			
			throw new RuntimeException("Missing property '"+ENVIRONMENT_KEY+"'");
		}
		
		subjectTemplate = PropertyUtils.getProperty(applicationProperties, getEmailSubjectPropertyKey());
		if(subjectTemplate==null||subjectTemplate.trim().length()==0) {
			
			throw new RuntimeException("Missing property '"+getEmailSubjectPropertyKey()+"'");
		}
		
		subject = subjectTemplate.replace(ENVIRONMENT_PLACE_HOLDER, environment.toUpperCase());
		
		emailFrequency = PropertyUtils.getProperty(applicationProperties, EMAIL_ERROR_SEND_FREQUENCY_KEY, 10) * 1000 * 60;

		nodeExpiryMinutes = (int)PropertyUtils.getProperty(applicationProperties, NEW_EXPIRY_DATE_MINUTES, 10);
		
	}

	protected WebAdeAuthentication getWebAdeAuthentication() {
		WebAdeAuthentication result = null;
		
		if(usernamePasswordAuthenticationToken!=null) {
			
			result = (WebAdeAuthentication) this.authenticationProvider.authenticate(usernamePasswordAuthenticationToken);
		}
		
		return result;
	}
	
	protected abstract Logger getLogger();
	
	protected void printErrors(List<Message> errors) {

		getLogger().error("Printing errors...");

		if(errors==null||errors.isEmpty()) {
			
			getLogger().error("No error messages.");
		} else {
			
			for(Message error:errors) {
				
				String message = "\nmessage: "+error.getMessage()+"\nmessageTemplate: "+error.getMessageTemplate()+"\npath: "+error.getPath();
				String[] messageArguments = error.getMessageArguments();
				
				if(messageArguments!=null) {
					
					message += "\nmessageArguments: ";
					
					for(int i=0;i<messageArguments.length;++i) {
						
						message += "\""+messageArguments[i]+"\"";
						
						if(i<messageArguments.length-1) {
							
							message += ",";
						}
					}
				}
				
				getLogger().error(message);
			}
		}
	}
	
	protected String formatErrorsAsHtml(List<Message> errors) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("<ul>");
		
		if(errors==null||errors.isEmpty()) {
			
			sb.append("<li>No error messages.</li>");
		} else {
			
			for(Message error:errors) {
				sb.append("<li>");
				
				sb.append("message: ")
				.append(StringEscapeUtils.escapeHtml4(error.getMessage()))
				.append("<br />messageTemplate: ")
				.append(StringEscapeUtils.escapeHtml4(error.getMessageTemplate()))
				.append("<br />path: ")
				.append(StringEscapeUtils.escapeHtml4(error.getPath()));
				String[] messageArguments = error.getMessageArguments();
				
				if(messageArguments!=null) {
					
					sb.append("<br />messageArguments: ");
					
					for(int i=0;i<messageArguments.length;++i) {
						
						sb.append("\"");
						sb.append(StringEscapeUtils.escapeHtml4(messageArguments[i]));
						sb.append("\"");
						
						if(i<messageArguments.length-1) {
							sb.append(",");
						}
					}
				}
				
				sb.append("</li>");
			}
		}
		
		sb.append("</ul>");
		return sb.toString();
	}
	
	protected String formatErrorsAsHtml(ServiceException e) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("<ul>");
		
		sb.append("<li>"+e.getMessage()+"</li>");
		
		sb.append("</ul>");
		return sb.toString();
	}
	
	protected void addError(String errorType, String error) {
		
		if(!this.errorTypes.contains(errorType)) {
			this.errorTypes.add(errorType);
		}
		
		List<String> errorList = errorMessages.get(errorType);
		if(errorList == null) {
			errorList = new ArrayList<>();
			this.errorMessages.put(errorType, errorList);
		}
		if(!errorList.contains(error)) {
			errorList.add(error);
		}
	}

	// TODO: Do we want to continue using emails for alerts?
	protected void sendErrors() {
		getLogger().debug("<sendErrors");
		
		try {
			
			boolean sendEmail = false;
			if(!errorMessages.isEmpty()) {
				if(lastErrorEmailSent == null) {
					sendEmail = true;
				} else {
					long elapsedTime = (Instant.now().toEpochMilli()) - (lastErrorEmailSent.toEpochMilli());
					
					sendEmail = elapsedTime > emailFrequency;
				}
			}
			
			if(sendEmail) {
				lastErrorEmailSent = Instant.now();
				
				MimeMessage message = new MimeMessage(emailSession);
				message.setSubject(subject);
				
				message.addRecipients(RecipientType.TO, toAddresses);
				InternetAddress emailFromAddess = new InternetAddress(emailFrom);
				message.setFrom(emailFromAddess);
				
				StringBuilder text = new StringBuilder();
				
				for(String errorType : this.errorTypes) {
					
					text.append(String.format("<h3>%s synchronization errors:</h3>", StringEscapeUtils.escapeHtml4(errorType)));
					text.append("<ul>");
					
					for(String error : this.errorMessages.get(errorType)) {
						text.append("<li>");
						text.append(error);
						text.append("</li>");
					}
					text.append("</ul>");
				}
				
				message.setContent(text.toString(), "text/html");
				getLogger().info("Sending Error Email to "+rawAddresses);
				Transport.send(message);
			}
			
			errorTypes.clear();
			errorMessages.clear();

		} catch (Throwable e) {
			getLogger().error(e.getMessage(), e);
		}
		
		getLogger().debug(">sendErrors");
	}

	protected abstract String getEmailSubjectPropertyKey();
	
	protected void setLoggingRequestId(String idSource) {
		final String contextKey = "requestId";
		ThreadContext.remove(contextKey);
		String requestId = idSource+UUID.randomUUID().toString().substring(24).toUpperCase();
		ThreadContext.put(contextKey, requestId);
	}
	
	public void setAuthenticationProvider(
			AuthenticationProvider authenticationProvider) {
		this.authenticationProvider = authenticationProvider;
	}

	public void setUsernamePasswordAuthenticationToken(
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) {
		this.usernamePasswordAuthenticationToken = usernamePasswordAuthenticationToken;
	}

}
