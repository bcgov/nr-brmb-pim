package ca.bc.gov.mal.cirras.underwriting.listener.land.management.event.listener.v1;

public class FailoverTimerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FailoverTimerException(String msg) {
		super(msg);
	}

	public FailoverTimerException(String msg, Throwable t) {
		super(msg, t);
	}
}