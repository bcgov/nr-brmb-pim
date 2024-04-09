package ca.bc.gov.mal.cirras.underwriting.listener.api.rest.test;

import java.time.Instant;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;
//import ca.bc.gov.nrs.wfhr.payroll.api.rest.v1.resource.PayrollEvent;
//import ca.bc.gov.nrs.wfhr.payroll.model.v1.DiarySubmission;

public class EventPublisher {

	static final Logger logger = LoggerFactory.getLogger(EventPublisher.class);
	
	private static ObjectMapper mapper = new ObjectMapper();

	private JmsTemplate jmsTopicTemplate;
	
	public EventPublisher(JmsTemplate jmsTopicTemplate) {
		this.jmsTopicTemplate = jmsTopicTemplate;
	}
	
	public void publish(
			String eventType,
			String sourceLink,
			Map<String, String> sourceIdentifiers) throws EventPublisherException {
		
//		String sourceType = DiarySubmission.class.getName();
		
//		publish(
//				eventType, 
//				sourceType, 
//				null, 
//				null, 
//				sourceIdentifiers, 
//				sourceLink,
//				Instant.now());
	}
	
	private void publish(
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
//			PayrollEvent event = new PayrollEvent();
//			event.setEventType(eventType);
//			event.setSourceType(sourceType);
//			event.setSourceIdentifiers(sourceIdentifiers);
//			event.setSourceLink(sourceLink);
//			event.setEventTimestamp(eventTimestamp);
//			event.setResourceBeforeUpdate(resourceBeforeUpdate);
//			event.setResourceAfterUpdate(resourceAfterUpdate);
//			
//			final String messageText = mapper.writeValueAsString(event);
//			
//			this.jmsTopicTemplate.send(new MessageCreator() {
//
//				@Override
//				public Message createMessage(Session session) throws JMSException {
//					logger.debug("<createMessage");
//					Message result;
//					
//					result = session.createTextMessage(messageText);
//					
//					logger.debug(">createMessage");
//					return result;
//				}});
			
		} catch (Throwable t) {

			throw new EventPublisherException(t.getMessage(), t);
		}

		logger.debug(">publish");
	}
}
