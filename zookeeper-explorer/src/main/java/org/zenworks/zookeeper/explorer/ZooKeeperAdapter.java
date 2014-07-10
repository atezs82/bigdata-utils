package org.zenworks.zookeeper.explorer;

import java.util.ArrayList;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryOneTime;

public class ZooKeeperAdapter {

    CuratorFramework curatorFramework;
    final String connectString;
    final ConnectionStateListener listener;


    public ZooKeeperAdapter(final String connectString, final ConnectionStateListener listener) {
        this.connectString = connectString;
        this.listener = listener;
    }

    public void init() {
        curatorFramework = CuratorFrameworkFactory.builder().connectString(connectString).retryPolicy(new ExponentialBackoffRetry(1000,3)).build();
        curatorFramework.getConnectionStateListenable().addListener(listener);
        curatorFramework.start();
        try {
            curatorFramework.getZookeeperClient().blockUntilConnectedOrTimedOut();
        } catch (Exception exc) {

        }
    }

    public List<String> getChildren(String node) {
        try {
            return curatorFramework.getChildren().forPath(node);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<String>();
        }
    }

    public void renameNode(final String oldPath, final String newPath) {
        String content = getNodeData(oldPath);
        deleteNode(oldPath);
        createNode(newPath, content);
    }

    public String getNodeData(String path) {
        try {
            return new String(curatorFramework.getData().forPath(path));
        } catch (Exception e) {
            return new String();
        }
    }

    public boolean isNode(String path) {
        try {
            return curatorFramework.checkExists().forPath(path) != null;
        } catch (Exception exc) {
            return false;
        }
    }

    public void deleteNode(String path) {
        try {
            curatorFramework.delete().deletingChildrenIfNeeded().forPath(path);
        } catch (Exception e) {

        }
    }

    public void createNode(String path, String initialContent) {
        try {
            curatorFramework.create().creatingParentsIfNeeded().forPath(path);
            setNodeData(path, initialContent);
        } catch (Exception e) {

        }
    }

    public void createNode(String path, byte[] initialContent) {
        try {
            curatorFramework.create().creatingParentsIfNeeded().forPath(path);
            setNodeData(path, initialContent);
        } catch (Exception e) {

        }
    }

    public void setNodeData(String path, String data) {
        try {
            curatorFramework.setData().forPath(path, data.getBytes());
        } catch (Exception e) {

        }
    }

    public void disconnect() {
        try {
            curatorFramework.close();
        } catch (Exception e) {

        }
    }

    public void setNodeData(String path, byte[] data) {
        try {
            curatorFramework.setData().forPath(path, data);
        } catch (Exception e) {

        }
    }

    public byte[] getNodeDataBytes(String pathInTree) {
        try {
            return curatorFramework.getData().forPath(pathInTree);
        } catch (Exception exc) {
            return new byte[]{};
        }
    }


    public boolean isReady() {
        return curatorFramework.getZookeeperClient().isConnected();
    }
}