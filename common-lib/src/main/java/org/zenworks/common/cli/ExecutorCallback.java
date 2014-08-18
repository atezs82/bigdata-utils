package org.zenworks.common.cli;

/**
 * Created by atezs82 on 8/9/14.
 */
public interface ExecutorCallback {

    void call(String line);

    void exception(Exception exc);

}
