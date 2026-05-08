package ca.bc.gov.mal.cirras.underwriting.controllers.async;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Properties;
import java.util.Random;
import java.util.Timer;

import jakarta.mail.internet.AddressException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import ca.bc.gov.mal.cirras.underwriting.services.FailOverService;
import ca.bc.gov.mal.cirras.underwriting.services.utils.PropertyUtils;
import ca.bc.gov.nrs.wfone.common.utils.ApplicationContextProvider;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

public class AsyncMasterTask extends AsynchronousTimerTask {

	private static final String EMAIL_SUBJECT_PROPERTY_KEY = "EMAIL_MASTER_TASK_SYNCH_ERROR_SUBJECT";
	
	private FailOverService failOverService;
	
	private static final String LOGGING_ID_SOURCE = "SYNCFR";
	private static final String PROCESS_NAME = "UNDERWRITING_EVENT_PUBLISHER";
	
	private String NODE_NAME;
	
	private Timer outboxTimer;
		
	public AsyncMasterTask(Properties applicationProperties) throws AddressException {
		super(applicationProperties);
	}

	private static final Logger logger = LoggerFactory.getLogger(AsyncMasterTask.class);

	private boolean initialized;
	
	public void init() {
		logger.debug("<init");
		
		RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
		NODE_NAME = runtimeBean.getName();
		logger.info("NODE_NAME = " + NODE_NAME);
		
		
		this.initialized = true;
				
		logger.debug(">init");
	}
	
	@Override
	public void run() {
		logger.info("<run - schedule Tasks for node "+NODE_NAME);

		if (!this.initialized) {

			throw new IllegalStateException("Task has not been initialized.");
		}

		try {

			setLoggingRequestId(LOGGING_ID_SOURCE);
			
			WebAdeAuthentication authentication = this.getWebAdeAuthentication();
						
			boolean masterNodeInd = this.failOverService.asyncCheckForMaster(PROCESS_NAME, NODE_NAME, Integer.valueOf(nodeExpiryMinutes), getUserId(authentication));
			
			ApplicationContext webApplicationContext = ApplicationContextProvider.getApplicationContext();
			
			if(masterNodeInd) {
				
				if(outboxTimer==null) {
					
					Boolean enabledInd = PropertyUtils.getProperty(this.applicationProperties, OUTBOX_POLLING_ENABLED_KEY, Boolean.TRUE);
					
					if(Boolean.TRUE.equals(enabledInd)) {
					
						outboxTimer = new Timer("Fetch Outbox", true);
						
						// Make the default 5 minutes right now to make testing easier
						LocalTime defaultPollingTime = LocalTime.now().plusMinutes(5);
						
						LocalTime pollingTime = PropertyUtils.getProperty(this.applicationProperties, OUTBOX_POLLING_TIME_KEY, defaultPollingTime);
						// We do not want the tasks on different nodes to start at the exact same time so adding some random seconds to minimize such probability. 
						pollingTime.plusSeconds(getRandomSeconds());
						logger.info(NODE_NAME+" OUTBOX_POLLING_TIME_KEY="+pollingTime);
						
						FetchOutboxTask fetchOutboxTask = webApplicationContext.getBean("fetchOutboxTask", FetchOutboxTask.class);
						fetchOutboxTask.init();
						
						Instant firstTime = ZonedDateTime.of(LocalDate.now(), pollingTime, ZoneId.systemDefault()).toInstant();
						
						if (Instant.now().isAfter(firstTime)) {
							firstTime = ZonedDateTime.of(LocalDate.now().plusDays(1), pollingTime, ZoneId.systemDefault()).toInstant();
						}
						
						outboxPoolingSecondsFrequency = PropertyUtils.getProperty(this.applicationProperties, OUTBOX_POLLING_SECONDS_FREQUENCY_KEY, 2*60*60);
						
						outboxTimer.schedule(fetchOutboxTask, Date.from(firstTime), outboxPoolingSecondsFrequency*1000 );

						logger.info("Succesfully scheduled Tasks for node "+NODE_NAME);
					}
				}
				
			} 
			else {
				this.killChildTimerTasks();
			}
		} catch (Throwable  e) {
			logger.error(e.getMessage(), e);
			addError(ERROR_TYPE_UNRECOVERABLE, "Encountered an unrecoverable error: " + e.getMessage());
			this.killChildTimerTasks();
		}

		sendErrors();
		
		logger.info(">run - schedule Tasks for node "+NODE_NAME);
	}
	
	public boolean killChildTimerTasks() {
		logger.info("<killChildTimerTasks");
		
		boolean result = false;
		
		if(outboxTimer!=null){
			
			outboxTimer.cancel();
			outboxTimer = null;
		}
				
		if(outboxTimer==null) {
			result = true;
		}
		
		logger.info(">killChildTimerTasks");
		return result;
	}
	
	private static final int randMinimum = 0;
	private static final int randMaximum = 40;
	private static int getRandomSeconds() {
		Random rand = new Random();
		int randomNum = randMinimum + rand.nextInt((randMaximum - randMinimum) + 1);		
		return randomNum;
	}

	private String getUserId(WebAdeAuthentication authentication) {
		String userId = "DEFAULT_USERID";

		if (authentication != null) {
			userId = authentication.getUserId();
		}

		return userId;
	}
	
	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Override
	protected String getEmailSubjectPropertyKey() {
		return EMAIL_SUBJECT_PROPERTY_KEY;
	}

	public void setFailOverService(FailOverService failOverService) {
		this.failOverService = failOverService;
	}
}
