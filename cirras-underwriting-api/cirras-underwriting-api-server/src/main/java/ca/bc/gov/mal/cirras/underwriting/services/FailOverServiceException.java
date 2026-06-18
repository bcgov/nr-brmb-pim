package ca.bc.gov.mal.cirras.underwriting.services;

public class FailOverServiceException extends RuntimeException
{
	private static final long serialVersionUID = -6418563091242776474L;

	public FailOverServiceException(String msg)
	{
		super(msg);
	}

	public FailOverServiceException(String msg, Throwable t)
	{
		super(msg, t);
	}
}
