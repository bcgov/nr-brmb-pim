package ca.bc.gov.mal.cirras.underwriting.listener.failover.service.api.v1.async;
public class ServiceException extends RuntimeException
{
	private static final long serialVersionUID = -6418563091242776474L;

	public ServiceException(String msg)
	{
		super(msg);
	}

	public ServiceException(String msg, Throwable t)
	{
		super(msg, t);
	}
}
