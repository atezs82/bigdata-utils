package org.zenworks.common.cli;


public class ExecutorFactory {

    public static CliExecutor getLocalExecutor() {
        return new LocalExecutor();
    }

    public static CliExecutor getRemoteExecutor(final String host, final String user, final String password) {
        return null;
    }

}
