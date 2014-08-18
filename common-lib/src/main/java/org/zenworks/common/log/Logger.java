package org.zenworks.common.log;

public interface Logger {
	
	public void info(String... message);
	
	public void warn(String... message);

	public void err(String... message);
	
	public void err(String message, Throwable t);

}
