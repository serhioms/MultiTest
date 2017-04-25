package ca.rdmss.test.multitest.increment;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Rule;
import org.junit.Test;

import ca.rdmss.multitest.MultiTest;
import ca.rdmss.multitest.MultiTestRule;
import ca.rdmss.multitest.MultiThread;

@MultiTest(repeatNo=IncrementSuite.MAX_TRY, threadSet=IncrementSuite.THREAD_SET)
public class Increment_Atomic {

	@Rule
	public MultiTestRule rule = new MultiTestRule(this);

	AtomicInteger counter = new AtomicInteger();

	@MultiThread
	public void thread(){
		counter.incrementAndGet();
	}
	
	@Test
	public void test() throws InstantiationException, IllegalAccessException, InterruptedException {
		System.out.printf("%s%sTotally incremented = %,d\n", rule.getResult(), (rule.isTable()? "\n":" "), counter.get());
	}

}
