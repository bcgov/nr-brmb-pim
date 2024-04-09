package ca.bc.gov.mal.cirras.underwriting.persistence.v1.utils;

public class DateFormatException extends Exception {

	private static final long serialVersionUID = 1L;

	public DateFormatException(String msg) {
		super(msg);
	}

	public DateFormatException(String msg, Throwable t) {
		super(msg, t);
	}
}
