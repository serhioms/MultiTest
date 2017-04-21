package ca.rdmss.test.multitest.increment;

import org.junit.Test;

import ca.rdmss.multitest.MultiTest;
import ca.rdmss.multitest.MultiTestHelper;
import ca.rdmss.multitest.MultiThread;

@MultiTest(repeatNo=IncrementSuite.MAX_TRY )
public class Increment_NonSynchronized_Volatile {

	volatile int counter;

	@MultiThread(threadSet=IncrementSuite.MAX_THREAD)
	public void thread(){
		counter++;
	}
	
	@Test
	public void test() throws InstantiationException, IllegalAccessException, InterruptedException {
		System.out.printf("%s%sTotally incremented = %,d\n", MultiTestHelper.start(this, IncrementSuite.THREAD_SET), IncrementSuite.THREAD_SET==null? " ":"\n", counter);
	}

}
