package ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1;

public class CirrasUnderwritingServiceException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public CirrasUnderwritingServiceException(String message) {
		super(message);
	}

	public CirrasUnderwritingServiceException(Throwable cause) {
		super(cause);
	}

	public CirrasUnderwritingServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
