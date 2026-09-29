package ca.bc.gov.mal.cirras.underwriting.listener.model.v1;

public enum SubmissionListenerType {
	InvalidMessageFormat,
	InvalidMessageContent,
	ApplicationError,
	Success,
	ValidationError, 
	RetryMessageLater
}
