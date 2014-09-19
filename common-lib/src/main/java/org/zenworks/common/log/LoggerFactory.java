package org.zenworks.common.log;

/**
 * Created by atezs82 on 8/9/14.
 */
public class LoggerFactory {

    public static Logger getLogger(String component, Class clazz) {
        return new SimpleApacheLogger(component, clazz);
    }

    public static Logger getConsoleLogger(String component, Class clazz) {
        return new ConsoleLogger(component, clazz);
    }

}
