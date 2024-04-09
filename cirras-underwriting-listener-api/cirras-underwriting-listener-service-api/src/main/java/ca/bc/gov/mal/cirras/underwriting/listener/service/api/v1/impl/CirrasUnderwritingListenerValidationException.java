package ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1.impl;

import java.util.ArrayList;
import java.util.List;

public class CirrasUnderwritingListenerValidationException extends Exception {

	private static final long serialVersionUID = 1L;
	
	List<String> errors = new ArrayList<>();
	
	public CirrasUnderwritingListenerValidationException(List<String> errors) {
	
		this.errors = errors;
	}

	public List<String> getErrors() {
		return errors;
	}
}
