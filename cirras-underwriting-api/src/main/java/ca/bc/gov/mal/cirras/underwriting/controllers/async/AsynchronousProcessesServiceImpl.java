package ca.bc.gov.mal.cirras.underwriting.controllers.async;

import java.util.Properties;
import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;

import ca.bc.gov.mal.cirras.underwriting.services.utils.PropertyUtils;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;

public class AsynchronousProcessesServiceImpl implements AsynchronousProcessesService, ApplicationListener<ApplicationEvent> {
	
	private static final Logger logger = LoggerFactory.getLogger(AsynchronousProcessesServiceImpl.class);
	
	public static final String MASTER_TASK_POLLING_PERIOD_KEY = "POLLING_PERIOD_SECONDS_MASTER_TASK";

	private Timer asyncTimer;
	
	private boolean started = false;
	
	private Properties applicationProperties;
	private AsyncMasterTask asyncMasterTask;

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		logger.info("<onApplicationEvent");
		
		if(event instanceof ContextRefreshedEvent) {
			logger.info("ContextRefreshedEvent");
			
			if(!started) {
				
				start();
			}
			
		} else if(event instanceof ContextStartedEvent) {
			logger.info("ContextStartedEvent");
			
			if(!started) {
				
				start();
			}
			
		} else if(event instanceof ContextStoppedEvent) {
			logger.info("ContextStoppedEvent");
			
			if(started) {
				
				stop();
			}
			
		} else if(event instanceof ContextClosedEvent) {
			logger.info("ContextClosedEvent");
			
			if(started) {
				
				stop();
			}
			
		} 
		logger.info(">onApplicationEvent");
	}
	
	@Override
	public void start() {
		logger.info("<AsynchronousProcessesService.start");
		
		if(started) {
			throw new IllegalStateException("Asynchronous master task has already started.");
		}
		
		try {
		
			if (asyncTimer == null) {

				long period = PropertyUtils.getProperty(this.applicationProperties, MASTER_TASK_POLLING_PERIOD_KEY, 60 * 5000); // every 5 min.
				logger.info("AsynchronousProcessesService.start(): MASTER_TASK_POLLING_PERIOD_KEY=" + period);

				if (period < 0) {

					logger.warn("AsyncMasterTask is disabled.");
				} else {

					asyncTimer = new Timer("Master Task", true);

					asyncMasterTask.init();

					asyncTimer.schedule(asyncMasterTask, 5000, period);
				}
			}
			this.started = true;
		} catch (Throwable e) {
			throw new ServiceException(e.getMessage(), e);
		}
		
		logger.info(">AsynchronousProcessesService.start");
	}
	
	@Override
	public void stop() {
		logger.info("<AsynchronousProcessesService.stop");
		
		if(!started) {
			throw new IllegalStateException("Asynchronous tasks are not running.");
		}
		
		if(asyncTimer!=null){
			
			boolean result = asyncMasterTask.killChildTimerTasks();
			
			if(result) {
				asyncTimer.cancel();
				asyncTimer = null;
			}
		}
		
		
		this.started = false;

		logger.info(">AsynchronousProcessesService.stop");
	}
	
	public void setApplicationProperties(Properties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}

	public void setAsyncMasterTask(AsyncMasterTask asyncMasterTask) {
		this.asyncMasterTask = asyncMasterTask;
	}
}
