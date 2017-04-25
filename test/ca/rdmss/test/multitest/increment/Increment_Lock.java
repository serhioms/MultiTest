package ca.rdmss.test.multitest.increment;

import java.util.concurrent.locks.ReentrantLock;

import org.junit.Rule;
import org.junit.Test;

import ca.rdmss.multitest.MultiTest;
import ca.rdmss.multitest.MultiTestRule;
import ca.rdmss.multitest.MultiThread;

@MultiTest(repeatNo=IncrementSuite.MAX_TRY, threadSet=IncrementSuite.THREAD_SET)
public class Increment_Lock {

	@Rule
	public MultiTestRule rule = new MultiTestRule(this);

	ReentrantLock lock = new ReentrantLock();

	int counter;

	@MultiThread
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
		System.out.printf("%s%sTotally incremented = %,d\n", rule.getResult(), (rule.isTable()? "\n":" "), counter);
	}

}
