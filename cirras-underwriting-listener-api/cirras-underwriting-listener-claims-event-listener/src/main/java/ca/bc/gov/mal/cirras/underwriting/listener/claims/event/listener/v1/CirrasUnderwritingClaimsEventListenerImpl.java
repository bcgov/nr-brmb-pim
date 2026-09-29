package ca.bc.gov.mal.cirras.underwriting.listener.claims.event.listener.v1;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
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
import org.slf4j.MDC;

import ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1.CirrasUnderwritingClaimsListenerService;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import io.nats.client.Connection;
import io.nats.client.JetStream;
import io.nats.client.JetStreamSubscription;
import io.nats.client.Message;
import io.nats.client.Nats;
import io.nats.client.Options;
import io.nats.client.PullSubscribeOptions;

public class CirrasUnderwritingClaimsEventListenerImpl implements EventListener, Runnable {

	private static final Logger logger = LoggerFactory.getLogger(CirrasUnderwritingClaimsEventListenerImpl.class);

	private Options messageQueueOptions;
	private String messageQueueStream;
	private String messageQueueConsumer;
	
	private long receiveTimeoutMillis = 500;
	private long initialRedeliveryDelaySeconds = 5;
		
	private boolean running = false;
	
	private CirrasUnderwritingClaimsListenerService cirrasUnderwritingClaimsListenerService;
	
	private List<String> errorMessages = new ArrayList<>();
	private Instant lastErrorEmailSent;
	
	private Session emailSession;
	private long emailFrequency;
	private String emailSubject;
	private String rawAddresses;
	private InternetAddress[] toAddresses;
	private String emailFrom;
	
	public CirrasUnderwritingClaimsEventListenerImpl() {
		logger.debug("<CirrasUnderwritingClaimsEventListenerImpl");
		
		logger.debug(">CirrasUnderwritingClaimsEventListenerImpl");
	}

	@Override
	public String getProcessName() {
		//Used in the failover process to identify this service. Used in table PROCESS_FAILOVR_OWNERSHIP
		return "UNDERWRITING_CLAIMS_EVENT_PROCESSOR";
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

		Connection nc = null;
		JetStreamSubscription sub = null;
		try {
			nc = Nats.connect(messageQueueOptions);

			logger.debug("Consumer: " + messageQueueConsumer);
			logger.debug("Stream: " + messageQueueStream);
			logger.debug("ReceiveTimeoutMillis: " + receiveTimeoutMillis);
			logger.debug("InitialRedeliveryDelaySeconds: " + initialRedeliveryDelaySeconds);
			
            JetStream js = nc.jetStream();

            PullSubscribeOptions pullOptions = PullSubscribeOptions.builder()
                    .durable(messageQueueConsumer)
                    .stream(messageQueueStream)
                    .bind(true)
                    .build();

            sub = js.subscribe(null, pullOptions);
            
            nc.flush(Duration.ofSeconds(5));
            
			while (running) {
				
				Message message = null;
				String msgLogStr = null;    // Message meta data for log messages.
				long deliveryCount = -1;
				try {

	                List<Message> messageList = sub.fetch(1, Duration.ofMillis(receiveTimeoutMillis));
					
	                if ( messageList != null && !messageList.isEmpty() ) {
	
	                	message = messageList.get(0);

	                	long consumerSeq = message.metaData().consumerSequence();
	                	long streamSeq = message.metaData().streamSequence();
	                	deliveryCount = message.metaData().deliveredCount();
	                	ZonedDateTime msgTime = message.metaData().timestamp();
	                	
						String requestId = "CIRRASUNDERWRITINGCLAIMSLISTENER_CONSUMER_SEQ" + consumerSeq;
						MDC.put(REQUEST_IDLOG4J_MDC_KEY, requestId);
						
						msgLogStr = messageMetaDataLogStr(consumerSeq, streamSeq, deliveryCount, msgTime);

						logger.debug("message: " + message);
						logger.debug(msgLogStr);

						String dataStr = null;
	                    if (message.getData() != null && message.getData().length > 0) {
	                    	dataStr = new String(message.getData(), StandardCharsets.UTF_8);

	                    	logger.debug("messageText: " + dataStr);

	                    } else {
	                    	logger.warn("Message data is empty");
	                    }

                    	try {
                    		processMessage(dataStr, msgLogStr);
                    		message.ack();
                    	} catch (Throwable t) {
                    		// nakWithDelay will delay re-delivery for initialRedeliveryDelaySeconds on the first nak, 
                    		// but subsequent naks seem to add the delay to the calculated backoff delay for the Consumer.
    	            		message.nakWithDelay(Duration.ofSeconds(initialRedeliveryDelaySeconds));
                    	} finally {
                    		message = null;
                    		msgLogStr = null;
                    	}
					}
					
				} catch(Throwable t) {

					if ( msgLogStr != null ) {
						throw new ServiceException("Error reading Message (" + msgLogStr + "): " + t.getMessage(), t);
					} else {
						throw t;
					}
					
				} finally {
					
					try {
						MDC.remove(REQUEST_IDLOG4J_MDC_KEY);
					} catch (Throwable t) {
						// In the unlikely event of an exception, ignore. It will either be fixed by the next iteration, 
						// or this thread is exiting anyway so it doesn't matter.
					}
				}
			}
			
		} catch (Throwable e) { 

			// Most likely a connection error. Just log it. This thread will be re-started in 5 mins and it will 
			// try again.
			logger.error(e.getMessage(), e);

			addError(e.getMessage());
			
			sendErrors();

		} finally {
			if ( sub != null ) {
				try {
					sub.unsubscribe();
					sub = null;
				} catch (Exception e) {
					logger.error("Unsubscribe failed: " + e.getMessage());
				}
			}

			if ( nc != null ) {
				try {
					nc.close();
					nc = null;
				} catch (Exception e) {
					logger.error("Close Connection failed: " + e.getMessage());
				}
			}		
		}

		logger.debug("<run");
	}

