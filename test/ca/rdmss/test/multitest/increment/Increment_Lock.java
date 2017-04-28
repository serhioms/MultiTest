package ca.rdmss.test.multitest.increment;

import java.util.concurrent.locks.ReentrantLock;

import org.junit.Rule;
import org.junit.Test;

import ca.rdmss.multitest.annotation.MultiTest;
import ca.rdmss.multitest.annotation.MultiThread;
import ca.rdmss.multitest.junitrule.MultiTestRule;

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
