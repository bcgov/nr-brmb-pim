package ca.bc.gov.mal.cirras.underwriting.controllers.publisher;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.mal.cirras.underwriting.data.resources.UnderwritingEvent;
import ca.bc.gov.mal.cirras.underwriting.services.EventPublisher;
import ca.bc.gov.mal.cirras.underwriting.services.EventPublisherException;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;
import io.nats.client.Connection;
import io.nats.client.Connection.Status;
import io.nats.client.JetStream;
import io.nats.client.Message;
import io.nats.client.Nats;
import io.nats.client.Options;
import io.nats.client.api.PublishAck;
import io.nats.client.impl.NatsMessage;

// TODO: Merge with interface?
public class EventPublisherImpl implements EventPublisher {

	static final Logger logger = LoggerFactory.getLogger(EventPublisherImpl.class);
	
	private static ObjectMapper mapper = new ObjectMapper();

	private Options messageQueueOptions;
	private String messageQueueSubject;

	// Not created until there is something to publish.
	private Connection natsConnection = null;
		
	@Override
	public void publish(
			String eventType,
			Object resourceBeforeUpdate,
			Object resourceAfterUpdate,
			Map<String, String> sourceIdentifiers) throws EventPublisherException {
				
		BaseResource source = null;

		BaseResource baseResourceBeforeUpdate = null;
		if(resourceBeforeUpdate!=null) {
			if(resourceBeforeUpdate instanceof BaseResource) {
				
				baseResourceBeforeUpdate = (BaseResource) resourceBeforeUpdate;
				
				source = baseResourceBeforeUpdate;
			} else {
				throw new EventPublisherException("Not expecting resourceBeforeUpdate object of type: "+resourceBeforeUpdate.getClass());
			}
		}

		BaseResource baseResourceAfterUpdate = null;
		if(resourceAfterUpdate!=null) {
			if(resourceAfterUpdate instanceof BaseResource) {
				
				baseResourceAfterUpdate = (BaseResource) resourceAfterUpdate;
				
				source = baseResourceAfterUpdate;
			} else {
				throw new EventPublisherException("Not expecting resourceAfterUpdate object of type: "+resourceAfterUpdate.getClass());
			}
		}

		String sourceType = null;
		String sourceLink = null;
		
		if ( source != null ) {
			sourceType = source.getClass().getName();
			sourceLink = source.getSelfLink();
		}

		publish(
				eventType, 
				sourceType, 
				baseResourceBeforeUpdate, 
				baseResourceAfterUpdate, 
				sourceIdentifiers, 
				sourceLink,
				Instant.now());
	}
	
	protected  void publish(
			String eventType,
			String sourceType,
			BaseResource resourceBeforeUpdate,
			BaseResource resourceAfterUpdate,
			Map<String, String> sourceIdentifiers,
			String sourceLink,
			Instant eventTimestamp)
			throws EventPublisherException {
		logger.debug("<publish " + eventType);

		try {
			UnderwritingEvent event = new UnderwritingEvent();
			event.setEventType(eventType);
			event.setSourceType(sourceType);
			event.setSourceIdentifiers(sourceIdentifiers);
			event.setSourceLink(sourceLink);
			event.setEventTimestamp(eventTimestamp);
			event.setResourceBeforeUpdate(resourceBeforeUpdate);
			event.setResourceAfterUpdate(resourceAfterUpdate);
			
			final String messageText = mapper.writeValueAsString(event);
			
			Connection nc = loadNatsConnection();
            JetStream js = nc.jetStream();
        				
            Message msg = NatsMessage.builder()
                    .subject(messageQueueSubject)
                    .data(messageText, StandardCharsets.UTF_8)
                    .build();

            PublishAck pa = js.publish(msg);

            logger.debug(String.format("Published message %s on subject %s, stream %s, seqno %d, has error %s.", 
    				messageText, messageQueueSubject, pa.getStream(), pa.getSeqno(), pa.hasError() ? "Yes" : "No"));

    		pa.throwOnHasError();
    		
		} catch (Throwable t) {

			throw new EventPublisherException(t.getMessage(), t);
		}

		logger.debug(">publish");
	}

	// Returns the NATS Connection. Creates it if necessary.
	private Connection loadNatsConnection() throws InterruptedException, IOException {

		if ( natsConnection == null ) {
			logger.info("Creating NATS connection.");
			natsConnection = Nats.connect(messageQueueOptions);

		} else if (natsConnection.getStatus() == Connection.Status.CLOSED || natsConnection.getStatus() == Connection.Status.DISCONNECTED ) {
			logger.info("Creating another NATS connection as the previous one was closed or disconnected.");
			natsConnection = null;
			natsConnection = Nats.connect(messageQueueOptions);
		}
		
		return natsConnection;
	}
	
	public void setMessageQueueOptions(Options messageQueueOptions) {
		this.messageQueueOptions = messageQueueOptions;
	}

	public void setMessageQueueSubject(String messageQueueSubject) {
		this.messageQueueSubject = messageQueueSubject;
	}
}
