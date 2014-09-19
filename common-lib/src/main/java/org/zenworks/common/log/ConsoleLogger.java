package org.zenworks.common.log;

import java.util.Date;

/**
 * Created by atezs82 on 9/19/14.
 */
public class ConsoleLogger implements Logger {

    private static final String LEVEL_WARN = "WARN";
    private static final String LEVEL_ERR = "ERROR";
    private static final String LEVEL_INFO = "INFO";
    String logClassName;
    String frameworkComponent;

    public ConsoleLogger(final String frameworkComponent, final Class<?> originatingClass) {
        this.frameworkComponent = frameworkComponent;
        logClassName = originatingClass.getName();
    }

    @Override
    public void info(String... message) {
       write(LEVEL_INFO,message);
    }

    @Override
    public void warn(String... message) {
       write(LEVEL_WARN, message);
    }

    @Override
    public void err(String... message) {
        write(LEVEL_ERR, message);
    }

    @Override
    public void err(String message, Throwable t) {
        write(LEVEL_ERR, message + " Nested exception type is: " + t.getClass().getName() + "\nStacktrace:" + t.toString());
    }

    private void write(String levelIdentifier, String message) {
        System.out.println("[" + new Date().toString() + "] "+ levelIdentifier + " - " + frameworkComponent + "/" + logClassName + " - " + message);
    }

    private void write(String levelIdentifier, String... messages) {
        for (String message:messages) {
            System.out.println("[" + new Date().toString() + "] "+ levelIdentifier + " - " + frameworkComponent + "/" + logClassName + " - " + message);
        }

    }
}
