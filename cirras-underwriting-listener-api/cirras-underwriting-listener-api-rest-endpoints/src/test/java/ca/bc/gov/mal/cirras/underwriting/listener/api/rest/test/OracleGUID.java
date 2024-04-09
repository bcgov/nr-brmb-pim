package ca.bc.gov.mal.cirras.underwriting.listener.api.rest.test;

import java.util.UUID;
import java.util.regex.Pattern;

public class OracleGUID
{
	/**
	 * Generates a random Oracle-formatted GUID.
	 * 
	 * @return as described
	 */
	public static String randomOracleGuid() 
	{       
	    return uuidToOracleGuid(UUID.randomUUID());
	}
	
	private static Pattern uuidTextPattern = Pattern.compile("(.{2})(.{2})(.{2})(.{2})-(.{2})(.{2})-(.{2})(.{2})-(.{2})(.{2})-(.{12})");
	private static Pattern oracleGuidPattern1 = Pattern.compile("([0-9A-F]{8})([0-9A-F]{4})([0-9A-F]{4})([0-9A-F]{4})([0-9A-F]{12})");
	private static Pattern oracleGuidPattern2 = Pattern.compile("(.{2})(.{2})(.{2})(.{2}).(.{2})(.{2}).(.{2})(.{2})(.{18})");
	
	/**
	 * Converts <code>uuid</code> into a String containing the textual representation of the UUID as an Oracle GUID.
	 * 
	 * @param uuid
	 * @return as described
	 */
	public static String uuidToOracleGuid(UUID uuid) 
	{       
		/**
		 * input: "9BB2A2B8DF8747B0982F2F1702E1D18B"
		 * output: "B8A2B29B-87DF-B047-982F-2F1702E1D18B"
		 */
		
		if (uuid == null)
		{
			throw new IllegalArgumentException();
		}
		
	    return uuidTextPattern.matcher(uuid.toString().toUpperCase())
	    		.replaceAll("$4$3$2$1$6$5$8$7$9$10$11");
	}
	
	public static boolean isValidOracleGuid(String oracleGuid)
	{
		return oracleGuid != null && oracleGuidPattern1.matcher(oracleGuid).matches();
	}
	
	/**
	 * Converts <code>guid</code>, a String containing a UUID in Oracle GUID format, into a UUID.
	 * 
	 * @param oracleGuid
	 * @return as described
	 */
	public static UUID oracleGuidToUUID(String oracleGuid) 
	{       
		/**
		 * input: "9BB2A2B8DF8747B0982F2F1702E1D18B"
		 * output: "B8A2B29B-87DF-B047-982F-2F1702E1D18B"
		 */
		
		if (oracleGuid == null || !isValidOracleGuid(oracleGuid))
		{
			throw new IllegalArgumentException();
		}
		
	    return UUID.fromString(
	    		oracleGuidPattern2.matcher(
	    				oracleGuidPattern1.matcher(oracleGuid.toUpperCase()).replaceAll("$1-$2-$3-$4-$5"))
	    						.replaceAll("$4$3$2$1-$6$5-$8$7$9"));
	}
}