	private void processMessage(String message, String msgLogStr) throws Throwable {
		logger.debug("<processMessage");

		try {
			
			FactoryContext factoryContext = new FactoryContext() {
				// do nothing
			};
			
			cirrasUnderwritingClaimsListenerService.processCirrasUnderwritingClaimsEvent(message, factoryContext);
		}
		catch (ServiceException e) {
			
			String errorMessage = "Saw " + e.getClass().getName() + " while processing cirras underwriting claims updates (ServiceException)"
					+ " for message (" + msgLogStr + ")"  
					+ (e.getMessage() != null ? ": reason " + e.getMessage() : "");
			logger.error(errorMessage, e);
			
			addError(errorMessage);
			sendErrors();
			
			throw (e);
			
		} catch (Throwable e) {
			String errorMessage = "Saw " + e.getClass().getName() + " while processing cirras underwriting claims updates (Throwable)"
					+ " for message (" + msgLogStr + ")"  
					+ (e.getMessage() != null ? ": reason " + e.getMessage() : "");
			logger.error(errorMessage, e);
			
			addError(errorMessage);
			sendErrors();
			
			throw (e);			
		} 
		
		logger.debug(">processMessage");
	}

	private String messageMetaDataLogStr(long consumerSeq, long streamSeq, long deliveryCount, ZonedDateTime msgTime) {
		
		return String.format("Consumer Seq %d, Stream Seq %d, Delivery Count %d, Timestamp %s", 
				consumerSeq,
				streamSeq,
				deliveryCount,
				msgTime
				);
	}
	
	protected void addError(String error) {
		
		errorMessages.add(error);
	}

	protected void sendErrors() {
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

	public void setMessageQueueOptions(Options messageQueueOptions) {
		this.messageQueueOptions = messageQueueOptions;
	}

	public void setMessageQueueStream(String messageQueueStream) {
		this.messageQueueStream = messageQueueStream;
	}

	public void setMessageQueueConsumer(String messageQueueConsumer) {
		this.messageQueueConsumer = messageQueueConsumer;
	}

	public void setReceiveTimeoutMilis(long receiveTimeoutMilis) {
		this.receiveTimeoutMillis = receiveTimeoutMilis;
	}

	public void setInitialRedeliveryDelaySeconds(long initialRedeliveryDelaySeconds) {
		this.initialRedeliveryDelaySeconds = initialRedeliveryDelaySeconds;
	}

	public void setCirrasUnderwritingClaimsListenerService(CirrasUnderwritingClaimsListenerService cirrasUnderwritingClaimsListenerService) {
		this.cirrasUnderwritingClaimsListenerService = cirrasUnderwritingClaimsListenerService;
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