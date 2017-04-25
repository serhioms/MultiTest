package ca.rdmss.test.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

public class TestUtil {
	
	private Map<String, AtomicInteger> results = new HashMap<String, AtomicInteger>(32);

	public void clean(){
		results.clear();
	}
	
	public void print(){
		double sum = 0;
		
		for(AtomicInteger counter: results.values()){
			sum += counter.get();
		}
		
		sum /= 100;
		
		System.out.println("");
		
		for(Entry<String, AtomicInteger> entry: results.entrySet()){
			System.out.printf("%50s = %4.1f %% = %,12d\n", entry.getKey(), entry.getValue().get()/sum, entry.getValue().get());
		}
	}
	
	public void count(String key){
		AtomicInteger counter = results.get(key);
		if( counter == null ){
			counter = new AtomicInteger(1);
			results.put(key,  counter);
		} else {
			counter.incrementAndGet();
		}
	}
}
