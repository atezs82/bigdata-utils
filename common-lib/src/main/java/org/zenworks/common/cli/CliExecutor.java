package org.zenworks.common.cli;

import javafx.util.Callback;

public interface CliExecutor {

    public CliExecutor connect();

    public String executeCommand(final String command) throws CliExecutorException;

    public void executeCommandAsync(final String command, final ExecutorCallback callback);

    public CliExecutor disconnect();

}
