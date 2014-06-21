package org.zenworks.zookeeper.explorer;

import java.util.ArrayList;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ZooKeeperAdapter {
	
	CuratorFramework curatorFramework;

	public ZooKeeperAdapter(final String connectString) {		
		curatorFramework = CuratorFrameworkFactory.builder().connectString(connectString).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
		curatorFramework.start();
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
	
	public void deleteNode(String path) {
		try {
		   curatorFramework.delete().deletingChildrenIfNeeded().forPath(path);
		} catch (Exception e) {
			
		}
	}
	
	public void createNode(String path,String initialContent) {
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


}
