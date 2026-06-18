package ca.bc.gov.mal.cirras.underwriting.controllers.publisher;

public class EventPublisherException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public EventPublisherException(String message) {
		super(message);
	}
	
	public EventPublisherException(String message, Throwable t) {
		super(message, t);
	}
}
