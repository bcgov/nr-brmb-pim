package ca.bc.gov.mal.cirras.underwriting.services;

import java.util.Map;

// TODO: Merge with impl class?
public interface EventPublisher {

	public void publish(
			String eventType, 
			Object resourceBeforeUpdate, 
			Object resourceAfterUpdate,
			Map<String, String> sourceIdentifiers)
		throws EventPublisherException;
}

