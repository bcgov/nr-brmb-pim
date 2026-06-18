package ca.bc.gov.mal.cirras.underwriting.services.reports;

public class JasperReportServiceException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public JasperReportServiceException(String reason, Exception cause)
	{
		super(reason, cause);
	}
	public JasperReportServiceException(String reason)
	{
		super(reason);
	}
}