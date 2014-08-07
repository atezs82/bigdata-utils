package org.zenworks.common.cli;

public interface CliExecutor {

    public void connect();

    public String executeCommand(final String command) throws CliExecutorException;

    public void disconnect();

}
