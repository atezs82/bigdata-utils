

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.exceptions.TransportException;

public class InfiniSpanModClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if (args.length >= 1) {
			RemoteCacheManager cacheContainer = new RemoteCacheManager(args[0]);
			cacheContainer.start();		
			try {
				RemoteCache cache = cacheContainer.getCache();				
				System.out.println("Connecting to cache successful at `"+args[0]+"`.");			
				if (args.length >= 3) {
					System.out.println("Attempting to set key `"+args[1]+"` to value `"+args[2]+"`.");
					cache.put(args[1], args[2]);					
				} else {
					System.out.println("Cache contains " + cache.size() + " number of keys.");					
				}
			} catch (TransportException trExc) {
				System.out.println("Connecting to cache was not successful at `"+args[0]+"`.");
			}
			cacheContainer.stop();
		} else {
			System.out.println("Provide Infinispan connect string as parameter.");
		}
		
	}	

}
