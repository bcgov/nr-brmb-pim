package ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1.impl;

public class SkipMessageException extends Exception {

	private static final long serialVersionUID = 1L;

	public SkipMessageException(String msg) {
		super(msg);
	}

}
