package ca.bc.gov.mal.cirras.underwriting.controllers.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.nats.client.Connection;
import io.nats.client.Consumer;
import io.nats.client.ErrorListener;
import io.nats.client.JetStreamSubscription;
import io.nats.client.Message;
import io.nats.client.ErrorListener.FlowControlSource;
import io.nats.client.support.Status;

public class NatsErrorListener implements ErrorListener {

	private static final Logger logger = LoggerFactory.getLogger(NatsErrorListener.class);

	@Override
	public void errorOccurred(Connection conn, String error) {
		logger.error(supplyMessage("errorOccurred", conn, null, null, "Error: ", error));
	}

	@Override
	public void exceptionOccurred(Connection conn, Exception exp) {
		logger.error(supplyMessage("exceptionOccurred", conn, null, null, "Exception: ", exp));
	}

	@Override
	public void flowControlProcessed(Connection conn, JetStreamSubscription sub, String subject, FlowControlSource source) {
		logger.debug(supplyMessage("flowControlProcessed", conn, null, sub, "Subject:", subject, "FlowControlSource:", source));
	}

	@Override
	public void heartbeatAlarm(Connection conn, JetStreamSubscription sub, long lastStreamSequence, long lastConsumerSequence) {
		logger.error(supplyMessage("heartbeatAlarm", conn, null, sub, "lastStreamSequence: ", lastStreamSequence, "lastConsumerSequence: ", lastConsumerSequence));
	}
	
	@Override
	public void messageDiscarded(Connection conn, Message msg) {
		logger.error(supplyMessage("messageDiscarded", conn, null, null, "Message: ", msg));
	}

	@Override
	public void pullStatusError(Connection conn, JetStreamSubscription sub, Status status) {
		logger.error(supplyMessage("pullStatusError", conn, null, sub, "Status:", status));
	}
	
	@Override
	public void pullStatusWarning(Connection conn, JetStreamSubscription sub, Status status) {
		logger.error(supplyMessage("pullStatusWarning", conn, null, sub, "Status:", status));
	}
	
	@Override
	public void slowConsumerDetected(Connection conn, Consumer consumer) {
		logger.error(supplyMessage("slowConsumerDetected", conn, consumer, null));
	}

	@Override
	public void socketWriteTimeout(Connection conn) {
		logger.error(supplyMessage("socketWriteTimeout", conn, null, null));
	}

	@Override
	public void unhandledStatus(Connection conn, JetStreamSubscription sub, Status status) {
		logger.error(supplyMessage("unhandledStatus", conn, null, sub, "Status:", status));
	}

}
