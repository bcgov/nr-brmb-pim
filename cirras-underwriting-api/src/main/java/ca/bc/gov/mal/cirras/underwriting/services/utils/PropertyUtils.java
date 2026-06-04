package ca.bc.gov.mal.cirras.underwriting.services.utils;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Properties;

public final class PropertyUtils {
	
	private PropertyUtils() {
		// private constructor
	}
	
	public static Boolean getProperty(Properties properties, String key, Boolean defaultValue) {
		Boolean result = defaultValue;
		
		String tmp = properties.getProperty(key);
		if(tmp!=null) {
			
			result = Boolean.valueOf(tmp);
		}
		
		return result;
	}
	
	public static LocalTime getProperty(Properties properties, String key, LocalTime defaultValue) {
		LocalTime result = defaultValue;
		
		String tmp = properties.getProperty(key);
		if(tmp!=null) {
			
			try {
				result = LocalTime.parse(tmp);
			} catch(DateTimeParseException e) {
				// do nothing
			}
		}
		
		return result;
	}
	
	public static long getProperty(Properties properties, String key, long defaultValue) {
		long result = defaultValue;
		
		String tmp = properties.getProperty(key);
		if(tmp!=null) {
			
			try {
				result = Long.parseLong(tmp);
			} catch(NumberFormatException e) {
				// do nothing
			}
		}
		
		return result;
	}
	
	public static String getProperty(Properties properties, String key) {
		return getProperty(properties, key, (String)null);
	}
	
	public static String getProperty(Properties properties, String key, String defaultValue) {
		String result = defaultValue;
		
		String tmp = properties.getProperty(key);
		if(tmp!=null) {
			result = tmp;
		}
		
		return result;
	}

}
