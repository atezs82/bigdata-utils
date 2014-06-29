package org.zenworks.common;

import org.apache.log4j.spi.LoggerFactory;

public class SimpleApacheLogger implements Logger {
	
	final String frameworkComponent;
	final org.apache.log4j.Logger actualLogger;
	
	public SimpleApacheLogger(final String frameworkComponent, final Class<?> logClass) {
		this.frameworkComponent = frameworkComponent;
		actualLogger = org.apache.log4j.Logger.getLogger(logClass);
	}
	
	public void err(final String message, final Throwable t) {
		actualLogger.error(frameworkComponent + " - " + concat(message),t);		
	}
	
	public void info(String... message) {
		actualLogger.info(frameworkComponent + " - " + concat(message));
	}

	public void warn(String... message) {
		actualLogger.info(frameworkComponent + " - " + concat(message));
	}

	public void err(String... message) {
		actualLogger.info(frameworkComponent + " - " + concat(message));		
	}
	
	private String concat(String... strings) {
		String result = new String();
		
		for (String s:strings) {
			result=result+s;
		}
		
		return result;
	}


}
