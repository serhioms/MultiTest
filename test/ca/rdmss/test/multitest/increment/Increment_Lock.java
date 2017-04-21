package ca.rdmss.test.multitest.increment;

import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

import ca.rdmss.multitest.ConcurrentTest;
import ca.rdmss.multitest.MultiTest;
import ca.rdmss.multitest.NewThread;

@ConcurrentTest( maxTry = IncrementSuite.MAX_TRY )
public class Increment_Lock {

	ReentrantLock lock = new ReentrantLock();

	int counter;

	@NewThread(howMany=IncrementSuite.MAX_THREAD)
	public void thread(){
		try {
			lock.lock();
			counter++;
		} finally {
			lock.unlock();
		}
	}
	
	@Test
	public void test() throws InstantiationException, IllegalAccessException, InterruptedException {
		System.out.printf("%s%sTotally incremented = %,d\n", MultiTest.start(this, IncrementSuite.THREAD_SET), IncrementSuite.THREAD_SET==null? " ":"\n", counter);
	}

}
