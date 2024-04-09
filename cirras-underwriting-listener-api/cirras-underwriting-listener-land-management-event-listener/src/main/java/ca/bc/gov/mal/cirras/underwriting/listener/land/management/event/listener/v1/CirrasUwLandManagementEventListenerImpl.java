package ca.bc.gov.mal.cirras.underwriting.listener.land.management.event.listener.v1;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.command.ActiveMQMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1.CirrasUnderwritingLandManagementListenerService;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;

public class CirrasUwLandManagementEventListenerImpl implements EventListener, Runnable {

	private static final Logger logger = LoggerFactory.getLogger(CirrasUwLandManagementEventListenerImpl.class);
	
	private ActiveMQConnectionFactory connectionFactory;
	private String queueName;
	private long receiveTimeout = 500;

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

		Connection connection = null;
		try {
			
			RedeliveryPolicy redeliveryPolicy = connectionFactory.getRedeliveryPolicy();
			logger.debug("RedeliveryDelay="+redeliveryPolicy.getRedeliveryDelay());
			logger.debug("InitialRedeliveryDelay="+redeliveryPolicy.getInitialRedeliveryDelay());
			int maximumRedeliveries = redeliveryPolicy.getMaximumRedeliveries();
			logger.debug("MaximumRedeliveries="+maximumRedeliveries);

			// Create a Connection
			connection = connectionFactory.createQueueConnection();
			
			connection.start();

			// Create a Session
			javax.jms.Session session = connection.createSession(true, javax.jms.Session.CLIENT_ACKNOWLEDGE);

			Queue destination = session.createQueue(queueName);

			MessageConsumer messageConsumer = session.createConsumer(destination);

			while (running) {
				
				Message message = null;
				try {

					message = messageConsumer.receive(receiveTimeout);
	
					if(message!=null) {
						
						String requestId = "CIRRASUNDERWRITINGLANDMANAGEMENTLISTENER" + message.getJMSMessageID();
						
						MDC.put(REQUEST_IDLOG4J_MDC_KEY, requestId);
						
						logger.debug("message=" + message);
						logger.debug("message.class=" + message.getClass());
						logger.debug("message.JMSRedelivered=" + message.getJMSRedelivered());
						
						int jmsXDeliveryCount = message.getIntProperty("JMSXDeliveryCount");
						logger.debug("jmsXDeliveryCount="+jmsXDeliveryCount);
						
						TextMessage textMessage = (TextMessage) message;
						
						String messageText = textMessage.getText();
						logger.debug("messageText=\n" + messageText);
						
						processMessage(messageText, jmsXDeliveryCount, maximumRedeliveries, message.getJMSMessageID());
						
						session.commit();
					}
					
				} catch(Throwable t) {
					
					logger.error(t.getMessage(), t);
					
					if(message!=null) {
						
						logger.debug("message="+message.getClass());
						
						if(message instanceof ActiveMQMessage) {
						
							ActiveMQMessage activeMQMessage = (ActiveMQMessage) message;
							
							try {
							
								activeMQMessage.setProperty(ActiveMQMessage.DLQ_DELIVERY_FAILURE_CAUSE_PROPERTY, t.getMessage());
							} catch(IOException e) {

								logger.warn("Failed to set "+ActiveMQMessage.DLQ_DELIVERY_FAILURE_CAUSE_PROPERTY+" property.", e);
							}
						}
					}
					
					session.rollback();
					
				} finally {
					
					MDC.remove(REQUEST_IDLOG4J_MDC_KEY);
				}
			}
			
		} catch (JMSException e) {
			
			logger.error(e.getMessage(), e);
		} finally {
			if(connection!=null) {
				
				try {
					connection.close();
				} catch (JMSException e) {
					// do nothing
				}
			}
		}

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

	public void setConnectionFactory(ActiveMQConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public void setReceiveTimeout(long receiveTimeout) {
		this.receiveTimeout = receiveTimeout;
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