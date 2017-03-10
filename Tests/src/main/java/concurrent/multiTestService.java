package concurrent;


import java.util.concurrent.ConcurrentHashMap;

/**
 * ≤‚ ‘ concurrentHashMap
 * @author Administrator
 *
 */

public class multiTestService {
	
	public ConcurrentHashMap<String, Object> getList(long startIndex , long endIndex){
		ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();
		for(long i = startIndex; i<endIndex; i++ ){
			map.put(i+"", "value"+i);
		}
		return map;
	}
	
}
