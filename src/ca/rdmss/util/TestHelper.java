package ca.rdmss.util;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class TestHelper {
	
	final public Map<String, AtomicInteger> results = new ConcurrentHashMap<String, AtomicInteger>(32);

	public void clean(){
		results.clear();
	}
	
	public int getTotal(){
		int ttl = 0;
		
		for(AtomicInteger counter: results.values()){
			ttl += counter.get();
		}

		return ttl;
	}
	
	public String getReport(){
		String result = "";
		
		double ttl = getTotal() / 100.0;
		
		System.out.println("");
		
		result += String.format("%50s %7s %12s\n", "Key", "Percent", "Actual val");
		result += String.format("%50s %7s %12s\n", "------------------", "-------", "------------");
		for(Entry<String, AtomicInteger> entry: results.entrySet()){
			result += String.format("%50s %5.1f %% %,12d\n", entry.getKey(), entry.getValue().get()/ttl, entry.getValue().get());
		}
		result += String.format("%50s %7s %12s\n", "------------------", "-------", "------------");
		
		return result;
	}
	
	public void count(String key){
		AtomicInteger counter = results.get(key);
		if( counter == null ){
			synchronized(TestHelper.class){
				counter = results.get(key);
				if( counter == null ){
					counter = new AtomicInteger(0);
					results.put(key,  counter);
				}
			}
		}
		counter.incrementAndGet();
	}
}
