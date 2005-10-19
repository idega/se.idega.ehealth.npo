package se.idega.ehealth.npo.libraries;

import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 

import se.idega.ehealth.npo.constants.Constants;
import se.idega.ehealth.npo.constants.LogDomains;
import se.idega.ehealth.npo.exceptions.InfrastructureException;


public class CommonLib
{
	private static Log log = LogFactory.getLog(LogDomains.LOG_DOMAIN_GUI);
	
	public static Properties loadNpoProperties(ClassLoader cl)
	{ 
		try
		{
			InputStream is = cl.getResourceAsStream(Constants.NPO_PROPERTIES_FILENAME);			
			if (is==null)
			{
				log.error(Constants.NPO_PROPERTIES_FILENAME+" not found in classpath");
				throw new InfrastructureException(Constants.NPO_PROPERTIES_FILENAME+" not found in classpath");
			} 
			Properties props = new Properties();
			props.load(is);
			is.close();
			return props;
		} catch (Exception ex)
		{
			log.error("Exception",ex);
			throw new InfrastructureException(ex);
		}		
	}
}
