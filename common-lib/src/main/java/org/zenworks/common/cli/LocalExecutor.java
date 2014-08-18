package org.zenworks.common.cli;

import javafx.util.Callback;
import org.zenworks.common.log.Logger;
import org.zenworks.common.log.LoggerFactory;

import java.io.*;

public class LocalExecutor implements CliExecutor {

    private final int WAIT_INTERVAL = 500;

    private final Logger logger = LoggerFactory.getLogger("COMMON",LocalExecutor.class);

    @Override
    public CliExecutor connect() {
       // native API does not require this
        return this;
    }

    @Override
    public String executeCommand(String command) throws CliExecutorException {
        String result = "";
        try {
           System.out.println("Executing " + command);

            ProcessBuilder builder = new ProcessBuilder(command.split(" ")).redirectError(ProcessBuilder.Redirect.INHERIT);

            Process p = builder.start();
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;

            while ( (line = reader.readLine()) != null) {
                result = result + line + "\n";
            }



            return result;
        } catch (IOException exc) {
            throw new CliExecutorException(exc.getMessage());
        } catch (InterruptedException exc) {
            throw new CliExecutorException(exc.getMessage());
        }
    }

    @Override
    public void executeCommandAsync(final String command, final ExecutorCallback callback) {

        Thread processThread = new Thread(new Runnable() {
            @Override
            public void run() {

               try {
                    Runtime runtime = Runtime.getRuntime();
                    Process p = runtime.exec(command);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line;

                    while ( (line = reader.readLine()) != null) {
                        callback.call(line + "\n");
                        Thread.sleep(WAIT_INTERVAL);
                    }

                } catch (IOException exc) {
                    callback.exception(exc);
                } catch (InterruptedException exc) {
                    callback.exception(exc);
                }

            }
        });

        processThread.run();

    }

    @Override
    public CliExecutor disconnect() {
       // native API does not require this
        return this;
    }
}
