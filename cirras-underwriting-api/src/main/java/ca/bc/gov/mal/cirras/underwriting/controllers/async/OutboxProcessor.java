package ca.bc.gov.mal.cirras.underwriting.controllers.async;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;

import ca.bc.gov.mal.cirras.underwriting.data.models.BaseOutbox;
import ca.bc.gov.mal.cirras.underwriting.services.CirrasUnderwritingOutboxService;
import ca.bc.gov.mal.cirras.underwriting.services.utils.PropertyUtils;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

public abstract class OutboxProcessor {
	
	public static final String OUTBOX_POLLING_ENABLED_KEY = "POLLING_OUTBOX_ENABLED";
	public static final String OUTBOX_POLLING_SECONDS_FREQUENCY_KEY = "POLLING_OUTBOX_SECONDS_FREQUENCY";
	public static final String OUTBOX_POLLING_MAX_RECORDS_KEY = "POLLING_OUTBOX_MAX_RECORDS";
	public static final String OUTBOX_POLLING_MAX_ITERATIONS_KEY = "POLLING_OUTBOX_MAX_ITERATIONS";
	
	
	private boolean isEnabled;
	private long frequency;
	private int maxRecords;
	private long maxIterations;

	private Instant lastRun;
		
	protected OutboxProcessor(Properties applicationProperties) {

		isEnabled = PropertyUtils.getProperty(applicationProperties, getEnabledPropertyKey(), Boolean.TRUE);
		
		// The configured frequency is decreased by 25 seconds to account for slight differences in run times between each execution of process() 
		// to try and ensure that it runs close to the expected frequency. So, for example, if frequency was configured as 60, but process() was last 
		// run 52 seconds ago, then this will still run, because the calculated frequency was actually every 35 seconds.
		frequency = (PropertyUtils.getProperty(applicationProperties, getFrequencyPropertyKey(), 60) - 25) * 1000;
		maxRecords = (int)PropertyUtils.getProperty(applicationProperties, getMaxRecordsPropertyKey(), 200);
		maxIterations = PropertyUtils.getProperty(applicationProperties, getMaxIterationsPropertyKey(), 10);

		lastRun = null;
	
	}

	public void process(WebAdeAuthentication authentication, CirrasUnderwritingOutboxService cirrasUnderwritingOutboxService) throws ServiceException {
		getLogger().debug("<process");
		
		// Check whether to process outboxes.
		// The value of frequency is dependent on how often the FetchOutboxTask runs.
		boolean doRun = false;
		if (!isEnabled) {
			doRun = false;
		}
		else if ( lastRun == null ) {
			doRun = true;
		} else {
			long elapsedTime = (Instant.now().toEpochMilli()) - (lastRun.toEpochMilli());
			doRun = elapsedTime > frequency;
		}

		if ( doRun ) { 
			lastRun = Instant.now();

			getLogger().info("Processing Outboxes");
			
			// Process outboxes until up to maxRecords x numIterations records have been processed.
			boolean noMoreRecords = false;
			int numRecordsProcessed = 0;
			int numEventsPublished = 0;
			for ( int i = 0; i < maxIterations && !noMoreRecords; i++ ) { 
				List<? extends BaseOutbox> outboxes = getNextOutboxes(maxRecords, authentication, cirrasUnderwritingOutboxService);
				
				if ( outboxes != null && !outboxes.isEmpty() ) { 

					Set<String> eventKeys = new HashSet<String>();
					for ( BaseOutbox outbox : outboxes ) { 

						// If the event type + primary key combination has already been processed, then do not 
						// publish the event again, since it would be a duplicate.
						String eventKey = outbox.getSourceKey() + "_" + outbox.getTransactionType() + "_" + outbox.getClass().getName();
						boolean doPublishEvent = false;
						if ( !eventKeys.contains(eventKey) ) {
							eventKeys.add(eventKey);
							doPublishEvent = true;
						}

						processOutbox(outbox, doPublishEvent, authentication, cirrasUnderwritingOutboxService);
						
						numRecordsProcessed++;
						if ( doPublishEvent ) {
							numEventsPublished++;
						}
					}
					
				} else {
					noMoreRecords = true;
				}
			}
			
			getLogger().info("Processed " + numRecordsProcessed + " Outboxes, generating " + numEventsPublished + " events.");
		}
		
		getLogger().debug("<process");
	}

	// Returns the full name of the outbox model object that this OutboxProcessor supports (i.e. BaseOutbox or a subclass thereof).
	abstract protected String getOutboxClassName();

	// Property name that controls whether this Outbox is processed.
	// The default value also enables the FetchOutboxTask.
	protected String getEnabledPropertyKey() {
		return OUTBOX_POLLING_ENABLED_KEY;
	}
	
	// Property name that controls how often this Outbox is processed.
	// The default value also controls how often FetchOutboxTask runs.
	protected String getFrequencyPropertyKey() {
		return OUTBOX_POLLING_SECONDS_FREQUENCY_KEY;
	}

	// Property name that controls how many records are fetched at a time for this Outbox.
	protected String getMaxRecordsPropertyKey() {
		return OUTBOX_POLLING_MAX_RECORDS_KEY;
	}

	// Property name that controls how many fetches are performed in one run for this Outbox.
	protected String getMaxIterationsPropertyKey() {
		return OUTBOX_POLLING_MAX_ITERATIONS_KEY;
	}
	
	abstract protected List<? extends BaseOutbox> getNextOutboxes(int maxRecords, WebAdeAuthentication authentication, CirrasUnderwritingOutboxService cirrasUnderwritingOutboxService) throws ServiceException;

	abstract protected void processOutbox(BaseOutbox outbox, boolean doPublishEvent, WebAdeAuthentication authentication, CirrasUnderwritingOutboxService cirrasUnderwritingOutboxService) throws ServiceException;

	abstract protected Logger getLogger();
	
}
