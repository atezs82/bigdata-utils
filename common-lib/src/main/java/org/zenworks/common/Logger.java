package org.zenworks.common;

import org.apache.log4j.spi.LoggerFactory;

public class Logger {
	
	final String frameworkComponent;
	final org.apache.log4j.Logger actualLogger;
	
	public Logger(final String frameworkComponent, final Class<?> logClass) {
		this.frameworkComponent = frameworkComponent;
		actualLogger = org.apache.log4j.Logger.getLogger(logClass);
	}
	
	public void err(final String message) {
		actualLogger.error(frameworkComponent + " - " + message);		
	}
	
	public void info(final String message) {
		actualLogger.info(frameworkComponent + " - " + message);		
	}
	
	public void warn(final String message) {
		actualLogger.warn(frameworkComponent + " - " + message);		
	}
	
	public void err(final String message, final Throwable t) {
		actualLogger.error(frameworkComponent + " - " + message,t);		
	}
	
	public void info(final String message, final Throwable t) {
		actualLogger.info(frameworkComponent + " - " + message,t);		
	}
	
	public void warn(final String message, final Throwable t) {
		actualLogger.warn(frameworkComponent + " - " + message,t);		
	}

}
