package ca.bc.gov.mal.cirras.underwriting.spring;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.bc.gov.mal.cirras.underwriting.controllers.publisher.EventPublisherImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.publisher.NatsAuthHandler;
import ca.bc.gov.mal.cirras.underwriting.controllers.publisher.NatsConnectionListener;
import ca.bc.gov.mal.cirras.underwriting.controllers.publisher.NatsErrorListener;
import ca.bc.gov.mal.cirras.underwriting.services.EventPublisher;
import io.nats.client.Options;

@Configuration
public class EventPublisherSpringConfig  {

	private static final Logger logger = LoggerFactory.getLogger(EventPublisherSpringConfig.class);
	
	public EventPublisherSpringConfig() {
		logger.info("<EventPublisherSpringConfig");
		
		logger.info(">EventPublisherSpringConfig");
	}

	@Value("${MESSAGE_QUEUE_URL}")
	private String natsURL;
	
	@Value("${MESSAGE_QUEUE_UNDERWRITING_REST_NKEY_SEED}")
	private String nkeySeed;
	
	@Bean 
	Options natsConnectionOptions() {

		logger.info("NATS Server URL: " + natsURL);
		
		NatsAuthHandler authHandler = new NatsAuthHandler(nkeySeed);
		NatsConnectionListener connListener = new NatsConnectionListener();
		NatsErrorListener errListener = new NatsErrorListener();

		Options.Builder builder = new Options.Builder()
                .server(natsURL)
                .connectionTimeout(Duration.ofSeconds(10))
                .pingInterval(Duration.ofSeconds(30))
                .reconnectWait(Duration.ofSeconds(5))
                .authHandler(authHandler)
                .connectionListener(connListener)
                .errorListener(errListener)
                .maxReconnects(3);
		
        Options options = builder.build();

        return options;

	}
	
	@Bean
	public EventPublisher eventPublisher() {
		EventPublisherImpl result;
		
		result = new EventPublisherImpl();
		result.setMessageQueueOptions(natsConnectionOptions());
		result.setMessageQueueSubject("underwriting-event-channel");
		
		return result;
	}
}
