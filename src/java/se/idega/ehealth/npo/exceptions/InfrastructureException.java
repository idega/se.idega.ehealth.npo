package se.idega.ehealth.npo.exceptions;

/*
 * 
 * fatal runtime exception, should be catched by server and an error page should be displayed
 */
public class InfrastructureException extends RuntimeException
{
	public InfrastructureException(String message, Throwable cause)
	{
		super(message,cause);
	}
	
	public InfrastructureException(String message)
	{
		super(message);
	}
	
	public InfrastructureException(Throwable cause)
	{
		super(cause);
	}	
}
