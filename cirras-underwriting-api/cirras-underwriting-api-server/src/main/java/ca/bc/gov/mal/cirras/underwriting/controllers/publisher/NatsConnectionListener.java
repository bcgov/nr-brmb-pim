package ca.bc.gov.mal.cirras.underwriting.controllers.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.nats.client.Connection;
import io.nats.client.ConnectionListener;
import io.nats.client.ConnectionListener.Events;


public class NatsConnectionListener implements ConnectionListener {

	private static final Logger logger = LoggerFactory.getLogger(NatsConnectionListener.class);

	@Override
	public void connectionEvent(Connection conn, Events type) {	
		logger.info("Connection Event: " + type);
	}
	
	@Override
	public void connectionEvent(Connection conn, Events type, Long time, String uriDetails) {
		logger.info("Connection Event: " + type + ", URI: " + uriDetails);
	}
	
}
