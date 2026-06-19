package ca.bc.gov.mal.cirras.underwriting.controllers.async;

import java.util.Properties;
import java.util.List;
import java.util.ArrayList;

import jakarta.annotation.PreDestroy;
import jakarta.mail.internet.AddressException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.services.CirrasUnderwritingOutboxService;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

public class FetchOutboxTask extends AsynchronousTimerTask {
	
	private static final Logger logger = LoggerFactory.getLogger(FetchOutboxTask.class);
	
	private static final String LOGGING_ID_SOURCE = "SYNCOB";
		
	public static final String EMAIL_SUBJECT_PROPERTY_KEY = "EMAIL_OUTBOX_SYNCH_ERROR_SUBJECT";

	private CirrasUnderwritingOutboxService cirrasUnderwritingOutboxService;

	private List<OutboxProcessor> outboxProcessorList;
	
	public FetchOutboxTask(Properties applicationProperties) throws AddressException {
		super(applicationProperties);

		OutboxProcessor dopYieldContractCommodityBerriesOutboxProcessor = new DopYieldContractCommodityBerriesOutboxProcessor(applicationProperties);

		// Outboxes are processed in order of dependency.
		outboxProcessorList = new ArrayList<OutboxProcessor>();
		outboxProcessorList.add(dopYieldContractCommodityBerriesOutboxProcessor);
		
	}
	
	private boolean initialized;
	
	public void init() {
		logger.debug("<init");
		
		this.initialized = true;
		
		logger.debug(">init");
	}
	
	
	@PreDestroy
	public void destroy() {
		logger.info("<destroy");
		cancel();
		logger.info(">destroy");
	}
	
	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Override
	public void run() {
		logger.info("<run");
		
		if(!this.initialized) {
			
			throw new IllegalStateException("Task has not been initialized.");
		}
		
		long startMillis = System.currentTimeMillis();
		
		setLoggingRequestId(LOGGING_ID_SOURCE);
		
		try {

			WebAdeAuthentication authentication = this.getWebAdeAuthentication();

			for ( OutboxProcessor op : outboxProcessorList ) {
				op.process(authentication, cirrasUnderwritingOutboxService);
			}
			
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
			addError(ERROR_TYPE_UNRECOVERABLE, "Encountered an unrecoverable error: "+e.getMessage());
		}
		
		addError("Test Error Email", "This is a test of the error email.");
		
		sendErrors();
		
		long executionMillis = System.currentTimeMillis() - startMillis;
		logger.debug("Execution Seconds: "+((executionMillis)/1000.0));
		
		logger.debug(">run");
	}

	public void setCirrasUnderwritingOutboxService(CirrasUnderwritingOutboxService cirrasUnderwritingOutboxService) {
		this.cirrasUnderwritingOutboxService = cirrasUnderwritingOutboxService;
	}
	
	public void setCirrasUnderwritingOutboxServicePublishAndDelete(boolean publishAndDelete) {
		this.cirrasUnderwritingOutboxService.setPublishAndDelete(publishAndDelete);
	}

	@Override
	protected String getEmailSubjectPropertyKey() {
		return EMAIL_SUBJECT_PROPERTY_KEY;
	}
}
