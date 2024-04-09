package ca.bc.gov.mal.cirras.underwriting.listener.policies.event.listener.v1;

import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;

//import ca.bc.gov.nrs.wfone.failover.service.api.v1.async.FailOverService;
import ca.bc.gov.mal.cirras.underwriting.listener.failover.service.api.v1.async.FailOverService;

public class FailoverEventListener implements ApplicationListener<ApplicationEvent> {
	
	private static final Logger logger = LoggerFactory.getLogger(FailoverEventListener.class);
	
	private Timer failoverTimer;
	
	private boolean started = false;
	
	private FailoverTask failoverTask;
	
	private FailOverService failOverService;
	
	private EventListener eventListener;
	
	public FailoverEventListener(FailOverService failOverService, EventListener eventListener) {

		this.failOverService = failOverService;
		this.eventListener = eventListener;
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		logger.info("<onApplicationEvent (Underwriting Policies)");
		
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
	
	private void start() {
		logger.info("<start (Underwriting Policies)");
		
		if(started) {
			throw new IllegalStateException("Failover timer has already started.");
		}
		
		try {
		
			if (failoverTimer == null) {
				
				long period = 60 * 5000; // every 5 min.
				
				failoverTimer = new Timer("Failover Timer", true);
				
				failoverTask = new FailoverTask(failOverService, eventListener);

				failoverTask.init();
				
				failoverTimer.schedule(failoverTask, 5000, period);
			}
			this.started = true;
			
		} catch (Throwable e) {
			
			throw new FailoverTimerException(e.getMessage(), e);
		}
		
		logger.info(">start");
	}
	
	private void stop() {
		logger.info("<stop (Underwriting Policies)");
		
		if(!started) {
			throw new IllegalStateException("Failover timer is not running.");
		}
		
		if(failoverTimer!=null){

			failoverTimer.cancel();
			failoverTimer = null;
		}
		
		this.started = false;

		logger.info(">stop");
	}
}
