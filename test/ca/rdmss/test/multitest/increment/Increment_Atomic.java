package ca.rdmss.test.multitest.increment;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import ca.rdmss.multitest.MultiTest;
import ca.rdmss.multitest.MultiTestHelper;
import ca.rdmss.multitest.MultiThread;

@MultiTest( repeatNo = IncrementSuite.MAX_TRY )
public class Increment_Atomic {

	AtomicInteger counter = new AtomicInteger();

	@MultiThread(threadSet=IncrementSuite.MAX_THREAD)
	public void thread(){
		counter.incrementAndGet();
	}
	
	@Test
	public void test() throws InstantiationException, IllegalAccessException, InterruptedException {
		System.out.printf("%s%sTotally incremented = %,d\n", MultiTestHelper.start(this, IncrementSuite.THREAD_SET), IncrementSuite.THREAD_SET==null? " ":"\n", counter.get());
	}

}
