package org.zenworks.zookeeper.explorer;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.zenworks.common.log.Logger;
import org.zenworks.common.log.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ZooKeeperAdapter {

    final String connectString;
    final ConnectionStateListener listener;
    final Logger logger = LoggerFactory.getConsoleLogger("zookeeper-explorer", ZooKeeperAdapter.class);
    CuratorFramework curatorFramework;


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
            logger.info("Started ZooKeeper adapter.");
        } catch (Exception exc) {
            logger.err("Unable to initialize ZooKeeper connection due to exception:", exc);

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

    public List<String> getNodeAndChildrenRecursively(final String node) {
        List<String> result = new ArrayList<String>();
        result.add(node);
        for (String child:getChildren(node)) {
           result.addAll(getChildren(node + "/" + child));
        }
        return result;
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
            logger.err("Unable to return node data for node `" + path + "`, returning empty data as content. Exception was:", e);
            return new String();
        }
    }

    public boolean isNode(String path) {
        try {
            return curatorFramework.checkExists().forPath(path) != null;
        } catch (Exception exc) {
            logger.err("Unable to determine node existence for node `" + path + "` due to exception:", exc);
            return false;
        }
    }

    public void deleteNode(String path) {
        try {
            curatorFramework.delete().deletingChildrenIfNeeded().forPath(path);
        } catch (Exception e) {
            logger.err("Unable to delete node referred by path `" + path + "` due to exception:", e);
        }
    }

    public void createNode(String path, String initialContent) {
        try {
            curatorFramework.create().creatingParentsIfNeeded().forPath(path);
            setNodeData(path, initialContent);
        } catch (Exception e) {
            logger.err("Unable to create node referred by path `" + path + "` due to exception:", e);
        }
    }

    public void createNode(String path, byte[] initialContent) {
        try {
            curatorFramework.create().creatingParentsIfNeeded().forPath(path);
            setNodeData(path, initialContent);
        } catch (Exception e) {
            logger.err("Unable to create node referred by path `" + path + "` due to exception:", e);
        }
    }

    public void setNodeData(String path, String data) {
        try {
            curatorFramework.setData().forPath(path, data.getBytes());
        } catch (Exception e) {
            logger.err("Unable to set node data for node referred by path `" + path + "` due to exception:", e);
        }
    }

    public void disconnect() {
        try {
            curatorFramework.close();
        } catch (Exception e) {
            logger.err("Unable disconnect from ZooKeeper due to exception:", e);
        }
    }

    public void setNodeData(String path, byte[] data) {
        try {
            curatorFramework.setData().forPath(path, data);
        } catch (Exception e) {
            logger.err("Unable to set node data for node referred by path `" + path + "` due to exception:", e);
        }
    }

    public byte[] getNodeDataBytes(String pathInTree) {
        try {
            return curatorFramework.getData().forPath(pathInTree);
        } catch (Exception exc) {
            logger.err("Unable to get node data for node referred by path `" + pathInTree + "` due to exception:", exc);
            return new byte[]{};
        }
    }

    public boolean isReady() {
        return curatorFramework.getZookeeperClient().isConnected();
    }

}