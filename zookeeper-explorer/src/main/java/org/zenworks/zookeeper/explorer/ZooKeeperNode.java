package org.zenworks.zookeeper.explorer;

public class ZooKeeperNode {
	
	String path;
	
	final String DELIMITER="/";
	
	public ZooKeeperNode(final String path) {
		this.path=path;		    
	}
	
	public String getPath() {
		return path;
	}
	
	public String getName() {
		String[] elements = path.split(DELIMITER);
		return (elements.length==0?path:elements[elements.length-1]);
	}
	
	@Override
	public String toString() {
		return (getName().equals("")?"ZooKeeper Root":getName());
	}

}
