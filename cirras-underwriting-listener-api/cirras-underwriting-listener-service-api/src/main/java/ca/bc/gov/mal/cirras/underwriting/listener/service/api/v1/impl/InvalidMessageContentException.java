package ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1.impl;

import java.util.List;

import ca.bc.gov.nrs.wfone.common.model.Message;

public class InvalidMessageContentException extends Exception {

	private static final long serialVersionUID = 1L;

	private List<Message> errorMessages;
	
	public InvalidMessageContentException(List<Message> errorMessages) {

		this.errorMessages = errorMessages;
	}

	public List<Message> getErrorMessages() {
		return errorMessages;
	}

}
