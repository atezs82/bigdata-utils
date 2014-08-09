package org.zenworks.common.cli;

public interface CliExecutor {

    public CliExecutor connect();

    public String executeCommand(final String command) throws CliExecutorException;

    public CliExecutor disconnect();

}
