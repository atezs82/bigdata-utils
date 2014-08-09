package org.zenworks.common.cli;


import com.jcraft.jsch.*;
import netscape.javascript.JSException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SshExecutor implements CliExecutor {

    Session session;

    final int DEFAULT_TIMEOUT=10000;

    public void init(final String host, final int sshPort, final String user, final String password) throws JSchException {
        JSch jsch = new JSch();
        Session session =jsch.getSession(user,host,sshPort);
        session.setPassword(password);
        session.setTimeout(DEFAULT_TIMEOUT);
    }

    @Override
    public CliExecutor connect() {
        // connect to server
        return this;
    }

    @Override
    public String executeCommand(String command) throws CliExecutorException {
        /*try {
            ChannelExec channel = (ChannelExec)session.openChannel("exec");
            channel.setCommand(command);

            InputStream in = channel.getInputStream();
            channel.connect();
            byte[] tmp=new byte[1024];
            while(true){
                while(in.available()>0){
                    int i=in.read(tmp, 0, 1024);
                    if(i<0)break;
                    System.out.print(new String(tmp, 0, i));
                }
                if(channel.isClosed()){
                    if(in.available()>0) continue;
                    System.out.println("exit-status: "+channel.getExitStatus());
                    break;
                }
                Thread.sleep(1000);
            }
            channel.disconnect();
            session.disconnect();

        } catch (JSchException exc) {
            throw new CliExecutorException("");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        return null;
    }

    @Override
    public CliExecutor disconnect() {
        return this;
    }
}
