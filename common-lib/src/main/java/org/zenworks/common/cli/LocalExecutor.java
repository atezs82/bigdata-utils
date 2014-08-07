package org.zenworks.common.cli;

import java.io.*;

public class LocalExecutor implements CliExecutor {
    @Override
    public void connect() {
       // native API does not require this
    }

    @Override
    public String executeCommand(String command) throws CliExecutorException {
        String result = "";
        try {
            Runtime runtime = Runtime.getRuntime();
            Process p = runtime.exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;

            while ( (line = reader.readLine()) != null) {
                result = result + line + "\n";
            }
            return result;
        } catch (IOException exc) {
            throw new CliExecutorException(exc.getMessage());
        }
    }

    @Override
    public void disconnect() {
       // native API does not require this
    }
}
