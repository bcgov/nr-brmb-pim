package ca.bc.gov.mal.cirras.underwriting.listener.land.management.event.listener.v1;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1.CirrasUnderwritingLandManagementListenerService;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;

public class CirrasUwLandManagementEventListenerImpl implements EventListener, Runnable {

	private static final Logger logger = LoggerFactory.getLogger(CirrasUwLandManagementEventListenerImpl.class);
	
	private boolean running = false;
	
	private CirrasUnderwritingLandManagementListenerService cirrasUnderwritingLandManagementListenerService;
	
	private List<String> errorMessages = new ArrayList<>();
	private Instant lastErrorEmailSent;
	
	private Session emailSession;
	private long emailFrequency;
	private String emailSubject;
	private String rawAddresses;
	private InternetAddress[] toAddresses;
	private String emailFrom;
	
	public CirrasUwLandManagementEventListenerImpl() {
		logger.debug("<CirrasUnderwritingLandManagementEventListenerImpl");
		
		logger.debug(">CirrasUnderwritingLandManagementEventListenerImpl");
	}

	@Override
	public String getProcessName() {
		//Used in the failover process to identify this service. Used in table PROCESS_FAILOVR_OWNERSHIP
		return "UNDERWRITING_LAND_MANAGEMENT_EVENT_PROCESSOR";
	}

	@Override
	public void startListening() {
		logger.debug("<startListening");
		
		running = true;
		
		if (brokerThread==null || !brokerThread.isAlive()) {

			thread(this, false);
		}

		logger.debug(">startListening");
	}

	@Override
	public void stopListening() {
		logger.debug("<stopListening");

		running = false;

		logger.debug(">stopListening");
	}

	private Thread brokerThread;
	
	public void thread(Runnable runnable, boolean daemon) {
		brokerThread = new Thread(runnable);
		brokerThread.setDaemon(daemon);
		brokerThread.start();
	}
	
	public static final String REQUEST_IDLOG4J_MDC_KEY = "requestId";

	@Override
	public void run() {
		logger.debug("<run");

		// Not used.
		
		logger.debug("<run");
	}

	private void processMessage(String message, int jmsXDeliveryCount, int maximumRedeliveries, String messageId) throws Throwable {
		logger.debug("<processMessage \n"+message);

		try {
			
			FactoryContext factoryContext = new FactoryContext() {
				// do nothing
			};
			
			cirrasUnderwritingLandManagementListenerService.processCirrasUnderwritingLandManagementEvent(message, factoryContext);
		}
		catch (ServiceException e) {
			
			String errorMessage = "Saw " + e.getClass().getName() + " while processing cirras underwriting land management updates (ServiceException)"
					+ (e.getMessage() != null ? ": reason " + e.getMessage() : "");
			logger.error(errorMessage, e);
			
			addError(errorMessage);

			sendErrors(messageId);
			
			throw (e);
			
		} catch (Throwable e) {
			String errorMessage = "Saw " + e.getClass().getName() + " while processing cirras underwriting land management updates (Throwable)"
					+ (e.getMessage() != null ? ": reason " + e.getMessage() : "");
			logger.error(errorMessage, e);
			
			addError(errorMessage);
			
			sendErrors(messageId);
			
			throw (e);			
		} 
		
		logger.debug(">processMessage");
	}

	protected void addError(String error) {
		
		errorMessages.add(error);
	}

	protected void sendErrors(String messageId) {
		logger.debug("<sendErrors");
		
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
				message.setSubject(emailSubject);
				
				message.addRecipients(RecipientType.TO, toAddresses);
				InternetAddress emailFromAddess= new InternetAddress(emailFrom);
				message.setFrom(emailFromAddess);
				
				StringBuilder text = new StringBuilder();
					
				text.append("<h3>Listener errors:</h3>");
				text.append("<ul>");
				
				for(String error : this.errorMessages) {
					text.append("<li>");
					text.append(error);
					text.append("</li>");
					text.append("<li>Message ID: ");
					text.append(messageId);
					text.append("</li>");
				}
				text.append("</ul>");
				
				message.setContent(text.toString(), "text/html");
				logger.info("Sending Error Email to "+rawAddresses);
				Transport.send(message);
			}
			
			errorMessages.clear();

		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
		}
		
		logger.debug(">sendErrors");
	}

	public void setRawAddresses(String rawAddresses) {
		
		this.rawAddresses = rawAddresses;
		
		try {
		
			if(rawAddresses==null) {
				this.toAddresses = new InternetAddress[] {};
			} else {
				String[] split = rawAddresses.split(";");
				
				this.toAddresses = new InternetAddress[split.length];
				
				for(int i=0;i<split.length;++i) {
					
					this.toAddresses[i] = new InternetAddress(split[i]);
				}
			}
		} catch (AddressException e) {
			throw new RuntimeException(e);
		}
	}


	public void setCirrasUnderwritingLandManagementListenerService(CirrasUnderwritingLandManagementListenerService cirrasUnderwritingLandManagementListenerService) {
		this.cirrasUnderwritingLandManagementListenerService = cirrasUnderwritingLandManagementListenerService;
	}

	public void setEmailFrequency(long emailFrequency) {
		this.emailFrequency = emailFrequency;
	}

	public void setEmailSession(Session emailSession) {
		this.emailSession = emailSession;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public void setEmailFrom(String emailFrom) {
		this.emailFrom = emailFrom;
	}

}